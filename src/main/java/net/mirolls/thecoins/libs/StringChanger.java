package net.mirolls.thecoins.libs;

import java.util.ArrayList;
import java.util.List;

public class StringChanger {
  public static String camelToSnake(String camelCase) {
    if (camelCase == null || camelCase.isEmpty()) {
      return camelCase;
    }

    return camelCase.replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase();
  }

  public static String snakeToCamel(String snakeCase) {
    if (snakeCase == null || snakeCase.isEmpty()) {
      return snakeCase;
    }

    String[] parts = snakeCase.split("_");
    StringBuilder camelCase = new StringBuilder(parts[0].toLowerCase());
    for (int i = 1; i < parts.length; i++) {
      camelCase.append(parts[i].substring(0, 1).toUpperCase())
          .append(parts[i].substring(1).toLowerCase());
    }
    return camelCase.toString();
  }

  public static List<String> splittingString(String words) {
    int maxLineLength = 25;
    List<String> loreLines = new ArrayList<>();

    // 以空格切分单词
    String[] wordStrings = words.split(" ");

    StringBuilder currentLine = new StringBuilder();
    for (String word : wordStrings) {
      // 如果当前行加上下一个单词的长度超过限制，先保存当前行
      if (currentLine.length() + word.length() + 1 > maxLineLength) {
        loreLines.add(currentLine.toString());
        currentLine = new StringBuilder();
      }
      // 添加单词到当前行
      if (!currentLine.isEmpty()) {
        currentLine.append(" "); // 添加空格分隔
      }
      currentLine.append(word);
    }

    // 添加最后一行
    if (!currentLine.isEmpty()) {
      loreLines.add(currentLine.toString());
    }

    return loreLines;
  }
}
