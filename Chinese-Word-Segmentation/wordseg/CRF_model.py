#!/usr/bin/env python
# -*- encoding: utf-8 -*-
'''
@File    :   CRF_model.py    
@Contact :   18302010009@fudan.edu.cn

@Modify Time      @Author         @Version    @Desciption
------------      ------------    --------    -----------
2020/12/1 16:41   Shen Zhengyu      1.0         None
'''

# import lib
import torch
import os


class CRFModel:
    scoreMap = {}
    UnigramTemplates = []
    BigramTemplates = []
    trained = False

    def __init__(self):
        if os.path.isfile("models/CRF.model"):
            checkpoint = torch.load("models/CRF.model")
            self.scoreMap = checkpoint['scoreMap']
            self.UnigramTemplates = checkpoint['UnigramTemplates']
            self.BigramTemplates = checkpoint['BigramTemplates']
            self.trained = True
        else:
            self.scoreMap = {}
            self.readTemplate()
        self.dic1 = {0: 'B', 1: 'I', 2: 'E', 3: 'S'}
        self.dic2 = {'B': 0, 'I': 1, 'E': 2, 'S': 3}

    #  加载模板
    def readTemplate(self):
        tempFile = open("../dataset/dataset1/template.utf8", encoding='utf-8')
        switchFlag = False
        for line in tempFile:
            tmpList = []
            if line.find("Unigram") > 0 or line.find("Bigram") > 0:
                continue
            if switchFlag:
                if line.find("/") > 0:
                    tmpList.append(int(line.split("/")[0].split("[")[-1].split(",")[0]))
                    tmpList.append(int(line.split("/")[-1].split("[")[-1].split(",")[0]))
                else:
                    num = line.split("[")[-1].split(",")[0]
                    tmpList.append(int(num))
                self.BigramTemplates.append(tmpList)
            else:
                if len(line.strip()) == 0:
                    switchFlag = True
                else:
                    if line.find("/") > 0:
                        tmpList.append(int(line.split("/")[0].split("[")[-1].split(",")[0]))
                        tmpList.append(int(line.split("/")[-1].split("[")[-1].split(",")[0]))
                    else:
                        num = line.split("[")[-1].split(",")[0]
                        tmpList.append(int(num))
                    self.UnigramTemplates.append(tmpList)

    # 按段和相应的序列存储
    def readData(self, fileName):
        sentences = []
        results = []
        tempFile = open(fileName, encoding='utf-8')
        sentence = ""
        result = ""
        for line in tempFile:
            line = line.strip()
            if line == "":
                if sentence != "" and result != "":
                    sentences.append(sentence)
                    results.append(result)
                sentence = ""
                result = ""
            else:
                data = line.split(" ")
                sentence += data[0]
                result += data[1]
        return [sentences, results]

    def showTemplates(self):
        print(self.UnigramTemplates)
        print(self.BigramTemplates)

    # 给出根据当前参数计算的中文句→状态预测的转变
    def segment(self, sentence):
        lens = len(sentence)
        initialStatus = []
        for i in range(4):
            initialStatus.append([])
            for j in range(lens):
                initialStatus[i].append("")
        scoreMap = []
        for i in range(4):
            scoreMap.append([])
            for j in range(lens):
                scoreMap[i].append(0)
        for col in range(0, lens):
            for row in range(0, 4):
                thisStatus = self.getStatus(row)
                if col == 0:
                    scoreMap[row][0] = self.calScoreOfUnigram(sentence, 0, thisStatus) + self.calScoreOfBigram(sentence, 0, " ", thisStatus)
                    initialStatus[row][0] = None
                else:
                    scores = [0, 0, 0, 0]
                    for i in range(0, 4):
                        uniScore = self.calScoreOfUnigram(sentence, col, thisStatus)
                        biScore = self.calScoreOfBigram(sentence, col, self.getStatus(i), thisStatus)
                        #trans score
                        scores[i] = scoreMap[i][col - 1] + uniScore + biScore
                    maxIndex = self.getMaxIndex(scores)
                    scoreMap[row][col] = scores[maxIndex]
                    initialStatus[row][col] = self.getStatus(maxIndex)
        results = [""] * lens
        scoreBuf = [0] * 4
        if lens > 0:
            for i in range(0, 4):
                scoreBuf[i] = scoreMap[i][lens - 1]
            results[lens - 1] = self.getStatus(self.getMaxIndex(scoreBuf))
        for backIndex in range(lens - 2, -1, -1):
            results[backIndex] = initialStatus[self.statusToRow(results[backIndex + 1])][backIndex + 1]
        res = "".join(results)
        return res

    # 训练
    def train(self, sentence, theoryRes):
        myRes = self.segment(sentence)
        lens = len(sentence)
        wrongNum = 0
        for i in range(0, lens):
            myResI = myRes[i]
            theoryResI = theoryRes[i]
            if myResI != theoryResI:
                wrongNum += 1
                uniTem = self.UnigramTemplates
                uniNum = len(uniTem)
                for uniIndex in range(0, uniNum):
                    uniMyKey = self.generateKey(uniTem[uniIndex], str(uniIndex), sentence, i, myResI)
                    if uniMyKey not in self.scoreMap:
                        self.scoreMap[uniMyKey] = -1
                    else:
                        myRawVal = self.scoreMap[uniMyKey]
                        self.scoreMap[uniMyKey] = myRawVal - 1
                    uniTheoryKey = self.generateKey(uniTem[uniIndex], str(uniIndex), sentence, i, theoryResI)
                    if uniTheoryKey not in self.scoreMap:
                        self.scoreMap[uniTheoryKey] = 1
                    else:
                        theoryRawVal = self.scoreMap[uniTheoryKey]
                        self.scoreMap[uniTheoryKey] = theoryRawVal + 1
                biTem = self.BigramTemplates
                biNum = len(biTem)
                for biIndex in range(0, biNum):
                    if i >= 1:
                        biMyKey = self.generateKey(biTem[biIndex], str(biIndex), sentence, i, myRes[i - 1:i + 1:])
                        biTheoryKey = self.generateKey(biTem[biIndex], str(biIndex), sentence, i, myRes[i - 1:i + 1:])
                    else:
                        biMyKey = self.generateKey(biTem[biIndex], str(biIndex), sentence, i, " " + str(myResI))
                        biTheoryKey = self.generateKey(biTem[biIndex], str(biIndex), sentence, i, " " + str(theoryResI))
                    if biMyKey not in self.scoreMap:
                        self.scoreMap[biMyKey] = -1
                    else:
                        myRawVal = self.scoreMap[biMyKey]
                        self.scoreMap[biMyKey] = myRawVal - 1
                    if biTheoryKey not in self.scoreMap:
                        self.scoreMap[biTheoryKey] = 1
                    else:
                        theoryRawVal = self.scoreMap[biTheoryKey]
                        self.scoreMap[biTheoryKey] = theoryRawVal + 1
        return wrongNum

    def generateKey(self, template, identity, sentence, pos, statusCovered):
        result = ""
        result += identity
        for i in template:
            index = pos + i
            if index < 0 or index >= len(sentence):
                result += " "
            else:
                result += sentence[index]
        result += "/"
        result += statusCovered
        return result

    def calScoreOfBigram(self, sentence, thisPos, preStatus, thisStatus):
        bigramScore = 0
        bigramTemplates = self.BigramTemplates
        for i in range(0, len(bigramTemplates)):
            key = self.generateKey(bigramTemplates[i], str(i), sentence, thisPos, preStatus + thisStatus)
            if key in self.scoreMap:
                bigramScore += self.scoreMap[key]
        return bigramScore

    def calScoreOfUnigram(self, sentence, thisPos, thisStatus):
        unigramScore = 0
        unigramTemplates = self.UnigramTemplates
        for i in range(0, len(unigramTemplates)):
            key = self.generateKey(unigramTemplates[i], str(i), sentence, thisPos, thisStatus)
            if key in self.scoreMap:
                unigramScore += self.scoreMap[key]
        return unigramScore

    def getStatus(self, row):
        if row >= 0 and row <= 3:
            return self.dic1[row]
        else:
            return None

    def statusToRow(self, status):
        returnVal = self.dic2[status] if status in self.dic2.keys() else -1
        return returnVal

    def getMaxIndex(self, list):
        origin = list.copy()
        origin.sort()
        index = list.index(origin[-1])
        return index

    def getDuplicate(self, s1, s2):
        length = min(len(s1), len(s2))
        count = 0
        for i in range(0, length):
            if s1[i] == s2[i]:
                count += 1
        return count

    def whole_train(self):
        train_datasets = ["../dataset/dataset2/train.utf8"]
        count = 0
        for trains in train_datasets:
            sentences, results = self.readData(trains)
            whole = len(sentences)
            trainNum = int(whole * 0.8)
            for testIndex in range(1, 15):
                count += 1
                wrongNum = 0
                totalTest = 0
                for i in range(0, trainNum):
                    sentence = sentences[i]
                    totalTest += len(sentence)
                    result = results[i]
                    wrongNum += self.train(sentence, result)
                correctNum = totalTest - wrongNum
                print("epoch" + str(count) + ":accuracy" + str(float(correctNum / totalTest)))
                total = 0
                correct = 0
                for i in range(trainNum, whole):
                    sentence = sentences[i]
                    total += len(sentence)
                    result = results[i]
                    myRes = self.segment(sentence)
                    correct += self.getDuplicate(result, myRes)
                accuracy = float(correct / total)
                print("accuracy" + str(accuracy))
                torch.save({
                    'scoreMap': self.scoreMap,
                    'BigramTemplates': self.BigramTemplates,
                    'UnigramTemplates': self.UnigramTemplates
                }, "../models/CRF.model")


    def predict(self, sentence):
        if self.trained:
            return self.segment(sentence)
        else:
            self.whole_train()
            return self.segment(sentence)

if __name__ == '__main__':
    model = CRFModel()
    model.whole_train()
