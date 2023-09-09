import NeuralNetwork as nn
import random as rand


class Data:
    def __init__(self, inputs, answers):
        self.inputs = inputs
        self.answers = answers
        self.timesUsed = 0

    def addOne(self):
        self.timesUsed += 1


if __name__ == '__main__':
    trainingData = [Data([1, 1], [0]),
                    Data([1, 0], [1]),
                    Data([0, 1], [1]),
                    Data([0, 0], [0])]

    trainingData2 = [Data([1, 1], [1]),
                     Data([1, 0], [0]),
                     Data([0, 1], [0]),
                     Data([0, 0], [0])]

    trainingData3 = [Data([1, 1], [1]),
                     Data([1, 0], [1]),
                     Data([0, 1], [1]),
                     Data([0, 0], [0])]

    print('Xor:')
    neuralNet = nn.NeuralNetwork(2, 10, 1, 0.1)
    maxIterations = 50000
    for i in range(maxIterations):
        # print('Iteration: ', i+1, '/', maxIterations)
        data = rand.choice(trainingData)
        data.addOne()
        neuralNet.train(data.inputs, data.answers)

    print('Times [0,0] used: ', trainingData[0].timesUsed)
    print('Times [1,1] used: ', trainingData[1].timesUsed)
    print('Times [1,0] used: ', trainingData[2].timesUsed)
    print('Times [0,1] used: ', trainingData[3].timesUsed)

    print(neuralNet.guess([0, 0]))
    print(neuralNet.guess([1, 1]))
    print(neuralNet.guess([1, 0]))
    print(neuralNet.guess([0, 1]))

    '''
    print('And:')
    neuralNet2 = nn.NeuralNetwork(2, 2, 1, 0.1)
    maxIterations = 50000
    for i in range(maxIterations):
        # print('Iteration: ', i+1, '/', maxIterations)
        data = rand.choice(trainingData2)
        data.addOne()
        neuralNet2.train(data.inputs, data.answers)

    print(neuralNet2.guess([0, 0]))
    print(neuralNet2.guess([1, 1]))
    print(neuralNet2.guess([1, 0]))
    print(neuralNet2.guess([0, 1]))

    print('Or:')
    neuralNet3 = nn.NeuralNetwork(2, 2, 1, 0.1)
    maxIterations = 50000
    for i in range(maxIterations):
        # print('Iteration: ', i+1, '/', maxIterations)
        data = rand.choice(trainingData3)
        data.addOne()
        neuralNet3.train(data.inputs, data.answers)

    print(neuralNet3.guess([0, 0]))
    print(neuralNet3.guess([1, 1]))
    print(neuralNet3.guess([1, 0]))
    print(neuralNet3.guess([0, 1]))
    '''
