package filter;

import java.nio.charset.StandardCharsets;

/**
 * @Author: Zhengyu Shen
 * @Date: 11/2/2021
 */
public class XMLFilter implements Filter{
//  public static void main(String[] args) {
//    XMLFilter x = new XMLFilter();
//    System.out.println(x.filter("<e><t>title</t><d>The quick brown fox jumps over the lazy dag</d></e>"));
//  }
  @Override
  public String filter(String rawText) {
    StringBuilder stringBuilder = new StringBuilder();
    int length = rawText.length();
    boolean skipMode = false;
    for (int i = 0; i < length; i++) {
      if (skipMode){
        if (rawText.charAt(i) == '>'){
          skipMode = false;
          if (stringBuilder.length() > 0 && stringBuilder.charAt(stringBuilder.length()-1) != ' '){
            stringBuilder.append(" ");
          }
        }
      }else{
        if (rawText.charAt(i) == '<'){
          skipMode = true;
        }else{
          stringBuilder.append(rawText.charAt(i));
        }
      }
    }
    return stringBuilder.toString().trim();
  }
}
