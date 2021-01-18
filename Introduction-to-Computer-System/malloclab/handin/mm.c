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

/* ˫�ֶ��� */
#define ALIGNMENT 8
/* ���������8�ı������루+7�ٰ�1~7�޼�����*/
#define ALIGN(size) (((size) + (ALIGNMENT-1)) & ~0x7) 
#define SIZE_T_SIZE (ALIGN(sizeof(size_t)))
#define WSIZE     4
#define DSIZE     8
/* ÿ����չ�ѵĿ��С����������*/
#define INITCHUNKSIZE (1<<6)
#define CHUNKSIZE (1<<12)
#define LISTMAX     16
#define MAX(x, y) ((x) > (y) ? (x) : (y))
#define MIN(x, y) ((x) < (y) ? (x) : (y))
#define PACK(size, alloc) ((size) | (alloc))
/* �ڶ�ָ�����ڵ��ڴ渳ֵʱ��Ҫע������ת�� */
#define GET(p)            (*(unsigned int *)(p))
#define PUT(p, val)       (*(unsigned int *)(p) = (val))
/* ��p��ֵptr */
#define SET_PTR(p, ptr) (*(unsigned int *)(p) = (unsigned int)(ptr))
#define GET_SIZE(p)  (GET(p) & ~0x7)
#define GET_ALLOC(p) (GET(p) & 0x1)
/* ����ͷ���ͽŲ� */
#define HDRP(ptr) ((char *)(ptr) - WSIZE)
#define FTRP(ptr) ((char *)(ptr) + GET_SIZE(HDRP(ptr)) - DSIZE)
#define NEXT_BLKP(ptr) ((char *)(ptr) + GET_SIZE((char *)(ptr) - WSIZE))
#define PREV_BLKP(ptr) ((char *)(ptr) - GET_SIZE((char *)(ptr) - DSIZE))
#define PRED_PTR(ptr) ((char *)(ptr))
/* ���ptr�ĺ�� */
#define SUCC_PTR(ptr) ((char *)(ptr) + WSIZE)

#define PRED(ptr) (*(char **)(ptr))
#define SUCC(ptr) (*(char **)(SUCC_PTR(ptr)))
void *segregated_free_lists[LISTMAX];
/* ��չ�Ѻ��� */
static void *extend_heap(size_t size);
/* �ϲ����ڵĿ��п� */
static void *coalesce(void *ptr);
/* ��prt��ָ���free block����allocate size��С�Ŀ飬���ʣ�µĿռ����2*DWSIZE����С��С�����ָ������������ */
static void *place(void *ptr, size_t size);
/* ��ptr��ָ��Ŀ��п���뵽������б��� */
static void insert_node(void *ptr, size_t size);
/* ��ptr��ָ��Ŀ�ӷ�����б���ɾ�� */
static void delete_node(void *ptr);


/*
 * ����sbrk������չ�ѣ�����ͷ��β����Ȼ�����������������
 * ������Ǻϲ��Ŀ����ԣ�ǰ���ǿ��п飩 
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
 * �����ݴ�С�࣬�ҵ����� 
 * ����õ������п��ǲ���λ��
 * �������п�Ĵ�С�������� 
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
    	    /* ����λ�����ó��ҵ���λ�� */
        behind_ptr = front_ptr;
        front_ptr = PRED(front_ptr);
    }

    /* A->B B��ǰ��A�Ǻ� */

    /* ������� */
    if (front_ptr != NULL)
    {
        /* 1. ->xx->insert->xx ���м���� �������ȵĺ�̣��Լ������ȡ���̣���̵�����*/
        if (behind_ptr != NULL)
        {
        	/* front_ptr ptr behind_ptr*/
            SET_PTR(PRED_PTR(ptr), front_ptr);
            SET_PTR(SUCC_PTR(front_ptr), ptr);
            SET_PTR(SUCC_PTR(ptr), behind_ptr);
            SET_PTR(PRED_PTR(behind_ptr), ptr);
        }
        /* 2. [listnumber]->insert->xx ǰ��Ϊ�պ�Ϊ�գ��ڿ�ͷ���룬���Һ�����֮ǰ��free��*/
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
        { /* 3. ->xxxx->insert ǰΪ�պ�Ϊ�� */
            SET_PTR(PRED_PTR(ptr), NULL);
            SET_PTR(SUCC_PTR(ptr), behind_ptr);
            SET_PTR(PRED_PTR(behind_ptr), ptr);
        }
        else
        { /* 4. [listnumber]->insert ǰ��Ϊ�գ�������Ϊ�գ���ʼ�� */
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
    /* ͨ����Ĵ�С�ҵ���Ӧ���� */
    while ((listnumber < LISTMAX - 1) && (size > 1))
    {
        size >>= 1;
        listnumber++;
    }

    if (PRED(ptr) != NULL)
    {
        /* 1. xxx-> ptr -> xxx ���ȵĺ������Ϊ�Լ��ĺ�� ��̵���������Ϊ�Լ�������*/
        if (SUCC(ptr) != NULL)
        {
            SET_PTR(SUCC_PTR(PRED(ptr)), SUCC(ptr));
            SET_PTR(PRED_PTR(SUCC(ptr)), PRED(ptr));
        }
        /* 2. [listnumber] -> ptr -> xxx ���ȷǿգ�����ǿյģ�����Լ������ȵĺ����Ϊ��*/
        else
        {
            SET_PTR(SUCC_PTR(PRED(ptr)), NULL);
            segregated_free_lists[listnumber] = PRED(ptr);
        }
    }
    else
    {
        /* 3. [listnumber] -> xxx -> ptr ����Ϊ�� ��̲�Ϊ�� �򽫺�̵���������Ϊ��*/
        if (SUCC(ptr) != NULL)
        {
            SET_PTR(PRED_PTR(SUCC(ptr)), NULL);
        }
        /* 4. [listnumber] -> ptr ���Ⱥ�̶�Ϊ�գ�������ֱ����Ϊ��*/
        else
        {
            segregated_free_lists[listnumber] = NULL;
        }
    }
}


/* 2*2��ǰ����������������ǰ/��/ǰ��ϲ���Ȼ������������ */
static void *coalesce(void *ptr)
{
	/* �ѷ�����Ϊtrue */
    _Bool is_prev_alloc = GET_ALLOC(HDRP(PREV_BLKP(ptr)));
    _Bool is_next_alloc = GET_ALLOC(HDRP(NEXT_BLKP(ptr)));
    size_t size = GET_SIZE(HDRP(ptr));
    /* �����ܳ����������ڵ�free�� */
    /* ��ptr���ǰ�������� */
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
    /* ���ϲ��õ�free����뵽�������ӱ��� */
    insert_node(ptr, size);
    return ptr;
}
/* ��prt��ָ���free block����allocate size��С�Ŀ飬���ʣ�µĿռ����2*DWSIZE����С��С�����ָ������������ */
static void *place(void *ptr, size_t size)
{
    size_t ptr_size = GET_SIZE(HDRP(ptr));
    /* allocate size��С�Ŀռ��ʣ��Ĵ�С */
    size_t remain = ptr_size - size;
    delete_node(ptr);
    /* ���ʣ��Ĵ�СС����С�飬�򲻷���ԭ�� */
    if (remain < DSIZE * 2)
    {
        PUT(HDRP(ptr), PACK(ptr_size, 1));
        PUT(FTRP(ptr), PACK(ptr_size, 1));
    }

    /* �������ԭ�飬������Ҫע������һ������� 
	* ���allocate��˳����С���������ͷŵĶ��Ǵ�ģ���ôС������������Ĵ�free��
	* �޷��ϲ����ܶ���Ƭ�������ж�������� 
    * ����binary-bal.rep��binary2-bal.rep�������ļ����õ�
    *  �ȿ�allocate�Ĵ�С�����allocate�Ĵ󣬾ͷ��ں󲿣������ǰ�������������ͷŽϴ���С�ģ�Ҳ�ܺϲ� 
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
    /* ��ʼ����������������� */
    for (listnumber = 0; listnumber < LISTMAX; listnumber++)
    {
        segregated_free_lists[listnumber] = NULL;
    }
    /* ��ʼ���� */
    if ((long)(heap = mem_sbrk(4 * WSIZE)) == -1)
        return -1;
    PUT(heap, 0);
    PUT(heap + (1 * WSIZE), PACK(DSIZE, 1));
    PUT(heap + (2 * WSIZE), PACK(DSIZE, 1));
    PUT(heap + (3 * WSIZE), PACK(0, 1));
    /* ��չ�� */
    if (extend_heap(INITCHUNKSIZE) == NULL)
        return -1;
    return 0;
}

/* �ں��ʵ������Һ��ʵĿ� */ 
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
            /* �ҵ���Ӧ��free�� */
            if (ptr != NULL)
                break;
        }
        searchsize >>= 1;
        listnumber++;
    }
    /* û���ҵ����ʵ�free�飬��չ�� */
    if (ptr == NULL)
    {
        if ((ptr = extend_heap(MAX(size, CHUNKSIZE))) == NULL)
            return NULL;
    }
    /* ��free����allocate size��С�Ŀ� */
    ptr = place(ptr, size);
    return ptr;
}

/* �ͷſ����Ҫ�����������������ϲ�  */ 
void mm_free(void *ptr)
{
    size_t size = GET_SIZE(HDRP(ptr));
    PUT(HDRP(ptr), PACK(size, 0));
    PUT(FTRP(ptr), PACK(size, 0));
    insert_node(ptr, size);
    coalesce(ptr);
}

/* ���µ����� 
 * ���sizeС��ԭ����Ĵ�С��ֱ�ӷ���ԭ���Ŀ�
 * ����ʱ�����ݺ����Ƿ���free�顢��Сѡ��ϲ�����������ռ� 
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

    /* ���sizeС��ԭ����Ĵ�С��ֱ�ӷ���ԭ���Ŀ� */
    if ((remain = GET_SIZE(HDRP(ptr)) - size) >= 0)
    {
        return ptr;
    }
    /* ����ַ������һ�����Ƿ�Ϊfree����߸ÿ��ǶѵĽ����飬�������ڵ�free�飬������Ƭ */
    else if (!GET_ALLOC(HDRP(NEXT_BLKP(ptr))) || !GET_SIZE(HDRP(NEXT_BLKP(ptr))))
    {
        /* ������free��ռ䲻������Ҫ��չ�� */
        if ((remain = GET_SIZE(HDRP(ptr)) + GET_SIZE(HDRP(NEXT_BLKP(ptr))) - size) < 0)
        {
            if (extend_heap(MAX(-remain, CHUNKSIZE)) == NULL)
                return NULL;
            remain += MAX(-remain, CHUNKSIZE);
        }

        /* ɾ���ո����õ�free�鲢�����¿��ͷβ */
        delete_node(NEXT_BLKP(ptr));
        PUT(HDRP(ptr), PACK(size + remain, 1));
        PUT(FTRP(ptr), PACK(size + remain, 1));
    }
    /* ��free������ã������룬�ƶ� */
    else
    {
        new_block = mm_malloc(size);
        memcpy(new_block, ptr, GET_SIZE(HDRP(ptr)));
        mm_free(ptr);
    }

    return new_block;
}














