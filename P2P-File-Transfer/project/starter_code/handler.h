#ifndef CN_BITTORRENT_HANDLER_H
#define CN_BITTORRENT_HANDLER_H

#include "sha.h"
#include "list.h"
#include "store.h"
#include "bt_parse.h"
#include "packet.h"
#include "conn.h"

void pkt_ntoh(packet_t *packet);

int parse_type(packet_t *packet);//解析包的类型

packet_t *handle_whohas(packet_t *pkt_whohas);//检查自己有没有包，尝试响应他人请求

int has_chunk(char *chunk_hash);//自己是否有chunk

packet_t *new_pkt(unsigned char type, unsigned short packet_len,unsigned int seq_num, unsigned int ack_num, char *payload);

list_t *new_whohas_pkt();//想要从他人获取包

void assemble_chunk_hash(char *payload, int chunk_num, chunk_t *chunks);

list_t *split_into_chunks(void *payload);

packet_t *handle_ihave(packet_t *pkt, bt_peer_t *peer);

void handle_get(int sock, packet_t *pkt, bt_peer_t *peer);

void send_data_pkts(upload_connection_t *conn, int sock, struct sockaddr *to);

packet_t **get_data_pkts(char *chunk_hash);

void handle_data(int sock, packet_t *pkt, bt_peer_t *peer);

void handle_ack(int sock, packet_t *pkt, bt_peer_t *peer);

void handle_timeout();

#endif //CN_BITTORRENT_HANDLER_H
