#ifndef CN_BITTORRENT_CONN_H
#define CN_BITTORRENT_CONN_H

#include "bt_parse.h"
#include "sha.h"
#include "packet.h"
#define CHUNK_SIZE     512
typedef struct chunk_buffer_s {
    char chunk_hash[SHA1_HASH_SIZE];
    char *data_buf;
} chunk_buffer_t;

typedef struct download_connection_s {
    int from_here; //缓存进chunk_buf->data_buf
    int next_expected_ack;
    bt_peer_t *sender;
    chunk_buffer_t *chunk_buf; //used to cache data
} download_connection_t;

typedef struct upload_connection_s {
    int last_ack;
    int to_send; //0到512
    int available; //发送窗口中最后一个可得的
    int duplicated_ACK_times;
    int cwnd; //拥塞窗口
    int ssthresh; //阈值
    int rtt_flag; //rtt结束
    long begin_time;
    packet_t **pkts; //缓存区
    bt_peer_t *receiver;
} upload_connection_t;

typedef struct download_pool_s {
    download_connection_t **conns;
    int conn_num;
    int max_conn;
} download_pool_t;

typedef struct upload_pool_s {
    upload_connection_t **conns;
    int conn_num;
    int max_conn;
} upload_pool_t;

chunk_buffer_t *init_chunk_buffer(char *hash);

void free_chunk_buffer(chunk_buffer_t *chunk_buf);

download_connection_t *initiate_download_connection(bt_peer_t *peer, chunk_buffer_t *chunk_buf);

download_connection_t *add_to_down_pool(download_pool_t *pool, bt_peer_t *peer, chunk_buffer_t *chunk_buf);

void remove_from_down_pool(download_pool_t *pool, bt_peer_t *peer);

void init_down_pool(download_pool_t *pool, int max_conn);

download_connection_t *get_down_conn(download_pool_t *pool, bt_peer_t *peer);

upload_connection_t *init_up_conn(bt_peer_t *peer, packet_t **pkts);

upload_connection_t *add_to_up_pool(upload_pool_t *pool, bt_peer_t *peer, packet_t **pkts);

void remove_from_up_pool(upload_pool_t *pool, bt_peer_t *peer);

void initiate_upload_pool(upload_pool_t *pool, int max_conn);

upload_connection_t *get_up_conn(upload_pool_t *pool, bt_peer_t *peer);


#endif //CN_BITTORRENT_CONN_H
