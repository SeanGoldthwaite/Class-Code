import argparse
import os
import sys
from PIL import Image
import transforms
import numpy as np
from skimage.io import imread
from part1 import bestmatch
from part1 import runargs

part1_image_dir = os.path.join(os.path.dirname(__file__), 'part1-images')
part2_image_dir = os.path.join(os.path.dirname(__file__), 'part2-images')
output_dir = os.path.join(os.path.dirname(__file__), 'outputs')

def main():
    parser = make_parser()
    args = parser.parse_args()

    if not os.path.exists(output_dir):
        os.makedirs(output_dir)

    if args.subcommand == 'part1':
        # Paths to all input images
        images = [os.path.join(part1_image_dir, img) for img in args.images]
        # Path to output
        output = os.path.join(output_dir, args.output)

        groups = (int)(sys.argv[2])

        print(f'\nk={args.k} \n{len(images)} images found \nOutput dir: {output}\n')
        assert args.k <= len(images)
        runargs(groups, images, output)

    elif args.subcommand == 'part2':
        coordinates = np.array([[ float(val) for val in coord.split(',')] for coord in args.coordinates])
        image1_coords = coordinates[::2]
        image2_coords = coordinates[1::2]

        image1 = open_image(os.path.join(part2_image_dir, args.image1))
        image2 = open_image(os.path.join(part2_image_dir, args.image2))

        print(f'\nn={args.n}\nImage 1 coordinates:\n{image1_coords}\nImage 2 coordinates:\n{image2_coords}')
        assert args.n == image1_coords.shape[0] and args.n == image2_coords.shape[0]

        if args.n == 1:
            image_t = transforms.translation(image2, image1_coords, image2_coords)
        elif args.n == 2:
            image_t = transforms.similarity(image2, image1_coords, image2_coords)
        elif args.n == 3:
            image_t = transforms.affine(image2, image1_coords, image2_coords)
        elif args.n == 4:
            image_t, _ = transforms.projective(image2, image1_coords, image2_coords)
        else:
            print('n must be 1,2,3 or 4')

        img = Image.fromarray(image_t)
        path = os.path.join(output_dir, args.output)
        img.save(path)

    elif args.subcommand == 'part3':
        image1 = open_image(os.path.join(part1_image_dir, args.image1))
        image2 = open_image(os.path.join(part1_image_dir, args.image2))

        pairs = bestmatch(image1, image2, n=23)

        image1_coords = []
        image2_coords = []
        for pair in pairs:
            image1_coords.append(list(pair[0]))
            image2_coords.append(list(pair[1]))

        image1_coords = np.array(image1_coords)
        image2_coords = np.array(image2_coords)

        # Remove duplicate rows to avoid singular matrices
        _,idx=np.unique(image1_coords, axis=0,return_index=True)
        image1_coords = image1_coords[np.sort(idx)]
        image2_coords = image2_coords[np.sort(idx)]

        transform_mat = transforms.ransac(image1_coords, image2_coords, 75, 30)
        print(transform_mat)
        image_t = transforms.inv_transform(image2, transform_mat)
        blended = transforms.blend_images(image1, image_t)
        blended_img = Image.fromarray(blended)

        path = os.path.join(output_dir, args.output)
        blended_img.save(path)
    else:
        return


def make_parser():
    parser = argparse.ArgumentParser(description='Assignment 2 Image transformations and Matching')
    subparsers = parser.add_subparsers(dest='subcommand')
    subparsers.required = True

    # Part 1 subparser
    parser_part1 = subparsers.add_parser('part1')
    parser_part1.add_argument('k', help='Number of clusters', type=int)
    parser_part1.add_argument('images', help = 'filenames without extention', nargs='+')
    parser_part1.add_argument('output', help='Output txt file')

    # Part 2 subparser
    parser_part2 = subparsers.add_parser('part2')
    parser_part2.add_argument('n', help='Transformation type', type=int)
    parser_part2.add_argument('image1', help='First image')
    parser_part2.add_argument('image2', help='Second image')
    parser_part2.add_argument('output', help='Output png')
    parser_part2.add_argument('coordinates', nargs='+')

    # Part 3 subparser  
    parser_part3 = subparsers.add_parser('part3')
    parser_part3.add_argument('image1', help='First image')
    parser_part3.add_argument('image2', help='Second image')
    parser_part3.add_argument('output', help='Output jpg file')

    return parser


def open_image(filename):
    if filename[-3:] == 'jpg':
        image = imread(os.path.join(part2_image_dir, filename))
    else:
        image = imread(os.path.join(part2_image_dir, filename + '.jpg'))

    return image


if __name__ == '__main__':
    main()
