#!/usr/bin/env python
# -*- encoding: utf-8 -*-
'''
@File    :   MySet.py
@Contact :   18302010009@fudan.edu.cn

@Modify Time      @Author         @Version    @Desciption
------------      ------------    --------    -----------
2020/11/4 21:54   Shen Zhengyu      1.0         None
'''

# import lib
import os
import torch
from torch.utils import data
from PIL import Image
import numpy as np
from torchvision import transforms

# transform = transforms.Compose([
#     transforms.ToTensor(),  # 将图片转换为Tensor,归一化至[0,1]
#     # transforms.Normalize(mean=[.5, .5, .5], std=[.5, .5, .5])  # 标准化至[-1,1]
# ])
transform = transforms.Compose(
    [transforms.ToTensor(),
     transforms.Normalize([0.5], [0.5])])

#定义自己的数据集合
class MySet(data.Dataset):

    def __init__(self,root):
        # 所有图片的绝对路径
        imgs=os.listdir(root)
        self.imgs = [imgs[0]] * len(imgs)
        for i in range(1,len(imgs)+1):
            wjm = str(i) + '.bmp'
            self.imgs[i-1] = os.path.join(root,wjm)
        print(self.imgs)
        self.transforms=transform

    def __getitem__(self, index):
        img_path = self.imgs[index]
        pil_img = Image.open(img_path)
        if self.transforms:
            data = self.transforms(pil_img)
        else:
            pil_img = np.asarray(pil_img)
            data = torch.from_numpy(pil_img)
        return data

    def __len__(self):
        return len(self.imgs)