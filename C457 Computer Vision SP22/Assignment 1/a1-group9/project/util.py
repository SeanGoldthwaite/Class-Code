import math
import numpy as np

# Iterate all pixels around pos that are within the bounds of 'shape'
def iterate_pixels_around(pos, shape):
    for dy in [-1, 0, 1]:
        for dx in [-1, 0, 1]:
            if dx != 0 or dy != 0:
                y = pos[0] + dy
                x = pos[1] + dx
                if x >= 0 and y >= 0 and x < shape[1] and y < shape[0]:
                    yield((y, x))

# Convert `value` from range [a_min, a_max] to range [b_min, b_max]
def lerp_range(value, a_min, a_max, b_min, b_max):
    value = (value - a_min) / (a_max - a_min)
    return value * (b_max - b_min) + b_min

# Calculate the centroid of the given dataset (where data is a dictionary mapping points to weights)
def get_centroid(data):
    oy = 0
    ox = 0
    n = 0
    for k, v in data.items():
        oy += k[0] * v
        ox += k[1] * v
        n += v
    return (oy/n, ox/n)

# Normalize dataset from [min, max] to [0, 1]
def get_normalized_dataset(data):
    maxval = max(data.values())
    minval = min(data.values())
    out = {}
    for k,v in data.items():
        out[k] = lerp_range(v, minval, maxval, 0, 1)
    return out

# Get maximum value in dataset
def get_dataset_maxval(data):
    max_v = -math.inf
    ret = None
    for k,v in data.items():
        if v > max_v:
            max_v = v
            ret = k
    return ret

# Create a gaussian kernel with the given standard deviation
def make_gaussian_1d(stddev):
    halfwidth = math.ceil(stddev) * 3
    k = halfwidth * 2 + 1
    array = np.zeros((k,), np.float64)
    for i in range(k):
        x = i - halfwidth
        z = x / stddev
        n = math.e**(-0.5 * z**2)
        array[i] = n/math.sqrt(math.pi*2)
    return array / np.sum(array)

# Get indices of all non-zero points in the given array
def get_points_1d(array: np.ndarray, thresh: float = 0):
    ret = []
    for i in range(array.shape[0]):
        if array[i] >= thresh:
            ret.append(i)
    return ret

# Supress non-maximals in an array
def supress_nonmaximals_1d(array, dip_thresh=0):
    ret = np.copy(array)
    for i in range(array.shape[0]):
        v = array[i]
        j = i - 1
        is_max = True
        while j >= 0:
            nv = array[j]
            if nv > v:
                is_max = False
                break
            elif v > nv + dip_thresh:
                break
            j -= 1
        if is_max:
            j = i + 1
            while j < array.shape[0]:
                nv = array[j]
                if nv >= v:
                    is_max = False
                    break
                elif v > nv + dip_thresh:
                    break
                j += 1
        if not is_max:
            ret[i] = 0
    return ret

# Get a list of all disconnected islands in `array`
def get_islands(array, lo_thresh, hi_thresh, absorb_maximals=True):
    ret = []
    used = set()
    for iy in range(array.shape[0]):
        for ix in range(array.shape[1]):
            value = array[iy, ix]
            pos = (iy, ix)
            if value >= hi_thresh and not pos in used:
                data = {}
                data[pos] = value
                ret.append(data)
                proc = {}
                proc[pos] = value
                used.add(pos)
                while len(proc) > 0:
                    proc_pos = list(proc.keys())[0]
                    proc_value = proc.pop(proc_pos)
                    for p in iterate_pixels_around(proc_pos, array.shape):
                        p_value = array[p[0], p[1]]
                        # only hi-thresh may consume hi-thresh
                        if not p in used and (p_value >= lo_thresh) and\
                                (absorb_maximals or proc_value >= hi_thresh or p_value < hi_thresh):
                            data[p] = p_value
                            proc[p] = p_value
                            used.add(p)
    return ret

# Get the length of the given vector
def length(vec):
    return np.sqrt(np.sum(vec**2))

# Normalize the given vector
def normalize(vec):
    return vec / length(vec)