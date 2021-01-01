#!/usr/bin/env python
# -*- encoding: utf-8 -*-
'''
@File    :   HMM_model.py    
@Contact :   18302010009@fudan.edu.cn

@Modify Time      @Author         @Version    @Desciption
------------      ------------    --------    -----------
2020/11/30 16:55   Shen Zhengyu      1.0         None
'''
from numpy import *
import os
import torch


class HMMModel:
    # 初始化所有概率矩阵
    def __init__(self):
        self.state_list = ['B', 'I', 'E', 'S']
        self.state_transition_matrix = {}
        self.emission_matrix = {}  # 发射概率
        self.word_set = set()  # 训练数据集中所有字的集合
        self.count_map = {}  # ‘B,I,E,S’每个状态在训练集中出现的次数
        self.num_of_trained_sentences = 0  # 训练集语句数量
        self.original_state_matrix = {}  # 初始状态分布
        if os.path.isfile("models/HMM.model"):
            checkpoint = torch.load("models/HMM.model")
            self.state_list = checkpoint['state_list']
            self.state_transition_matrix = checkpoint['state_transition_matrix']
            self.emission_matrix = checkpoint['emission_matrix ']
            self.original_state_matrix = checkpoint['original_state_matrix']
        else:
            trainset1 = open("dataset/dataset1/train.utf8", encoding='utf-8')
            trainset2 = open("dataset/dataset2/train.utf8", encoding='utf-8')
            trainsets = [trainset1, trainset2]
            self.initiate_arrays()
            tmpline = ""
            for trainset in trainsets:
                for word_line in trainset:
                    if "" == word_line.strip():
                        # 一段结束了
                        line = tmpline.strip()
                        tmpline = ""
                        self.num_of_trained_sentences += 1
                        # 单字集合
                        word_list = []
                        for k in range(len(line)):
                            if line[k] == ' ':
                                continue
                            word_list.append(line[k])
                        # 取并集，逐步扩大word_set
                        self.word_set = self.word_set | set(word_list)
                        line = line.split(' ')
                        # 得到line列表，每个元素都是一个词语
                        line_state = []
                        for i in line:
                            # append 状态描述
                            line_state.extend(self.get_tag(i))
                        # 为第一个词的original_state_matrix位置 +1
                        self.original_state_matrix[line_state[0]] += 1  # 初始状态分布概率
                        for j in range(len(line_state) - 1):
                            # 不断计算转移概率
                            self.state_transition_matrix[line_state[j]][line_state[j + 1]] += 1
                        for p in range(len(line_state)):
                            if len(word_list) != 0:
                                self.count_map[line_state[p]] += 1  # 记录每一个状态的出现次数
                                for state in self.state_list:
                                    if word_list[p] not in self.emission_matrix[state]:
                                        # 保证健壮性
                                        self.emission_matrix[state][word_list[p]] = 0.0
                                # 状态-值的发射概率
                                self.emission_matrix[line_state[p]][word_list[p]] += 1
                    else:
                        list = word_line.replace("\n", "").split(" ")
                        word = list[0]
                        label = list[1]
                        # 按词分开
                        if label == 'S' or label == 'E':
                            tmpline = tmpline + word + " "
                        elif label == 'I' or label == 'B':
                            tmpline += word
            # 取对数
            self.Logarithm()
            torch.save({
                'state_transition_matrix': self.state_transition_matrix,
                'emission_matrix ': self.emission_matrix,
                'original_state_matrix': self.original_state_matrix,
                'state_list': self.state_list
            }, "models/HMM.model")

    # 初始化
    def initiate_arrays(self):
        for state0 in self.state_list:
            self.state_transition_matrix[state0] = {}
            for state1 in self.state_list:
                self.state_transition_matrix[state0][state1] = 0.0
        for state in self.state_list:
            self.original_state_matrix[state] = 0.0
            self.emission_matrix[state] = {}
            self.array_E = {}
            self.count_map[state] = 0

    # 对训练集获取状态标签
    def get_tag(self, word):
        tag = []
        if len(word) == 1:
            tag = ['S']
        elif len(word) == 2:
            tag = ['B', 'E']
        else:
            num = len(word) - 2
            tag.append('B')
            tag.extend(['I'] * num)
            tag.append('E')
        return tag

    # 将参数估计的概率取对数，对概率0取无穷小-3.14e+100
    def Logarithm(self):
        min = -3.14e+100
        for key in self.original_state_matrix:
            if self.original_state_matrix[key] == 0:
                self.original_state_matrix[key] = min
            else:
                self.original_state_matrix[key] = log(self.original_state_matrix[key] / self.num_of_trained_sentences)
        for key0 in self.state_transition_matrix:
            for key1 in self.state_transition_matrix[key0]:
                self.state_transition_matrix[key0][key1] = min if self.state_transition_matrix[key0][
                                                                      key1] == 0.0 else log(
                    self.state_transition_matrix[key0][key1] / self.count_map[key0])
        for key in self.emission_matrix:
            for word in self.emission_matrix[key]:
                self.emission_matrix[key][word] = min if self.emission_matrix[key][word] == 0.0 else log(
                    self.emission_matrix[key][word] / self.count_map[key])

    # Viterbi算法求测试集最优状态序列
    def Viterbi(self, sentence, original_state_matrix, state_transition_matrix, emission_matrix):
        tab = [{}]  # for dp 4是状态数(0:B,1:E,2:M,3:S)，15是输入句子的字数。比如 `weight[0][2] `代表 状态B的条件下，出现’硕’这个字的可能性。
        path = {} # 4是状态数(0:B,1:E,2:M,3:S)，15是输入句子的字数。比如 `path[0][2]` 代表 `weight[0][2]`取到最大时，前一个字的状态，比如 `path[0][2]` = 1, 则代表 `weight[0][2]`取到最大时，前一个字(也就是明)的状态是E。记录前一个字的状态是为了使用viterbi算法计算完整个 `weight[4][15]` 之后，能对输入句子从右向左地回溯回来，找出对应的状态序列。
        min = -3.14e+100
        if sentence[0] not in emission_matrix['B']:
            for state in self.state_list:
                emission_matrix[state][sentence[0]] = 0 if state == 'S' else min
        for state in self.state_list:
            tab[0][state] = original_state_matrix[state] + emission_matrix[state][sentence[0]]
            path[state] = [state]
        for i in range(1, len(sentence)):
            tab.append({})
            new_path = {}
            for state in self.state_list:
                emission_matrix[state]['begin'] = 0 if state == 'B' else min
            for state in self.state_list:
                emission_matrix[state]['end'] = 0 if state == 'E' else min
            for state0 in self.state_list:
                items = []
                for state1 in self.state_list:
                    # 例外情况
                    if sentence[i] not in emission_matrix[state0]:
                        tmp = emission_matrix[state0]['end'] if sentence[i - 1] not in emission_matrix[state0] else \
                        emission_matrix[state0]['begin']
                        # 概率计算
                        prob = tab[i - 1][state1] + state_transition_matrix[state1][state0] + tmp
                    else:
                        prob = tab[i - 1][state1] + state_transition_matrix[state1][state0] + emission_matrix[state0][
                            sentence[i]]
                    items.append((prob, state1))
                # bset:(prob,state)
                most = max(items)
                tab[i][state0] = most[0]
                new_path[state0] = path[most[1]] + [state0]
            path = new_path
        prob, state = max([(tab[len(sentence) - 1][state], state) for state in self.state_list])
        return path[state]

    # 根据状态序列进行分词
    def segment_tag(self, sentence, tag):
        word_list = []
        start = -1
        started = False
        if len(tag) != len(sentence):
            return None
        if len(tag) == 1:
            word_list.append(sentence[0])
        else:
            if tag[-1] == 'B' or tag[-1] == 'I':
                tag[-1] = 'E' if tag[-2] == 'B' or tag[-2] == 'I' else 'S'
            for i in range(len(tag)):
                if tag[i] == 'S':
                    if started:
                        started = False
                        word_list.append(sentence[start:i])
                    word_list.append(sentence[i])
                elif tag[i] == 'B':
                    if started:
                        word_list.append(sentence[start:i])
                    start = i
                    started = True
                elif tag[i] == 'E':
                    started = False
                    word_list.append(sentence[start:i + 1])
                elif tag[i] == 'I':
                    continue
        return word_list

    def predict(self, sentence):
        line = sentence.strip()
        segment = self.segment_tag(line, self.Viterbi(line, self.original_state_matrix, self.state_transition_matrix,
                                                      self.emission_matrix))
        result = ''
        for i in range(len(segment)):
            if len(segment[i]) == 1:
                result = result + 'S'
            elif len(segment[i]) == 2:
                result = result + 'BE'
            else:
                result = result + 'B' + (len(segment[i]) - 2) * 'I' + 'E'
        return result
