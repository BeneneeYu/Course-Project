//
// Created by 86460 on 2020/4/19.
//

#ifndef DICEGAME3_GAME_H
#define DICEGAME3_GAME_H
#include "Player.h"
#include <string>

class Game {
public:
    Player *player;
    int control;
    int strategy;
    int resultA;
    int resultB;
    char way;
    std::string word;
    int rounds;
    bool over;
    bool empty;
    std::string winner[100];
    int howMany;
    Player *playerA;
    Player *playerB;
    void attend(Player playerToAttend);
    void play();
    void check();
    void printWord(char name, char way, int result, int dice1, int dice2);
    bool isEnded();
    bool isOver();
    int judge();
    void save(std::string win);
    void show();
    Game();
    Game(Player player1, Player player2, int s);
};
#endif //DICEGAME3_GAME_H
