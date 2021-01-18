#!/usr/bin/env python
# -*- encoding: utf-8 -*-
'''
@File    :   obtainCompanyData.py    
@Contact :   18302010009@fudan.edu.cn

@Modify Time      @Author         @Version    @Desciption
------------      ------------    --------    -----------
2020/9/10 19:52   Shen Zhengyu      1.0         None
'''
# import lib
import xlwt
import xlrd


def writeHeader(worksheet):
    worksheet.write(0, 0, "企业代号")
    worksheet.write(0, 1, "发票号码")
    worksheet.write(0, 2, "开票日期")
    worksheet.write(0, 3, "购方单位代号")
    worksheet.write(0, 4, "金额")
    worksheet.write(0, 5, "税额")
    worksheet.write(0, 6, "价税合计")
    worksheet.write(0, 7, "发票状态")
    return


def getData(fileName, outputPosition):
    data = xlrd.open_workbook(fileName)
    companyInfo = data.sheet_by_name("企业信息")
    jxInfo = data.sheet_by_name("进项发票信息")
    xxInfo = data.sheet_by_name("销项发票信息")
    row = companyInfo.nrows
    for i in range(row):
        if i == 0:
            continue
        companyNum = companyInfo.cell_value(i, 0)
        companyName = companyInfo.cell_value(i, 1)
        workbook = xlwt.Workbook(encoding='utf-8')
        worksheet1 = workbook.add_sheet('进项发票信息')
        worksheet2 = workbook.add_sheet('销项发票信息')
        worksheet3 = workbook.add_sheet('进项统计')
        worksheet4 = workbook.add_sheet('销项统计')
        worksheet5 = workbook.add_sheet('进项作废')
        worksheet6 = workbook.add_sheet('销项作废')

        writeHeader(worksheet1)
        writeHeader(worksheet2)
        nrows1 = jxInfo.nrows
        flag1 = 2
        dict1 = dict()
        dict11 = dict()
        fs = 0
        for j in range(nrows1):
            val = jxInfo.cell_value(j, 0)
            if val == companyNum:
                worksheet1.write(flag1, 0, label=jxInfo.cell_value(j, 0))
                worksheet1.write(flag1, 1, label=jxInfo.cell_value(j, 1))
                day = jxInfo.cell_value(j, 2)
                sum = jxInfo.cell_value(j, 6)
                status = jxInfo.cell_value(j, 7)
                worksheet1.write(flag1, 2, label=day)
                if status == '有效发票':
                    keys = dict1.keys()
                    if day in keys:
                        dict1[day][0] = dict1[day][0] + 1
                        dict1[day][1] = dict1[day][1] + sum
                    else:
                        dict1[day] = [1, sum]
                    if sum < 0:
                        fs += 1

                else:
                    keys = dict11.keys()
                    if day in keys:
                        dict11[day][0] = dict11[day][0] + 1
                        dict11[day][1] = dict11[day][1] + sum
                    else:
                        dict11[day] = [1, sum]
                worksheet1.write(flag1, 3, label=jxInfo.cell_value(j, 3))
                worksheet1.write(flag1, 4, label=jxInfo.cell_value(j, 4))
                worksheet1.write(flag1, 5, label=jxInfo.cell_value(j, 5))
                worksheet1.write(flag1, 6, label=sum)
                worksheet1.write(flag1, 7, label=status)
                flag1 += 1
            if val != companyNum and flag1 > 2:
                break
        cnt1 = 2
        worksheet3.write(0, 0, label='日期')
        worksheet3.write(0, 1, label='发票数目')
        worksheet3.write(0, 2, label='发票价税')
        asum = 0
        amountsum = 0
        for key, value in dict1.items():
            worksheet3.write(cnt1, 0, label=key)
            worksheet3.write(cnt1, 1, label=value[0])
            worksheet3.write(cnt1, 2, label=value[1])
            asum += int(value[0])
            amountsum += int(value[1])

            cnt1 += 1
        worksheet3.write(1, 0, label='总和')
        worksheet3.write(1, 1, label=asum)
        worksheet3.write(1, 2, label=amountsum)
        worksheet3.write(1, 3, label='正数率')
        worksheet3.write(1, 4, label=1 - (fs * 1.0 / asum))

        cnt1 = 2
        bfsum = 0
        bfjgsum = 0
        worksheet5.write(0, 0, label='日期')
        worksheet5.write(0, 1, label='作废数目')
        worksheet5.write(0, 2, label='作废价税')
        for key, value in dict11.items():
            worksheet5.write(cnt1, 0, label=key)
            bfsum += value[0]
            worksheet5.write(cnt1, 1, label=value[0])
            bfjgsum += int(value[1])
            worksheet5.write(cnt1, 2, label=value[1])
            cnt1 += 1
        worksheet5.write(1, 0, label='总和')
        worksheet5.write(1, 1, label=bfsum)
        worksheet5.write(1, 2, label=bfjgsum)
        dict2 = dict()
        dict22 = dict()
        nrows2 = xxInfo.nrows
        flag2 = 2
        fs = 0
        for k in range(nrows2):
            val = xxInfo.cell_value(k, 0)
            if val == companyNum:
                worksheet2.write(flag2, 0, label=xxInfo.cell_value(k, 0))
                worksheet2.write(flag2, 1, label=xxInfo.cell_value(k, 1))
                day = xxInfo.cell_value(k, 2)
                sum = xxInfo.cell_value(k, 6)
                status = xxInfo.cell_value(k, 7)
                worksheet2.write(flag2, 2, label=day)
                if status == '有效发票':
                    keys = dict2.keys()
                    if day in keys:
                        dict2[day][0] = dict2[day][0] + 1
                        dict2[day][1] = dict2[day][1] + sum
                    else:
                        dict2[day] = [1, sum]
                    if sum < 0:
                        fs += 1
                else:
                    keys = dict22.keys()
                    if day in keys:
                        dict22[day][0] = dict22[day][0] + 1
                        dict22[day][1] = dict22[day][1] + sum
                    else:
                        dict22[day] = [1, sum]
                worksheet2.write(flag2, 3, label=xxInfo.cell_value(k, 3))
                worksheet2.write(flag2, 4, label=xxInfo.cell_value(k, 4))
                worksheet2.write(flag2, 5, label=xxInfo.cell_value(k, 5))
                worksheet2.write(flag2, 6, label=sum)
                worksheet2.write(flag2, 7, label=status)
                flag2 += 1
            if val != companyNum and flag2 > 2:
                break
        cnt1 = 2
        asum = 0
        amountsum = 0
        worksheet4.write(0, 0, label='日期')
        worksheet4.write(0, 1, label='发票数目')
        worksheet4.write(0, 2, label='发票价税')

        for key, value in dict2.items():
            worksheet4.write(cnt1, 0, label=key)
            worksheet4.write(cnt1, 1, label=value[0])
            asum += int(value[0])
            worksheet4.write(cnt1, 2, label=value[1])
            amountsum += int(value[1])
            cnt1 += 1
        worksheet4.write(1, 0, label='总和')
        worksheet4.write(1, 1, label=asum)
        worksheet4.write(1, 2, label=amountsum)
        worksheet4.write(1, 3, label='正数率')
        worksheet4.write(1, 4, label=(1 - (fs * 1.0 / asum)))

        cnt1 = 2
        bfsum = 0
        bfjgsum = 0
        worksheet6.write(0, 0, label='日期')
        worksheet6.write(0, 1, label='作废数目')
        worksheet6.write(0, 2, label='作废价税')
        for key, value in dict22.items():
            worksheet6.write(cnt1, 0, label=key)
            worksheet6.write(cnt1, 1, label=value[0])
            worksheet6.write(cnt1, 2, label=value[1])
            bfsum += value[0]
            bfjgsum += int(value[1])
            cnt1 += 1
        worksheet6.write(1, 0, label='总和')
        worksheet6.write(1, 1, label=bfsum)
        worksheet6.write(1, 2, label=bfjgsum)

        name = companyNum + '.xls'
        workbook.save(outputPosition + "\\" + name)


getData("附件1：123家有信贷记录企业的相关数据.xlsx", "Q1各公司数据")
getData("附件2：302家无信贷记录企业的相关数据.xlsx", "Q2各公司数据")
