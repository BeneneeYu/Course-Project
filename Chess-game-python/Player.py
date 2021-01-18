#!/usr/bin/env python
# -*- encoding: utf-8 -*-
'''
@File    :   Player.py    
@Contact :   18302010009@fudan.edu.cn

@Modify Time      @Author         @Version    @Desciption
------------      ------------    --------    -----------
2020/5/21 13:40   Shen Zhengyu      1.0         None
'''

# import lib
import random
import File

class Player:
    character = 0

    def to_put(self, chessboard, column):
        pass


class Robot(Player):
    character = -1

    def to_put(self, chessboard, column):
        if chessboard.is_full():
            File.print_and_write("棋盘放满了")
            return -1
        while True:
            column = random.randint(1, 8)
            if chessboard.can_put(column):
                return chessboard.put(column, self)



class Person(Player):
    character = 1

    def to_put(self, chessboard, column):
        if not chessboard.is_full():
            if chessboard.can_put(column):
                return chessboard.put(column, self)
            else:
                return -1
