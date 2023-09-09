import numpy as np
import math, sys
import cv2
from PIL import Image


def test_ransac():
    im1 = cv2.imread('part2-images/book1.jpg', cv2.IMREAD_COLOR)
    im1 = cv2.cvtColor(im1, cv2.COLOR_BGR2RGB)
    im2 = cv2.imread('part2-images/book2.jpg', cv2.IMREAD_COLOR)
    im2 = cv2.cvtColor(im2, cv2.COLOR_BGR2RGB)

    image1_coords = np.array([[318, 256], [534, 372], [316,670], [73,473]])
    image2_coords = np.array([[141, 131], [480, 159], [493,630], [64,601]])

    post, transform_mat = projective(im2, image1_coords, image2_coords)

    h, w, c = im1.shape

    sample_x = np.random.choice(w, size=10, replace=False).astype(np.float16)
    sample_y = np.random.choice(h, size=10, replace=False).astype(np.float16)

    sigma = 1
    sample_y += np.random.normal(loc=0, scale=sigma, size=sample_y.shape)

    image1_coords = np.array([[x, y] for x, y in zip(sample_x, sample_y)])
    a = np.vstack((image1_coords.transpose(), np.ones(image1_coords.shape[0])))
    b = np.dot(transform_mat, a)
    image2_coords = remove_homogeneous(b).transpose()

    best_transform = ransac(image1_coords, image2_coords, 10, 9e-13)

    post = inv_transform(im2, best_transform)
    blended = blend_images(post, im1)
    blended_img = Image.fromarray(blended)
    # blended_img.show()
    print(f'Comparing the real and estimated transformation matrices')
    print(f'Sum Squared Error: {np.mean((best_transform - transform_mat)**2)}')
    print(f'SSE / sum_all_vals: {np.mean((best_transform - transform_mat)**2) / np.mean(best_transform + transform_mat)}')

def ransac(image1_coords, image2_coords, n, threshhold):
    if image1_coords.shape[0] == 4:
        return transform_mat(image1_coords, image2_coords)
    elif image1_coords.shape[0] < 4:
        print('Not enough points to compute transform')
        return
    max_inliers = 0
    best_transform = np.identity(3)
    for _ in range(n):        
        # Takes sample to calculate transformation hypothesis
        row_idx = np.random.choice(image1_coords.shape[0], size=4, replace=False)
        sample1 = image1_coords[row_idx,:]
        sample2 = image2_coords[row_idx,:]

        # Rows not in samples to count inliers
        sample1_c = np.array([row for row in image1_coords if row not in sample1])
        sample2_c = np.array([row for row in image2_coords if row not in sample2])
        
        try:
            mat = transform_mat(sample1, sample2)
        except np.linalg.LinAlgError:
            continue

        # Apply transformation
        a = np.vstack((sample1_c.transpose(), np.ones(sample1_c.shape[0])))
        b = np.dot(mat, a)
        points = remove_homogeneous(b).transpose()

        # Count inliers
        inliers = 0
        for p1, p2 in zip(sample2_c, points):
            if cv2.norm(p1, p2, cv2.NORM_L2) < threshhold:
                inliers += 1

        if inliers > max_inliers:
            max_inliers = inliers
            best_transform = mat

    return best_transform


def blend_images(img1_arr, img2_arr):
    h1, w1, _ = img1_arr.shape
    h2, w2, _ = img2_arr.shape

    h, w = max(h1, h2), max(w1, w2)
    if h > 5000 or w > 5000:
        sys.exit(f'Image dims ({h, w, 3}) too large, exiting')
    res = np.zeros((h, w, 3))
    res.fill(127)

    for i, row in enumerate(img1_arr):
        for j, row in enumerate(row):
            res[i,j] = img1_arr[i,j]

    for i, row in enumerate(img2_arr):
        for j, row in enumerate(row):
            res[i,j] = (res[i,j] + img2_arr[i,j]) / 2

    return res.astype(np.uint8)


def translation(image_arr, image1_coords, image2_coords):
    mat = np.identity(3)
    mat[0,2] = image2_coords[0,0] - image1_coords[0,0] #T_x
    mat[1,2] = image2_coords[0,1] - image1_coords[0,1] #T_y

    homg_points = homogenize(image_arr)
    homg_points = np.dot(mat, homg_points)
    return reconstruct_2D(homg_points, image_arr)


def similarity(image_arr, image1_coords, image2_coords):
    im1_p2_rel = np.subtract(image1_coords[1,:], image1_coords[0,:])
    im2_p2_rel = np.subtract(image2_coords[1,:], image2_coords[0,:])

    ang1 = np.arctan2(im1_p2_rel[1], im1_p2_rel[0])
    ang2 = np.arctan2(im2_p2_rel[1], im2_p2_rel[0])

    theta = ang1 - ang2
    s, c = np.sin(theta), np.cos(theta)
    rot = np.array([[c, s], [-s, c]])

    p2 = np.dot(rot, image1_coords.transpose()).transpose()
    translation = np.mean(np.subtract(image2_coords, p2), axis=0)
    
    transform_mat = np.identity(3)
    transform_mat[0,2] = translation[0]
    transform_mat[1,2] = translation[1]
    transform_mat[:2,:2] = rot

    return inv_transform(image_arr, transform_mat)

def affine(image_arr, image1_coords, image2_coords):
    X = np.zeros((0,6))

    for point in image1_coords:
        a = np.append(point, 1)
        x = np.zeros((2, 6))
        x[0, :3] = a
        x[1, 3:] = a
        X = np.vstack((X, x))
    
    x_prime = image2_coords.reshape((6,1))

    transform_mat = np.linalg.solve(X, x_prime)
    transform_mat = np.vstack((transform_mat.reshape(2,3), [0, 0, 1]))

    return inv_transform(image_arr, transform_mat)


def projective(image_arr, image1_coords, image2_coords):
    # Find the transformation matrix
    image1_homg = np.append(image1_coords.transpose(), np.ones((1, 4)), axis=0)
    image2_homg = np.append(image2_coords.transpose(), np.ones((1, 4)), axis=0)

    a = np.linalg.solve(image1_homg[:,:3], image1_homg[:,3:])
    A = image1_homg[:,:3] * a.transpose()

    b = np.linalg.solve(image2_homg[:,:3], image2_homg[:,3:])
    B = image2_homg[:,:3] * b.transpose()

    # Transforms image1 to image2
    mat = np.dot(B, np.linalg.inv(A))
    # Transform image2 to image1
    mat_inv = np.linalg.inv(mat)

    return inv_transform(image_arr, mat_inv), mat_inv


# python a2.py part2 4 book1 book2 output 318,256 141,131 534,372 480,159 316,670 493,630 73,473 64,601
def inv_transform(image_arr, transform_mat):
    h, w, c = image_arr.shape
    corners = np.array([[0,0,1], [w, 0, 1], [0, h, 1], [w, h, 1]]).transpose()
    corners_transform = np.dot(transform_mat, corners)
    transform_dims = np.amax(corners_transform, axis=1)
    new_h, new_w = int(transform_dims[1]) + 1, int(transform_dims[0]) + 1
    if new_h > 5000 or new_w > 5000:
        sys.exit(f'Image dims ({new_h, new_w, 3}) too large, exiting')

    new_image = np.ones((new_h, new_w, 3), dtype=np.uint8) * 127
    new_image_homg = homogenize(new_image)

    mat_inv = np.linalg.inv(transform_mat)
    inv_transform = np.dot(mat_inv, new_image_homg)
    inv_transform = remove_homogeneous(inv_transform)
    
    pre_points = [(point[1], point[0]) for point in inv_transform.transpose()]
    post_points = [(int(point[1]), int(point[0])) for point in new_image_homg.transpose()]

    for pre_point, post_point in zip(pre_points, post_points):
        pre_h, pre_w = pre_point

        if within_bound(pre_h, pre_w, h - 1, w - 1):
            # Bilinear interpolation
            # Variables named like wheels on a car
            fl = image_arr[(math.floor(pre_h), math.floor(pre_w))] # (0, 0)
            fr = image_arr[(math.floor(pre_h), math.ceil(pre_w))] # (0, 1)
            rl = image_arr[(math.ceil(pre_h), math.floor(pre_w))] # (1, 0)
            rr = image_arr[(math.ceil(pre_h), math.ceil(pre_w))] # (1, 1)

            # Ugly way of getting the decimals of a float
            decimal_h, decimal_w = (pre_h - math.floor(pre_h), pre_w - math.floor(pre_w))

            color = (1 - decimal_h) * (1 - decimal_w) * fl + decimal_w * (1 - decimal_h) * fr + (1 - decimal_w) * decimal_h * rl + decimal_h * decimal_w * rr
            new_image[post_point] = color

    return new_image


def transform_mat(image1_coords, image2_coords):
    # Find the transformation matrix
    image1_homg = np.append(image1_coords.transpose(), np.ones((1, 4)), axis=0)
    image2_homg = np.append(image2_coords.transpose(), np.ones((1, 4)), axis=0)

    a = np.linalg.solve(image1_homg[:,:3], image1_homg[:,3:])
    A = image1_homg[:,:3] * a.transpose()

    b = np.linalg.solve(image2_homg[:,:3], image2_homg[:,3:])
    B = image2_homg[:,:3] * b.transpose()

    # Transforms image1 to image2
    mat = np.dot(B, np.linalg.inv(A))

    return mat


def transform(image_arr, transform_mat):
    homg_points = homogenize(image_arr)
    transform_points = np.dot(transform_mat, homg_points)

    return reconstruct_2D(transform_points, image_arr)


def homogenize(image_arr):
    h, w = image_arr.shape[:2]
    y, x = np.indices((h, w), dtype=int)
    homg_points = np.stack((x.ravel(), y.ravel(), np.ones(y.size)))

    return homg_points


def reconstruct_2D(homg_points, image_arr):
    # remove homogeneous coordinate
    homg_points = remove_homogeneous(homg_points)

    # moves upper-left corner to (0, 0)
    mins = np.amin(homg_points, axis=1)
    min_y, min_x = mins[1], mins[0]
    if min_x < 0 or min_y < 0:
        homg_points -= np.array([[min_x], [min_y]])

    #reconstruct original image
    dims = np.amax(homg_points, axis=1)
    w, h = int(dims[0]) + 1, int(dims[1]) + 1
    new_image = np.ones((h, w, 3), dtype=np.uint8) * 127

    pre_points = [(int(point[1]), int(point[0])) for point in homogenize(image_arr).transpose()]
    post_points = [(int(point[1]), int(point[0])) for point in homg_points.transpose()]
    
    for pre_point, post_point in zip(pre_points, post_points):
        new_image[post_point] = image_arr[pre_point]

    return new_image


def within_bound(y, x, h, w):
    return y >= 0 and y < h and x >= 0 and x < w 

# divides the first 2 rows by the 3rd 
def remove_homogeneous(homg_points):
    return np.array([homg_points[0,] / homg_points[2,], homg_points[1,] / homg_points[2,]])

#test_ransac()