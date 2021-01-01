//
// Created by 86460 on 2020/4/19.
//

#include "Service.h"
#include "Game.h"
#include "Player.h"
#include <iostream>
#include <string>
using namespace std;
int Service::start() {
    Player player1('A');
    Player player2('B');
    Game game;
    game.attend(player1);
    game.attend(player2);
    bool started = false;
    cout << "Please input your command('s' to start, 'r' to roll the dice ,'q' to quit)" << endl;
    char instruction;
    while (true) {
        game.check();
        if (game.isOver()) {
            cout << "This round is over,do you " << player1.getName() << " want to play again?" << endl;
            cout << "Input 'n' to quit,otherwise the game restart." << endl;

            char again;
            cin >> again;
            if(again == 'n') {
                return 0;
            }
            else{
                game.over = false;
                game.rounds = 0;
                game.control = 1;
                game.playerA->round = 0;
                game.playerB->round = 0;

                cout << "Please input your command('s' to start, 'r' to roll the dice ,'q' to quit)" << endl;
                continue;
            }
        }
        cin >> instruction;
        if (instruction == 's') {
            started = true;
            cout << "Please input 'r' to roll the dice" << endl;
        } else if (instruction == 'r') {
            if (started) {
                game.play();

            } else {
                cout << "Please input 's' to start the game first!" << endl;
                continue;
            }
        } else if (instruction == 'q') {
            exit(1);
        } else {
            cout << "Wrong input!Please input your command('s' to start, 'r' to roll the dice ,'q' to quit)"
                 << endl;
        }
    }

}

int quit() {
    cout << "See you!" << endl;
    return 0;
}