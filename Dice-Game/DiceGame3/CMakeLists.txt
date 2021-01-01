cmake_minimum_required(VERSION 3.15)
project(DiceGame3)

set(CMAKE_CXX_STANDARD 14)

add_executable(DiceGame3 main.cpp Dice.cpp Dice.h Player.cpp Player.h Game.cpp Game.h Service.cpp Service.h)