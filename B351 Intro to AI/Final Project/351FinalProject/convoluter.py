import numpy as np
import loader
import math
import random as rand


class PoolingLayer:

    def __init__(self, size):
        """
        @param size: size of the sections to be max pooled, stride will be the same as the size
        """
        self.size = size
        self.last_image = None

    def feedforward(self, image):
        result = []
        self.last_image = []

        for image_region, i, j in iterate_regions_pooling(image, self.size):
            self.last_image.extend(self.backprop_helper(image_region))
            result.append(np.amax(image_region))

        new_dim = int(math.sqrt(len(result)))
        return np.asarray(result).reshape((new_dim, new_dim))

    def backprop(self, gradient):
        vals = gradient[0][0].transpose().tolist()
        self.last_image = list(map(lambda x: self.map_func(x, vals), self.last_image))
        new_dim = int(math.sqrt(len(self.last_image)))
        return np.asarray(self.last_image).reshape((new_dim, new_dim))

    def map_func(self, x, vals):
        return vals.pop(0)[0] if x == 1 else 0

    def backprop_helper(self, image_region):
        h, w = image_region.shape
        max_ind = h, w
        max = image_region[0, 0]

        # finds max value
        for i in range(h):
            for j in range(w):
                if image_region[i, j] > max:
                    max = image_region[i, j]
                    max_ind = i, j

        # changes everything but max value to 1
        for i in range(h):
            for j in range(w):
                if (i, j) == max_ind:
                    image_region[i, j] = 1
                else:
                    image_region[i, j] = 0

        # if all values are the same, sets random index as 1
        if not np.isin(1, image_region):
            image_region[rand.randint(0, w - 1), rand.randint(0, h - 1)] = 1

        return image_region.flatten().tolist()


class reLU:

    def __init__(self):
        pass

    def feedforward(self, image):
        return np.vectorize(self.activation_function)(image)

    def backprop(self, gradient):
        return gradient

    def activation_function(self, x):
        return sigmoid(x)


class FilterLayer:

    def __init__(self, filter_size):
        """
        @param filter_size: size of the filter/kernel, filter will be square
        """
        self.size = filter_size
        self.filter = np.random.rand(filter_size, filter_size)
        self.last_image = None
        self.last_convolution = None

    def feedforward(self, image):
        self.last_image = image
        result = []

        # loops through image regions, multiplies
        for image_region, i, j in iterate_regions(image, self.size):
            product = np.multiply(image_region, self.filter)
            result.append(product.sum())

        new_dim = int(math.sqrt(len(result)))
        self.last_convolution = np.asarray(result).reshape((new_dim, new_dim))
        return self.last_convolution

    def backprop(self, gradient):
        H, W = self.last_image.shape
        x_H, x_W = self.last_convolution.shape

        # calculates space_delta
        space_delta = np.zeros(self.last_convolution.shape)
        for i in range(x_H):
            for j in range(x_W):
                if self.last_image[i, j] == 0:
                    space_delta[i, j] = 0
                else:
                    space_delta[i, j] = gradient[i, j] / self.last_convolution[i, j]

        filter_error = np.zeros((self.size, self.size))
        # calculates gradient for the filter
        for m in range(self.size):
            for n in range(self.size):
                sum = 0
                for i in range(H - self.size - 1):
                    for j in range(W - self.size - 1):
                        sum += space_delta[i, j] * self.last_image[i + m, j + n]
                if sum == 0:
                    filter_error[m, n] = 0
                else:
                    filter_error[m, n] = gradient.mean() / sum

        # applies changes
        filter_error = np.multiply(filter_error, 0.1)
        self.filter = np.subtract(self.filter, filter_error)

        # calculates gradient for the next layer
        result = np.zeros(self.last_image.shape)

        for i in range(H - self.size):
            for j in range(W - self.size):
                sum = 0
                for m in range(self.size):
                    for n in range(self.size):
                        # print(i, j, m, m)
                        sum += gradient[i - m, j - n] * self.filter[m, n] * self.last_image[i, j]
                if sum == 0:
                    result[i, j] = 0
                else:
                    result[i, j] = gradient.mean() / sum

        return result

    def printFilter(self):
        print(self.filter)


def iterate_regions_pooling(image, width):
    h, w = image.shape
    new_h = h // 2
    new_w = w // 2

    for i in range(new_h):
        for j in range(new_w):
            image_region = image[(i * width):(i * width + width), (j * width):(j * width + width)]
            yield image_region, i, j


def iterate_regions(image, width):
    h, w = image.shape

    for i in range(h - width + 1):
        for j in range(w - width + 1):
            image_region = image[i:i + width, j:j + width]
            yield image_region, i, j


def sigmoid(x):
    return 1 / (1 + math.exp(-x))


def dsigmoid(y):
    return y * (1 - y)
