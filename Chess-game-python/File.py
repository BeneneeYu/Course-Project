#!/usr/bin/env python
# -*- encoding: utf-8 -*-
'''
@File    :   File.py    
@Contact :   18302010009@fudan.edu.cn

@Modify Time      @Author         @Version    @Desciption
------------      ------------    --------    -----------
2020/5/21 15:58   Shen Zhengyu      1.0         None
'''

# import lib
file = "game.txt"
# 使用异常处理功能，读写文件
def print_and_write(str,end="\n"):
    print(str,end=end)
    with open(file, 'a') as file_object:
        file_object.writelines(str + end)

def write(str):
    with open(file, 'a') as file_object:
        file_object.write(str + "\n")
