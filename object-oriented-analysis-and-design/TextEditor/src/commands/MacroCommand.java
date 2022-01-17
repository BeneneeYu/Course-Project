package commands;

import java.util.ArrayList;

/**
 * @Author: Zhengyu Shen
 * @Date: 11/2/2021
 */
public class MacroCommand implements Command{
  private ArrayList<Command> commands;
  private String macroCommandName;

  public MacroCommand(ArrayList<Command> commands, String macroCommandName) {
    this.commands = commands;
    this.macroCommandName = macroCommandName;
  }

  @Override
  public void execute() {
    for (int i = 0; i < commands.size(); i++) {
      commands.get(commands.size() - i - 1).execute();
    }
  }

  @Override
  public void unExecute() {
    for (Command command : commands) {
      command.unExecute();
    }
  }

  @Override
  public String getCommand() {
    return macroCommandName;
  }

  @Override
  public Command getNewCommand() {
    return new MacroCommand(commands, macroCommandName);
  }
}
