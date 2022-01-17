package commands;

import structure.StringText;

/**
 * @Author: Zhengyu Shen
 * @Date: 11/2/2021
 */
public class AddStringToTailCommand implements Command {

  private StringText currentText;

  private String toAdd;

  public AddStringToTailCommand(StringText currentText, String toAdd) {
    this.currentText = currentText;
    this.toAdd = toAdd;
  }

  @Override
  public void execute() {
    currentText.addStringToTail(toAdd);
  }

  @Override
  public void unExecute() {
    currentText.deleteStringFromTail(toAdd.length());
  }

  @Override
  public String getCommand() {
    String toAddWhole = "\"" + toAdd + "\"";
    return "A " + toAddWhole;
  }

  @Override
  public Command getNewCommand() {
    return new AddStringToTailCommand(currentText, toAdd);
  }
}
