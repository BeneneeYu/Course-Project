//
// Created by 86460 on 2020/4/19.
//
#include <iostream>
#include <random>
#include <ctime>
#include "Dice.h"
Dice::Dice(){
    this->value = 0;
}
int Dice::getValue() {
    return value;
}
void Dice::roll() {
            unsigned seed;  // Random generator seed
        // Use the time function to get a "seed” value for srand
        seed = time(0);
        srand(seed);
        value = rand() % 6 + 1;
}