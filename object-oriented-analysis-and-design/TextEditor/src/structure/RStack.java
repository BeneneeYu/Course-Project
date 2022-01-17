package structure;

import java.util.ArrayList;
import java.util.List;

import commands.Command;

/**
 * @Author: Zhengyu Shen
 * @Date: 11/2/2021
 */
public class RStack {
  private List<Command> modifiedCommands;

  public RStack() {
    modifiedCommands = new ArrayList<>();
  }

  public int getSize() {
    return modifiedCommands.size();
  }

  public boolean isNull(){
    return getSize() == 0;
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
