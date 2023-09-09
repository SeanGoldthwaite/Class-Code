import tensorflow as tf
import numpy as np
import scipy.io


def load_vgg(input_image):
    net = {}

    a, h, w, c = input_image.shape

    vgg_net = scipy.io.loadmat('imagenet-vgg-verydeep-19.mat')

    global vgg_layers
    vgg_layers = vgg_net['layers'][0]

    net['input'] = tf.Variable(np.zeros((1, h, w, c), dtype=np.float32))
    
    # layer group 1
    net['conv1_1'] = conv_layer(input=net['input'], weights=get_weights(0))
    net['relu1_1'] = relu_layer(input=net['conv1_1'], bias=get_bias(0))

    net['conv1_2'] = conv_layer(input=net['relu1_1'], weights=get_weights(2))
    net['relu1_2'] = relu_layer(input=net['conv1_2'], bias=get_bias(2))

    net['pool1'] = pool_layer(input=net['relu1_2'])

    # layer group 2
    net['conv2_1'] = conv_layer(input=net['pool1'], weights=get_weights(5))
    net['relu2_1'] = relu_layer(input=net['conv2_1'], bias=get_bias(5))
    
    net['conv2_2'] = conv_layer(input=net['relu2_1'], weights=get_weights(7))
    net['relu2_2'] = relu_layer(input=net['conv2_2'], bias=get_bias(7))

    net['pool2'] = pool_layer(input=net['relu2_2'])

    # layer group 3
    net['conv3_1'] = conv_layer(input=net['pool2'], weights=get_weights(10))
    net['relu3_1'] = relu_layer(input=net['conv3_1'], bias=get_bias(10))
    
    net['conv3_2'] = conv_layer(input=net['relu3_1'], weights=get_weights(12))
    net['relu3_2'] = relu_layer(input=net['conv3_2'], bias=get_bias(12))
    
    net['conv3_3'] = conv_layer(input=net['relu3_2'], weights=get_weights(14))
    net['relu3_3'] = relu_layer(input=net['conv3_3'], bias=get_bias(14))
    
    net['conv3_4'] = conv_layer(input=net['relu3_3'], weights=get_weights(16))
    net['relu3_4'] = relu_layer(input=net['conv3_4'], bias=get_bias(16))

    net['pool3'] = pool_layer(input=net['relu3_4'])

    # layer group 4
    net['conv4_1'] = conv_layer(input=net['pool3'], weights=get_weights(19))
    net['relu4_1'] = relu_layer(input=net['conv4_1'], bias=get_bias(19))
    
    net['conv4_2'] = conv_layer(input=net['relu4_1'], weights=get_weights(21))
    net['relu4_2'] = relu_layer(input=net['conv4_2'], bias=get_bias(21))
    
    net['conv4_3'] = conv_layer(input=net['relu4_2'], weights=get_weights(23))
    net['relu4_3'] = relu_layer(input=net['conv4_3'], bias=get_bias(23))
    
    net['conv4_4'] = conv_layer(input=net['relu4_3'], weights=get_weights(25))
    net['relu4_4'] = relu_layer(input=net['conv4_4'], bias=get_bias(25))

    net['pool4'] = pool_layer(input=net['relu4_4'])

    # layer group 5
    net['conv5_1'] = conv_layer(input=net['pool4'], weights=get_weights(28))
    net['relu5_1'] = relu_layer(input=net['conv5_1'], bias=get_bias(28))
    
    net['conv5_2'] = conv_layer(input=net['relu5_1'], weights=get_weights(30))
    net['relu5_2'] = relu_layer(input=net['conv5_2'], bias=get_bias(30))
    
    net['conv5_3'] = conv_layer(input=net['relu5_2'], weights=get_weights(32))
    net['relu5_3'] = relu_layer(input=net['conv5_3'], bias=get_bias(32))
    
    net['conv5_4'] = conv_layer(input=net['relu5_3'], weights=get_weights(34))
    net['relu5_4'] = relu_layer(input=net['conv5_4'], bias=get_bias(34))

    net['pool5'] = pool_layer(input=net['relu5_4'])

    return net

def conv_layer(input, weights):
    return tf.nn.conv2d(input, weights, strides=[1,1,1,1], padding='SAME')

def relu_layer(input, bias):
    return tf.nn.relu(input + bias)

def pool_layer(input):
    return tf.nn.avg_pool(input, ksize=[1,2,2,1], strides=[1,2,2,1], padding='SAME')
    
def get_weights(i):
    weights = vgg_layers[i][0][0][2][0][0]
    return tf.constant(weights)

def get_bias(i):
    bias = vgg_layers[i][0][0][2][0][1]
    return tf.constant(np.reshape(bias, (bias.size)))
