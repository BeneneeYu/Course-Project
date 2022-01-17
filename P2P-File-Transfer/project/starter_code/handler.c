#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <signal.h>
#include "handler.h"
#include "sha.h"
#include "list.h"
#include "store.h"
#include "get_task_handler.h"
#include "spiffy.h"
#include "chunk.h"
#include <string.h>
extern list_t *chunk_tracker;
extern list_t *chunk_ihave;
extern get_task_handler_t get_task_handler;
extern upload_pool_t up_pool;
extern download_pool_t down_pool;
extern char master_file_name[256];
extern bt_config_t config;
static upload_connection_t *this_up_conn;

void pkt_ntoh(packet_t *packet) {
    packet->header.magic = ntohs(packet->header.magic);
    packet->header.header_len = ntohs(packet->header.header_len);
    packet->header.packet_len = ntohs(packet->header.packet_len);
    packet->header.seq_num = ntohl(packet->header.seq_num);
    packet->header.ack_num = ntohl(packet->header.ack_num);
}

int parse_type(packet_t *packet) {
    header_t header = packet->header;
    if (header.magic != MAGIC_NUM || header.type < 0 || header.version != 1 || header.type > 5) {
        return -1;
    } else {
        return header.type;
    }
}

packet_t *handle_whohas(packet_t *pkt_whohas) {
    int req_num = pkt_whohas->data[0];
    int from_here = 4;
    char *chunk_hash = pkt_whohas->data + from_here;
    char payload[DATALEN];
    int has_num = 0;
    for (int i = 0; i < req_num; i++) {
        if (has_chunk(chunk_hash)) {
            has_num++;
            memcpy(payload + from_here, chunk_hash, SHA1_HASH_SIZE);
            from_here += SHA1_HASH_SIZE;
        }
        chunk_hash += SHA1_HASH_SIZE;
    }

    if (has_num == 0) {
        return NULL;
    } else {
        memset(payload, 0, 4);
        payload[0] = has_num;
        return new_pkt(IHAVE, HEADERLEN + from_here, 0, 0, payload);
    }
}

packet_t *
new_pkt(unsigned char type, unsigned short packet_len, unsigned int seq_num, unsigned int ack_num, char *payload) {
    packet_t *packet = (packet_t *) malloc(sizeof(packet_t));
    packet->header.magic = htons(MAGIC_NUM);
    packet->header.version = 1;
    packet->header.type = type;
    packet->header.seq_num = htonl(seq_num);
    packet->header.ack_num = htonl(ack_num);
    packet->header.header_len = htons(HEADERLEN);
    packet->header.packet_len = htons(packet_len);
    if (payload != NULL) {
        memcpy(packet->data, payload, packet_len - HEADERLEN);
    }
    return packet;
}

int has_chunk(char *chunk_hash) {
    if (chunk_ihave->node_num == 0) {
        return 0;
    }
    node_t *curr_node = chunk_ihave->head;
    chunk_t *curr_chunk;
    while (curr_node != NULL) {
        curr_chunk = (chunk_t *) (curr_node->data);
        if (memcmp(curr_chunk->chunk_hash, chunk_hash, SHA1_HASH_SIZE) == 0) {
            return 1;
        }
        curr_node = curr_node->next;
    }
    return 0;
}

list_t *new_whohas_pkt() {
    list_t *list = init_list();
    unsigned short pkt_len;
    char payload[DATALEN];
    int tmpNum = 4;
    //不需要切片
    if (get_task_handler.get_num <= MAX_CHUNK_NUM) {
        pkt_len = HEADERLEN + tmpNum + get_task_handler.get_num * SHA1_HASH_SIZE;
        assemble_chunk_hash(payload, get_task_handler.get_num, get_task_handler.get_chunks);
        add_node(list, new_pkt(WHOHAS, pkt_len, 0, 0, payload));
    } else {
        int quotient = get_task_handler.get_num / MAX_CHUNK_NUM;
        int remainder = get_task_handler.get_num - quotient * MAX_CHUNK_NUM;
        for (int i = 0; i < quotient; i++) {
            pkt_len = HEADERLEN + tmpNum + MAX_CHUNK_NUM * SHA1_HASH_SIZE;
            assemble_chunk_hash(payload, MAX_CHUNK_NUM, get_task_handler.get_chunks + i * MAX_CHUNK_NUM);
            add_node(list, new_pkt(WHOHAS, pkt_len, 0, 0, payload));
        }

        pkt_len = HEADERLEN + tmpNum + remainder * SHA1_HASH_SIZE;
        assemble_chunk_hash(payload, remainder, get_task_handler.get_chunks + quotient * MAX_CHUNK_NUM);
        add_node(list, new_pkt(WHOHAS, pkt_len, 0, 0, payload));
    }
    return list;
}

void assemble_chunk_hash(char *payload, int chunk_num, chunk_t *chunks) {
    int need = 0;
    char *hash_stater_p = payload + 4;
    for (int i = 0; i < chunk_num; i++) {
        //找到提供者
        if (get_task_handler.providers[i] != NULL) {
            continue;
        }
        memcpy(hash_stater_p + i * SHA1_HASH_SIZE, chunks[i].chunk_hash, SHA1_HASH_SIZE);
        need++;
    }

    memset(payload, 0, 4);
    payload[0] = need;
}

list_t *split_into_chunks(void *payload) {
    list_t *chunks = init_list();
    int num = ((char *) payload)[0];
    char *from_here = payload + 4;
    for (int i = 0; i < num; i++) {
        char *chunk = malloc(SHA1_HASH_SIZE);
        memcpy(chunk, from_here + i * SHA1_HASH_SIZE, SHA1_HASH_SIZE);
        add_node(chunks, chunk);
    }
    return chunks;
}

packet_t *handle_ihave(packet_t *pkt, bt_peer_t *peer) {
    if (get_down_conn(&down_pool, peer) != NULL) {
        return NULL;
    } else {
        list_t *chunks = split_into_chunks(pkt->data);
        if (down_pool.conn_num >= down_pool.max_conn) {
            fprintf(stderr, "download pool is full, please wait for another connection stopping.");
            return NULL;
        } else {
            char *to_download = update_chunk_provider(chunks, peer);
            chunk_buffer_t *chunk_buf = init_chunk_buffer(to_download);
            add_to_down_pool(&down_pool, peer, chunk_buf);
            packet_t *get_pkt = new_pkt(GET, HEADERLEN + SHA1_HASH_SIZE, 0, 0, to_download);
            return get_pkt;
        }
    }
}

void handle_get(int sock, packet_t *pkt, bt_peer_t *peer) {
    upload_connection_t *up_conn = get_up_conn(&up_pool, peer);
    //重复GET包
    if (up_conn != NULL) {
        remove_from_up_pool(&up_pool, peer);
        up_conn = NULL;
    }

    if (up_pool.conn_num >= up_pool.max_conn) {
        fprintf(stderr, "upload pool is full!");
    } else {
        char to_upload[SHA1_HASH_SIZE];
        memcpy(to_upload, pkt->data, SHA1_HASH_SIZE);
        packet_t **data_pkt = get_data_pkts(to_upload);
        up_conn = add_to_up_pool(&up_pool, peer, data_pkt);
        this_up_conn = up_conn;
        send_data_pkts(up_conn, sock, (struct sockaddr *) (&(peer->addr)));
        alarm(2);
    }
}

packet_t **get_data_pkts(char *chunk_hash) {
    int id;
    for (node_t *node = chunk_tracker->head; node != NULL; node = node->next) {
        char *this_chunk_hash = ((chunk_t *) (node->data))->chunk_hash;
        if (memcmp(this_chunk_hash, chunk_hash, SHA1_HASH_SIZE) == 0) {
            id = ((chunk_t *) (node->data))->id;
            break;
        }
    }

    FILE *fd = fopen(master_file_name, "r");
    fseek(fd, id * BT_CHUNK_SIZE, SEEK_SET);
    char data[1024];
    packet_t **data_pkts = malloc(CHUNK_SIZE * sizeof(packet_t *));
    for (unsigned int i = 0; i < CHUNK_SIZE; i++) {
        fread(data, 1024, 1, fd);
        data_pkts[i] = new_pkt(DATA, HEADERLEN + 1024, i + 1, 0, data);
    }
    fclose(fd);
    return data_pkts;
}

void send_data_pkts(upload_connection_t *conn, int sock, struct sockaddr *to) {
    int id_sender = config.identity;
    int id_receiver = conn->receiver->id;
    while (conn->to_send < conn->available) {
        spiffy_sendto(sock, conn->pkts[conn->to_send],
                      conn->pkts[conn->to_send]->header.packet_len, 0, to, sizeof(*to));
        conn->to_send++;
    }
}

void handle_data(int sock, packet_t *pkt, bt_peer_t *peer) {
    download_connection_t *down_conn = get_down_conn(&down_pool, peer);
    unsigned int seq = pkt->header.seq_num;
    int data_len = pkt->header.packet_len - HEADERLEN;
    if (down_conn == NULL) {
        return NULL;
    }

    packet_t *ack_pkt;
    if (seq == down_conn->next_expected_ack) {
        memcpy(down_conn->chunk_buf->data_buf + down_conn->from_here, pkt->data, data_len);
        down_conn->from_here += data_len;
        down_conn->next_expected_ack += 1;
        ack_pkt = new_pkt(ACK, HEADERLEN, 0, seq, NULL);
    } else {
        ack_pkt = new_pkt(ACK, HEADERLEN, 0, down_conn->next_expected_ack - 1, NULL);
    }

    struct sockaddr *to = (struct sockaddr *) (&(peer->addr));
    spiffy_sendto(sock, ack_pkt, ack_pkt->header.packet_len, 0, to, sizeof(*to));
    free(ack_pkt);

    if (down_conn->from_here == BT_CHUNK_SIZE) {
        add_and_check_data(down_conn->chunk_buf->chunk_hash, down_conn->chunk_buf->data_buf);
        remove_from_down_pool(&down_pool, peer);
        if (is_task_finished()) {
            printf("write data to the target file\n");
            write_data();
        } else {
            printf("continue GET task\n");
            for (int i = 0; i < get_task_handler.get_num; i++) {
                if (get_task_handler.status[i] == 0 && get_task_handler.providers[i] != NULL) {
                    char *to_download = get_task_handler.get_chunks[i].chunk_hash;
                    chunk_buffer_t *chunk_buf = init_chunk_buffer(to_download);
                    add_to_down_pool(&down_pool, peer, chunk_buf);
                    packet_t *get_pkt = new_pkt(GET, HEADERLEN + SHA1_HASH_SIZE, 0, 0, to_download);
                    struct sockaddr *get_data_from = (struct sockaddr *) (&(get_task_handler.providers[i]->addr));
                    spiffy_sendto(sock, get_pkt, get_pkt->header.packet_len, 0, get_data_from, sizeof(*get_data_from));
                    get_task_handler.status[i] = 2;
                    break;
                }
            }
        }
    }
}

void handle_ack(int sock, packet_t *pkt, bt_peer_t *peer) {
    int ack_num = pkt->header.ack_num;
    upload_connection_t *up_conn = get_up_conn(&up_pool, peer);
    if (up_conn == NULL) {
        return NULL;
    }

    if (ack_num == CHUNK_SIZE) {
        alarm(0);
        remove_from_up_pool(&up_pool, peer);
    } else if (ack_num > up_conn->last_ack) {
        alarm(0);
        int add_wnd = ack_num - up_conn->last_ack;
        up_conn->last_ack = ack_num;
        up_conn->duplicated_ACK_times = 0;

        if (up_conn->cwnd < up_conn->ssthresh) {
            up_conn->cwnd += add_wnd;
            up_conn->rtt_flag = ack_num + up_conn->cwnd;
        } else {
            if (ack_num >= up_conn->rtt_flag) {
                up_conn->cwnd += 1;
                up_conn->rtt_flag += up_conn->cwnd;
            }
        }

        up_conn->to_send = up_conn->to_send > ack_num ? up_conn->to_send : ack_num;
        int next_available = ack_num + up_conn->cwnd;
        up_conn->available = next_available <= CHUNK_SIZE ? next_available : CHUNK_SIZE;
        send_data_pkts(up_conn, sock, (struct sockaddr *) (&(peer->addr)));
        alarm(2);
    } else if (ack_num == up_conn->last_ack) {
        up_conn->duplicated_ACK_times++;
        if (up_conn->duplicated_ACK_times >= 3) {
            alarm(0);
            up_conn->duplicated_ACK_times = 0;
            up_conn->to_send = ack_num;

            int up_ssthresh = (int) (up_conn->cwnd / 2);
            up_conn->ssthresh = up_ssthresh >= 2 ? up_ssthresh : 2;
            up_conn->cwnd = 1;

            up_conn->available = ack_num + up_conn->cwnd;
            send_data_pkts(up_conn, sock, (struct sockaddr *) (&(peer->addr)));
            alarm(2);
        }
    }
}

void handle_timeout() {
    this_up_conn->to_send = this_up_conn->last_ack;
    this_up_conn->duplicated_ACK_times = 0;

    int up_ssthresh = (int) (this_up_conn->cwnd / 2);
    this_up_conn->ssthresh = up_ssthresh >= 2 ? up_ssthresh : 2;
    this_up_conn->cwnd = 1;

    this_up_conn->available = this_up_conn->to_send + this_up_conn->cwnd;
    send_data_pkts(this_up_conn, config.sock, (struct sockaddr *) (&(this_up_conn->receiver->addr)));
    alarm(2);
}
