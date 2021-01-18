#!/usr/bin/env python
# -*- encoding: utf-8 -*-
'''
@File    :   Data.py.py    
@Contact :   18302010009@fudan.edu.cn

@Modify Time      @Author         @Version    @Desciption
------------      ------------    --------    -----------
2020/5/21 13:45   Shen Zhengyu      1.0         None
'''

# import lib

piece_of_robot = 'O'
piece_of_person = 'X'
welcome = ''' Hi,我是劳拉，我们来玩一局四连环。我用O型棋子，你用X型棋子。
游戏规则：双方轮流选择棋盘的列号放进自己的棋子，
          若棋盘上有四颗相同型号的棋子在一行、一列或一条斜线上连接起来，
          则使用该型号棋子的玩家就赢了!

开始了！这是棋盘的初始状态：'''

init = ''' 1 2 3 4 5 6 7 8
| | | | | | | | |
| | | | | | | | |
| | | | | | | | |
| | | | | | | | |
| | | | | | | | |
| | | | | | | | |
-----------------\n
'''