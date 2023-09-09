# 351FinalProject

## GUI
#### Dependancies
```
Tkinter: Used to implement the GUI.
Pillow: Used to grab the image and resize to correct parameters.(pip install Pillow)
win32gui: Used to assist in the process of grabbing the image. (Can be done by pip install pywin32)
HDRFullyConnected: Used to get the predictions.
NeuralNetwork: Used to get predictions.
```

#### Clear
Used to clear the reset the canvas after an image has been drawn.

#### Paint
Uses binding from the left mouse button in order to draw onto the canvas

#### Calculate
Uses Wi32gui to grab the exact location of where the canvas is on your sceen then uses Pillow to grab that image on the location. It then saves that image and resizes it in order for it to fit the parameters of the predictCustom function in HDRFullyConnected.

## Neural Network
#### Dependancies
```
Numpy: Used for the vast majority of matrix math calculations. ( 'pip install numpy' in command line)
Random: Used for an inherent randomness to creating new weights.
Math: Used for other, non-matrix math related calculations.
OS: Used for file directories.
```
#### Constructor
The Neural Network class requires at least 4 arguments, but up to 6 can be used. They are:
```
int inputNodes: The number of input nodes
int hiddenPerceptrons: The number of hidden nodes in the first layer
int hidden2Perceptrons: The number of hidden nodes in the second layer
int outputPerceptrons: The number of output nodes
float learningRate: Set to 0.1 by default, determines the overall learning rate for the network. Must be greater than 0.
boolean loadFromFile: Set to False by default, determines if the neural network is completely new or if it shall load its weights from the /weights/ folder
```

#### Guess
The Neural Network will attempt to guess given any array of inputs, so long as its length matches the number of input nodes. **Must be fed an array, not a numpy matrix.**

#### Train
The Neural Network will first attempt to guess given any array of inputs, so long as its length matches the number of input nodes. Then it will calculate the errors from the actual output array and backpropagate the error across the weights. **Must be fed two arrays, both input and output, not numpy matrices.**

#### SaveNetwork
The Neural Network will save all weights to their respective .npy files in the /weights/ folder.

## HDRFullyConnected
#### Dependancies
```
NeuralNetwork: Self explanatory
Numpy: Used for the vast majority of matrix math calculations. ( 'pip install numpy' in command line)
Random: Used for an inherent randomness to feeding data into the fully connected network.
Loader: Used to get the data to be used for training.
Pillow: Used to import custom images. ( 'pip install Pillow' in command line)
```

#### GetGuess
Given a neural network and a piece of a data **which must include both an input array/matrix and an output array/matrix**, will return both the network's guess and the actual answer.

#### GetGuessCustom
Given a neural network and a piece of data **which must include an input array/matrix**, will return the network's guess.

#### Train
Given a neural network, an integer of the number of times the dataset should be run through, and a training dataset, continuously push the training data through until all epochs are completed.

#### Predict
Given a neural network and a testing dataset, run through all data in the testing set and calculate the percentage of corrected guesses from the network.

#### PredictCustom
Helper function for gui.py, **should not be used otherwise.**

#### Running
By default, running the file will initialize a 784-100-100-10 Nerual Network with a learning rate of 0.001, which does read from the the /weights/ folder. It will also run the predict function on all of the testing data. Uncommenting the line of code with the train function will allow it to train for as many epochs as desired.


## convoluter.py
Attempt at a convolutional layer for a CNN

#### Dependancies:
```
Numpy: Used for matrix multiplication and reshaping arrays
Loader.py: For loading mnist data
Math: Square rooting
Random: To intialize random kernels
```
### FilterLayer:

#### feedforward:
Applies a filter/kernel to the input image with no padding
#### backprop:
Does not work. There is a lot of conplicated math I don't understand behind this stage

### PoolingLayer:
#### feedforward:
Applies max pooling with specified size to input image
#### backprop:
Orients the gradient values from the previous layer to where the maxs were in the forward pass

### reLU:
#### feedforward:
Applies the specified activation function to the input image
#### backprop:
Simply passes input to next layer
This may not be the proper way to do reLu backprop

#### Example use:
```
f1 = convoluter.FilterLayer(3)
re1 = convoluter.reLU()
f2 = convoluter.FilterLayer(3)
re2 = convoluter.reLU()
p1 = convoluter.PoolingLayer(2)

data = rand.choice(training_data)
data.addOne()	img = data.inputs
img = re0.feedforward(img)
img = f1.feedforward(img)
img = re1.feedforward(img)
img = f2.feedforward(img)
img = re2.feedforward(img)	
img = p1.feedforward(img)	
img = img.flatten().tolist()	
gradients = network.train(img, data.answers)	
gradients = p1.backprop(gradients)	
gradients = re2.backprop(gradients)	
gradients = f2.backprop(gradients)
gradients = re2.backprop(gradients)	
gradients = f1.backprop(gradients)
```


## loader.py
Formats and returns the mnist data in a useful format
	
#### Dependancies:
```
gzip
numpy
matplotlib
os
```
#### load_data:
Returns a tuple of training_data, testing_data
which are python lists of tuples containing a 28x28 np.matrix
and a length 10 python list where each value is 0 except for correct label to the corresponding image, which is 1


## tf_comparison.py
	
Constructs a similar multilayer perceptron network as the project using tensorflow
Used for comparing our accuracy results to a known working solution

https://www.easy-tensorflow.com/tf-tutorials/convolutional-neural-nets-cnns/cnn1?view=article&id=108:cnn
Code was taken almost entirely from this tutorial with only minor modifications
We are not claiming it is our own code, it is definitely not.

#### Dependancies:
This module uses tensorflow which only supports a maximum of python 3.7
If you want to run it, you must use python 3.7 and install tensorflow
The latest version of Anaconda will also work because it comes pre-packaged with python 3.7 and tensorflow

```
tensorflow (pip install tensorflow)
numpy
matplotlib
```

#### fc_layer:
returns a fully connected tensorflow layer of a neural network with specifies input, size, and activation function (sigmoid, tanh, or None)

#### main
hyperparameters:
```
epochs
batch_size
learning_rate
h1 (size of hidden layer(s))
```
