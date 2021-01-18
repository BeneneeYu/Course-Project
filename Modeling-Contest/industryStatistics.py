#!/usr/bin/env python
# -*- encoding: utf-8 -*-
'''
@File    :   industryStatistics.py    
@Contact :   18302010009@fudan.edu.cn

@Modify Time      @Author         @Version    @Desciption
------------      ------------    --------    -----------
2020/9/11 20:45   Shen Zhengyu      1.0         None
'''

# import lib
import xlwt
import xlrd
import os
import re

data = xlrd.open_workbook("附件2：302家无信贷记录企业的相关数据.xlsx")
qyxx = data.sheet_by_name("企业信息")
str1 = "个体经营"
map = {"个体经营": 0}
rows = qyxx.nrows
for i in range(0, rows):
    if i == 0:
        continue
    name = qyxx.cell_value(i, 1)
    if str1 in name:
        map[str1] = map[str1] + 1
    else:
        strtmp = name.replace("*", "")
        map[strtmp] = 1
workbook = xlwt.Workbook(encoding='utf-8')
worksheet1 = workbook.add_sheet("统计")
worksheet1.col(0).width = 256 * 20
count = 0
for k, v in map.items():
    worksheet1.write(count, 0, label=k)
    worksheet1.write(count, 1, label=v)
    count += 1
workbook.save("D:\\Programming\\Python_programmig\\WinterProject\\汇总\\行业情况.xls")
