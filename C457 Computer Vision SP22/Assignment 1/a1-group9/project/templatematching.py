import numpy as np
import cvlib
from PIL import Image

def compute_template_mask(image, template):
    ker = 2 * template - 1
    # leftover stuff from simplifying the hamming distance equation into a convolution 
    resid = template.size - np.sum(template)
    convolution = cvlib.convolve(image, ker, dtype=np.float64) + resid
    np.divide(convolution, (np.max(convolution)/255), out=convolution)
    return convolution

def iterate_pixels_around(pos, shape):
    for dy in [-1, 0, 1]:
        for dx in [-1, 0, 1]:
            # these ifs are redundant
            # but when they're not there
            # weird things happen
            if dx != 0:
                x = pos[1] + dx
            else:
                x = pos[1]

            if dy != 0:
                y = pos[0] + dy
            else:
                y = pos[0]
            
            if x >= 0 and y >= 0 and x < shape[1] and y < shape[0] and not(dx == 0 and dy == 0):
                yield((y, x))

def mask_to_clusters(mask, min_cluster_size, max_cluster_size, template):
    # Modified Mean shift clustering to find centers of notes to outline
    # General idea is pick a detected pixel and start a new cluster
    # Add the neighbors of that pixel that were also detected to the cluster
    # Once no more detected pixels border the cluster
    # Calculate the center and that should be the center of a notehead
    height, width  = mask.shape
    clusters = []
    max_point_dist = cvlib.euclidean_distance((0, 0), template.array.shape)

    for i in range(height):
        for j in range(width):
            # find a pixel that was detected, start new cluster
            if mask[i, j]:
                new_cluster = [(i, j)]
                while True:
                    # expand the current cluster 
                    for pixel in new_cluster:
                        points = [point for point in iterate_pixels_around(pixel, mask.shape) if mask[point] and cvlib.euclidean_distance((i, j), point) < max_point_dist]
                        if len(points) > 2:
                            for point in points:
                                new_cluster.append(point)
                                mask[point] = False

                        if len(new_cluster) >= max_cluster_size:
                            break
                    
                    # Only keep clusters of a certain size
                    if len(new_cluster) >= min_cluster_size:
                        # find the center of the current cluster
                        center = np.mean(new_cluster, axis=0)
                        center = (int(center[0]), int(center[1]))
                        # add that to cluster_centers
                        clusters.append(center)
                    break
    
    return clusters


def show_mask(image_arr, mask_arr):
    image_arr = np.array(Image.fromarray(image_arr).convert('RGB'))
    height, width, channels = image_arr.shape
    for i in range(height):
        for j in range(width):
            if mask_arr[i, j]:
                image_arr[i, j] = (255, 0, 0)

    Image.fromarray(image_arr).show()