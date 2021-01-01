#!/usr/bin/env python
# -*- encoding: utf-8 -*-
'''
@File    :   rotate.py    
@Contact :   18302010009@fudan.edu.cn

@Modify Time      @Author         @Version    @Desciption
------------      ------------    --------    -----------
2020/11/8 13:50   Shen Zhengyu      1.0         None
'''

# import lib

from PIL import Image
import matplotlib.pyplot as plt
import os
import cv2
import numpy as np
def rotate_whole_img(img):
    """
       rotate noise
       rotate angle is 0 - 20
    """
    angle = np.random.randint(0, 10)
    h, w = img.shape[:2]
    center = (w / 2, h / 2)
    M = cv2.getRotationMatrix2D(center, angle, 1)
    im = cv2.warpAffine(img, M, (w, h), borderValue=(255, 255, 255))
    return im

root = './ttest'
root1 = './tttest'
imgs = os.listdir(root)
print(imgs)
# 读取图像
for i in imgs:
    img = cv2.imread(root + "/" + i)
    im_rotate = rotate_whole_img(img)
    cv2.imwrite(root1 + "/" + i,im_rotate)


