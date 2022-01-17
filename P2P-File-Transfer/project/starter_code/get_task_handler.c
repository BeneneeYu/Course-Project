#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "get_task_handler.h"
#include "sha.h"
#include "chunk.h"

extern get_task_handler_t get_task_handler;

void init_task(char *chunk_file, char *out_file) {
    FILE *fd = fopen(chunk_file, "r");

    int line_num = 0;
    char read_buf[256];
    int id_buf;
    char hash_buf[2 * SHA1_HASH_SIZE];
    while (fgets(read_buf, 256, fd)) {
        line_num++;
    }
    get_task_handler.get_num = line_num;
    get_task_handler.get_chunks = malloc(line_num * sizeof(chunk_t));
    get_task_handler.chunk_data = malloc(line_num * sizeof(char *));
    strcpy(get_task_handler.output, out_file);
    get_task_handler.status = malloc(line_num * sizeof(int));
    get_task_handler.providers = malloc(line_num * sizeof(bt_peer_t *));
    for (int i = 0; i < line_num; i++) {
        get_task_handler.status[i] = 0;
        get_task_handler.providers[i] = NULL;
    }

    fseek(fd, 0, SEEK_SET);
    int index = 0;
    while (fgets(read_buf, 256, fd)) {
        if (sscanf(read_buf, "%d %s", &id_buf, hash_buf) != 2) {
            continue;
        }
        get_task_handler.get_chunks[index].id = id_buf;
        hex2binary(hash_buf, 2 * SHA1_HASH_SIZE, get_task_handler.get_chunks[index].chunk_hash);
        index++;
    }
}

char *update_chunk_provider(list_t *chunk_hash_list, bt_peer_t *peer) {
    int num = get_task_handler.get_num;
    char *to_download = NULL;
    int is_first = 1;
    for (node_t *node = chunk_hash_list->head; node != NULL; node = node->next) {
        char *chunk_hash = (char *) (node->data);
        for (int i = 0; i < num; i++) {
            char *this_chunk_hash = get_task_handler.get_chunks[i].chunk_hash;
            if (memcmp(this_chunk_hash, chunk_hash, SHA1_HASH_SIZE) == 0) {
                if (get_task_handler.status[i] == 0) {
                    get_task_handler.providers[i] = peer;

                    if (is_first == 1) {
                        get_task_handler.status[i] = 2;
                        to_download = get_task_handler.get_chunks[i].chunk_hash;
                        is_first++;
                    }
                }
                break;
            }
        }
    }
    return to_download;
}

void add_and_check_data(char *hash, char *data) {
    int i;
    for (i = 0; i < get_task_handler.get_num; i++) {
        char *this_hash = get_task_handler.get_chunks[i].chunk_hash;
        if (memcmp(this_hash, hash, SHA1_HASH_SIZE) == 0) {
            get_task_handler.chunk_data[i] = malloc(BT_CHUNK_SIZE);
            memcpy(get_task_handler.chunk_data[i], data, BT_CHUNK_SIZE);
            break;
        }
    }

    uint8_t hash_my_data[SHA1_HASH_SIZE];
    char *my_data = get_task_handler.chunk_data[i];
    shahash((uint8_t *) my_data, BT_CHUNK_SIZE, hash_my_data);
    get_task_handler.status[i] = memcmp(hash_my_data, hash, SHA1_HASH_SIZE) == 0 ? 1 : 0;
}

int is_task_finished() {
    for (int i = 0; i < get_task_handler.get_num; i++) {
        if (get_task_handler.status[i] != 1) {
            return 0;
        }
    }
    return 1;
}


void write_data() {
    FILE *fd = fopen(get_task_handler.output, "wb+");
    for (int i = 0; i < get_task_handler.get_num; i++) {
        fwrite(get_task_handler.chunk_data[i], 1024, 512, fd);
    }
    fclose(fd);
    for (int i = 0; i < get_task_handler.get_num; i++) {
        free(get_task_handler.chunk_data[i]);
    }
    free(get_task_handler.chunk_data);
    free(get_task_handler.status);
    free(get_task_handler.providers);
    free(get_task_handler.get_chunks);
}
