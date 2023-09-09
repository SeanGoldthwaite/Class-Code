
import numpy as np
from PIL import Image
import edgedetect
import math

def gaussian (l=5, s=1.):
    a = np.linspace(-(l-1)/2, (l-1)/2, l)
    g = np.exp(-0.5 * np.square(a) / np.square(s))
    result = np.outer(g, g)
    result /= np.sum(result)
    return result
    
# separate a rank 1 matrix into a product of two column vectors
def separate(matrix):

    source_i, source_j, source = -1, -1, np.inf

    # find the smallest nonzero entry in the matrix
    for i in range(len(matrix)):
        for j in range(len(matrix[0])):
            if matrix[i][j] != 0 and matrix[i][j] < source:
                source_i, source_j, source = i, j, matrix[i][j]

    hy = matrix[source_i,:].reshape(1, (len(matrix)))
    hx = matrix[:,source_j].reshape((len(matrix)), 1) / source
    return hx, hy

# Implement a function that convolves a greyscale image I with an arbitrary two-dimensional kernel H
# padding type can be 'zero' or 'mirror' at the moment 2.22.2022
# dtype parameter is necessary because numpy has problems when the image and kernel have different types.
# Generally I recommend just using np.float64, and converting down when necessary
def convolve(image, kernel, dtype=None, padding='zero'):
    #get dimension of kernel
    k_height, k_width = kernel.shape[0], kernel.shape[1]
    #get kernel center
    k_center_y, k_center_x = (k_height-1)//2, (k_width-1)//2
    if dtype == None:
        dtype = image.dtype
    convolution = np.zeros(image.shape, dtype=dtype)
    images = [[np.roll(image, (iy-k_center_y, ix-k_center_x), axis=(0, 1)) for ix in range(k_width)] for iy in range(k_height)]
    # TODO: obey padding rule
    for iy in range(k_height):
        for ix in range(k_width):
            np.add(convolution, images[iy][ix] * kernel[iy][ix], out=convolution, dtype=dtype)
    return convolution

class NotSeparableException (Exception):
    pass

# Implement a function that convolves a greyscale image I with a separable kernel H
# padding type can be 'zero' or 'mirror' at the moment 2.22.2022
def convolve_sep(image, kernel, dtype=None, padding='zero'):
    #test if kernel is separable
    if np.linalg.matrix_rank(kernel) != 1:
        raise NotSeparableException ("Tried to separate a non-separable matrix")

    #separate the kernel into two vectors
    hx, hy = separate(kernel)

    #convolve by one vector and then the other
    result = convolve(image, hx, dtype, padding)
    result = convolve(result, hy, dtype, padding)

    #return the result
    return result


class PaddingTypeException(Exception):
    pass

def euclidean_distance (p1, p2):
    return np.linalg.norm(np.subtract(p2, p1))

class ImageViewer:
    def __init__(self, image, padding_type):
        self.image = image
        self.padding_type = padding_type
        self.dim_y = len(image)
        self.dim_x = len(image[0])

    def __zero_padding(self, i, j):
        if i < 0 or j < 0:
            return 0
        if i >= self.dim_y or j >= self.dim_x:
            return 0
        return self.image[i][j]

    def __mirror_padding(self, i, j):
        if i < 0: i = -i
        if j < 0: j = -j
        if i >= self.dim_y: i = 2*self.dim_y - i - 1
        if j >= self.dim_x: j = 2*self.dim_x - j - 1
        return self.image[i][j]

    def __getitem__(self, key):
        return self.image[key]

    def lerp(self, iy, ix):
        ix1 = math.floor(ix)
        ix2 = math.ceil(ix)
        iy1 = math.floor(iy)
        iy2 = math.ceil(iy)
        tx = ix - ix1
        ty = iy - iy1
        t11 = (1 - tx) * (1 - ty)
        t21 = tx * (1 - ty)
        t12 = (1 - tx) * ty
        t22 = tx * ty
        p11 = self(int(iy1), int(ix1))
        p12 = self(int(iy2), int(ix1))
        p21 = self(int(iy1), int(ix2))
        p22 = self(int(iy2), int(ix2))
        return p11*t11 + p12*t12 + p21*t21 + p22*t22

    def __call__(self, iy, ix):
        if self.padding_type == 'zero':
            return self.__zero_padding(iy, ix)
        if self.padding_type == 'mirror':
            return self.__mirror_padding(iy, ix)
        raise PaddingTypeException ("Padding style not supported")