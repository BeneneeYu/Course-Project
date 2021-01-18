#!/usr/bin/env python
# -*- encoding: utf-8 -*-
'''
@File    :   update2.py    
@Contact :   18302010009@fudan.edu.cn

@Modify Time      @Author         @Version    @Desciption
------------      ------------    --------    -----------
2020/9/13 10:15   Shen Zhengyu      1.0         None
'''

# import lib
import xlwt
import xlrd

data = xlrd.open_workbook("行业受影响情况.xls")
data2 = xlrd.open_workbook("Q2各公司数据汇总.xls")
syxqk = data.sheet_by_name("统计")
tj1 = data2.sheet_by_name("统计(2)")

rows = syxqk.nrows
workbook = xlwt.Workbook(encoding='utf-8')
worksheet1 = workbook.add_sheet("统计")
worksheet1.col(1).width = 256 * 20
worksheet1.col(2).width = 256 * 20
worksheet1.col(3).width = 256 * 20
worksheet1.col(4).width = 256 * 20
for ii in range(256, rows, 1):
    i = ii - 256
    if i == 0:
        worksheet1.write(i, 0, label="企业代号")
        worksheet1.write(i, 1, label="更新进项总额")
        worksheet1.write(i, 2, label="更新销项总额")
        worksheet1.write(i, 3, label="进项频次方差")
        worksheet1.write(i, 4, label="销项频次方差")
        continue

    xs = syxqk.cell_value(ii - 1, 2)
    name = tj1.cell_value(0, i)
    jxze = tj1.cell_value(1, i)
    xxze = tj1.cell_value(2, i)
    jxpcfc = tj1.cell_value(3, i)
    xxpcfc = tj1.cell_value(4, i)
    worksheet1.write(i, 0, label=name)
    worksheet1.write(i, 1, label=jxze * float(xs))
    worksheet1.write(i, 2, label=xxze * float(xs))
    worksheet1.write(i, 3, label=jxpcfc)
    worksheet1.write(i, 4, label=xxpcfc)
workbook.save("D:\\Programming\\Python_programmig\\WinterProject\\汇总\\行业受影响情况update2.xls")
