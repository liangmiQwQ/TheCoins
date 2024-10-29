package net.mirolls.thecoins.file;

import java.util.HashMap;

public class Translation {
  private final String lang;
  private HashMap<String, String> cachedTranslation;

  Translation(String lang) {
    this.lang = lang;
  }

  public static void initTranslation() {
    if (!MinecraftFile.isFileExists("skyblock", "zh_cn.json")) {
      MinecraftFile.createFile("skyblock", "zh_cn.json");
    }

    if (!MinecraftFile.isFileExists("skyblock", "en_us.json")) {
      MinecraftFile.createFile("skyblock", "en_us.json");
    }
  }

  public String getTranslation(String translateID) {
    initTranslation();

    if (cachedTranslation.isEmpty()) {
      cachedTranslation = MinecraftFile.getJSON("skyblock", lang + ".json");
    }
    if (cachedTranslation != null) {
      return cachedTranslation.get(translateID);
    } else {
      return "";
    }
  }
}
