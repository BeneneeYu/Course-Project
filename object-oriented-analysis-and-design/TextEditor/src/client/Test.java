package client;

import java.util.ArrayList;

/**
 * @program: object-oriented-analysis-and-design
 * @description:
 * @author: Shen Zhengyu
 * @create: 2021-11-03 23:00
 **/
public class Test {

  public static void main(String[] args) {
    String[] commands;
    ArrayList<String> commandList = new ArrayList<>();
    commandList.add("testMode");
    commandList.add("-t \"Hotel \"");// Initiate the string, type in word 'Hotel'.
    commandList.add("s");// Print the string.
    commandList.add("A \"California\"");// Append the word 'California' to string.
    commandList.add("a \"Welcome to the \"");// Add the sentence 'Welcome to the' to the head.
    commandList.add("D 11");// Delete the last 10 characters, which is ' California'.
    commandList.add("d 8");// Delete the first 8 characters, which is 'Welcome '.
    commandList.add("l 10");// Show last ten commands.
    commandList.add("u");// Undo 'Delete the first 8 characters'.
    commandList.add("r");// Redo 'Delete the first 8 characters'.
    commandList.add("m 5 m10");// Define macro command m10.
    commandList.add("$m10");// Use macro command m10.
    commandList.add("a \"Welcome \"");
    commandList.add("A \" California\"");
    commandList.add("A \" Balifornia\"");
    commandList.add("spell");
    commandList.add("spell-a");
    commandList.add("spell-m");
    commandList.add("lang fra");
    commandList.add("spell-a");
    commandList.add("content xml");
    commandList.add("lang eng");
    commandList.add("a \"<A>\"");
    commandList.add("A \"</A>\"");
    commandList.add("spell-a");
    commandList.add("q");// Quit.

    int size = commandList.size();
    commands = new String[size];
    for (int i = 0; i < commandList.size(); i++) {
      commands[i] = commandList.get(i);
    }
    Application.main(commands);
  }
}
