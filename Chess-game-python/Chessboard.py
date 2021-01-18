#!/usr/bin/env python
# -*- encoding: utf-8 -*-
'''
@File    :   Chessboard.py    
@Contact :   18302010009@fudan.edu.cn

@Modify Time      @Author         @Version    @Desciption
------------      ------------    --------    -----------
2020/5/21 13:49   Shen Zhengyu      1.0         None
'''
# import lib
import Data
import File


def print_inline(str):
    File.print_and_write(str, end='')


# 方法都需要传入修正过的参数，从0开始
class Chessboard:
    # 没有人下过：0，机器人占位：-1，玩家占位：1
    column = 8
    row = 6
    board = [[0] * 9 for i in range(7)]
    count = 0

    # 记录每一列已经放入的棋子的数目
    container = [0 for _ in range(9)]

    def __init__(self):
        pass

    # 判断棋盘是否已满的方法
    def is_full(self):
        return self.count == (self.column * self.row)

    # 展示棋盘情况
    def print_chessboard(self):
        File.print_and_write(" 1 2 3 4 5 6 7 8")
        for i in range(1, len(self.board)):
            for j in range(1, len(self.board[0])):
                print_inline('|')
                if self.board[i][j] == 1:
                    print_inline(Data.piece_of_person)
                elif self.board[i][j] == -1:
                    print_inline(Data.piece_of_robot)
                else:
                    print_inline(' ')
            File.print_and_write("|")
        File.print_and_write("-----------------\n")


    def can_put(self, column):
        return self.container[column] < 6

    # 下棋的方法
    # 记得二位列表行列标志从1开始
    def put(self, column, player):
        self.board[self.row - self.container[column]][column] = player.character
        self.container[column] += 1
        self.count += 1
        return [self.row - self.container[column] + 1, column]
