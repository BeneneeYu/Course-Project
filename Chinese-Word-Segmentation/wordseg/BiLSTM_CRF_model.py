#!/usr/bin/env python
# -*- encoding: utf-8 -*-
'''
@File    :   BiLSTM_CRF_model.py    
@Contact :   18302010009@fudan.edu.cn

@Modify Time      @Author         @Version    @Desciption
------------      ------------    --------    -----------
2020/12/2 18:08   Shen Zhengyu      1.0         None
'''

# import lib
import torch
from torch import nn
from torch import optim
import datetime


class BiLSTM_CRF(nn.Module):
    def __init__(self):
        super(BiLSTM_CRF, self).__init__()
        self.embedding_dim = 256
        self.hidden_dim = 128
        self.epochs = 50
        self.START_TAG = "<START>"
        self.STOP_TAG = "<STOP>"
        self.tag_to_ix = {"B": 0, "I": 1, "E": 2, "S": 3, self.START_TAG: 4, self.STOP_TAG: 5}
        self.trainSet = {}  # 维护句子对应状态
        tmp_sentence = ''
        tmp_sentence_state = ''
        pre_null = False
        with open('../dataset/dataset1/train.utf8', encoding='utf-8') as trainFile:
            while True:
                s = trainFile.readline()
                if s == "":  # 文件读完
                    break
                s = s.strip()  # 去掉头尾空格
                if s == "":  # 读到换行符
                    if not pre_null:
                        self.trainSet[tmp_sentence] = tmp_sentence_state
                        tmp_sentence = ''
                        tmp_sentence_state = ''
                    pre_null = True
                    continue
                pre_null = False
                s = s.replace(" ", "")
                tmp_sentence += s[0]
                tmp_sentence_state += s[1]
        # print(self.trainSet)
        content = []
        label = []
        for key in self.trainSet:
            tmp_content = []
            tmpLabel = []
            for i in range(len(key)):
                tmp_content.extend(key[i])
            content.append(tmp_content)
            for i in range(len(self.trainSet[key])):
                tmpLabel.extend(self.trainSet[key][i])
            label.append(tmpLabel)
        self.data = []
        for i in range(len(label)):
            self.data.append((content[i], label[i]))

        self.word_to_ix = {'U': 0}
        for sentence, tags in self.data:
            for word in sentence:
                if word not in self.word_to_ix:
                    # 如果这个word不在字典中，那么将它加入字典，并且对应当前字典的长度
                    self.word_to_ix[word] = len(self.word_to_ix)

        self.hasTrained = False

        self.vocab_size = len(self.word_to_ix)
        self.tagset_size = len(self.tag_to_ix)

        self.word_embeds = nn.Embedding(self.vocab_size, self.embedding_dim)
        self.lstm = nn.LSTM(self.embedding_dim, hidden_size=self.hidden_dim // 2,num_layers=1, bidirectional=True)
        self.hidden2tag = nn.Linear(self.hidden_dim, self.tagset_size)
        self.transitions = nn.Parameter(torch.randn(self.tagset_size, self.tagset_size))
        self.transitions.data[self.tag_to_ix[self.START_TAG], :] = -10000
        self.transitions.data[:, self.tag_to_ix[self.STOP_TAG]] = -10000
        self.hidden = self.initiate_hidden_layer()

    # @param seq:句子的字序列，idxs:字序列对应的向量，to_ix:字和序号的字典
    def prepare_sequence(selr, seq, to_ix):  #
        idxs = [to_ix[w] for w in seq]
        return torch.tensor(idxs, dtype=torch.long)

    # @return 每一行最大值和索引
    def argMax(self, vec):
        _, idx = torch.max(vec, 1)
        return idx.item()

    def log_sum_exp(self, vec):
        max_score = vec[0, self.argMax(vec)]
        max_score_broadcast = max_score.view(1, -1).expand(1, vec.size()[1])
        return max_score + torch.log(torch.sum(torch.exp(vec - max_score_broadcast)))

    # 前向
    def forward_procedure(self, feats):
        init_alphas = torch.full((1, self.tagset_size), -10000.)
        init_alphas[0][self.tag_to_ix[self.START_TAG]] = 0.
        forward_var = init_alphas
        for feat in feats:
            alphas_t = []
            for next_tag in range(self.tagset_size):
                # trans score + emission score
                next_tag_var = forward_var + self.transitions[next_tag].view(1, -1) + feat[next_tag].view(1, -1).expand(1, self.tagset_size)
                alphas_t.append(self.log_sum_exp(next_tag_var).view(1))
            forward_var = torch.cat(alphas_t).view(1, -1)
        terminal_var = forward_var + self.transitions[self.tag_to_ix[self.STOP_TAG]]
        alpha = self.log_sum_exp(terminal_var)
        return alpha

    # 获取特征
    def get_lstm_features(self, sentence):
        self.hidden = self.initiate_hidden_layer()
        embeds = self.word_embeds(sentence).view(len(sentence), 1, -1)
        lstm_out, self.hidden = self.lstm(embeds, self.hidden)
        lstm_out = lstm_out.view(len(sentence), self.hidden_dim)
        lstm_features = self.hidden2tag(lstm_out)
        return lstm_features


    def initiate_hidden_layer(self):
        seed = self.hidden_dim // 2
        return (torch.randn(2, 1, seed), torch.randn(2, 1, seed))


    # 评分
    def scoreSentence(self, feats, tags):
        score = torch.zeros(1)
        tags = torch.cat([torch.tensor([self.tag_to_ix[self.START_TAG]], dtype=torch.long), tags])
        for i, feat in enumerate(feats):
            score = score + self.transitions[tags[i + 1], tags[i]] + feat[tags[i + 1]]
        score = score + self.transitions[self.tag_to_ix[self.STOP_TAG], tags[-1]]
        return score

    def Viterbi(self, feats):
        backpointers = []
        # log space
        tmp_vars = torch.full((1, self.tagset_size), -10000.)
        tmp_vars[0][self.tag_to_ix[self.START_TAG]] = 0
        forward_var = tmp_vars
        for feat in feats:
            bptrs_t = []
            viterbivars_t = []
            for next_tag in range(self.tagset_size):
                next_tag_var = forward_var + self.transitions[next_tag]
                best_tag_id = self.argMax(next_tag_var)
                bptrs_t.append(best_tag_id)
                viterbivars_t.append(next_tag_var[0][best_tag_id].view(1))
            forward_var = (torch.cat(viterbivars_t) + feat).view(1, -1)
            backpointers.append(bptrs_t)
        terminal_var = forward_var + self.transitions[self.tag_to_ix[self.STOP_TAG]]
        best_tag_id = self.argMax(terminal_var)
        path_score = terminal_var[0][best_tag_id]
        best_path = [best_tag_id]
        for bptrs_t in reversed(backpointers):
            best_tag_id = bptrs_t[best_tag_id]
            best_path.append(best_tag_id)
        start = best_path.pop()
        assert start == self.tag_to_ix[self.START_TAG]
        best_path.reverse()
        return path_score, best_path

    def negLogLikelihood(self, sentence, tags):
        feats = self.get_lstm_features(sentence)
        forward_score = self.forward_procedure(feats)
        gold_score = self.scoreSentence(feats, tags)
        return forward_score - gold_score

    def forward(self, sentence):
        lstm_feats = self.get_lstm_features(sentence)
        score, tag_seq = self.Viterbi(lstm_feats)
        return score, tag_seq

    def myTrain(self):
        if self.hasTrained:
            return
        self = torch.load("../models/BiLstm-crf62-tensor(2.2528).model")
        optimizer = optim.Adam(self.parameters(), lr=0.005)

        for epoch in range(self.epochs):
            print(datetime.datetime.now())
            print("epoch " + str(epoch + 1) + " 开始")
            for sentence, tags in self.data:
                self.zero_grad()
                sentence_in = self.prepare_sequence(sentence, self.word_to_ix)
                targets = torch.tensor([self.tag_to_ix[t] for t in tags], dtype=torch.long)
                loss = self.negLogLikelihood(sentence_in, targets)
                loss.backward()
                optimizer.step()
            print('epoch/epochs:{}/{},loss:{:.6f}'.format(epoch + 1, self.epochs, loss.data[0]))
            torch.save(self, '../models/BiLstm-crf' + str(epoch + 65) + '-' + str(loss.data[0]) + '.model')

    def predict(self, sentence):
        self.hasTrained = True
        for i in range(len(sentence)):
            if sentence[i] not in list(self.word_to_ix.keys()):
                tmp = list(sentence)
                tmp[i] = 'U'
                sentence = ''.join(tmp)
        net = torch.load('models/BiLstm-crf62-tensor(2.2528).model')
        net.eval()
        precheck_sent = self.prepare_sequence(sentence, self.word_to_ix)
        # 调用forward
        label = net(precheck_sent)[1]
        str = ""
        pre = 'E'
        smartAss = True
        for i in label:
            # B I E S
            if i == 0:
                if smartAss and (pre == 'B' or pre == 'I'):
                    str += 'E'
                    pre = 'E'
                else:
                    str += 'B'
                    pre = 'B'
            elif i == 1:
                if smartAss and (pre == 'E' or pre == 'S'):
                    str += 'B'
                    pre = 'B'
                else:
                    str += 'I'
                    pre = 'I'
            elif i == 2:
                if smartAss and (pre == 'E' or pre == 'S'):
                    str += 'B'
                    pre = 'B'
                else:
                    str += 'E'
                    pre = 'E'
            elif i == 3:
                if smartAss and (pre == 'I' or pre == 'B'):
                    str += 'E'
                    pre = 'E'
                else:
                    str += 'S'
                    pre = 'S'
        if str[-1] == 'I' or str[-1] == 'B':
            if len(str) >= 2:
                if str[-2] == 'S':
                    str = str[0:-1] + 'S'
                else:
                    str = str[0:-1] + 'E'
            else:
                str = str[0:-1] + 'S'
        return str


if __name__ == '__main__':
    model = BiLSTM_CRF()
    model.myTrain()
    print(model.predict("我爱你中国"))
