#!/usr/bin/python3

### CSCI-B 351 / COGS-Q 351 Spring 2020
### Framework code copyright 2020 B351/Q351 instruction team.
### Do not copy or redistribute this code without permission
### and do not share your solutions outside of this class.
### Doing so constitutes academic misconduct and copyright infringement.

import math
import random
# import requests
from board import Board


class BasePlayer:
    def __init__(self, max_depth):
        self.max_depth = max_depth

    ##################
    #      TODO      #
    ##################
    # Assign integer scores to the three terminal states
    # P2_WIN_SCORE < TIE_SCORE < P1_WIN_SCORE
    # Access these with "self.TIE_SCORE", etc.
    P1_WIN_SCORE = 48
    P2_WIN_SCORE = -48
    TIE_SCORE = 0

    # Returns a heuristic for the board position
    # Good positions for 0 pieces should be positive and
    # good positions for 1 pieces should be negative
    # for all boards, P2_WIN_SCORE < heuristic(b) < P1_WIN_SCORE
    def heuristic(self, board):
        p1_pits = board.p1_pits
        p2_pits = board.p2_pits
        p1_pot = board.p1_pot
        p2_pot = board.p2_pot
        heuristic_p1 = self.P1_WIN_SCORE
        heuristic_p2 = self.P2_WIN_SCORE
        for i, pit in enumerate(p1_pits):
            if pit == 0:
                continue
            heuristic_p1 -= pit
            end_pit = i + pit
            if end_pit == 6:
                heuristic_p1 -= pit
            elif (5 - end_pit >= 0) and p1_pits[end_pit] == 0:
                heuristic_p1 -= p2_pits[5 - end_pit] + 1

        for i, pit in enumerate(p2_pits):
            if pit == 0:
                continue
            heuristic_p2 += pit
            end_pit = i + pit
            if end_pit == 6:
                heuristic_p2 += pit
            elif (5 - end_pit >= 0) and p2_pits[end_pit] == 0:
                heuristic_p2 += p1_pits[5 - end_pit] + 1

        return heuristic_p1 + heuristic_p2 + p1_pot - p2_pot

    def findMove(self, trace):
        raise NotImplementedError


class ManualPlayer(BasePlayer):
    def __init__(self, max_depth=None):
        BasePlayer.__init__(self, max_depth)

    def findMove(self, trace):
        board = Board(trace)
        opts = "  "
        for c in range(6):
            opts += " " + (str(c + 1) if board.isValidMove(c) else ' ') + "  "

        while True:
            if board.turn == 0:
                print("\n")
                board.printSpaced()
                print(opts)
                pit = input("Pick a pit (P1 side): ")
            else:
                print("\n")
                print(" " + opts[::-1])
                board.printSpaced()
                pit = input("Pick a pit (P2 side): ")
            try:
                pit = int(pit) - 1
            except ValueError:
                continue
            if board.isValidMove(pit):
                return pit


class RandomPlayer(BasePlayer):
    def __init__(self, max_depth=None):
        BasePlayer.__init__(self, max_depth)
        # self.random = random.Random(13487951347859)
        self.random = random.Random()

    def findMove(self, trace):
        board = Board(trace)
        options = list(board.getAllValidMoves())
        return self.random.choice(options)


class RemotePlayer(BasePlayer):
    def __init__(self, max_depth=None):
        BasePlayer.__init__(self, max_depth)
        self.instructor_url = "http://silo.cs.indiana.edu:30005"
        if self.max_depth > 8:
            print("It refused to go that hard. Sorry.")
            self.max_depth = 8

    def findMove(self, trace):
        import requests
        r = requests.get(f'{self.instructor_url}/getmove/{self.max_depth},{trace}')
        move = int(r.text)
        return move


class PlayerMM(BasePlayer):
    ##################
    #      TODO      #
    ##################
    # performs minimax on board with depth.
    # returns the best move and best score as a tuple
    def minimax(self, board, depth):
        if depth == 0:
            return None, self.heuristic(board)
        if board.game_over:
            if board.winner == 1:
                return None, self.P2_WIN_SCORE
            elif board.winner == 0:
                return None, self.P1_WIN_SCORE
            else:
                return None, self.TIE_SCORE
        if board.turn == 0:  # max
            best_val = -math.inf
            options = list(board.getAllValidMoves())
            best_move = options[0]
            for move in options:
                board.makeMove(move)
                val = self.minimax(board, depth - 1)[1]
                board.undoMove()
                if best_val < val:
                    best_val = val
                    best_move = move
            return best_move, best_val
        else:  # min
            best_val = math.inf
            options = list(board.getAllValidMoves())
            best_move = options[0]
            for move in options:
                board.makeMove(move)
                val = self.minimax(board, depth - 1)[1]
                board.undoMove()
                if best_val > val:
                    best_val = val
                    best_move = move
            return best_move, best_val

    def findMove(self, trace):
        board = Board(trace)
        print("board.game_over", board.winner)
        move, score = self.minimax(board, self.max_depth)
        return move


class PlayerAB(BasePlayer):
    ##################
    #      TODO      #
    ##################
    # performs minimax with alpha-beta pruning on board with depth.
    # alpha represents the score of max's current strategy
    # beta  represents the score of min's current strategy
    # in a cutoff situation, return the score that resulted in the cutoff
    # returns the best move and best score as a tuple
    def alphaBeta(self, board, depth, alpha, beta):
        if depth == 0:
            return None, self.heuristic(board)
        if board.game_over:
            if board.winner == 1:
                return None, self.P2_WIN_SCORE
            elif board.winner == 0:
                return None, self.P1_WIN_SCORE
            else:
                return None, self.TIE_SCORE
        if board.turn == 0:  # max
            best_val = -math.inf
            options = list(board.getAllValidMoves())
            best_move = options[0]
            for move in options:
                board.makeMove(move)
                val = self.alphaBeta(board, depth - 1, alpha, beta)[1]
                board.undoMove()
                if best_val < val:
                    best_val = val
                    best_move = move
                if alpha < best_val:
                    alpha = best_val
                if alpha >= beta:
                    break
            return best_move, best_val
        else:  # min
            best_val = math.inf
            options = list(board.getAllValidMoves())
            best_move = options[0]
            for move in options:
                board.makeMove(move)
                val = self.alphaBeta(board, depth - 1, alpha, beta)[1]
                board.undoMove()
                if best_val > val:
                    best_val = val
                    best_move = move
                if beta > best_val:
                    beta = best_val
                if alpha >= beta:
                    break
            return best_move, best_val

    def findMove(self, trace):
        board = Board(trace)
        move, score = self.alphaBeta(board, self.max_depth, -math.inf, math.inf)
        return move


class PlayerDP(PlayerAB):
    """ A version of PlayerAB that implements dynamic programming
        to cache values for its heuristic function, improving performance. """

    def __init__(self, max_depth):
        PlayerAB.__init__(self, max_depth)
        self.resolved = {}

    ##################
    #      TODO      #
    ##################
    # if a saved heuristic value exists in self.resolved for board.state, returns that value
    # otherwise, uses BasePlayer.heuristic to get a heuristic value and saves it under board.state
    def heuristic(self, board):
        key = board.state
        if key in self.resolved:
            return self.resolved[key]
        else:
            self.resolved[key] = BasePlayer.heuristic(self, board)
            return self.resolved[key]


class PlayerBonus(BasePlayer):
    """ This class is here to give you space to experiment for your ultimate Mancala AI,
        your one and only PlayerBonus. This is only used for the extra credit tournament. """

    def findMove(self, trace):
        raise NotImplementedError


#######################################################
###########Example Subclass for Testing
#######################################################

# This will inherit your findMove from above, but will override the heuristic function with
# a new one; you can swap out the type of player by changing X in "class TestPlayer(X):"
class TestPlayer(BasePlayer):
    # define your new heuristic here
    def heuristic(self):
        pass
