#!/usr/bin/env python
# -*- encoding: utf-8 -*-
'''
@File    :   work.py    
@Contact :   18302010009@fudan.edu.cn

@Modify Time      @Author         @Version    @Desciption
------------      ------------    --------    -----------
2020/10/28 22:47   Shen Zhengyu      1.0         None
'''

# import lib
import torch
import time
from torch import nn, optim
from torch.autograd import Variable
from torch.utils.data import DataLoader
from torchvision import datasets, transforms
from torchvision.datasets import ImageFolder
from MySet import MySet
import numpy as np
import cnn

# 定义一些超参数
batch_size = 64
learning_rate = 0.001
num_epoches = 6

def zh(i):
    if i >= 1 and i <=3:
        return i+9
    elif i == 0:
        return 1
    else:
        return i-2
# 数据预处理。transforms.ToTensor()将图片转换成PyTorch中处理的对象Tensor,并且进行标准化（数据在0~1之间）
# transforms.Normalize()做归一化。它进行了减均值，再除以标准差。两个参数分别是均值和标准差
# transforms.Compose()函数则是将各种预处理的操作组合到了一起
# 归一化到-1，1
def loadData(trainSrc,testSrc,normalTest):
    data_tf = transforms.Compose(
        [transforms.ToTensor(),
         transforms.Normalize([0.5], [0.5])])
    train_dataset = ImageFolder(trainSrc, transform=data_tf)
    trainLoader = DataLoader(train_dataset,batch_size=batch_size, shuffle=True)
    if normalTest:
        test_dataset = MySet(testSrc)
        test_loader = DataLoader(test_dataset, shuffle=False)
    else:
        test_dataset = ImageFolder(testSrc, transform=data_tf)
        test_loader = DataLoader(test_dataset, batch_size=batch_size, shuffle=True)
    return trainLoader,test_loader,train_dataset,test_dataset,normalTest
# 选择模型
model = cnn.CNN()
# model = net.Activation_Net(28 * 28, 300, 100, 10)
# model = net.Batch_Net(28 * 28, 300, 100, 10)
if torch.cuda.is_available():
    model = model.cuda()

# 定义损失函数和优化器
criterion = nn.CrossEntropyLoss()
optimizer = optim.Adam(model.parameters(),lr=learning_rate, betas=(0.9, 0.999), eps=1e-08, weight_decay=0)
# train_loader,test_loader,train_dataset,test_dataset,normalTest = loadData("completeTrainSet\\","completeTestSet\\",True)
train_loader,test_loader,train_dataset,test_dataset,normalTest = loadData("train\\","test\\",False) #测试精准度
# train_loader,test_loader,train_dataset,test_dataset,normalTest = loadData("train\\","ttest\\",True) #试着直接输出结果

# 训练模型
# 面试时，需要将它替换为全数据集
for i in range(1,num_epoches):
    for data in train_loader:
        # img是3*28*28列表 label是长度64列表
        img = data[0]
        img = img.narrow(1,0,1)
        label = data[1]
        # img.view(64,1,28,28)
        # print('img')
        # # 第一层batch
        # print(len(img))
        # # 第二层rgb信道数
        # print(len(img[0]))
        # # 第三层长
        # print(len(img[0][0]))
        # # 第四层宽
        # print(len(img[0][0][0]))
        # print(img[0][0][5])
        # print('label')
        # # 一个batch size中的各个图片类别
        # print(len(label))
        # img = img.view(img.size(0), -1)
        img = Variable(img)
        if torch.cuda.is_available():
            img = img.cuda()
            label = label.cuda()
        else:
            img = Variable(img)
            label = Variable(label)
        out = model(img)
        loss = criterion(out, label)
        print_loss = loss.data.item()
        optimizer.zero_grad()
        loss.backward()
        optimizer.step()
    print('epoch: {}, loss: {:.4}'.format(i, loss.data.item()))

# 模拟识别任务
model.eval()
eval_loss = 0
eval_acc = 0
if normalTest:
    str_list = []
    for data in test_loader:
        img = data
    # img = img.view(img.size(0), -1)
        img = Variable(img)
        if torch.cuda.is_available():
            img = img.cuda()
        out = model(img)
        for i in out:
            str_list.append(str(zh(i.argmax().item())))
            str_list.append("\n")
    del(str_list[-1])
    data = ''.join(str_list)
    result2txt = str(data)  # data是前面运行出的数据，先将其转为字符串才能写入
    with open('result' + str(time.time()) + '.txt', 'a') as file_handle:  # .txt可以不自己新建,代码会自动新建
        file_handle.write(result2txt)  # 写入
else:
    for data in test_loader:
        img = data[0]
        img = img.narrow(1, 0, 1)
        label = data[1]

        with torch.no_grad():
            img   = Variable(img)
            label = Variable(label)

        out  = model(img)
        loss = criterion(out, label)
        eval_loss += loss.item() * label.size(0)
        _, pred = torch.max(out, 1)
        num_correct = (pred == label).sum()
        eval_acc += num_correct.item()
    print('Test Loss: {:.6f}, Acc: {:.6f}'.format(eval_loss / (len(test_dataset)), eval_acc / (len(test_dataset))))



