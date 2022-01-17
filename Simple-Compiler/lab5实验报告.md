# 语法产生式的改变

新增了control 语法产生式（if或while）

stmt改变，增加control作为候选

对于控制流部分的codegen 实现，需要进行Basicblock 的创建和分支跳转语句的使用，具体可以参考LLVM Tutorial 的Control Flow 章节。

支持的语法产生式

# 实验思路

•以if-else为例

•扩展对于if-else的词法分析器

•建立if-else的抽象语法树类型

•扩展if-else的语法解析器，将语句解析并构建到AST

•利用llvm提供的接口，生成if-else的代码，进行完全的codegen

# 支持的语法产生式

```
< program > ::= < gdecl >∗< function >∗
< gdecl > ::= extern < prototype >;
< function > ::= < prototype >< body >
< prototype > ::= < type >< ident > (< paramlist >)
< paramlist > ::= ϵ| < type >< ident > [,< type >< ident >]∗
< body > ::= {[< stmt >]∗}
< stmt > ::= < simp >; | < return >; | < decl >; | < control >
< control > ::= if(< exp >) {body} [else {< body >}]
| while(< exp >) {< body >} | for(< stmt >;< exp >; < stmt >) {< body >}
< decl > ::= < type >< ident > [,< ident >]∗
< simp > ::= < ident >=< exp >
< return > ::= return < exp >
< exp > ::= (< exp >)| < const > | < ident > |
< exp >< binop >< exp > | < callee >
< callee > ::= < ident > (ϵ| < exp > [,< exp >]∗)
< ident > ::= [A − Z_a − z][0 − 9A − Z_a − z]∗
< const > ::= < intconst > | < doubleconst >
< binop > ::= + | - | * | <
< intconst > ::= [0 − 9][0 − 9]∗
< doubleconst > ::= < intconst > . < intconst >
< type > ::= int|double
```

# 拓展实现

增加char

增加for

增加block中分支多个语句

增加declare中的直接赋值，int i = 2，现在只有对变量的类型声明

