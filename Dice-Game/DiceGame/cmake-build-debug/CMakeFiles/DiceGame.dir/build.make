# CMAKE generated file: DO NOT EDIT!
# Generated by "MinGW Makefiles" Generator, CMake Version 3.15

# Delete rule output on recipe failure.
.DELETE_ON_ERROR:


#=============================================================================
# Special targets provided by cmake.

# Disable implicit rules so canonical targets will work.
.SUFFIXES:


# Remove some rules from gmake that .SUFFIXES does not remove.
SUFFIXES =

.SUFFIXES: .hpux_make_needs_suffix_list


# Suppress display of executed commands.
$(VERBOSE).SILENT:


# A target that is always out of date.
cmake_force:

.PHONY : cmake_force

#=============================================================================
# Set environment variables for the build.

SHELL = cmd.exe

# The CMake executable.
CMAKE_COMMAND = "D:\JetBrains\CLion 2019.3.3\bin\cmake\win\bin\cmake.exe"

# The command to remove a file.
RM = "D:\JetBrains\CLion 2019.3.3\bin\cmake\win\bin\cmake.exe" -E remove -f

# Escaping for special characters.
EQUALS = =

# The top-level source directory on which CMake was run.
CMAKE_SOURCE_DIR = D:\Programming\C++_programming\DiceGame

# The top-level build directory on which CMake was run.
CMAKE_BINARY_DIR = D:\Programming\C++_programming\DiceGame\cmake-build-debug

# Include any dependencies generated for this target.
include CMakeFiles/DiceGame.dir/depend.make

# Include the progress variables for this target.
include CMakeFiles/DiceGame.dir/progress.make

# Include the compile flags for this target's objects.
include CMakeFiles/DiceGame.dir/flags.make

CMakeFiles/DiceGame.dir/main.cpp.obj: CMakeFiles/DiceGame.dir/flags.make
CMakeFiles/DiceGame.dir/main.cpp.obj: ../main.cpp
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green --progress-dir=D:\Programming\C++_programming\DiceGame\cmake-build-debug\CMakeFiles --progress-num=$(CMAKE_PROGRESS_1) "Building CXX object CMakeFiles/DiceGame.dir/main.cpp.obj"
	D:\mingw64\bin\g++.exe  $(CXX_DEFINES) $(CXX_INCLUDES) $(CXX_FLAGS) -o CMakeFiles\DiceGame.dir\main.cpp.obj -c D:\Programming\C++_programming\DiceGame\main.cpp

CMakeFiles/DiceGame.dir/main.cpp.i: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Preprocessing CXX source to CMakeFiles/DiceGame.dir/main.cpp.i"
	D:\mingw64\bin\g++.exe $(CXX_DEFINES) $(CXX_INCLUDES) $(CXX_FLAGS) -E D:\Programming\C++_programming\DiceGame\main.cpp > CMakeFiles\DiceGame.dir\main.cpp.i

CMakeFiles/DiceGame.dir/main.cpp.s: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Compiling CXX source to assembly CMakeFiles/DiceGame.dir/main.cpp.s"
	D:\mingw64\bin\g++.exe $(CXX_DEFINES) $(CXX_INCLUDES) $(CXX_FLAGS) -S D:\Programming\C++_programming\DiceGame\main.cpp -o CMakeFiles\DiceGame.dir\main.cpp.s

# Object files for target DiceGame
DiceGame_OBJECTS = \
"CMakeFiles/DiceGame.dir/main.cpp.obj"

# External object files for target DiceGame
DiceGame_EXTERNAL_OBJECTS =

DiceGame.exe: CMakeFiles/DiceGame.dir/main.cpp.obj
DiceGame.exe: CMakeFiles/DiceGame.dir/build.make
DiceGame.exe: CMakeFiles/DiceGame.dir/linklibs.rsp
DiceGame.exe: CMakeFiles/DiceGame.dir/objects1.rsp
DiceGame.exe: CMakeFiles/DiceGame.dir/link.txt
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green --bold --progress-dir=D:\Programming\C++_programming\DiceGame\cmake-build-debug\CMakeFiles --progress-num=$(CMAKE_PROGRESS_2) "Linking CXX executable DiceGame.exe"
	$(CMAKE_COMMAND) -E cmake_link_script CMakeFiles\DiceGame.dir\link.txt --verbose=$(VERBOSE)

# Rule to build all files generated by this target.
CMakeFiles/DiceGame.dir/build: DiceGame.exe

.PHONY : CMakeFiles/DiceGame.dir/build

CMakeFiles/DiceGame.dir/clean:
	$(CMAKE_COMMAND) -P CMakeFiles\DiceGame.dir\cmake_clean.cmake
.PHONY : CMakeFiles/DiceGame.dir/clean

CMakeFiles/DiceGame.dir/depend:
	$(CMAKE_COMMAND) -E cmake_depends "MinGW Makefiles" D:\Programming\C++_programming\DiceGame D:\Programming\C++_programming\DiceGame D:\Programming\C++_programming\DiceGame\cmake-build-debug D:\Programming\C++_programming\DiceGame\cmake-build-debug D:\Programming\C++_programming\DiceGame\cmake-build-debug\CMakeFiles\DiceGame.dir\DependInfo.cmake --color=$(COLOR)
.PHONY : CMakeFiles/DiceGame.dir/depend

