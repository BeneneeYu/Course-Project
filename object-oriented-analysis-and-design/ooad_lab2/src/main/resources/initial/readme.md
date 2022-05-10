# 学生数据：Students_Info.txt

- 每行存储一位学生的信息
- 学生信息格式为："<学号>,  <学生名>, <专业>"
- 所有信息只包含数字、英文字母和常见符号

# 课程数据：Courses_Info.txt

- 每行存储一门课程的信息
- 学生信息格式为："<课程编号>,  <课程名>, <学分>, <可兑换学分的交流课程编号>"
  - 若<可兑换学分的交流课程编号>='none'，表示这门课没有可兑换学分的交流课程
  - 若<可兑换学分的交流课程编号>='exchange'，表示这门课是一门交流课程
- 所有信息只包含数字、英文字母和常见符号

# 培养方案：<major>.txt

- <major>表示培养方案对应的专业名称
- 每类课程以"[<Type Name>] <requirement>"开头，<Type Name>表示该类课程的类别名，<requirement>表示该类课程要求的学分数或门数
  - [Basic Compulsory]：必修基础课
  - [Major Compulsory]：专业基础课
  - [Major Elective]：专业选修课
  - [Module <id>]：第<id>类模块课
  - [Direction <name>]：<name>方向课
  - [Other Elective]：任意选修课
  
- 除任意选修外，各类课程接下来若干行每行记录一个属于该类的课程的课程编号：

# 修读文件：Learning.txt

- 每行存储一组课程修读的信息
- 课程修读信息格式为："<学号>, <课程编号>"

# 用例内容

1. 向系统中导入学生数据Students_Info.txt和课程数据Courses_info.txt
2. 向系统中导入培养方案Computer Science.txt和Software Engineering.txt
3. 按照Learning.txt将修读信息导入系统
4. 输出San Zhang和Si Li的A方向报表
   - 报表内容应与result.xlsx中的SanZhang_1部分和SiLi_1部分的内容一致。
5. San Zhang将专业切换成Software Engineering
6. 向系统中导入培养方案Network Engineering.txt
7. Si Li将专业切换成Network Engineering
8. 输出San Zhang和Si Li的B方向报表
   - 报表内容应与result.xlsx中的SanZhang_2部分和SiLi_2部分的内容一致。