package commands;

/**
 * @Author: Zhengyu Shen
 * @Date: 11/2/2021
 */
public interface Command {
  void execute();
  void unExecute();
  String getCommand();
  Command getNewCommand();
}
