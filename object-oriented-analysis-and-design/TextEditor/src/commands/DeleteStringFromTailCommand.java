package commands;

import structure.StringText;

/**
 * @Author: Zhengyu Shen
 * @Date: 11/2/2021
 */
public class DeleteStringFromTailCommand implements Command{
  private StringText currentText;

  private String toDelete;

  private int size;

  public DeleteStringFromTailCommand(StringText currentText, int size) {
    this.currentText = currentText;
    this.size = size;
    this.toDelete = null;
  }

  @Override
  public void execute() {
    toDelete = currentText.deleteStringFromTail(size);
  }

  @Override
  public void unExecute() {
    currentText.addStringToTail(toDelete);
  }

  @Override
  public String getCommand() {
    return "D " + size;
  }

  @Override
  public Command getNewCommand() {
    return new DeleteStringFromTailCommand(currentText, size);
  }
}
