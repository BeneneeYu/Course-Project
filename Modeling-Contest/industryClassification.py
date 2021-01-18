#!/usr/bin/env python
# -*- encoding: utf-8 -*-
'''
@File    :   industryClassification.py    
@Contact :   18302010009@fudan.edu.cn

@Modify Time      @Author         @Version    @Desciption
------------      ------------    --------    -----------
2020/9/12 20:01   Shen Zhengyu      1.0         None
'''

# import lib
import xlwt
import xlrd

data = xlrd.open_workbook("附件2：302家无信贷记录企业的相关数据.xlsx")
data2 = xlrd.open_workbook("Q2.xls")
qyxx = data.sheet_by_name("企业信息")
slz = data2.sheet_by_name("统计")
rows = qyxx.nrows
workbook = xlwt.Workbook(encoding='utf-8')
worksheet1 = workbook.add_sheet("统计")
worksheet1.col(0).width = 256 * 20
worksheet1.col(1).width = 256 * 20

count = 0
str1 = "个体经营"
dict1 = ['旅游', '建', '制造', '食品', '建设', '物流', '旅游', '厂', '店', '部', '社', '业', '工程', '运', '站', '机械', '安装', '劳务', '场', '材',
         '天然气', '机电', '材', '品', '演艺']
dict2 = ['医', '教育', '科技', '服务', '策划', '广告', '管理', '文化', '技术', '发展', '药', '电子', '贸易', '营销', '商', '网', '图书']
for i in range(0, rows):
    if i == 0:
        worksheet1.write(i, 0, label='企业代号')
        worksheet1.write(i, 1, label='企业名')
        worksheet1.write(i, 2, label='受影响系数')

        continue
    print(i)
    num = qyxx.cell_value(i, 0)
    name = qyxx.cell_value(i, 1)
    worksheet1.write(i, 0, label=num)
    worksheet1.write(i, 1, label=name)
    syx = 1
    for str in dict1:
        if str in name:
            syx = 0.618
            break
    for str in dict2:
        if str in name:
            syx = 1.414
            break
    if str1 in name:
        syx = 0.832
    print(syx)
    worksheet1.write(i, 2, label=syx)

workbook.save("D:\\Programming\\Python_programmig\\WinterProject\\汇总\\行业受影响情况.xls")
