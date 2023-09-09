import gzip
import numpy as np
import matplotlib.pyplot as plt
import os

image_size = 28
num_training_images = 60000
num_testing_images = 10000

dir = os.path.dirname(__file__)


def load_data():
    """returns a tuple of (training_data, testing_data)
        training_data and testing_data are each lists of tuples
        *_data[0] is a 28x28 numpy matrix representing the image
        *_data[1] is a length 10 python list representing the correct label
            for the image"""
    training_images = get_training_images()
    training_labels = get_training_labels()
    testing_images = get_testing_images()
    testing_labels = get_testing_labels()
    return (list(zip(training_images, training_labels)),
            list(zip(testing_images, testing_labels)))


def get_training_images():
    f = gzip.open(os.path.join(dir, 'data', 'train-images-idx3-ubyte.gz'), 'r')
    f.read(16)
    buf = f.read(image_size * image_size * num_training_images)
    data = np.frombuffer(buf, dtype=np.uint8).astype(np.float32)
    data = data.reshape(num_training_images, image_size, image_size, 1)
    res = [np.asmatrix(item) for item in data]
    return res


def get_training_labels():
    f = gzip.open(os.path.join(dir, 'data', 'train-labels-idx1-ubyte.gz'), 'r')
    f.read(8)
    result = []
    for i in range(60000):
        buf = f.read(1)
        label = np.frombuffer(buf, dtype=np.uint8).astype(np.int64)
        result.append(vectorize(label))
    f.close()
    return result


def get_testing_images():
    f = gzip.open(os.path.join(dir, 'data', 't10k-images-idx3-ubyte.gz'), 'r')
    f.read(16)
    buf = f.read(image_size * image_size * num_testing_images)
    data = np.frombuffer(buf, dtype=np.uint8).astype(np.float32)
    data = data.reshape(num_testing_images, image_size, image_size, 1)
    res = [np.asmatrix(item) for item in data]
    return res


def get_testing_labels():
    f = gzip.open(os.path.join(dir, 'data', 't10k-labels-idx1-ubyte.gz'), 'r')
    f.read(8)
    result = []
    for i in range(10000):
        buf = f.read(1)
        label = np.frombuffer(buf, dtype=np.uint8).astype(np.int64)
        result.append(vectorize(label))
    f.close()
    return result


def vectorize(n):
    res = np.zeros((10, 1))
    res[n] = 1.0
    return res


def show_image(img):
    image = np.asarray(img).squeeze()
    plt.imshow(image)
    plt.show()
