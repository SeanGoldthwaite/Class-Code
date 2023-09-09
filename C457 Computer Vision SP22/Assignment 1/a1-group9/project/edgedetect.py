from cmath import isnan
from ctypes import Union
import cvlib
import numpy as np
import math
import itertools
import util

a = np.array([-1, 0, 1], dtype=np.float64).reshape((3, 1))
b = np.array([1, 2, 1], dtype=np.float64).reshape((1, 3))
sobel_y = (a * b) / 8.0
sobel_x = np.transpose(sobel_y)

class Point:
    def __init__(self, p_pos, p_value):
        self.pos = p_pos
        self.value = p_value
        self.weight = 1
    def dis(self, other):
        return abs(self.pos - other.pos)
    def merge(self, other):
        weightsum = self.weight + other.weight
        p = Point((self.pos * self.weight + other.pos * other.weight) / weightsum, (self.value * self.weight + other.value * other.weight) / weightsum)
        p.weight = weightsum
        return p
    def merge_unweighted(self, other):
        p = Point((self.pos + other.pos) / 2, (self.value + other.value) / 2)
        p.weight = (self.weight + other.weight) / 2
        return p
    def importance(self):
        return self.value * self.weight#**2
    def __lt__(self, other):
        return self.dis() < other.dis()
    def __eq__(self, other):
        return self.dis() == other.dis()

# Apply magnitude step of sobel operator
def apply_sobel_magnitude(image):
    return np.sqrt(cvlib.convolve_sep(image, sobel_x, dtype=np.float64) ** 2.0 + cvlib.convolve_sep(image, sobel_y, dtype=np.float64) ** 2.0)

# Apply orientation step of sobel operator
def apply_sobel_orientation(image):
    return np.arctan2(cvlib.convolve_sep(image, sobel_y, dtype=np.float64), cvlib.convolve_sep(image, sobel_x, dtype=np.float64))

# Combine the magnitude and orientation sobel components into a normal map
def combine_sobel(magnitude_map, orientation_map, padding='zero'):
    assert magnitude_map.shape == orientation_map.shape
    magview = cvlib.ImageViewer(magnitude_map, padding)
    orview = cvlib.ImageViewer(orientation_map, padding)
    def get_pixel(iy, ix):
        magnitude = magview(iy, ix) / 255.0
        if magnitude > 1e-5:
            theta = orview(iy, ix)
            return np.array([
                math.sin(theta) * magnitude,
                math.cos(theta) * magnitude,
                math.sqrt(1.0 - magnitude)
            ])
        else:
            return np.array([0, 0, 1])
    return np.array([[get_pixel(iy, ix) for ix in range(magnitude_map.shape[1])] for iy in range(magnitude_map.shape[0])])

direction_dr = util.normalize(np.array([1, 1]))
direction_dl = util.normalize(np.array([1, -1]))

# Create a normalmap for the given image (as a heightmap), by estimating the
# gradient for each pixel at 45 degree angles.
# The returned normal map will have the shape [W, H, 3], and each pixel will be
# represented by the vector [X, Y, Z], where X and Y represent image coordinates,
# and 'Z' points 'out' of the screen.
# As such, X and Y have range [-1.0, 1.0] while Z has range [0.0, 1.0]
def create_normalmap(image, padding='zero'):
    image = image.astype(np.float32) / 255.0
    im = cvlib.ImageViewer(image, padding)
    def get_pixel(iy, ix):
        vec_dr = (im(iy + 1, ix + 1) - im(iy, ix)) * direction_dr
        vec_dl = (im(iy, ix + 1) - im(iy + 1, ix)) * direction_dl
        vec = (vec_dr + vec_dl) / math.sqrt(2)
        veclen = util.length(vec)
        if veclen >= 1:
            return util.normalize(np.array([vec[0], vec[1], 0]))
        else:
            v = np.array([vec[0], vec[1], math.sqrt(1.0 - vec[0]**2 - vec[1]**2)])
            return v
    return np.array([[get_pixel(iy, ix) for ix in range(image.shape[1])] for iy in range(image.shape[0])])

# Supress non-maximal pixels along the image's edges.
# Returns a new magnitude map with non-maximals removed.
def supress_nonmaximals(magnitude_map, orientation_map, padding='zero'):
    assert magnitude_map.shape == orientation_map.shape
    ret = np.copy(magnitude_map)
    magview = cvlib.ImageViewer(magnitude_map, padding)
    orview = cvlib.ImageViewer(orientation_map, padding)
    for iy in range(magnitude_map.shape[0]):
        for ix in range(magnitude_map.shape[1]):
            magnitude = magnitude_map[iy, ix]
            theta = orientation_map[iy, ix]
            is_maximum = True
            if magnitude > 1e-5:
                vec = np.array([math.sin(theta), math.cos(theta)]) * magnitude
                vec = util.normalize(vec)
                for m in [-1, 1]:
                    v = vec * m
                    p = magview.lerp(iy + v[0], ix + v[1])
                    if p > magnitude:
                        is_maximum = False
                        break
            if not is_maximum:
                ret[iy][ix] = 0
    return ret

# Apply canny thresholding to the given magnitude map.
def apply_canny(magnitude_map, lo_thresh, hi_thresh, padding='zero'):
    assert hi_thresh >= lo_thresh
    ret = np.empty(magnitude_map.shape)
    lo_map = set()
    hi_map = set()
    # Determine which pixels are above thresholds
    for iy in range(magnitude_map.shape[0]):
        for ix in range(magnitude_map.shape[1]):
            mag = magnitude_map[iy, ix]
            if mag >= hi_thresh:
                hi_map.add((iy, ix))
            elif mag >= lo_thresh:
                lo_map.add((iy, ix))
    to_traverse = set(hi_map)
    # Traverse pixels around every detected edge, and add them as edges if
    # they are above low threshold
    while len(to_traverse) > 0:
        hi_pos = to_traverse.pop()
        for pos in util.iterate_pixels_around(hi_pos, magnitude_map.shape):
            if pos in lo_map:
                to_traverse.add(pos)
                lo_map.remove(pos)
                hi_map.add(pos)
    # Apply changes
    for iy in range(ret.shape[0]):
        for ix in range(ret.shape[1]):
            ret[iy, ix] = magnitude_map[iy, ix] if (iy, ix) in hi_map else 0
    return ret
