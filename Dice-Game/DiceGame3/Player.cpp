//
// Created by 86460 on 2020/4/19.
//
#include <iostream>
#include "Player.h"
#include <string>
using namespace std;
Player::Player(){
    round = 0;
};


Player::Player(char nam) {
    round = 0;
    name = nam;
}
string Player::getName() {
    return name;
}

int Player::play(){
    round += 1;
    if (round == 1) {
        dice1.roll();
        return dice1.getValue();
    } else {
        dice2.roll();
        return dice2.getValue();
    }
};