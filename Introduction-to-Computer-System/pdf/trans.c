/* 
 * Zhengyu Shen 18302010009
 * trans.c - Matrix transpose B = A^T
 *
 * Each transpose function must have a prototype of the form:
 * void trans(int M, int N, int A[N][M], int B[M][N]);
 *
 * A transpose function is evaluated by counting the number of misses
 * on a 1KB direct mapped cache with a block size of 32 bytes.
 */ 
#include <stdio.h>
#include "cachelab.h"

int is_transpose(int M, int N, int A[N][M], int B[M][N]);

/* 
 * transpose_submit - This is the solution transpose function that you
 *     will be graded on for Part B of the assignment. Do not change
 *     the description string "Transpose submission", as the driver
 *     searches for that string to identify the transpose function to
 *     be graded. 
 */
char transpose_submit_desc[] = "Transpose submission";
void transpose_submit(int M, int N, int A[N][M], int B[M][N])
{   
    int val0,val1,val2,val3,val4,val5,val6,val7;
    int i,j,k,h;
    if(N == 32){
        for(i = 0;i < 4;i++){
            for(j = 0;j < 4;j++){
                for(k = i*8;k < (i+1)*8;k++){
                    h = j*8;
		    val0 = A[k][h];
		    val1 = A[k][h+1];
                    val2 = A[k][h+2];
		    val3 = A[k][h+3];
 		    val4 = A[k][h+4];
		    val5 = A[k][h+5];
                    val6 = A[k][h+6];
		    val7 = A[k][h+7];
                    B[h][k] = val0;
 		    B[h+1][k] = val1;
		    B[h+2][k] = val2;
		    B[h+3][k] = val3;
                    B[h+4][k] = val4;
		    B[h+5][k] = val5;
		    B[h+6][k] = val6;
		    B[h+7][k] = val7;
                }
            }
        }
    }
    else if(N==64){
        for(i=0;i<64;i+=8){
            for(j=0;j<64;j+=8){
                for(k=j;k<j+4;++k){
                    val0 = A[k][i];val1 = A[k][i+1];val2 = A[k][i+2];val3 = A[k][i+3];
                    val4 = A[k][i+4];val5 = A[k][i+5];val6 = A[k][i+6];val7 = A[k][i+7];

                    B[i][k] = val0;B[i][k+4] = val4;B[i+1][k] = val1;B[i+1][k+4] = val5;
                    B[i+2][k] = val2;B[i+2][k+4] = val6;B[i+3][k] = val3;B[i+3][k+4] = val7;                               
                }
                for(k=i;k<i+4;++k){
                    val0 = B[k][j+4];val1 = B[k][j+5];val2 = B[k][j+6];val3 = B[k][j+7];
                    val4 = A[j+4][k];val5 = A[j+5][k];val6 = A[j+6][k];val7 = A[j+7][k];

                    B[k][j+4] = val4;B[k][j+5] = val5;B[k][j+6] = val6;B[k][j+7] = val7;
                    B[k+4][j] = val0;B[k+4][j+1] = val1;B[k+4][j+2] = val2;B[k+4][j+3] = val3;
                }
                for(k=i+4;k<i+8;++k){
                    val0 = A[j+4][k];val1 = A[j+5][k];val2 = A[j+6][k];val3 = A[j+7][k];
                    B[k][j+4] = val0;B[k][j+5] = val1;B[k][j+6] = val2;B[k][j+7] = val3;
                }
            }
        }
    }
    else{
        for(i=0;i<N;i+=16){
            for(j=0;j<M;j+=16){
                for(k=i;k<i+16&&k<N;k++){
                    for(h=j;h<j+16&&h<M;h++){
                        B[h][k]=A[k][h];
                    }
                }
            }
        }
    }
}

/* 
 * You can define additional transpose functions below. We've defined
 * a simple one below to help you get started. 
 */ 

/* 
 * trans - A simple baseline transpose function, not optimized for the cache.
 */
char trans_desc[] = "Simple row-wise scan transpose";
void trans(int M, int N, int A[N][M], int B[M][N])
{
    int i, j, tmp;

    for (i = 0; i < N; i++) {
        for (j = 0; j < M; j++) {
            tmp = A[i][j];
            B[j][i] = tmp;
        }
    }    

}

/*
 * registerFunctions - This function registers your transpose
 *     functions with the driver.  At runtime, the driver will
 *     evaluate each of the registered functions and summarize their
 *     performance. This is a handy way to experiment with different
 *     transpose strategies.
 */
void registerFunctions()
{
    /* Register your solution function */
    registerTransFunction(transpose_submit, transpose_submit_desc); 

    /* Register any additional transpose functions */
    registerTransFunction(trans, trans_desc); 

}

/* 
 * is_transpose - This helper function checks if B is the transpose of
 *     A. You can check the correctness of your transpose by calling
 *     it before returning from the transpose function.
 */
int is_transpose(int M, int N, int A[N][M], int B[M][N])
{
    int i, j;

    for (i = 0; i < N; i++) {
        for (j = 0; j < M; ++j) {
            if (A[i][j] != B[j][i]) {
                return 0;
            }
        }
    }
    return 1;
}

