package structure;

import java.util.ArrayList;
import java.util.List;

import commands.Command;
import commands.MacroCommand;

/**
 * @Author: Zhengyu Shen
 * @Date: 11/2/2021
 */
public class UStack {

  private List<Command> modifiedCommands;

  public UStack() {
    modifiedCommands = new ArrayList<>();
  }

  public ArrayList<Command> getLatestnModifyingCommands(int n) {
    ArrayList<Command> commands = new ArrayList<>();
    n = Math.min(n, modifiedCommands.size());
    for (int i = 1; i <= n; i++) {
      commands.add(modifiedCommands.get(modifiedCommands.size() - i).getNewCommand());
    }
    return commands;
  }

  public void showList(int num) {
    for (int i = 1; i <= Math.min(num, modifiedCommands.size()); i++) {
      String commandName = modifiedCommands.get(modifiedCommands.size() - i).getCommand();
      if (modifiedCommands.get(modifiedCommands.size() - i) instanceof MacroCommand){
        commandName = "$" + commandName;
      }
      System.out.println(i + " " + commandName);
    }
  }

  public boolean isNull() {
    return getSize() == 0;
  }

  public int getSize() {
    return modifiedCommands.size();
  }

  public void push(Command command) {
    modifiedCommands.add(command);
  }

  public Command pop() {
    if (modifiedCommands.size() > 0) {
      return modifiedCommands.remove(modifiedCommands.size() - 1);
    }
    return null;
  }
}
