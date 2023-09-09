## B457 Final Project

### Running

My project uses the pre-trained VGG-19 network which is too large for git to handle and needs to be downloaded [here](https://www.vlfeat.org/matconvnet/models/imagenet-vgg-verydeep-19.mat) and placed in the directory that contains style_transfer.py

My project runs from the command line with just 2 arguments: Input and output. These files must be placed in the `images/` directory. For example `python style_transfer.py tubingen.jpg shipwreck.jpg` If the images are *jpgs* then you can leave the `.jpg` file extension off like `python style_transfer.py tubingen shipwreck`

It is only configured to run on the CPU which takes about 40-60 minutes, but it saves *checkpoint* images every 100 iterations to the `checkpoints` directory. The image changes most in the first 300ish transformations so you can get preliminary images quicker. 

Github Link: https://github.iu.edu/sgoldthw/B457-Style-Transfer