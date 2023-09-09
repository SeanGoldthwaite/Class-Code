import unittest

import numpy as np
import numpy.testing
import cvlib

class CVTest(unittest.TestCase):
    def assert_nparray_equal(self, a, b):
        self.assertIsNone(np.testing.assert_array_equal(a, b))
    def assert_nparray_almost_equal(self, a, b):
        self.assertIsNone(np.testing.assert_array_almost_equal(a, b))

class TestConvolution(CVTest):
    def test_ones(self):
        image = np.ones((3,3))
        kernel = np.ones((3,3))
        expected = np.array([[4, 6, 4],
                             [6, 9, 6],
                             [4, 6, 4]])
        convolved = cvlib.convolve(image,kernel)
        self.assert_nparray_equal(expected, convolved)

        expected = np.array([[9, 9, 9],
                             [9, 9, 9],
                             [9, 9, 9]])
        convolved = cvlib.convolve(image,kernel,'mirror')
        self.assert_nparray_equal(expected, convolved)

    def test_ones_sep(self):
        image = np.ones((3,3))
        kernel = np.ones((3,3))
        expected = np.array([[4, 6, 4],
                             [6, 9, 6],
                             [4, 6, 4]])

        convolved = cvlib.convolve_sep(image,kernel)
        self.assert_nparray_equal(expected, convolved)

        expected = np.array([[9, 9, 9],
                             [9, 9, 9],
                             [9, 9, 9]])
        convolved = cvlib.convolve_sep(image,kernel,'mirror')
        self.assert_nparray_equal(expected, convolved)



    def test_identity(self):
        image = np.arange(9).reshape((3,3))
        kernel = np.zeros((3,3))
        kernel[1][1] = 1
        convolved = cvlib.convolve(image, kernel)
        self.assert_nparray_equal(image, convolved)


    def test_identity_sep(self):
        image = np.arange(9).reshape((3,3))
        kernel = np.zeros((3,3))
        kernel[1][1] = 1

        convolved = cvlib.convolve_sep(image, kernel)
        self.assert_nparray_equal(image, convolved)

    def test_separate(self):
        a = np.arange(3).reshape((3,1))
        b = np.arange(3).reshape((1,3))
        hx, hy = cvlib.separate(a*b)
        self.assert_nparray_equal(a*b, hx*hy)
    
    def test_separable_error(self):
        image = np.zeros((3,3))
        kernel = np.arange(9).reshape((3,3))
        self.assertRaises(cvlib.NotSeparableException, lambda : cvlib.convolve_sep(image, kernel))

    #some random tests
    def verify_separable(self, n):
        a = np.random.randint(10, size=n).reshape((n,1))
        b = np.random.randint(10, size=n).reshape((1,n))
        hx, hy = cvlib.separate(a*b)
        self.assert_nparray_almost_equal(a*b, hx*hy)

    def test_random_separable_5(self):
        for _ in range(20): self.verify_separable(5)
    def test_random_separable_4(self):
        for _ in range(20): self.verify_separable(4)
    def test_random_separable_3(self):
        for _ in range(20): self.verify_separable(3)


if __name__ == '__main__':
    unittest.main()
