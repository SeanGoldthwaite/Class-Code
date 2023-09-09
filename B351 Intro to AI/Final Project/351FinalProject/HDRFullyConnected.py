import NeuralNetwork as nn
import random as rand
import numpy as np
# import math
import loader
import PIL
import os


def getGuess(neuralNetwork, data):
    guesses = (np.asarray(neuralNetwork.guess(
        np.asarray(data[0] / 255, np.float64).flatten())).flatten())
    answers = np.asarray(data[1]).flatten()

    return [np.argmax(guesses),
            np.argmax(answers)]


def getGuessCustom(neuralNetwork, data):
    '''
    Given a neural network and a custom PIL image, return a value on what
    the network believes the image to be
    '''

    guesses = (np.asarray(neuralNetwork.guess(
        (np.asarray(list(data.getdata()), np.float64) / 255)
        .flatten())).flatten())

    print(guesses)

    return np.argmax(guesses)


def train(neuralNetwork, epochs, trainingData, decreaseLR=False):
    trainingDataLength = len(trainingData)

    for j in range(epochs):
        epochTraining = rand.sample(trainingData, trainingDataLength)

        for i in range(trainingDataLength):
            if(i % 10000 == 0):
                print('Iteration: ', i+(j*trainingDataLength))
            data = epochTraining[i]
            neuralNet.train(np.asarray(data[0] / 255, np.float64).flatten(),
                            np.asarray(data[1]).flatten())

        neuralNet.saveNetwork()
        print('Autosaved Network')


def predict(neuralNet, testingData):

    testingDataLength = len(testingData)

    correct = 0
    for i in range(testingDataLength):
        data = testingData[i]
        # rand.choice(testingData)
        guessesAnswersArray = getGuess(neuralNet, data)
        if(i % 100 == 0):
            print('Guess ', i+1, ': ', guessesAnswersArray[0],
                  ' Real: ', guessesAnswersArray[1])
        if(guessesAnswersArray[0] == guessesAnswersArray[1]):
            correct += 1

    print('Percentage of Correct Guesses: ',
          (correct/testingDataLength)*100, '% (', correct, ' out of ',
          testingDataLength, ')')
    print('Current learning rate: ', neuralNet.getLearningRate())


def predictCustom(neuralNet, testingData):
    guess = getGuessCustom(neuralNet, testingData)
    print('Guess :', guess)

    return guess


if __name__ == '__main__':

    dir = os.path.dirname(__file__)
    customFile = os.path.join(dir, 'custom')
    testIMG = PIL.Image.open(os.path.join(customFile, 'ci1.png'))

    data = loader.load_data()
    trainingData = data[0]
    testingData = data[1]

    neuralNet = nn.NeuralNetwork(784, 100, 100, 10, 0.001, True)

    epochs = 10

    # train(neuralNet, epochs, trainingData)

    predict(neuralNet, testingData)

    print('Saving Neural Network')
    neuralNet.saveNetwork()
