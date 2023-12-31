#! python3.7

# Tensorflow is only updated to python 3.7
# You will need to downgrade if using 3.8.*

# https://www.easy-tensorflow.com/tf-tutorials/convolutional-neural-nets-cnns/cnn1?view=article&id=108:cnn
# credit to this tutorial for essentially all of the code.

import tensorflow as tf
import numpy as np
import matplotlib.pyplot as plt
from tensorflow.examples.tutorials.mnist import input_data
from tensorflow.keras import layers
from tensorflow.keras.models import Sequential


def get_batch(x, y, start, end):
    x_batch = x[start:end]
    y_batch = y[start:end]
    return x_batch, y_batch


def randomize(x, y):
    """ Randomizes the order of data samples and their corresponding labels"""
    permutation = np.random.permutation(y.shape[0])
    shuffled_x = x[permutation, :]
    shuffled_y = y[permutation]
    return shuffled_x, shuffled_y


def random_batch(x, y, length):
    x_shuffle, y_shuffle = [], []
    for i in range(length):
        ind = np.random.randint(0, len(x))
        x_shuffle.append(x[ind])
        y_shuffle.append(y[ind])
    return x_shuffle, y_shuffle


def weight_variable(name, shape):
    """
    Create a weight variable with appropriate initialization
    :param name: weight name
    :param shape: weight shape
    :return: initialized weight variable
    """
    initer = tf.truncated_normal_initializer(stddev=0.01)
    return tf.get_variable('W_' + name,
                           dtype=tf.float32,
                           shape=shape,
                           initializer=initer)


def bias_variable(name, shape):
    """
    Create a bias variable with appropriate initialization
    :param name: bias variable name
    :param shape: bias variable shape
    :return: initialized bias variable
    """
    initial = tf.constant(0., shape=shape, dtype=tf.float32)
    return tf.get_variable('b_' + name,
                           dtype=tf.float32,
                           initializer=initial)


def fc_layer(x, num_units, name, activation_function=None):
    """
    Create a fully-connected layer
    :param x: input from previous layer
    :param num_units: number of hidden units in the fully-connected layer
    :param name: layer name
    :param activation_function: sigmoid, tanh, or None
    :return: The output array
    """
    in_dim = x.get_shape()[1]
    W = weight_variable(name, shape=[in_dim, num_units])
    b = bias_variable(name, [num_units])
    layer = tf.matmul(x, W)
    layer += b
    if activation_function == 'sigmoid':
        layer = tf.nn.sigmoid(layer)
    elif activation_function == 'tanh':
        layer = tf.nn.tanh(layer)

    return layer


def plot_images(images, cls_true, cls_pred=None, title=None):
    """
    Create figure with 3x3 sub-plots.
    :param images: array of images to be plotted, (9, img_h*img_w)
    :param cls_true: corresponding true labels (9,)
    :param cls_pred: corresponding true labels (9,)
    """
    fig, axes = plt.subplots(3, 3, figsize=(9, 9))
    fig.subplots_adjust(hspace=0.3, wspace=0.3)
    for i, ax in enumerate(axes.flat):
        # Plot image.
        ax.imshow(images[i].reshape(28, 28), cmap='binary')

        # Show true and predicted classes.
        if cls_pred is None:
            ax_title = "True: {0}".format(cls_true[i])
        else:
            ax_title = "True: {0}, Pred: {1}".format(cls_true[i], cls_pred[i])

        ax.set_title(ax_title)

        # Remove ticks from the plot.
        ax.set_xticks([])
        ax.set_yticks([])

    if title:
        plt.suptitle(title, size=20)
    plt.show(block=False)


def plot_example_errors(images, cls_true, cls_pred, title=None):
    """
    Function for plotting examples of images that have been mis-classified
    :param images: array of all images, (#imgs, img_h*img_w)
    :param cls_true: corresponding true labels, (#imgs,)
    :param cls_pred: corresponding predicted labels, (#imgs,)
    """
    # Negate the boolean array.
    incorrect = np.logical_not(np.equal(cls_pred, cls_true))

    # Get the images from the test-set that have been
    # incorrectly classified.
    incorrect_images = images[incorrect]

    # Get the true and predicted classes for those images.
    cls_pred = cls_pred[incorrect]
    cls_true = cls_true[incorrect]

    # Plot the first 9 images.
    plot_images(images=incorrect_images[0:9],
                cls_true=cls_true[0:9],
                cls_pred=cls_pred[0:9],
                title=title)


if __name__ == '__main__':
    mnist = input_data.read_data_sets("MNIST_data/", one_hot=True)

    x_train, y_train = mnist.train.images, mnist.train.labels
    x_test, y_test = mnist.test.images, mnist.test.labels

    img_h = img_w = 28  # mnist images are 28x28
    img_size_flat = img_h * img_w  # 28x28=784, the total number of pixels
    n_classes = 10  # Number of classes, one class per digit

    # hyperparameters
    epochs = 40
    batch_size = 55000
    learning_rate = 0.01  # Learning rate
    h1 = 32  # Number of neurons in fc layers

    x = tf.placeholder(tf.float32, shape=[None, img_size_flat], name='X')
    y = tf.placeholder(tf.float32, shape=[None, n_classes], name='Y')

    fc1 = fc_layer(x, h1, 'fc1', activation_function='sigmoid')
    #fc2 = fc_layer(fc1, h1, 'fc2', activation_function='tanh')
    output_logits = fc_layer(fc1, n_classes, 'out', activation_function=None)

    cls_prediction = tf.argmax(output_logits, axis=1, name='predictions')

    loss = tf.reduce_mean(tf.nn.softmax_cross_entropy_with_logits(labels=y, logits=output_logits), name='loss')
    optimizer = tf.train.AdamOptimizer(learning_rate=learning_rate, name='Adam-op').minimize(loss)
    correct_prediction = tf.equal(tf.argmax(output_logits, 1), tf.argmax(y, 1), name='correct_pred')
    accuracy = tf.reduce_mean(tf.cast(correct_prediction, tf.float32), name='accuracy')

    init = tf.global_variables_initializer()

    sess = tf.InteractiveSession()
    sess.run(init)
    global_step = 0
    # Number of training iterations in each epoch
    num_tr_iter = int(len(y_train) / batch_size)
    for epoch in range(epochs):
        #print('Training epoch: {}'.format(epoch + 1))
        x_train, y_train = randomize(x_train, y_train)
        for iteration in range(num_tr_iter):
            global_step += 1
            start = iteration * batch_size
            end = (iteration + 1) * batch_size
            x_batch, y_batch = get_batch(x_train, y_train, start, end)

            # Run optimization op (backprop)
            feed_dict_batch = {x: x_batch, y: y_batch}
            sess.run(optimizer, feed_dict=feed_dict_batch)

        # Run validation after every epoch
        x_validation_batch, y_validation_batch = random_batch(x_train, y_train, 1000)
        feed_dict_valid = {x: x_validation_batch, y: y_validation_batch}
        loss_valid, acc_valid = sess.run([loss, accuracy], feed_dict=feed_dict_valid)
        #print('---------------------------------------------------------')
        print("Epoch: {0}, validation loss: {1:.2f}, validation accuracy: {2:.01%}".
              format(epoch + 1, loss_valid, acc_valid))
        #print('---------------------------------------------------------')

    # x: x_test[:1000], y: y_test[:1000]
    x_test_batch, y_test_batch = random_batch(x_test, y_test, 1000)
    feed_dict_test = {x: x_test_batch, y: y_test_batch}
    loss_test, acc_test = sess.run([loss, accuracy], feed_dict=feed_dict_test)
    print('---------------------------------------------------------')
    print("Test loss: {0:.2f}, test accuracy: {1:.01%}".format(loss_test, acc_test))
    print('---------------------------------------------------------')

    # Plot some of the correct and misclassified examples
    cls_pred = sess.run(cls_prediction, feed_dict=feed_dict_test)
    cls_true = np.argmax(y_test[:1000], axis=1)
    plot_images(x_test, cls_true, cls_pred, title='Correct Examples')
    plot_example_errors(x_test[:1000], cls_true, cls_pred, title='Misclassified Examples')
    plt.show()