#!/usr/bin/env python
# -*- encoding: utf-8 -*-
'''
@File    :   Game.py    
@Contact :   18302010009@fudan.edu.cn

@Modify Time      @Author         @Version    @Desciption
------------      ------------    --------    -----------
2020/5/21 14:41   Shen Zhengyu      1.0         None
'''

# import lib
import Chessboard
import Player


def valid(row, column):
    return 1 <= row <= 6 and 1 <= column <= 8


class Game:
    chessboard = Chessboard.Chessboard()
    robot = Player.Robot()
    person = Player.Person()

    # while()

    # 棋盘满了或有人获胜
    def is_terminated(self):
        if self.chessboard.is_full():
            return True

    # 赢棋的情况必然出现在有人下了一步之后
    # 分别从水平，竖直，斜向判断
    def is_win(self, row, column, character):
        horizontal_value = self.horizontal_is_win(row, column, character)
        vertical_value = self.vertical_is_win(row, column, character)
        bias_value = self.bias_is_win(row, column, character)
        for i in [horizontal_value,vertical_value,bias_value]:
            if i != 0:
                return i
        return 0

    def horizontal_is_win(self, row, column, character):
        left_max = 0
        right_max = 0
        i = row
        j = column
        while j >= 1 and character == self.chessboard.board[i][j]:
            j -= 1
            left_max += 1
        i = row
        j = column
        while j <= 8 and character == self.chessboard.board[i][j]:
            j += 1
            right_max += 1
        count = left_max + right_max - 1
        if count >= 4: return character
        return 0

    def vertical_is_win(self, row, column, character):
        top_max = 0
        down_max = 0
        i = row
        j = column
        while i >= 1 and character == self.chessboard.board[i][j]:
            i -= 1
            down_max += 1
        i = row
        j = column
        while i <= 6 and character == self.chessboard.board[i][j]:
            i += 1
            top_max += 1
        count = top_max + down_max - 1
        if count >= 4: return character
        return 0

    def bias_is_win(self, row, column, character):
        left_top_max = 0
        left_down_max = 0
        right_top_max = 0
        right_down_max = 0
        i = row
        j = column
        while valid(i, j) and character == self.chessboard.board[i][j]:
            i -= 1
            j -= 1
            left_top_max += 1
        i = row
        j = column
        while valid(i, j) and character == self.chessboard.board[i][j]:
            i += 1
            j += 1
            right_down_max += 1
        count = left_top_max + right_down_max - 1
        if count >= 4:return character

        i = row
        j = column
        while valid(i, j) and character == self.chessboard.board[i][j]:
            i += 1
            j -= 1
            left_down_max += 1
        i = row
        j = column
        while valid(i, j) and character == self.chessboard.board[i][j]:
            i -= 1
            j += 1
            right_top_max += 1
        count = left_down_max + right_top_max - 1
        if count >= 4:return character
        return 0

