//
// Created by 86460 on 2020/4/19.
//

#ifndef DICEGAME3_PLAYER_H
#define DICEGAME3_PLAYER_H
#include "Dice.h"
#include <string>
using namespace std;
class Player {
public:
    Dice dice1;
    Dice dice2;
    int round;

    //0代表加起来，1代表乘起来
    int play();
    explicit Player(char name);
    Player();
    string getName();
private:
    string name;
};


#endif //DICEGAME3_PLAYER_H
