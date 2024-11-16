package net.mirolls.thecoins.libs;

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
}
