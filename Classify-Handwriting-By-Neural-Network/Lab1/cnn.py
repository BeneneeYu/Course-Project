#!/usr/bin/env python
# -*- encoding: utf-8 -*-
'''
@File    :   cnn.py    
@Contact :   18302010009@fudan.edu.cn

@Modify Time      @Author         @Version    @Desciption
------------      ------------    --------    -----------
2020/10/28 22:45   Shen Zhengyu      1.0         None
'''

# import lib
from torch import nn


class CNN(nn.Module):
    def __init__(self):
        super(CNN, self).__init__()
        self.ConvolutionLayer1 = nn.Sequential(
            nn.Conv2d(1, 25, kernel_size=3),
            nn.BatchNorm2d(25),
            nn.ReLU(inplace=True)
        )

        self.MaxPoolingLayer2 = nn.Sequential(
            nn.MaxPool2d(kernel_size=2, stride=2)
        )

        self.ConvolutionLayer3 = nn.Sequential(
            nn.Conv2d(25, 50, kernel_size=3),
            nn.BatchNorm2d(50),
            nn.ReLU(inplace=True)
        )

        self.MaxPoolingLayer4 = nn.Sequential(
            nn.MaxPool2d(kernel_size=2, stride=2)
        )

        self.FCLayer = nn.Sequential(
            nn.Linear(50 * 5 * 5, 1024),
            nn.ReLU(inplace=True),
            nn.Dropout(0.2),
            nn.Linear(1024, 128),
            nn.ReLU(inplace=True),
            nn.Dropout(0.5),
            nn.Linear(128, 12)
        )

    def forward(self, x):
        x = self.ConvolutionLayer1(x)
        x = self.MaxPoolingLayer2(x)
        x = self.ConvolutionLayer3(x)
        x = self.MaxPoolingLayer4(x)
        x = x.view(x.size(0), -1)
        x = self.FCLayer(x)
        return x
