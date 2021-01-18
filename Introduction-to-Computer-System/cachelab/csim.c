/*
 * Zhengyu Shen 18302010009 
 * Address can be divided to 3 parts: t,s,b
 * First:Choose the set, according to the s set index bits from the middle of raw address
 * then the s bits are interpreted as an unsigned integer,set number,to find the set  
 * Second:Make sure if a copy of the word w is stored in cache lines contained in the found set
 * Third:If we hit,we know that w is somewhere in the block. 
 * Try to find where the word starts in the block.
 */
#include "cachelab.h"
#include <stdio.h>
#include <string.h>
#include <getopt.h>	
#include <stdlib.h>
#include <unistd.h>
#include <math.h>
typedef struct
{
	unsigned valid;		//valid bit
	int lastUsedTime;	//for LRU policy
	unsigned long long cacheTag;	//	CT (cache tag)
} Block;

int s = -1, E = -1, b = -1, verbosity = 0;
int hit = 0, miss = 0, eviction = 0;
int overAllTime = 0;
Block *cachePool;

void displayHelp(char* argv[], int ERROR)
{
    printf("  Usage: %s [-hv] -s <num> -E <num> -b <num> -t <file>\n", argv[0]);
    puts("  Options:");
    puts("    -h         Print this help message.");
    puts("    -v         Optional verbose flag.");
    puts("    -s <num>   Number of set index bits.");
    puts("    -E <num>   Number of lines per set.");
    puts("    -b <num>   Number of block offset bits.");
    puts("    -t <file>  Trace file.");
    puts("");
    puts("");
    puts("    Examples:");
    printf("    %s -s 4 -E 1 -b 4 -t traces/yi.trace\n", argv[0]);
    printf("    %s -v -s 8 -E 2 -b 4 -t traces/yi.trace\n", argv[0]);
    if (ERROR)
    {
    	exit(1);
    }
    else
    {
    	exit(0);
    }
}
/*
 * If what we want happens to exist in level k,it is a cache hit.
 * If the cache at level k is empty, any try will miss.
 * This kind of miss is cold miss.
 * LRU policy will expel the last accessed one. 
 */
void visitCache(unsigned long long address)
{
	unsigned long long cacheTag = address >> b >> s;
	unsigned cacheIndex = address >> b & ((1 << s) - 1);//Get the order number
	
	Block *cacherow = cachePool + E * cacheIndex;
	Block *victimRow = cacherow;
	//Now we know the set,visit every row
	for (int i = 0; i < E; ++i)
	{
		if  (!cacherow[i].valid)	//	compulsory miss
		{
			++miss;
			cacherow[i].valid = 1;
			cacherow[i].lastUsedTime = overAllTime;
			cacherow[i].cacheTag = cacheTag;
			return;
		}
		if  (cacherow[i].lastUsedTime && cacherow[i].cacheTag == cacheTag)	//	hit
		{
			cacherow[i].lastUsedTime = overAllTime;
			++hit;
			return;
		}
		//Every time we find a row with low lastUsedTime,let it be the victimRow 
		if(cacherow[i].lastUsedTime < victimRow->lastUsedTime)	//	select the victim block
		{
			victimRow = cacherow + i;
		}
	}
	/*
	 *	capacity miss
	 */
	++miss;
	++eviction;
	victimRow->lastUsedTime = overAllTime;
	victimRow->cacheTag = cacheTag;
}
int main(int argc, char **argv)
{
	FILE *tracefile;
	char opt;
	while ((opt = getopt(argc, argv, "s:E:b:t:vh")) != EOF)
	{
		switch (opt)
		{
			case 's':	//Number of sets
				s = atoi(optarg);
				break;

			case 'E':	//Associativity
				E = atoi(optarg);
				break;

			case 'b':	//Number of block bits,B = 2^b
				b = atoi(optarg);
				break;

			case 't':	//Name of the valgrind trace to replay
				tracefile = fopen(optarg, "r");
				break;

			case 'v':	//	use the -v option for a detailed record of each hit and miss.
				verbosity = 1;
				break;

			case 'h':	//	Optional help flag that prints usage info
				displayHelp(argv, 0);
				//exit(0);

			default:
				displayHelp(argv, 1);
				//exit(1);
		}
	}
	
	if (s == -1 || E == -1 || b == -1 || tracefile == NULL)
	{
		printf("%s: Missing required command line argument\n", argv[0]);
		displayHelp(argv, 1);
		//exit(1);
	}

	cachePool = (Block*) malloc(sizeof(Block) * E * (1 << s) );
	memset(cachePool, 0, sizeof(Block) * E * (1 << s) );

	/*
	 *	Read instructions,format is
	 *	[space]operation address,size
	 */
	{
		char operation;
		unsigned long long address;
		unsigned size;
		//I 0400d7d4,8
		//M 0421c7f0,4
		//L 04f6b868,8
		//S 7ff0005c8,8
		while (fscanf(tracefile, "%s%llx,%u\n", &operation, &address, &size) == 3)
		{
			++overAllTime;
			switch (operation) {
				
				case 'M':	//	a data modify
					visitCache(address);
					++hit;	//	must hit in the second visitation.
					break;

				case 'L':	//	a data load
					visitCache(address);
					break;

				case 'S':	//	a data store
					visitCache(address);
					break;

				case 'I':	//	an instruction load
					break;
			}
		}
	}
	fclose(tracefile);
	free(cachePool);
    printSummary(hit, miss, eviction);
    return 0;
}
