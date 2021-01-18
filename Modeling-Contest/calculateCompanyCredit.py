#!/usr/bin/env python
# -*- encoding: utf-8 -*-
'''
@File    :   calculateCompanyCredit.py    
@Contact :   18302010009@fudan.edu.cn

@Modify Time      @Author         @Version    @Desciption
------------      ------------    --------    -----------
2020/9/11 15:00   Shen Zhengyu      1.0         计算企业信誉
'''

# import lib
import xlwt
import xlrd
import os
import re


def bfcal(rootdir, startPos, fileName):
    workbook = xlwt.Workbook(encoding='utf-8')
    worksheet1 = workbook.add_sheet("统计")
    worksheet1.col(0).width = 256 * 20
    worksheet1.col(1).width = 256 * 20
    worksheet1.col(2).width = 256 * 20
    worksheet1.col(3).width = 256 * 20
    worksheet1.col(4).width = 256 * 20
    worksheet1.col(5).width = 256 * 20

    worksheet1.write(0, 1, label='进项发票有效率')
    worksheet1.write(0, 2, label='进项发票正数率')
    worksheet1.write(0, 3, label='销项发票有效率')
    worksheet1.write(0, 4, label='销项发票正数率')
    worksheet1.write(0, 5, label='信誉评价值')
    data = xlrd.open_workbook(fileName)
    qyxx = data.sheet_by_name("企业信息")
    list = os.listdir(rootdir)
    for i in range(0, len(list)):
        path = os.path.join(rootdir, list[i])
        if os.path.isfile(path):
            numStr = re.findall(r"\d+", list[i])[0]
            num = int(re.findall(r"\d+", list[i])[0]) - startPos
            worksheet1.write(num, 0, label='E' + numStr)
            data = xlrd.open_workbook(path)
            jxzf = data.sheet_by_name("进项作废")
            jxtj = data.sheet_by_name("进项统计")
            xxzf = data.sheet_by_name("销项作废")
            xxtj = data.sheet_by_name("销项统计")
            worksheet1.write(num, 1, label=jxtj.cell_value(1, 1) / (jxzf.cell_value(1, 1) + jxtj.cell_value(1, 1)))
            worksheet1.write(num, 2, label=jxtj.cell_value(1, 4))
            worksheet1.write(num, 3, label=xxtj.cell_value(1, 1) / (xxzf.cell_value(1, 1) + xxtj.cell_value(1, 1)))
            worksheet1.write(num, 4, label=xxtj.cell_value(1, 4))
            if startPos == 0:
                pj = qyxx.cell_value(int(num), 2)
                pjnum = 0
                if pj == 'A':
                    pjnum = 0.6
                elif pj == 'B':
                    pjnum = 0.3
                elif pj == 'C':
                    pjnum = 0.1
                else:
                    pjnum = 0
                worksheet1.write(int(num), 5, label=pjnum)

    workbook.save("汇总\\" + "信誉汇总" + rootdir + ".xls")


def creditcal(rootdir):
    workbook = xlwt.Workbook(encoding='utf-8')
    worksheet1 = workbook.add_sheet("统计")
    worksheet1.col(0).width = 256 * 20
    worksheet1.col(1).width = 256 * 20
    worksheet1.write(1, 0, label='信誉评分')
    worksheet1.write(2, 0, label='是否违规')
    worksheet1.write(0, 1, label='进项作废发票数目占比')
    list = os.listdir(rootdir)
    wholeData = xlrd.open_workbook("附件2：302家无信贷记录企业的相关数据.xlsx")
    qyxx = wholeData.sheet_by_name("企业信息")
    for i in range(0, len(list)):
        path = os.path.join(rootdir, list[i])
        if os.path.isfile(path):
            numStr = re.findall(r"\d+", list[i])[0]
            num = int(numStr) - 123
            worksheet1.write(num, 0, label='E' + numStr)
            data = xlrd.open_workbook(path)
            jxzf = data.sheet_by_name("进项作废")
            jxtj = data.sheet_by_name("进项统计")
            worksheet1.write(num, 1, label=jxzf.cell_value(1, 1) / (jxzf.cell_value(1, 1) + jxtj.cell_value(1, 1)))
            wg = qyxx.cell_value(int(num), 3)
            if wg == "是":
                wg = 0
            else:
                wg = 1
            worksheet1.write(2, int(num), label=str(wg))
            pj = qyxx.cell_value(int(num), 2)
            pjnum = 0
            if pj == 'A':
                pjnum = 0.6
            elif pj == 'B':
                pjnum = 0.3
            elif pj == 'C':
                pjnum = 0.1
            else:
                pjnum = 0
            worksheet1.write(1, int(num), label=str(pjnum))

    workbook.save("汇总\\" + "信誉评价" + rootdir + ".xls")


bfcal("Q1各公司数据", 0, "附件1：123家有信贷记录企业的相关数据.xlsx")
bfcal("Q2各公司数据", 123, "附件2：302家无信贷记录企业的相关数据.xlsx")
creditcal("Q2各公司数据")
