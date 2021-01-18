#!/usr/bin/env python
# -*- encoding: utf-8 -*-
'''
@File    :   calDebt.py    
@Contact :   18302010009@fudan.edu.cn

@Modify Time      @Author         @Version    @Desciption
------------      ------------    --------    -----------
2020/9/11 21:57   Shen Zhengyu      1.0         根据阈值公式等计算给定贷款额和利率
'''

# import lib
import xlwt
import xlrd


def calDebt(fileName, lowestSlf, lowestXyf, fd1, fd2, fd3, fd4, max):
    data = xlrd.open_workbook(fileName)
    xx = data.sheet_by_name("Sheet1")
    rows = xx.nrows
    workbook = xlwt.Workbook(encoding='utf-8')
    wooksheet = workbook.add_sheet("统计")
    for i in range(rows):
        if i == 0:
            continue
        slf = xx.cell_value(i, 1)
        xyf = xx.cell_value(i, 2)
        dkje = 0
        nll = 0
        if slf >= lowestSlf and xyf >= lowestXyf:
            dkje = 90 * (slf - lowestSlf) / (max - lowestSlf) + 10
            if xyf >= fd1:
                nll = 0.04
            elif xyf >= fd2:
                nll = 0.0467
            else:
                nll = 0.0534

        wooksheet.write(i, 0, label=xx.cell_value(i, 0))
        wooksheet.write(i, 1, label=slf)
        wooksheet.write(i, 2, label=xyf)
        wooksheet.write(i, 3, label=dkje)  # 金额
        wooksheet.write(i, 4, label=nll)  # 年利率
    workbook.save(fileName + "结论update.xls")


# calDebt("conclusion_1(1).xlsx",0.5392,0.4487,0.8063,0.6360,0.5529,0.906806)
# calDebt("conclusion_2(2)(1).xlsx",0.180185,0.321788,0.58317,0.501466,0.458484,0.413571,0.715678)
calDebt("conclusion_3.xlsx", 0.580164, 0.391343, 0.524544, 0.457938, 0.123, 0.123, 0.660746)
