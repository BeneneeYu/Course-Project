#!/usr/bin/env python
# -*- encoding: utf-8 -*-
'''
@File    :   Server.py    
@Contact :   18302010009@fudan.edu.cn

@Modify Time      @Author         @Version    @Desciption
------------      ------------    --------    -----------
2020/5/21 15:06   Shen Zhengyu      1.0         None
'''

# import lib
import Game
import Data
import os
import time
import File
import random

class Server:
    gameNum = 0
    def __init__(self):
        while True:
            gameNum = random.randint(10000,99999)
            directory = os.getcwd()
            list = os.listdir(directory)
            for i in range(0, len(list)):
                if list[i].find("四连环Log") != -1:
                    if list[i][7:12] == str(gameNum):
                        continue
            break
        filename = "四连环Log-" + str(gameNum) + ".txt"

        File.file = filename
        File.print_and_write(Data.welcome)
        File.print_and_write(Data.init)
    def robot_put(self,game):
        time.sleep(0.3)
        value = game.robot.to_put(game.chessboard,0)
        if value != -1:
            File.print_and_write("轮到我了,我把O棋子放在第" + str(value[1]) + "列...")
            game.chessboard.print_chessboard()
            return value
        else:
            self.draw()

    def person_put(self,game):
        while True:
            File.print_and_write("轮到你了,你放X棋子,请选择列号(1-8):",end="")
            # 使用异常处理功能，针对玩家不输入的情况
            try:
                column = int(input())
            except:
                column = random.randint(1,8)
                print("你未输入列号，系统为你随机选取" + str(column))

            File.write(str(column))
            if column not in range(1,9):
                File.print_and_write("请输入1-8间的整数")
                continue
            value = game.person.to_put(game.chessboard,column)
            if value != -1:
                game.chessboard.print_chessboard()
                return value
            else:
                File.print_and_write("该列已经不能放置,请重新输入")
                continue

    def draw(self):
        File.print_and_write("*******难分胜负！@_@")
        try:
            os.rename(File.file,File.file.split('.')[0] + "(平局).txt")
        except Exception as e:
            pass
        else:
            pass
        input("按任意键结束…")
        exit(0)


    def compete(self):
        game = Game.Game()
        # -1代表机器走，1代表人走
        flag = -1
        while True:
            coordinate = [-1,-1]
            if flag == -1:
                coordinate = self.robot_put(game)
                flag = 1
            else:
                coordinate =  self.person_put(game)
                flag = -1
            is_win = game.is_win(coordinate[0], coordinate[1], -flag)
            if is_win != 0:
                if is_win == -1:
                    File.print_and_write("*******耶，我赢了！^_^")
                else:
                    File.print_and_write("*******好吧，你赢了！^_*")
                input("按任意键结束…")
                exit(0)
