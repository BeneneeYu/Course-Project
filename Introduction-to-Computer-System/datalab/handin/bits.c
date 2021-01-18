/* 
 * CS:APP Data Lab 
 * 
 * <Please put your name and userid here>
 * 
 * bits.c - Source file with your solutions to the Lab.
 *          This is the file you will hand in to your instructor.
 *
 * WARNING: Do not include the <stdio.h> header; it confuses the dlc
 * compiler. You can still use printf for debugging without including
 * <stdio.h>, although you might get a compiler warning. In general,
 * it's not good practice to ignore compiler warnings, but in this
 * case it's OK.  
 */

#if 0
/*
 * Instructions to Students:
 *
 * STEP 1: Read the following instructions carefully.
 */

You will provide your solution to the Data Lab by
editing the collection of functions in this source file.

INTEGER CODING RULES:
 
  Replace the "return" statement in each function with one
  or more lines of C code that implements the function. Your code 
  must conform to the following style:
 
  int Funct(arg1, arg2, ...) {
      /* brief description of how your implementation works */
      int var1 = Expr1;
      ...
      int varM = ExprM;

      varJ = ExprJ;
      ...
      varN = ExprN;
      return ExprR;
  }

  Each "Expr" is an expression using ONLY the following:
  1. Integer constants 0 through 255 (0xFF), inclusive. You are
      not allowed to use big constants such as 0xffffffff.
  2. Function arguments and local variables (no global variables).
  3. Unary integer operations ! ~
  4. Binary integer operations & ^ | + << >>
    
  Some of the problems restrict the set of allowed operators even further.
  Each "Expr" may consist of multiple operators. You are not restricted to
  one operator per line.

  You are expressly forbidden to:
  1. Use any control constructs such as if, do, while, for, switch, etc.
  2. Define or use any macros.
  3. Define any additional functions in this file.
  4. Call any functions.
  5. Use any other operations, such as &&, ||, -, or ?:
  6. Use any form of casting.
  7. Use any data type other than int.  This implies that you
     cannot use arrays, structs, or unions.

 
  You may assume that your machine:
  1. Uses 2s complement, 32-bit representations of integers.
  2. Performs right shifts arithmetically.
  3. Has unpredictable behavior when shifting an integer by more
     than the word size.

EXAMPLES OF ACCEPTABLE CODING STYLE:
  /*
   * pow2plus1 - returns 2^x + 1, where 0 <= x <= 31
   */
  int pow2plus1(int x) {
     /* exploit ability of shifts to compute powers of 2 */
     return (1 << x) + 1;
  }

  /*
   * pow2plus4 - returns 2^x + 4, where 0 <= x <= 31
   */
  int pow2plus4(int x) {
     /* exploit ability of shifts to compute powers of 2 */
     int result = (1 << x);
     result += 4;
     return result;
  }

FLOATING POINT CODING RULES

For the problems that require you to implent floating-point operations,
the coding rules are less strict.  You are allowed to use looping and
conditional control.  You are allowed to use both ints and unsigneds.
You can use arbitrary integer and unsigned constants.

You are expressly forbidden to:
  1. Define or use any macros.
  2. Define any additional functions in this file.
  3. Call any functions.
  4. Use any form of casting.
  5. Use any data type other than int or unsigned.  This means that you
     cannot use arrays, structs, or unions.
  6. Use any floating point data types, operations, or constants.


NOTES:
  1. Use the dlc (data lab checker) compiler (described in the handout) to 
     check the legality of your solutions.
  2. Each function has a maximum number of operators (! ~ & ^ | + << >>)
     that you are allowed to use for your implementation of the function. 
     The max operator count is checked by dlc. Note that '=' is not 
     counted; you may use as many of these as you want without penalty.
  3. Use the btest test harness to check your functions for correctness.
  4. Use the BDD checker to formally verify your functions
  5. The maximum number of ops for each function is given in the
     header comment for each function. If there are any inconsistencies 
     between the maximum ops in the writeup and in this file, consider
     this file the authoritative source.

/*
 * STEP 2: Modify the following functions according the coding rules.
 * 
 *   IMPORTANT. TO AVOID GRADING SURPRISES:
 *   1. Use the dlc compiler to check that your solutions conform
 *      to the coding rules.
 *   2. Use the BDD checker to formally verify that your solutions produce 
 *      the correct answers.
 */


#endif
/* Copyright (C) 1991-2016 Free Software Foundation, Inc.
   This file is part of the GNU C Library.

   The GNU C Library is free software; you can redistribute it and/or
   modify it under the terms of the GNU Lesser General Public
   License as published by the Free Software Foundation; either
   version 2.1 of the License, or (at your option) any later version.

   The GNU C Library is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
   Lesser General Public License for more details.

   You should have received a copy of the GNU Lesser General Public
   License along with the GNU C Library; if not, see
   <http://www.gnu.org/licenses/>.  */
/* This header is separate from features.h so that the compiler can
   include it implicitly at the start of every compilation.  It must
   not itself include <features.h> or any other header that includes
   <features.h> because the implicit include comes before any feature
   test macros that may be defined in a source file before it first
   explicitly includes a system header.  GCC knows the name of this
   header in order to preinclude it.  */
/* glibc's intent is to support the IEC 559 math functionality, real
   and complex.  If the GCC (4.9 and later) predefined macros
   specifying compiler intent are available, use them to determine
   whether the overall intent is to support these features; otherwise,
   presume an older compiler has intent to support these features and
   define these macros by default.  */
/* wchar_t uses Unicode 8.0.0.  Version 8.0 of the Unicode Standard is
   synchronized with ISO/IEC 10646:2014, plus Amendment 1 (published
   2015-05-15).  */
/* We do not support C11 <threads.h>.  */
/* 
 * bitAnd - x&y using only ~ and | 
 *   Example: bitAnd(6, 5) = 4
 *   Legal ops: ~ |
 *   Max ops: 8
 *   Rating: 1
 */
int bitAnd(int x, int y) {
/* 
 *用 ~ 和 | 代替 & 摩尔定律的等价形式
 */
  int result = ~(~x|~y);
  return result;
}
/* 
 * getByte - Extract byte n from word x
 *   Bytes numbered from 0 (LSB) to 3 (MSB)
 *   Examples: getByte(0x12345678,1) = 0x56
 *   Legal ops: ! ~ & ^ | + << >>
 *   Max ops: 6
 *   Rating: 2
 */
int getByte(int x, int n) {
/*
 *给定n ，求出第n个字节 
 *一个字节=8bit，一个十六进制位是4bit，所以移动(n << 3)bit后，将他与0xFF取交集
 */
  int temp = n << 3;
  temp = x >> temp;
  temp = temp & 0xff;
  return temp;
}
/* 
 * logicalShift - shift x to the right by n, using a logical shift
 *   Can assume that 0 <= n <= 31
 *   Examples: logicalShift(0x87654321,4) = 0x08765432
 *   Legal ops: ! ~ & ^ | + << >>
 *   Max ops: 20
 *   Rating: 3 
 */
int logicalShift(int x, int n) {
/*
 *实现逻辑右移。逻辑右移左边补 0，而算数右移左边补符号位。
 *所以构造出全1右移n位，左n位都是0的数，将原x也右移n位，与该数取交
 */
  int temp = ~(1 << 31);
  temp = ((temp >> n ) << 1) + 1;
  temp = (x >> n) & temp;
  return temp; 
}
/*
 * bitCount - returns count of number of 1's in word
 *   Examples: bitCount(5) = 2, bitCount(7) = 3
 *   Legal ops: ! ~ & ^ | + << >>
 *   Max ops: 40
 *   Rating: 4
 */
int bitCount(int x) {
/*
 *一位一位移动来测可得，但步骤太多。可以利用分治法。
 *依次统计每2位、4位、8位、16位、32位。
 */
  int count;
  int formask1 = (0x55) | (0x55 << 8);
  int mask1 = (formask1 << 16) | (formask1);
  int formask2 = (0x33) | (0x33 << 8);
  int mask2 = (formask2 << 16) | (formask2);
  int formask3 = (0x0f) | (0x0f << 8);
  int mask3 = (formask3 << 16) | (formask3);
  int mask4 = (0xff << 16) | (0xff);
  int mask5 = (0xff << 8) | (0xff);
  count = (x & mask1) + ((x >> 1) & mask1);
  count = (count & mask2) + ((count >> 2) & mask2);
  count = (count & mask3) + ((count >> 4) & mask3);
  count = (count & mask4) + ((count >> 8) & mask4);
  count = (count & mask5) + ((count >> 16) & mask5);
  return count;
}
/* 
 * bang - Compute !x without using !
 *   Examples: bang(3) = 0, bang(0) = 1
 *   Legal ops: ~ & ^ | + << >>
 *   Max ops: 12
 *   Rating: 4 
 */
int bang(int x) {
/*
 *对于0x0...0,0x0...0 | ((~0x0...0)+1)=0
 *而对于其它，都不可能，因为符号位上必为1，依据此性质可得。
 */
  int temp = (~x) + 1;
  int result = x | temp;
  result = result >> 31;
  result = result + 1;
  return result;
}
/* 
 * tmin - return minimum two's complement integer 
 *   Legal ops: ! ~ & ^ | + << >>
 *   Max ops: 4
 *   Rating: 1
 */
int tmin(void) {
/*
 *0x80000000，转化为32位知1代表负，而0000000代表最小的。
 */
  int result = 1 << 31;
  return  result;
}
/* 
 * fitsBits - return 1 if x can be represented as an 
 *  n-bit, two's complement integer.
 *   1 <= n <= 32
 *   Examples: fitsBits(5,3) = 0, fitsBits(-4,3) = 1
 *   Legal ops: ! ~ & ^ | + << >>
 *   Max ops: 15
 *   Rating: 2
 */
int fitsBits(int x, int n) {
/*
 *如果可以表示，则前32-n位与该值无关，只表示符号
 *那么可以先左移n位，再算术右移n位，判断是否相等，相等则可表示。
 */
  int temp = (x << (32 + ~n + 1)) >> (32 + ~n +1);
  int result = !(temp ^ x);
  return result;
}
/* 
 * divpwr2 - Compute x/(2^n), for 0 <= n <= 30
 *  Round toward zero
 *   Examples: divpwr2(15,1) = 7, divpwr2(-33,4) = -2
 *   Legal ops: ! ~ & ^ | + << >>
 *   Max ops: 15
 *   Rating: 2
 */
int divpwr2(int x, int n) {
/*
 *正数可直接右移得，而负数有问题，会取远离0的那个整数。除非是2^n。
 * 构造一个bias加在x上，当非2^n时，进位解决了取法问题。2^n不影响。
 */
  int sign = x >> 31;
  int mask = (1 << n) + (~0);
  int bias = sign & mask;
  int result = (x+bias) >> n;
  return result;
}
/* 
 * negate - return -x 
 *   Example: negate(1) = -1.
 *   Legal ops: ! ~ & ^ | + << >>
 *   Max ops: 5
 *   Rating: 2
 */
int negate(int x) {
/*
 *取反加一
 */
  int result = (~x) + 1;
  return result;
}
/* 
 * isPositive - return 1 if x > 0, return 0 otherwise 
 *   Example: isPositive(-1) = 0.
 *   Legal ops: ! ~ & ^ | + << >>
 *   Max ops: 8
 *   Rating: 3
 */
int isPositive(int x) {
/*
 *拒绝两种情况：负（符号位1）和0（取反得1）
 */
  int temp1 = (x >> 31) & 1;
  int temp2 = !x;
  int result = !(temp1 | temp2);
  return result;
}
/* 
 * isLessOrEqual - if x <= y  then return 1, else return 0 
 *   Example: isLessOrEqual(4,5) = 1.
 *   Legal ops: ! ~ & ^ | + << >>
 *   Max ops: 24
 *   Rating: 3
 */
int isLessOrEqual(int x, int y) {
/*
 *考虑到异号计算溢出，有三种情况： 
 *1：相等 2：同号，且x-y<0 3：异号，且x<0
 */
  int ifequal = !(x ^ y);//if equals
  int sub = x + ~y + 1; //x-y
  int ifsub = (sub >> 31) & 1; // x-y < 0
  int both = ((x ^ y) >> 31) & 1;//both pisitive or negative
  int xne = ((x >> 31) & 1);//sign of x neagtive
  int result = ifequal | (both & xne) | ((both ^ 1) & ifsub);
  return result;
}
/*
 * ilog2 - return floor(log base 2 of x), where x > 0
 *   Example: ilog2(16) = 4
 *   Legal ops: ! ~ & ^ | + << >>
 *   Max ops: 90
 *   Rating: 4
 */
int ilog2(int x) {
/*
 *因为x大于0，所以本质是找最左边的1。
 *采用二分法，先左移16位看是否大于0。
 *如果大于0，!一次后是0，再!一次后是1，<<4得到16，可以加入答案.
 *再把范围缩短一半，再左移或右移8位，进行同样操作。
 */
  int result = 0;
  result = ((!!(x>>16)) << 4);
  result = result + ((!!(x>>(result+8))) << 3);
  result = result + ((!!(x>>(result+4))) << 2); 
  result = result + ((!!(x>>(result+2))) << 1); 
  result = result + ((!!(x>>(result+1))) << 0); 
  return result;
}
/* 
 * float_neg - Return bit-level equivalent of expression -f for
 *   floating point argument f.
 *   Both the argument and result are passed as unsigned int's, but
 *   they are to be interpreted as the bit-level representations of
 *   single-precision floating point values.
 *   When argument is NaN, return argument.
 *   Legal ops: Any integer/unsigned operations incl. ||, &&. also if, while
 *   Max ops: 10
 *   Rating: 2
 */
unsigned float_neg(unsigned uf) {
/*
 *nan的条件：E位是255（满1）且F位不是0，若全是0，则是infinity。 
 *则先不管符号位，左移一位，找到左8位都是1的。
 *若它与左8位全1其它全0不全等，则直接返回。 其它的，返回^x的结果得反
 */
  unsigned temp1 = 0x80000000;
  unsigned temp2 = 0xFF000000;
  unsigned tempx = uf << 1;
  if((tempx & temp2) == temp2 )
  {
  if(tempx != temp2)
	{
	return uf;
	}
  }
  return temp1 ^ uf;
}
/* 
 * float_i2f - Return bit-level equivalent of expression (float) x
 *   Result is returned as unsigned int, but
 *   it is to be interpreted as the bit-level representation of a
 *   single-precision floating point values.
 *   Legal ops: Any integer/unsigned operations incl. ||, &&. also if, while
 *   Max ops: 30
 *   Rating: 4
 */
unsigned float_i2f(int x) {
/*
 *取最左边作为符号位。转化为绝对值，并且记录下来符号位。
 *计算左边有多少个 0得到指数位。最后把阶码和尾码移动到相应的位置
 */
  unsigned abs = x;
  unsigned sign = 0x0;
  unsigned count = 0;
  unsigned flag = 0;
  unsigned mask = 0x80000000;
  if(x == 0)
  {
  return 0;
  }
  if(x < 0)
  {
  abs = -x;
  sign = 0x80000000;
  }
  while (!(mask & abs)){
  mask >>= 1;
  count = count + 1;
  }
  abs <<= count + 1;
  if(((abs & 0x1ff) > 0x100) || ((abs & 0x3ff) == 0x300)){
  flag = 1;
  }
  return sign + (abs >> 9) + ((158 - count) << 23) + flag;
  }
/* 
 * float_twice - Return bit-level equivalent of expression 2*f for
 *   floating point argument f.
 *   Both the argument and result are passed as unsigned int's, but
 *   they are to be interpreted as the bit-level representation of
 *   single-precision floating point values.
 *   When argument is NaN, return argument
 *   Legal ops: Any integer/unsigned operations incl. ||, &&. also if, while
 *   Max ops: 30
 *   Rating: 4
 */
unsigned float_twice(unsigned uf) {
/*
 *根据浮点是否规格化进行操作
 */
  if(( 0x7f800000 & uf) == 0){
  uf = (uf & 0x80000000) | (( uf & 0x007fffff) << 1);
}
  else if((0x7f800000 & uf) != 0x7f800000)
{
  uf = uf + 0x00800000;
}
  return uf;
}
