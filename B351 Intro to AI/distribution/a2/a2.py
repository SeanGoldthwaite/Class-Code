#!/usr/bin/python3
# B351/Q351 Spring 2020
# Professor Saúl Blanco
# Do not share these assignments or their solutions outside of this class.

import csv
import itertools
import random

class Board():

    ##########################################
    ####   Constructor
    ##########################################
    def __init__(self, filename):

        # initialize all of the variables
        self.n2 = 0
        self.n = 0
        self.spaces = 0
        self.board = None
        self.valsInRows = None
        self.valsInCols = None
        self.valsInBoxes = None
        self.unsolvedSpaces = None

        # load the file and initialize the in-memory board with the data
        self.loadSudoku(filename)


    # loads the sudoku board from the given file
    def loadSudoku(self, filename):

        with open(filename) as csvFile:
            self.n = -1
            reader = csv.reader(csvFile)
            for row in reader:

                # Assign the n value and construct the approriately sized dependent data
                if self.n == -1:
                    self.n = int(len(row) ** (1/2))
                    if not self.n ** 2 == len(row):
                        raise Exception('Each row must have n^2 values! (See row 0)')
                    else:
                        self.n2 = len(row)
                        self.spaces = self.n ** 4
                        self.board = {}
                        self.valsInRows = [set() for _ in range(self.n2)]
                        self.valsInCols = [set() for _ in range(self.n2)]
                        self.valsInBoxes = [set() for _ in range(self.n2)]
                        self.unsolvedSpaces = set(itertools.product(range(self.n2), range(self.n2)))

                # check if each row has the correct number of values
                else:
                    if len(row) != self.n2:
                        raise Exception('Each row must have the same number of values. (See row ' + str(reader.line_num - 1) + ')')

                # add each value to the correct place in the board; record that the row, col, and box contains value
                for index, item in enumerate(row):
                    if not item == '':
                        self.board[(reader.line_num-1, index)] = int(item)
                        self.valsInRows[reader.line_num-1].add(int(item))
                        self.valsInCols[index].add(int(item))
                        self.valsInBoxes[self.spaceToBox(reader.line_num-1, index)].add(int(item))
                        self.unsolvedSpaces.remove((reader.line_num-1, index))


    ##########################################
    ####   Utility Functions
    ##########################################

    # converts a given row and column to its inner box number
    def spaceToBox(self, row, col):
        return self.n * (row // self.n) + col // self.n

    # prints out a command line representation of the board
    def print(self):
        for r in range(self.n2):
            # add row divider
            if r % self.n == 0 and not r == 0:
                if self.n2 > 9:
                    print("  " + "----" * self.n2)
                else:
                    print("  " + "---" * self.n2)

            row = ""

            for c in range(self.n2):

                if (r,c) in self.board:
                    val = self.board[(r,c)]
                else:
                    val = None

                # add column divider
                if c % self.n == 0 and not c == 0:
                    row += " | "
                else:
                    row += "  "

                # add value placeholder
                if self.n2 > 9:
                    if val is None: row += "__"
                    else: row += "%2i" % val
                else:
                    if val is None: row += "_"
                    else: row += str(val)
            print(row)


    ##########################################
    ####   Move Functions - YOUR IMPLEMENTATIONS GO HERE
    ##########################################

    # makes a move, records it in its row, col, and box, and removes the space from unsolvedSpaces
    def makeMove(self, space, value):
        self.board[(space[0], space[1])] = value
        self.valsInRows[space[0]].add(value)
        self.valsInCols[space[1]].add(value)
        self.valsInBoxes[self.spaceToBox(space[0], space[1])].add(value)
        self.unsolvedSpaces.remove((space[0], space[1]))

    # removes the move, its record in its row, col, and box, and adds the space back to unsolvedSpaces
    def undoMove(self, space, value):
        del self.board[(space[0], space[1])]
        self.valsInRows[space[0]].remove(value)
        self.valsInCols[space[1]].remove(value)
        self.valsInBoxes[self.spaceToBox(space[0], space[1])].remove(value)
        self.unsolvedSpaces.add((space[0], space[1]))

    # returns True if the space is empty and on the board,
    # and assigning value to it if not blocked by any constraints
    def isValidMove(self, space, value):
        if not space in self.unsolvedSpaces:
            return False
        if value in self.valsInBoxes[self.spaceToBox(space[0], space[1])]:
            return False
        if value in self.valsInRows[space[0]]:
            return False
        if value in self.valsInCols[space[1]]:
            return False
            
        return True

    # optional helper function for use by getMostConstrainedUnsolvedSpace
    def evaluateSpace(self, space):
        possibilities = []
        for i in range(1, self.n2 + 1):
            if self.isValidMove((space[0], space[1]), i):
                possibilities.append(i)
        return possibilities

    # gets the unsolved space with the most current constraints
    # returns None if unsolvedSpaces is empty
    def getMostConstrainedUnsolvedSpace(self):
        if self.unsolvedSpaces == set():
            return None
        constraints= []
        for space in self.unsolvedSpaces:
            constraints.append([self.evaluateSpace(space), space[0], space[1]])
        constraints = sorted(constraints, key=lambda x: len(x[0]))
        return (constraints[0][1], constraints[0][2])

class Solver:
    ##########################################
    ####   Constructor
    ##########################################
    def __init__(self):
        pass

    ##########################################
    ####   Solver
    ##########################################

    # recursively selects the most constrained unsolved space and attempts
    # to assign a value to it

    # upon completion, it will leave the board in the solved state (or original
    # state if a solution does not exist)

    # returns True if a solution exists and False if one does not
    def solveBoard(self, board):
        square = board.getMostConstrainedUnsolvedSpace()
        
        if square is None:
            return True
        
        possibilities = []
        for i in range(1, board.n2 + 1):
            if board.isValidMove((square[0], square[1]), i):
                possibilities.append(i)
                
        if len(possibilities) == 0:
            return False

        random.shuffle(possibilities)
        for possibility in possibilities:
            board.makeMove((square[0], square[1]), possibility)
            if not self.solveBoard(board):
                board.undoMove((square[0], square[1]), possibility)
            else:
                return True
            
        return False
                


if __name__ == "__main__":
    # change this to the input file that you'd like to test
    #board = Board('tests/test-6-ridiculous/00.csv')
    #board = Board('tests/example.csv')
    board = Board('tests/test-4-tough/01.csv')
    s = Solver()
    board.print()
    print("\n  ---------------------------\n")
    s.solveBoard(board)
    board.print()
