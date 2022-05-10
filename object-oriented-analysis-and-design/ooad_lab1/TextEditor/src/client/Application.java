package client;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

import checker.SpellChecker;
import commands.AddStringToHeadCommand;
import commands.AddStringToTailCommand;
import commands.Command;
import commands.DeleteStringFromHeadCommand;
import commands.DeleteStringFromTailCommand;
import commands.DeleteWrongWordsCommand;
import commands.MacroCommand;
import filter.Filter;
import filter.TextFilter;
import filter.XMLFilter;
import structure.RStack;
import structure.StringText;
import structure.UStack;

/**
 * @Author: Zhengyu Shen
 * @Date: 11/2/2021
 * @Decription: The application for responding to requests user makes.
 */
public class Application {

  public static void main(String[] args) {
//    test();
    RStack rStack = new RStack();
    UStack uStack = new UStack();
    Filter filter;
    SpellChecker spellChecker;
    StringText stringText = new StringText();
    Scanner scanner = new Scanner(System.in);
    String languageMode = "eng";
    String contentMode = "txt";
    HashMap<String, MacroCommand> macroCommandHashMap = new HashMap<>();
    int num;
    String command;
    Command tempCommand;
    MacroCommand tempMacroCommand;
    int round = 0;
    boolean testMode = false;
    if (args.length > 0 && "testMode".equals(args[0])){
      round = 1;
      testMode = true;
    }
    if (args.length > 0 && "-t".equals(args[0])){
      stringText.setText(args[1]);
    }
    while (true) {
      if (testMode) {
        command = args[round];
      } else {
        command = scanner.nextLine();
      }
      String[] arguments = command.split(" ");
      String commandType = arguments[0];
      round++;
      try {
        switch (commandType) {
        case "-t":
          stringText.setText(removeQuotation(command));
          stringText.showText();
          break;
        case "s":
          stringText.showText();
          break;
        case "A":
          tempCommand = new AddStringToTailCommand(stringText, removeQuotation(command));
          tempCommand.execute();
          uStack.push(tempCommand);
          stringText.showText();
          break;
        case "a":
          tempCommand = new AddStringToHeadCommand(stringText, removeQuotation(command));
          tempCommand.execute();
          uStack.push(tempCommand);
          stringText.showText();
          break;
        case "D":
          tempCommand = new DeleteStringFromTailCommand(stringText, Integer.parseInt(arguments[1]));
          tempCommand.execute();
          uStack.push(tempCommand);
          stringText.showText();
          break;
        case "d":
          tempCommand = new DeleteStringFromHeadCommand(stringText, Integer.parseInt(arguments[1]));
          tempCommand.execute();
          uStack.push(tempCommand);
          stringText.showText();
          break;
        case "l":
          uStack.showList(Integer.parseInt(arguments[1]));
          break;
        case "u":
          if (uStack.isNull()) {
            break;
          }
          tempCommand = uStack.pop();
          rStack.push(tempCommand);
          tempCommand.unExecute();
//          stringText.showText();
          break;
        case "r":
          if (rStack.isNull()) {
            break;
          }
          tempCommand = rStack.pop();
          uStack.push(tempCommand);
          tempCommand.execute();
//          stringText.showText();
          break;
        case "m":
          num = Integer.parseInt(arguments[1]);
          tempMacroCommand = new MacroCommand(uStack.getLatestnModifyingCommands(num), arguments[2]);
          macroCommandHashMap.put(arguments[2], tempMacroCommand);
          break;
        case "lang":
          languageMode = arguments[1];
          break;
        case "content":
          contentMode = arguments[1];
          break;
        case "spell":
          filter = initiateFilter(contentMode);
          spellChecker = new SpellChecker(languageMode);
          spellChecker.printCheckResult(spellChecker.check(filter.filter(stringText.getCurrentString())));
          break;
        case "spell-a":
          filter = initiateFilter(contentMode);
          spellChecker = new SpellChecker(languageMode);
          spellChecker.markSpellMistakes(stringText.getCurrentString(), filter.filter(stringText.getCurrentString()));
          break;
        case "spell-m":
          filter = initiateFilter(contentMode);
          spellChecker = new SpellChecker(languageMode);
          tempCommand = new DeleteWrongWordsCommand(stringText,spellChecker.check(filter.filter(stringText.getCurrentString())),spellChecker.purify(filter.filter(stringText.getCurrentString())));
          tempCommand.execute();
          uStack.push(tempCommand);
          stringText.showText();
          break;
        case "q":
          System.exit(0);
          break;
        default:
          if (arguments[0].charAt(0) == '$') {

            tempMacroCommand = macroCommandHashMap.get(arguments[0].replace("$", ""));
            if (null == tempMacroCommand) {
              break;
            }
            tempMacroCommand.execute();
            uStack.push(tempMacroCommand);
            stringText.showText();
          }
          break;
        }
      } catch (Exception e) {
        System.out.println("Wrong command: " + e.getMessage());
      }
    }
  }

  private static String removeQuotation(String rawString) {
    return rawString.split("\"")[1].replace("\n", "");
  }

  private static void test() {
    Filter f = new XMLFilter();
    System.out.println(f.filter("<e><t>title</t><d>the Stream is actually being modified during access</d></e>"));
    SpellChecker c = new SpellChecker("eng");
    System.out.println(c.purify(",,, 2 asdasd         222 ,.,. 2331 aaa . 23"));
    for (String s : c.purify(",,, 2 asdasd         222 ,.,. 2331 aaa . 23").split(" ")) {
      System.out.println(s);
    }
  }

  private static Filter initiateFilter(String filterType) {
    if ("txt".equals(filterType)) {
      return new TextFilter();
    } else if ("xml".equals(filterType)) {
      return new XMLFilter();
    }
    return new TextFilter();
  }
}
