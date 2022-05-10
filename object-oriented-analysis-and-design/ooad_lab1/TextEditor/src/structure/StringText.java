package structure;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Author: Zhengyu Shen
 * @Date: 11/2/2021
 * @Description: Store current text to edit.
 */
public class StringText {

  private String currentString;

  public StringText() {
    currentString = "";
  }

  public String getCurrentString(){
    return currentString;
  }
  public void setText(String originalText){
    currentString = originalText;
  }
  public void showText() {
    System.out.println(currentString);
  }

  public void addStringToHead(String toAdd) {
    currentString = toAdd + currentString;
  }

  public void addStringToTail(String toAdd) {
    currentString += toAdd;
  }

  public String deleteSpecificWords(String[] specificWords,String purifiedString){
    List<String> specificWordsList = Arrays.asList(specificWords);
    String[] words = purifiedString.split(" ");
    for (int i = 0; i < words.length; i++) {
      if (specificWordsList.contains(words[i])){
        currentString = currentString.replace(words[i],"");
      }
    }
    return currentString;
  }
  public String deleteStringFromHead(int size) {
    size = Math.min(size, currentString.length());
    byte[] bytes = currentString.getBytes();
    byte[] temp = new byte[bytes.length - size];
    byte[] deleted = new byte[size];
    System.arraycopy(bytes, size, temp, 0, bytes.length - size);
    System.arraycopy(bytes, 0, deleted, 0, size);
    currentString = new String(temp);
    return new String(deleted);
  }

  public String deleteStringFromTail(int size) {
    size = Math.min(size, currentString.length());
    byte[] bytes = currentString.getBytes();
    byte[] temp = new byte[bytes.length - size];
    byte[] deleted = new byte[size];
    System.arraycopy(bytes, 0, temp, 0, bytes.length - size);
    System.arraycopy(bytes, bytes.length - size, deleted, 0, size);
    currentString = new String(temp);
    return new String(deleted);
  }
}
