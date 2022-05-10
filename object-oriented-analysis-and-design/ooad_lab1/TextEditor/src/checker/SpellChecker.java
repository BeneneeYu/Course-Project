package checker;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * @Author: Zhengyu Shen
 * @Date: 11/2/2021
 */
public class SpellChecker {

  HashMap<String, String> languageSource;

  String languageType;

  List<String> wordList;

  char[] separators;

  public SpellChecker(String languageType) {
    languageSource = new HashMap<>();
    languageSource.put("eng", "eng.txt");
    languageSource.put("fra", "fra.txt");
    this.languageType = languageType;
    separators = new char[] { ' ', ',', '.' };
    wordList = new ArrayList<>();
    String type = languageSource.get(languageType);
    if (null == type || "".equals(type)){
      type = "eng";
    }
    initiateWordList(type);
  }

  public String[] check(String rawText) {
    String[] words = purify(rawText).split(" ");
    String[] wrongSpelledWords;
    ArrayList<String> wrongSpelledWordsList = new ArrayList<>();
    for (String word : words) {
      if (wordList.contains(word)){

      }else{
        wrongSpelledWordsList.add(word);
      }
    }
    wrongSpelledWords = new String[wrongSpelledWordsList.size()];
    for (int i = 0; i < wrongSpelledWords.length; i++) {
      wrongSpelledWords[i] = wrongSpelledWordsList.get(i);
    }
    return wrongSpelledWords;
  }

  public void printCheckResult(String[] wrongSpelledWords){
    for (String wrongSpelledWord : wrongSpelledWords) {
      System.out.println(wrongSpelledWord);
    }
  }

  public void markSpellMistakes(String rawText,String pureText){
    String[] wrongSpelledWords = check(pureText);
    ArrayList<String> wrongSpelledWordsList = new ArrayList<>();
    for (String wrongSpelledWord : wrongSpelledWords) {
      if (!wrongSpelledWordsList.contains(wrongSpelledWord)){
        wrongSpelledWordsList.add(wrongSpelledWord);
      }
    }
    for (String wrongSpelledWord : wrongSpelledWordsList) {
      rawText = rawText.replace(wrongSpelledWord,"*[" + wrongSpelledWord + "]");
    }
    System.out.println(rawText);
  }


  // Make sure that words are separated by space.
  public String purify(String rawText) {
    StringBuilder stringBuilder = new StringBuilder();
    int length = rawText.length();
    boolean skipMode = false;
    char tmp;
    for (int i = 0; i < length; i++) {
      tmp = rawText.charAt(i);
      if (skipMode) {
        if (!isSeparator(tmp)) {
          stringBuilder.append(" ").append(tmp);
          skipMode = false;
        }
      } else {
        if (isSeparator(tmp)) {
          skipMode = true;
        } else {
          stringBuilder.append(tmp);
        }
      }
    }
    return stringBuilder.toString().trim();
  }

  private boolean isSeparator(char c) {
    for (char separator : separators) {
      if (separator == c) {
        return true;
      }
    }
    return false;
  }

  public void initiateWordList(String url) {
    String projectDir = System.getProperty( "user.dir" );
    String fileSeparator = System.getProperty("file.separator");
    File file1 = new File(projectDir + fileSeparator + "TextEditor" + fileSeparator + "src" + fileSeparator + url); // 创建File类对象
    BufferedReader br = null; // 创建reader缓冲区将文件流装进去
    try {
      br = new BufferedReader(new FileReader(file1));
      String lineTxt = null;
      while ((lineTxt = br.readLine()) != null) {
        wordList.add(lineTxt.trim());
      }
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (br != null) {
        try {
          br.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }
}
