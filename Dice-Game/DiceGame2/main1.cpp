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
    int flag = 0;
public:explicit Player(int flagOut){
        flag = flagOut;
    }
public:explicit Player()= default;
    int play() {
        round += 1;
        if (round == 1) {
            dice1.roll();
            return dice1.getValue();
        } else {
            dice2.roll();
            return dice2.getValue();
        }
    }

    int getRound() {
        return round;
    }

    int getResult() {
        if (flag == 0){
            return ((dice1.getValue() + dice2.getValue()) % 6);
        } else{
            return ((dice1.getValue() * dice2.getValue()) % 6);
        }
    }
};

class Game{
public:
    Player playerA = Player(0);
    Player playerB = Player(1);
    Player *player{};
    int control = 1;
    string word;
    int rounds = 0;
    bool over = false;
    void play() {
        player = control == 1 ? &playerA : &playerB;
        word = control == 1?"player A":"player B";
        std::cout << "Hello " << word << "! You roll the dice!" << std::endl;
        int result = player->play();
            cout << "The result is " << result << endl;
            cout << "Another player can input 'r' to roll the dice now!" << endl;
            control = -control;
            rounds += 1;
    }
    void check(){
        if(isEnded()){
            printWord('A','+', playerA.getResult(),playerA.dice1.value,playerA.dice2.value);
            printWord('B','*', playerB.getResult(),playerB.dice1.value,playerB.dice2.value);
            if (judge() == 3){
                cout << "Player A wins!" << endl;
            }else if(judge() == 1){
                cout << "No one wins!" << endl;
            }else{
                cout << "Player B wins!" << endl;
            }
            over = true;
        }
    }
    void printWord(char name,char way,int result,int dice1,int dice2){
        cout << "Player "<< name << " scores " << result << "((" << dice1 << way << dice2 << ")%6="<< result << ")" << endl;
    }
    bool isEnded() {
        return rounds == 4;
    }
    bool isOver(){
        return over;
    }

    bool willBeOver(){
        return rounds == 4;
    }

    int judge(){
        if (playerA.getResult() > playerB.getResult()){
            return 3;
        }else if (playerA.getResult() == playerB.getResult()){
            return 1;
        }else{
            return 0;
        }
    }
};

class Service {
public:
    int start() {
        cout << "Two players, roll dice twice, player A uses addition, player B uses multiplication, divide by 6.\n"
                "The player with bigger result wins." << endl;
        cout << "Please input your command('s' to start, 'r' to roll the dice ,'q' to quit)" << endl;
        char instruction;
        Game game;
        bool started = false;
        while (true) {
            game.check();
            if(game.isOver()){
                return 0;
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
                return quit();
            } else {
                cout << "Wrong input!Please input your command('s' to start, 'r' to roll the dice ,'q' to quit)"
                     << endl;
            }
        }

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