# random number generator
import random
from typing import Tuple

import unittest
from PIL import Image

# for drawing text over image
from PIL import ImageDraw

# sample unit test class. a test is created by subclassing unittest.TestCase
class TestWhite2Red(unittest.TestCase):
    def test_white(self):
        # tuples are not automatically compared by value, so assertTupleEqual is needed
        self.assertTupleEqual(white2red(200), (200, 0, 0))

    def test_other(self):
        self.assertTupleEqual(white2red(50), (50, 50, 50))


def white2red(grayscale_val : int) -> Tuple[int, int, int]:
    if grayscale_val >= 190:
        return (grayscale_val, 0, 0)
    return (grayscale_val, grayscale_val, grayscale_val)

if __name__ == '__main__':

    # load the image
    im = Image.open('first_photograph.png')

    #Check it's  width ,  height, and number of  color channels
    print('Image is %s pixels  wide. '%im.width)
    print('Image is %s  pixels high. '%im.height)
    print('Image mode  is %s.'% im.mode) #(8-bit pixels, black and white)

    #pixels are accessed via a (X,Y) tuple
    print('Pixel value is %s '% im.getpixel((10 ,10)))
    #pixels can be modified by specifying the coordinate and RGB value
    im.putpixel((10 ,10), 20)
    print('New pixel value is %s'%im.getpixel((10 ,10)))

    # Create a new blank color image the same  size as the input
    colorim = Image.new('RGB', (im.width , im.height), color =0)
    # Loops over the new color image and
    # fills in brighter area that was white first grayscale image we loaded with red colors!
    # Basically we transformed the gray colored first ever photographh into a red colored one!
    for x in range(im.width):
        for y in range(im.height):
            colorim.putpixel((x,y), white2red(grayscale_val=im.getpixel((x,y))))

    colorim.show()
    colorim.save('output.png')

    # adding text on top of the image at a random position
    colorim_with_text = ImageDraw.Draw(colorim)
    # generate a random X-coordinate and a random Y-coordinate
    text_x_coord = random.randint(0 , colorim.width//2)
    text_y_coord = random.randint(0 , colorim.height)
    colorim_with_text.text((text_x_coord, text_y_coord),
                        'View from the Window at Le Gras',(255,255,255))
    colorim.save('output_with_text.png')

    unittest.main() # run sample unit tests
