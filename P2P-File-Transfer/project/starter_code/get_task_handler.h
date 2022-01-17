//
// Shen Zhengyu 18302010009
//

#ifndef CN_BITTORRENT_get_task_handler_H
#define CN_BITTORRENT_get_task_handler_H

#include "list.h"
#include "bt_parse.h"
#include "store.h"

typedef struct get_task_handler_s {
    int get_num;
    //0-2-1
    int *status;
    bt_peer_t **providers;
    chunk_t *get_chunks;
    char **chunk_data;
    char output[BT_FILENAME_LEN];
} get_task_handler_t;

void init_task(char *chunk_file, char *out_file);

char *update_chunk_provider(list_t *chunk_hash_list, bt_peer_t *peer);

void add_and_check_data(char *hash, char *data);//根据hash，成功则更新hash

int is_task_finished();

void write_data();

#endif //CN_BITTORRENT_get_task_handler_H
