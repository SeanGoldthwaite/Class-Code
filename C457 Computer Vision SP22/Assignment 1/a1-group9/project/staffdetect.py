import numpy as np
import math
import util

# A HoughSpace object with parameters (center, size)
# Center values are normalized [0, 1] where 0=top and 1=bottom
# Size is radius of entire staff. i.e. two gap sizes
class HoughSpaceStaff:
    def __init__(self, shape):
        self.shape = shape
        self.image = np.zeros(shape, dtype=np.float64)
        self.center_range = [0, 1]
        self.size_range = [0, 0.5]
    # Get the center value for the given center index
    def get_center_value(self, ci):
        return util.lerp_range(ci, 0, self.shape[0]-1, self.center_range[0], self.center_range[1])
    # Get the center index for the given center value
    def get_center_index(self, c):
        ci = math.floor(util.lerp_range(c, self.center_range[0], self.center_range[1], 0, self.shape[0]-1))
        return ci
    # Get the size value for the given size index
    def get_size_value(self, si):
        return util.lerp_range(si, 0, self.shape[1]-1, self.size_range[0], self.size_range[1])
    # Get the size index for the given size value
    def get_size_index(self, s):
        si = math.floor(util.lerp_range(s, self.size_range[0], self.size_range[1], 0, self.shape[1]-1))
        return si
    def get_size_mode(self):
        p = np.argmax(self.image)
        index = np.unravel_index(np.argmax(self.image, axis=None), self.shape)
        return self.get_size_value(index[1])
    def get_points_of_size(self, size):
        si = self.get_size_index(size)
        return self.image[:,si]
    def apply_size(self, size):
        si = self.get_size_index(size)
        self.image[:,:si] *= 0
        self.image[:,si+1:] *= 0
    # Cast a vote for the given position with the given magnitude
    def cast_vote(self, y, magnitude):
        if magnitude <= 0:
            return
        ci = self.get_center_index(y)
        for si in range(self.shape[1]):
            radius = self.get_size_value(si)
            scalar = 1
            if si > 20:
                scalar = util.lerp_range(radius, self.get_size_value(20), self.size_range[1], 1, 0)
            if si >= 10 and si < self.shape[1]:
                # need at least 10 pixels
                # higher radii are deemed 'less likely'
                self.image[ci, si] += magnitude * scalar
        for gap in [-2, -1, 1, 2]:
            for ci in range(self.shape[0]):
                # for cy, find size that matches
                cy = self.get_center_value(ci)
                radius = (cy - y) * (2 / gap)
                si = self.get_size_index(radius)
                scalar = 1
                if si > 20:
                    scalar = util.lerp_range(radius, self.get_size_value(20), self.size_range[1], 1, 0)
                if si >= 10 and si < self.shape[1]:
                    self.image[ci, si] += magnitude * scalar
    # Get this hough space but normalized
    def get_normalized(self):
        maxval = np.max(self.image)
        if maxval <= 1e-5:
            return self.image
        else:
            return self.image / maxval

def do_staffs_overlap(a, b):
    a1 = a[0] - a[1]
    a2 = a[0] + a[1]
    b1 = b[0] - b[1]
    b2 = b[0] + b[1]
    return b2 > a1 and b1 < a2

def remove_outlier_staffs(staffs, max_dev=1):
    if len(staffs) <= 2:
        return staffs
    radii = []
    for s in staffs:
        radii.append(s[1])
    std = np.std(radii)
    mean = np.mean(radii)
    if std > max_dev:
        max_z = 0
        index = -1
        for i, staff in enumerate(staffs):
            z = abs((staff[1] - mean) / std)
            if z > max_z:
                max_z = z
                index = i
        return remove_outlier_staffs(staffs[:index] + staffs[index+1:])
    return staffs

def remove_overlapping_staffs(staffs):
    i = 0
    while i < len(staffs):
        j = i + 1
        did_remove = False
        while j < len(staffs):
            if do_staffs_overlap(staffs[i], staffs[j]):
                if staffs[i][1] < staffs[j][1]:
                    # i is smaller, remove i
                    did_remove = True
                    break
                else:
                    # j is smaller, remove j
                    staffs.pop(j)
                    continue
            else:
                j += 1
        if did_remove:
            staffs.pop(i)
        else:
            i += 1
