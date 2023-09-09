import numpy as np
import random as rand
import math
import os


class NeuralNetwork:

    def __init__(self, inputNodes, hiddenPerceptrons, hidden2Perceptrons,
                 outputPerceptrons, learningRate=0.1, loadFromFile=False):
        """
        Sets the input notes, hidden and output perceptrons to be used
        in the neural network
        :param int inputNodes: the number of input nodes that should be used
        :param int hiddenPerceptrons: the number of hidden perceptrons that
        should be used
        :param int hidden2Perceptrons: the number of hidden perceptrons in
        the second layer that should be used
        :param int outputPerceptrons: the number of output perceptrons that
        should be used
        :param float learningRate: the percentage amount used to train the
        neural network. should be between 0 and 1.
        :param boolean loadFromFile: set to True to load the weights from
        the weights folder
        """

        self.inputNodes = inputNodes
        self.hiddenNodes = hiddenPerceptrons
        self.hidden2Nodes = hidden2Perceptrons
        self.outputNodes = outputPerceptrons
        self.learningRate = learningRate

        self.dir = os.path.dirname(__file__)

        if(loadFromFile):
            self.weightsIH = np.load('weights/weightsIH.npy')
            self.weightsH2H = np.load('weights/weightsH2H.npy')
            self.weightsHO = np.load('weights/weightsHO.npy')
            self.biasH = np.load('weights/biasH.npy')
            self.biasH2 = np.load('weights/biasH2.npy')
            self.biasO = np.load('weights/biasO.npy')

        else:
            self.weightsIH = self.determineWeights(self.hiddenNodes,
                                                   self.inputNodes)
            self.weightsH2H = self.determineWeights(self.hidden2Nodes,
                                                    self.hiddenNodes)
            self.weightsHO = self.determineWeights(self.outputNodes,
                                                   self.hidden2Nodes)

            self.biasH = self.determineWeights(self.hiddenNodes, 1)
            self.biasH2 = self.determineWeights(self.hidden2Nodes, 1)
            self.biasO = self.determineWeights(self.outputNodes, 1)

        self.activationFunt = np.vectorize(self.sigmoid)
        self.dactivationFunt = np.vectorize(self.dsigmoid)

    def setLearningRate(self, learningRate):
        self.learningRate = max(learningRate, 0.00000001)

    def getLearningRate(self):
        return self.learningRate

    def feedforward(self, inputArray):
        valueArray = []

        # turns the inputs into a matrix to be used
        inputs = self.matrixFromArray(inputArray)
        valueArray.append(inputs)

        # dot product of the weights from inputs to hidden nodes and adds bias
        hidden = self.weightsIH * inputs
        hidden = hidden + self.biasH

        # activation function
        hidden = self.activationFunt(hidden)
        valueArray.append(hidden)

        # dot product of the weights from hidden layer 1 to hidden layer 2
        hidden2 = self.weightsH2H * hidden
        hidden2 = hidden2 + self.biasH2

        # activation function
        hidden2 = self.activationFunt(hidden2)
        valueArray.append(hidden2)

        # dot product of the weights from hidden to ouput nodes and adds bias
        output = self.weightsHO * hidden2
        output = output + self.biasO

        # activation function
        output = self.activationFunt(output)
        valueArray.append(output)

        return valueArray

    def guess(self, inputArray):
        '''
        Gets only the outputs from the feedforward algorithm
        '''
        return self.feedforward(inputArray)[3]

    def train(self, inputArray, answerArray):
        valuesFromFeedforward = self.feedforward(inputArray)
        inputs = valuesFromFeedforward[0]
        hidden = valuesFromFeedforward[1]
        hidden2 = valuesFromFeedforward[2]
        outputs = valuesFromFeedforward[3]

        # get the answers for supervised learning
        answers = self.matrixFromArray(answerArray)

        # calculates the error
        outputError = answers - outputs

        # calculates the gradients
        gradients = self.dactivationFunt(outputs)
        gradients = np.multiply(gradients, outputError)
        gradients = gradients * self.learningRate

        # calculates the deltas
        hidden2T = np.transpose(hidden2)
        weightHODeltas = gradients * hidden2T

        # adjust weights by deltas
        self.weightsHO = self.weightsHO + weightHODeltas
        # adjust biases by deltas
        self.biasO = self.biasO + gradients

        # calculates the hidden layer 2 errors
        weightshoT = np.transpose(self.weightsHO)
        hidden2Error = weightshoT * outputError

        # calculates hidden2 gradients
        hidden2Gradient = self.dactivationFunt(hidden2)
        hidden2Gradient = np.multiply(hidden2Gradient, hidden2Error)
        hidden2Gradient = hidden2Gradient * self.learningRate

        # calculates H2H deltas
        hiddenT = np.transpose(hidden)
        weightH2HDeltas = hidden2Gradient * hiddenT

        # adjusts weights by deltas
        self.weightsH2H = self.weightsH2H + weightH2HDeltas
        # adjusts biases
        self.biasH2 = self.biasH2 + hidden2Gradient

        # calculates the hidden layer errors
        weightsh2hT = np.transpose(self.weightsH2H)
        hiddenError = weightsh2hT * hidden2Error

        # calculates hidden gradients
        hiddenGradient = self.dactivationFunt(hidden)
        hiddenGradient = np.multiply(hiddenGradient, hiddenError)
        hiddenGradient = hiddenGradient * self.learningRate

        # calculates IH deltas
        inputsT = np.transpose(inputs)
        weightIHDeltas = hiddenGradient * inputsT

        # adjusts weights by deltas
        self.weightsIH = self.weightsIH + weightIHDeltas
        # adjusts biases
        self.biasH = self.biasH + hiddenGradient

        return weightIHDeltas

    def determineWeights(self, rows, columns):
        """
        Provides a random weight matrix
        :param int rows: the number rows for the weight matrix
        :param int columns: the number columns for the weight matrix
        """

        output = []
        for i in range(rows):
            row = []
            for j in range(columns):
                row.append(rand.random() * 2 - 1)
            output.append(row)
        return np.mat(output)

    def matrixFromArray(self, input):
        """
        Creates a Nx1 matrix from a given array
        :param array input: the inputs to be turned into a matrix
        """

        output = []
        for i in input:
            output.append([i])

        return np.mat(output)

    def sigmoid(self, x):
        x = round(x, 10)
        if x < 0:
            return 1 - 1 / (1 + math.exp(x))
        else:
            return 1 / (1 + math.exp(-x))

    def dsigmoid(self, y):
        return y * (1 - y)

    def tanh(self, x):
        return math.tanh(x)

    def dtanh(self, y):
        return 1 - math.pow(self.tanh(y), 2)

    def saveNetwork(self):
        np.save('weights/weightsIH.npy', self.weightsIH)
        np.save('weights/weightsH2H.npy', self.weightsH2H)
        np.save('weights/weightsHO.npy', self.weightsHO)
        np.save('weights/biasH.npy', self.biasH)
        np.save('weights/biasH2.npy', self.biasH2)
        np.save('weights/biasO.npy', self.biasO)
