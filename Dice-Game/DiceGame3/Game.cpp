//
// Created by 86460 on 2020/4/19.
//
#include <iostream>
#include "Game.h"
#include "Player.h"

Game::Game(){
    strategy=0;
    rounds=0;
    way='+';
    control=1;
    over=false;
    empty=true;
    howMany=0;
};
Game::Game(Player player1, Player player2, int s) {
    strategy=0;
    rounds=0;
    way='+';
    control=1;
    over=false;
    empty=true;
    howMany=0;
    playerA = &player1;
    playerB = &player2;
    strategy = s;
}
void Game::attend(Player playerToAttend) {
    if(empty){
        std::cout << "Hello " << playerToAttend.getName() << "! Please select your scoring formula!" << std::endl;
        std::cout << "If you input 0,we use addition,if you input 1,we use multiplication,at last divide it by 6." << std::endl;
        std::cout << "Now input '0' or '1'!" << std::endl;
        std::cin >> strategy;
        way = strategy == 0?'+':'*';
        playerA = &playerToAttend;
        empty = false;
    }else{
        std::cout << "Hello " << playerToAttend.getName() << "!" << std::endl;
        std::cout << "The former player select " << way << ". Input y or n to express if you agree with the formula" << std::endl;
        std::cout << "Now input 'y' or 'n'!" << std::endl;

        char choice;
        std::cin >> choice;
        if(choice == 'y'){
            playerB = &playerToAttend;
            std::cout << "Attend successfully!" << std::endl;
            return;
        }else{
            std::cout << "Attend failure!" << std::endl;
            exit(1);
        }
    }
}

void Game::play() {
    player = control == 1 ? playerA : playerB;
    word = control == 1 ? "player A" : "player B";
    std::cout << "Hello " << word << "! You roll the dice!" << std::endl;
    int result = player->play();
    std::cout << "The result is " << result << std::endl;
    std::cout << "Another player can input 'r' to roll the dice now!" << std::endl;
    control = -control;
    rounds += 1;
}
void Game::show(){
    for(int i = 0; i < howMany;i++){
        if (!winner[i].empty()){
            std::cout << "Round " << i+1 << ": " << winner[i] <<" wins!" << std::endl;
        }
        else{
            std::cout << "Round " << i+1 << ": " << "No one wins!" << std::endl;
        }
    }
}
void Game::check() {
    if (isEnded()) {
        if (strategy == 0) {
            resultA = (playerA->dice1.getValue() + playerA->dice2.getValue()) % 6;
            resultB = (playerB->dice1.getValue() + playerB->dice2.getValue()) % 6;
            way = '+';
        } else {
            resultA = (playerA->dice1.getValue() * playerA->dice2.getValue()) % 6;
            resultB = (playerB->dice1.getValue() * playerB->dice2.getValue()) % 6;
            way = '*';
        }
        printWord('A', way, resultA, playerA->dice1.value, playerA->dice2.value);
        printWord('B', way, resultB, playerB->dice1.value, playerB->dice2.value);
        if (judge() == 3) {
            std::cout << "Player A wins!" << std::endl;
            save(playerA->getName());
        } else if (judge() == 1) {
            std::cout << "No one wins!" << std::endl;
            save("");
        } else {
            std::cout << "Player B wins!" << std::endl;
            save(playerB->getName());
        }
        show();
        over = true;
    }
}
void Game::printWord(char name, char way, int result, int dice1, int dice2) {
    std::cout << "Player " << name << " scores " << result << "((" << dice1 << way << dice2 << ")%6=" << result << ")"
              << std::endl;
}
bool Game::isEnded() {
    return rounds == 4;
}
bool Game::isOver() {
    return over;
}
int Game::judge() {
    if (resultA > resultB) {
        return 3;
    } else if (resultA == resultB) {
        return 1;
    } else {
        return 0;
    }
}
void Game::save(std::string win){
    winner[howMany] = win;
    howMany++;
}