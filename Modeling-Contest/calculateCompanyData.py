#!/usr/bin/env python
# -*- encoding: utf-8 -*-
'''
@File    :   calculateCompanyData.py    
@Contact :   18302010009@fudan.edu.cn

@Modify Time      @Author         @Version    @Desciption
------------      ------------    --------    -----------
2020/9/11 14:02   Shen Zhengyu      1.0         根据文件夹下统计
'''

# import lib
import xlwt
import xlrd
import os
import re


def calCompanyData(rootdir, startPos):
    workbook = xlwt.Workbook(encoding='utf-8')
    worksheet1 = workbook.add_sheet("统计")
    worksheet1.write(1, 0, label='进项总额')
    worksheet1.write(2, 0, label='销项总额')
    worksheet1.write(3, 0, label='进项频次方差')
    worksheet1.write(4, 0, label='销项频次方差')
    list = os.listdir(rootdir)
    filename = rootdir + "汇总"
    for i in range(0, len(list)):
        if int(re.findall(r"\d+", list[i])[0]) - startPos >= 256:
            worksheet1 = workbook.add_sheet("统计(2)")
            worksheet1.write(1, 0, label='进项总额')
            worksheet1.write(2, 0, label='销项总额')
            worksheet1.write(3, 0, label='进项频次方差')
            worksheet1.write(4, 0, label='销项频次方差')
            startPos += 255
        path = os.path.join(rootdir, list[i])
        if os.path.isfile(path):
            num = re.findall(r"\d+", list[i])[0]
            worksheet1.col(int(num) - startPos).width = 256 * 20
            worksheet1.write(0, int(num) - startPos, label='E' + num)
            data = xlrd.open_workbook(path)
            jxtj = data.sheet_by_name("进项统计")
            xxtj = data.sheet_by_name("销项统计")
            worksheet1.write(1, int(num) - startPos, label=jxtj.cell_value(1, 2))
            worksheet1.write(2, int(num) - startPos, label=xxtj.cell_value(1, 2))
            if jxtj.cell_value(1, 1) != 0:
                cnt = 0
                c = 0
                row = jxtj.nrows
                for i in range(row):
                    if (i == 0 or i == 1):
                        continue
                    cnt += int(jxtj.cell_value(i, 1))
                    c = i
                if c == 2:
                    alll = 0
                else:
                    average = cnt * 1.0 / (c - 2)
                    j = 0
                    all = 0
                    while j < c:
                        if j == 0 or j == 1:
                            j += 1
                            continue
                        all += (jxtj.cell_value(j, 1) - average) * (jxtj.cell_value(j, 1) - average)
                        j += 1
                    alll = all * 1.0 / (j - 2)
                worksheet1.write(3, int(num) - startPos, label=str(alll))
            if xxtj.cell_value(1, 1) != 0:
                cnt = 0
                c = 0
                row = xxtj.nrows
                for i in range(row):
                    if i == 0 or i == 1:
                        continue
                    cnt += xxtj.cell_value(i, 1)
                    c = i
                if c == 2:
                    alll = 0
                else:
                    average = cnt * 1.0 / (c - 2)
                    j = 2
                    all = 0
                    while j < c:
                        if j == 0 or j == 1:
                            j += 1
                            continue
                        all += (xxtj.cell_value(j, 1) - average) * (xxtj.cell_value(j, 1) - average)
                        j += 1
                    alll = all * 1.0 / (j - 2)
                worksheet1.write(4, int(num) - startPos, label=str(alll))
    workbook.save("D:\\Programming\\Python_programmig\\WinterProject\\汇总\\" + filename + ".xls")


calCompanyData("Q1各公司数据", 0)
calCompanyData("Q2各公司数据", 123)
