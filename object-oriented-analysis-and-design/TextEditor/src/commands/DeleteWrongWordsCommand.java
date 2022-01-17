package commands;

import java.util.Arrays;

import structure.StringText;

/**
 * @Author: Zhengyu Shen
 * @Date: 11/4/2021
 */
public class DeleteWrongWordsCommand implements Command{

  private StringText currentText;

  private String oldText;

  private String[] specificWords;

  private String purifiedString;

  public DeleteWrongWordsCommand(StringText currentText, String[] specificWords, String purifiedString) {
    this.currentText = currentText;
    this.specificWords = specificWords;
    this.purifiedString = purifiedString;
    oldText = currentText.getCurrentString();
  }

  @Override
  public void execute() {
    currentText.deleteSpecificWords(specificWords,purifiedString);
  }


  @Override
  public void unExecute() {
    currentText.setText(oldText);
  }

  @Override
  public String getCommand() {
    return "spell-m";
  }

  @Override
  public Command getNewCommand() {
    return new DeleteWrongWordsCommand(currentText, specificWords, purifiedString);
  }
}
