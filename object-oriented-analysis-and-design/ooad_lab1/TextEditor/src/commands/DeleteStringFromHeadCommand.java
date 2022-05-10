package commands;

import structure.StringText;

/**
 * @Author: Zhengyu Shen
 * @Date: 11/2/2021
 */
public class DeleteStringFromHeadCommand implements Command {

  private StringText currentText;

  private String toDelete;

  private int size;

  public DeleteStringFromHeadCommand(StringText currentText, int size) {
    this.currentText = currentText;
    this.toDelete = null;
    this.size = size;
  }

  @Override
  public void execute() {
    toDelete = currentText.deleteStringFromHead(size);
  }

  @Override
  public void unExecute() {
    currentText.addStringToHead(toDelete);
  }

  @Override
  public String getCommand() {
    return "d " + size;
  }

  @Override
  public Command getNewCommand() {
    return new DeleteStringFromHeadCommand(currentText, size);
  }
}
