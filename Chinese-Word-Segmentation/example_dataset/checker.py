#!/usr/bin/env python
# -*- encoding: utf-8 -*-
'''
@File    :   checker.py.py    
@Contact :   18302010009@fudan.edu.cn

@Modify Time      @Author         @Version    @Desciption
------------      ------------    --------    -----------
2020/11/30 17:29   Shen Zhengyu      1.0         None
'''

# import lib
file1 = open("output1.txt")
result1 = ''
for line in file1:
    result1 = result1 + line.strip("\n")

file2 = open("output.utf8")
result2 = ''
for line in file2:
    result2 = result2 + line.strip("\n")

count = 0
for i in range(len(result1)):
    if result1[i] == result2[i]:
        count += 1
print(count*1.0/len(result2))