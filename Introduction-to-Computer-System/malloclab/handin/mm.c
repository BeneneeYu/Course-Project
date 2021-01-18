/*
 * mm-naive.c - The fastest, least memory-efficient malloc package.
 * 
 * In this naive approach, a block is allocated by simply incrementing
 * the brk pointer.  A block is pure payload. There are no headers or
 * footers.  Blocks are never coalesced or reused. Realloc is
 * implemented directly using mm_malloc and mm_free.
 *
 * NOTE TO STUDENTS: Replace this header comment with your own header
 * comment that gives a high level description of your solution.
 */
#include <stdio.h>
#include <stdlib.h>
#include <assert.h>
#include <unistd.h>
#include <string.h>
#include "mm.h"
#include "memlib.h"

/*********************************************************
 * NOTE TO STUDENTS: Before you do anything else, please
 * provide your team information in the following struct.
 ********************************************************/
team_t team = {
    /* Your student_id */
    "18302010009",
    /* Your full name */
    "Shen Zhengyu",
    /* Your email address */
    "18302010009@fudan.edu.cn",
    /* leave blank  */
    "",
    /* leave blank */
    ""
};

/* 双字对齐 */
#define ALIGNMENT 8
/* 向上最近的8的倍数舍入（+7再把1~7修剪掉）*/
#define ALIGN(size) (((size) + (ALIGNMENT-1)) & ~0x7) 
#define SIZE_T_SIZE (ALIGN(sizeof(size_t)))
#define WSIZE     4
#define DSIZE     8
/* 每次扩展堆的块大小，慢慢利用*/
#define INITCHUNKSIZE (1<<6)
#define CHUNKSIZE (1<<12)
#define LISTMAX     16
#define MAX(x, y) ((x) > (y) ? (x) : (y))
#define MIN(x, y) ((x) < (y) ? (x) : (y))
#define PACK(size, alloc) ((size) | (alloc))
/* 在对指针所在的内存赋值时，要注意类型转换 */
#define GET(p)            (*(unsigned int *)(p))
#define PUT(p, val)       (*(unsigned int *)(p) = (val))
/* 给p赋值ptr */
#define SET_PTR(p, ptr) (*(unsigned int *)(p) = (unsigned int)(ptr))
#define GET_SIZE(p)  (GET(p) & ~0x7)
#define GET_ALLOC(p) (GET(p) & 0x1)
/* 计算头部和脚部 */
#define HDRP(ptr) ((char *)(ptr) - WSIZE)
#define FTRP(ptr) ((char *)(ptr) + GET_SIZE(HDRP(ptr)) - DSIZE)
#define NEXT_BLKP(ptr) ((char *)(ptr) + GET_SIZE((char *)(ptr) - WSIZE))
#define PREV_BLKP(ptr) ((char *)(ptr) - GET_SIZE((char *)(ptr) - DSIZE))
#define PRED_PTR(ptr) ((char *)(ptr))
/* 获得ptr的后继 */
#define SUCC_PTR(ptr) ((char *)(ptr) + WSIZE)

#define PRED(ptr) (*(char **)(ptr))
#define SUCC(ptr) (*(char **)(SUCC_PTR(ptr)))
void *segregated_free_lists[LISTMAX];
/* 扩展堆函数 */
static void *extend_heap(size_t size);
/* 合并相邻的空闲块 */
static void *coalesce(void *ptr);
/* 在prt所指向的free block块中allocate size大小的块，如果剩下的空间大于2*DWSIZE（最小大小），分割后放入空闲链表 */
static void *place(void *ptr, size_t size);
/* 将ptr所指向的空闲块插入到分离空闲表中 */
static void insert_node(void *ptr, size_t size);
/* 将ptr所指向的块从分离空闲表中删除 */
static void delete_node(void *ptr);


/*
 * 调用sbrk函数扩展堆，设置头和尾部，然后插入分离空闲链表中
 * 插入后考虑合并的可能性（前面是空闲块） 
 */
static void *extend_heap(size_t size)
{
    void *ptr;
    size = ALIGN(size);
    if ((ptr = mem_sbrk(size)) == (void *)-1)
        return NULL;
    PUT(HDRP(ptr), PACK(size, 0));
    PUT(FTRP(ptr), PACK(size, 0));
    PUT(HDRP(NEXT_BLKP(ptr)), PACK(0, 1));
    insert_node(ptr, size);
    return coalesce(ptr);
}

/*
 * 先依据大小类，找到链表 
 * 在求得的链表中考虑插入位置
 * 保持链中块的大小是增长的 
 */
static void insert_node(void *ptr, size_t size)
{
    int listnumber = 0;
    void *front_ptr = NULL;
    void *behind_ptr = NULL;
    while ((listnumber < LISTMAX - 1) && (size > 1))
    {
        size >>= 1;
        listnumber++;
    }
    front_ptr = segregated_free_lists[listnumber];
    while ((front_ptr != NULL) && (size > GET_SIZE(HDRP(front_ptr))))
    {
    	    /* 插入位置设置成找到的位置 */
        behind_ptr = front_ptr;
        front_ptr = PRED(front_ptr);
    }

    /* A->B B是前，A是后 */

    /* 四种情况 */
    if (front_ptr != NULL)
    {
        /* 1. ->xx->insert->xx 在中间插入 更改祖先的后继，自己的祖先、后继，后继的祖先*/
        if (behind_ptr != NULL)
        {
        	/* front_ptr ptr behind_ptr*/
            SET_PTR(PRED_PTR(ptr), front_ptr);
            SET_PTR(SUCC_PTR(front_ptr), ptr);
            SET_PTR(SUCC_PTR(ptr), behind_ptr);
            SET_PTR(PRED_PTR(behind_ptr), ptr);
        }
        /* 2. [listnumber]->insert->xx 前不为空后为空，在开头插入，而且后面有之前的free块*/
        else
        {
            SET_PTR(PRED_PTR(ptr), front_ptr);
            SET_PTR(SUCC_PTR(front_ptr), ptr);
            SET_PTR(SUCC_PTR(ptr), NULL);
            segregated_free_lists[listnumber] = ptr;
        }
    }
    else
    {
        if (behind_ptr != NULL)
        { /* 3. ->xxxx->insert 前为空后不为空 */
            SET_PTR(PRED_PTR(ptr), NULL);
            SET_PTR(SUCC_PTR(ptr), behind_ptr);
            SET_PTR(PRED_PTR(behind_ptr), ptr);
        }
        else
        { /* 4. [listnumber]->insert 前后都为空，即该链为空，初始化 */
            SET_PTR(PRED_PTR(ptr), NULL);
            SET_PTR(SUCC_PTR(ptr), NULL);
            segregated_free_lists[listnumber] = ptr;
        }
    }
}


static void delete_node(void *ptr)
{
    int listnumber = 0;
    size_t size = GET_SIZE(HDRP(ptr));
    /* 通过块的大小找到对应的链 */
    while ((listnumber < LISTMAX - 1) && (size > 1))
    {
        size >>= 1;
        listnumber++;
    }

    if (PRED(ptr) != NULL)
    {
        /* 1. xxx-> ptr -> xxx 祖先的后继设置为自己的后继 后继的祖先设置为自己的祖先*/
        if (SUCC(ptr) != NULL)
        {
            SET_PTR(SUCC_PTR(PRED(ptr)), SUCC(ptr));
            SET_PTR(PRED_PTR(SUCC(ptr)), PRED(ptr));
        }
        /* 2. [listnumber] -> ptr -> xxx 祖先非空，后继是空的，则把自己的祖先的后继设为空*/
        else
        {
            SET_PTR(SUCC_PTR(PRED(ptr)), NULL);
            segregated_free_lists[listnumber] = PRED(ptr);
        }
    }
    else
    {
        /* 3. [listnumber] -> xxx -> ptr 祖先为空 后继不为空 则将后继的祖先设置为空*/
        if (SUCC(ptr) != NULL)
        {
            SET_PTR(PRED_PTR(SUCC(ptr)), NULL);
        }
        /* 4. [listnumber] -> ptr 祖先后继都为空，则链表直接置为空*/
        else
        {
            segregated_free_lists[listnumber] = NULL;
        }
    }
}


/* 2*2种前后情况，根据情况和前/后/前后合并，然后加入空闲链表 */
static void *coalesce(void *ptr)
{
	/* 已分配则为true */
    _Bool is_prev_alloc = GET_ALLOC(HDRP(PREV_BLKP(ptr)));
    _Bool is_next_alloc = GET_ALLOC(HDRP(NEXT_BLKP(ptr)));
    size_t size = GET_SIZE(HDRP(ptr));
    /* 不可能出现两个相邻的free块 */
    /* 看ptr块的前、后块情况 */
    if (is_prev_alloc && is_next_alloc)
    {
        return ptr;
    }
    else if (is_prev_alloc && !is_next_alloc)
    {
        delete_node(ptr);
        delete_node(NEXT_BLKP(ptr));
        size += GET_SIZE(HDRP(NEXT_BLKP(ptr)));
        PUT(HDRP(ptr), PACK(size, 0));
        PUT(FTRP(ptr), PACK(size, 0));
    }
    else if (!is_prev_alloc && is_next_alloc)
    {
        delete_node(ptr);
        delete_node(PREV_BLKP(ptr));
        size += GET_SIZE(HDRP(PREV_BLKP(ptr)));
        PUT(FTRP(ptr), PACK(size, 0));
        PUT(HDRP(PREV_BLKP(ptr)), PACK(size, 0));
        ptr = PREV_BLKP(ptr);
    }
    else
    {
        delete_node(ptr);
        delete_node(PREV_BLKP(ptr));
        delete_node(NEXT_BLKP(ptr));
        size += GET_SIZE(HDRP(PREV_BLKP(ptr))) + GET_SIZE(HDRP(NEXT_BLKP(ptr)));
        PUT(HDRP(PREV_BLKP(ptr)), PACK(size, 0));
        PUT(FTRP(NEXT_BLKP(ptr)), PACK(size, 0));
        ptr = PREV_BLKP(ptr);
    }
    /* 将合并好的free块加入到空闲链接表中 */
    insert_node(ptr, size);
    return ptr;
}
/* 在prt所指向的free block块中allocate size大小的块，如果剩下的空间大于2*DWSIZE（最小大小），分割后放入空闲链表 */
static void *place(void *ptr, size_t size)
{
    size_t ptr_size = GET_SIZE(HDRP(ptr));
    /* allocate size大小的空间后剩余的大小 */
    size_t remain = ptr_size - size;
    delete_node(ptr);
    /* 如果剩余的大小小于最小块，则不分离原块 */
    if (remain < DSIZE * 2)
    {
        PUT(HDRP(ptr), PACK(ptr_size, 1));
        PUT(FTRP(ptr), PACK(ptr_size, 1));
    }

    /* 否则分离原块，但这里要注意这样一种情况： 
	* 如果allocate的顺序是小大间隔，但释放的都是大的，那么小的阻隔在连续的大free块
	* 无法合并，很多碎片，所以判断这种情况 
    * 根据binary-bal.rep和binary2-bal.rep这两个文件设置的
    *  先看allocate的大小，如果allocate的大，就放在后部，否则放前部，这样连续释放较大或较小的，也能合并 
    */
     
    else if (size >= 100)
    {
        PUT(HDRP(ptr), PACK(remain, 0));
        PUT(FTRP(ptr), PACK(remain, 0));
        PUT(HDRP(NEXT_BLKP(ptr)), PACK(size, 1));
        PUT(FTRP(NEXT_BLKP(ptr)), PACK(size, 1));
        insert_node(ptr, remain);
        return NEXT_BLKP(ptr);
    }
    else
    {
        PUT(HDRP(ptr), PACK(size, 1));
        PUT(FTRP(ptr), PACK(size, 1));
        PUT(HDRP(NEXT_BLKP(ptr)), PACK(remain, 0));
        PUT(FTRP(NEXT_BLKP(ptr)), PACK(remain, 0));
        insert_node(NEXT_BLKP(ptr), remain);
    }
    return ptr;
}


int mm_init(void)
{
    int listnumber;
    char *heap; 
    /* 初始化分离空闲链表数组 */
    for (listnumber = 0; listnumber < LISTMAX; listnumber++)
    {
        segregated_free_lists[listnumber] = NULL;
    }
    /* 初始化堆 */
    if ((long)(heap = mem_sbrk(4 * WSIZE)) == -1)
        return -1;
    PUT(heap, 0);
    PUT(heap + (1 * WSIZE), PACK(DSIZE, 1));
    PUT(heap + (2 * WSIZE), PACK(DSIZE, 1));
    PUT(heap + (3 * WSIZE), PACK(0, 1));
    /* 扩展堆 */
    if (extend_heap(INITCHUNKSIZE) == NULL)
        return -1;
    return 0;
}

/* 在合适的链里找合适的块 */ 
void *mm_malloc(size_t size)
{
    if (size == 0)
        return NULL;
    if (size <= DSIZE)
    {
        size = 2 * DSIZE;
    }
    else
    {
        size = ALIGN(size + DSIZE);
    }
    int listnumber = 0;
    size_t searchsize = size;
    void *ptr = NULL;
    while (listnumber < LISTMAX)
    {
        if (((searchsize <= 1) && (segregated_free_lists[listnumber] != NULL)))
        {
            ptr = segregated_free_lists[listnumber];
            while ((ptr != NULL) && ((size > GET_SIZE(HDRP(ptr)))))
            {
                ptr = PRED(ptr);
            }
            /* 找到对应的free块 */
            if (ptr != NULL)
                break;
        }
        searchsize >>= 1;
        listnumber++;
    }
    /* 没有找到合适的free块，扩展堆 */
    if (ptr == NULL)
    {
        if ((ptr = extend_heap(MAX(size, CHUNKSIZE))) == NULL)
            return NULL;
    }
    /* 在free块中allocate size大小的块 */
    ptr = place(ptr, size);
    return ptr;
}

/* 释放块后，需要插入分离空闲链表，并合并  */ 
void mm_free(void *ptr)
{
    size_t size = GET_SIZE(HDRP(ptr));
    PUT(HDRP(ptr), PACK(size, 0));
    PUT(FTRP(ptr), PACK(size, 0));
    insert_node(ptr, size);
    coalesce(ptr);
}

/* 重新调整块 
 * 如果size小于原来块的大小，直接返回原来的块
 * 扩大时，根据后面是否是free块、大小选择合并或重新申请空间 
 */ 
void *mm_realloc(void *ptr, size_t size)
{
    void *new_block = ptr;
    int remain;
    if (size == 0)
        return NULL;
    if (size <= DSIZE)
    {
        size = 2 * DSIZE;
    }
    else
    {
        size = ALIGN(size + DSIZE);
    }

    /* 如果size小于原来块的大小，直接返回原来的块 */
    if ((remain = GET_SIZE(HDRP(ptr)) - size) >= 0)
    {
        return ptr;
    }
    /* 检查地址连续下一个块是否为free块或者该块是堆的结束块，利用相邻的free块，减少碎片 */
    else if (!GET_ALLOC(HDRP(NEXT_BLKP(ptr))) || !GET_SIZE(HDRP(NEXT_BLKP(ptr))))
    {
        /* 若加上free块空间不够，需要扩展块 */
        if ((remain = GET_SIZE(HDRP(ptr)) + GET_SIZE(HDRP(NEXT_BLKP(ptr))) - size) < 0)
        {
            if (extend_heap(MAX(-remain, CHUNKSIZE)) == NULL)
                return NULL;
            remain += MAX(-remain, CHUNKSIZE);
        }

        /* 删除刚刚利用的free块并设置新块的头尾 */
        delete_node(NEXT_BLKP(ptr));
        PUT(HDRP(ptr), PACK(size + remain, 1));
        PUT(FTRP(ptr), PACK(size + remain, 1));
    }
    /* 无free块可利用，则申请，移动 */
    else
    {
        new_block = mm_malloc(size);
        memcpy(new_block, ptr, GET_SIZE(HDRP(ptr)));
        mm_free(ptr);
    }

    return new_block;
}














