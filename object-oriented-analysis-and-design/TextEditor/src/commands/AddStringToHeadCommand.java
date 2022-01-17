package commands;

import structure.StringText;

/**
 * @Author: Zhengyu Shen
 * @Date: 11/2/2021
 */
public class AddStringToHeadCommand implements Command {

  private StringText currentText;

  private String toAdd;

  public AddStringToHeadCommand(StringText currentText, String toAdd) {
    this.currentText = currentText;
    this.toAdd = toAdd;
  }

  @Override
  public void execute() {
    currentText.addStringToHead(toAdd);
  }

  @Override
  public void unExecute() {
    currentText.deleteStringFromHead(toAdd.length());
  }

  @Override
  public String getCommand() {
    String toAddWhole = "\"" + toAdd + "\"";
    return "a " + toAddWhole;
  }

  @Override
  public Command getNewCommand() {
    return new AddStringToHeadCommand(currentText, toAdd);
  }
}
