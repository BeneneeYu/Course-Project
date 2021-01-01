#include <iostream>
#include <random>
#include <ctime>

using namespace std;

class Dice {
public:
    int value = 0;

    int getValue() {
        return value;
    }

    void roll() {
        unsigned seed;  // Random generator seed
        // Use the time function to get a "seed” value for srand
        seed = time(0);
        srand(seed);
        value = rand() % 6 + 1;
    }
};
class Player {
public:
    Dice dice1;
    Dice dice2;
    int round = 0;

    int play() {
        round += 1;
        if (round == 1) {
            dice1.roll();
            return dice1.getValue();
        } else {
            dice2.roll();
            judge();
            return dice2.getValue();
        }
    }

    bool judge() {
        return dice1.getValue() + dice2.getValue() == 7;
    }

    int getRound() {
        return round;
    }

    int getSum() {
        return dice1.getValue() + dice2.getValue();
    }
};




class Game {
public:
    Player player;

    void play() {
        std::cout << "You roll the dice!" << std::endl;
        int result = player.play();
        if (!isOver()) {
            cout << "The result is " << result << endl;
            cout << "You can input 'r' to roll the dice once more!" << endl;
        } else {
            cout << "The result is " << result << endl;
            if (isWinner()) {
                cout << "Your sum is 7 and you win!" << endl;
                cout << "------------------------------------------" << endl;

            } else {
                cout << "Your sum is " << player.getSum() << " and you lose!" << endl;
                cout << "------------------------------------------" << endl;

            }
        }
    }

    bool isOver() {
        return player.getRound() == 2;
    }

    bool isWinner() {
        return player.judge();
    }
};
class Service{
public:
    int start() {
        cout << "Please input your command('s' to start, 'r' to roll the dice ,'q' to quit)" << endl;
        cout << "You can roll the dice for two times,if the sum is seven,you'll win.Otherwise you will lose." << endl;
        char instruction;
        Game game;
        bool started = false;
         while (true) {
            cin >> instruction;
            if (instruction == 's') {
                Game game1;
                game = game1;
                started = true;
                cout << "Please input 'r' to roll the dice" << endl;
            } else if (instruction == 'r') {
                if (started) {
                    game.play();
                    if (game.isOver()) {
                        cout << "Input 't' to restart the game,or you will quit." << endl;
                        cin >> instruction;
                        if (instruction == 't') {
                            return restart();
                        } else {
                            return 0;
                        }
                    }
                } else {
                    cout << "Please input 's' to start the game first!" << endl;
                    continue;
                }
            } else if (instruction == 'q') {
                return quit();
            } else {
                cout << "Wrong input!Please input your command('s' to start, 'r' to roll the dice ,'q' to quit)"
                     << endl;
            }
        }
    }
    int restart(){
        return start();
    }
    int quit(){
        cout << "See you!" << endl;
        return 0;
    }

};

int main() {
    Service service;
    return service.start();
}


