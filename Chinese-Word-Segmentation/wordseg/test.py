#!/usr/bin/env python
# -*- encoding: utf-8 -*-
'''
@File    :   test.py    
@Contact :   18302010009@fudan.edu.cn

@Modify Time      @Author         @Version    @Desciption
------------      ------------    --------    -----------
2020/11/30 20:39   Shen Zhengyu      1.0         None
'''

# import lib
import os
class Test():
    # print(os.path.exists("models/HMM/array_Pi.data") and os.path.exists("models/HMM/array_A.data") and os.path.exists("models/HMM/array_B.data"))
    # trainset = open("../dataset/dataset2/train.utf8", encoding='utf-8')
    # for line in trainset:
    #     print(line)

    # def create_file(filename):
    #     """
    #     创建日志文件夹和日志文件
    #     :param filename:
    #     :return:
    #     """
    #     path = filename[0:filename.rfind("/")]
    #     if not os.path.isdir(path):  # 无文件夹时创建
    #         os.makedirs(path)
    #     if not os.path.isfile(filename):  # 无文件时创建
    #         fd = open(filename, mode="w", encoding="utf-8")
    #         fd.close()
    #     else:
    #         pass

    # create_file("../models/HMM/1.txt")
    # print(os.path.exists("../models/HMM/array_Pi.data") and os.path.exists("../models/HMM/array_A.data") and os.path.exists("../models/HMM/array_B.data"))
    #
    # print(os.path.isfile("../models/HMM/array_Pi.txt"))
    def show(self):
        from .CRF_model import CRFModel
        model = CRFModel()
        model.readTemplate()
        model.showTemplates()

# if __name__=='__main__':
str = "cnm"
print(str[0:-1])
# class Solution(object):
#     numSet = set()
#     def isHappy(self, n):
#         """
#         :type n: int
#         :rtype: bool
#         """
#         sum = 0
#         while n > 0:
#             sum += (n%10)*(n%10)
#             n /= 10
#         if sum in self.numSet:
#             return False
#         else:
#             self.numSet.add(sum)
#             self.isHappy(sum)