package net.mirolls.thecoins.file;

import java.util.HashMap;

public class LanguageConfig {
  private HashMap<String, String> cachedPlayerLang;

  public static void initLanguageConfigFile() {
    if (!MinecraftFile.isFileExists("skyblock", "language.json")) {
      MinecraftFile.createFile("skyblock", "language.json");
    }
  }

}
