package net.mirolls.thecoins.file;

import java.util.HashMap;

public class Translation {
  private final String lang;
  private HashMap<String, String> cachedTranslation;

  public Translation(String lang) {
    this.lang = lang;
  }

  private static void initTranslation() {
    if (!MinecraftFile.isFileExists("skyblock", "zh_cn.json")) {
      MinecraftFile.createFile("skyblock", "zh_cn.json");
    }

    if (!MinecraftFile.isFileExists("skyblock", "en_us.json")) {
      MinecraftFile.createFile("skyblock", "en_us.json");
    }
  }

  public static void initTranslationAtFirst() {
    if (MinecraftFile.isFileExists("skyblock", "zh_cn.json")) {
      MinecraftFile.removeFile("skyblock", "zh_cn.json");
    }

    if (MinecraftFile.isFileExists("skyblock", "en_us.json")) {
      MinecraftFile.removeFile("skyblock", "en_us.json");
    }

    MinecraftFile.createFile("skyblock", "en_us.json");
    MinecraftFile.createFile("skyblock", "zh_cn.json");
  }

  public String getTranslation(String translateID) {
    initTranslation();

    if (cachedTranslation == null) {
      cachedTranslation = MinecraftFile.getJSON("skyblock", lang + ".json");
    }
    if (!cachedTranslation.isEmpty()) {
      return cachedTranslation.get(translateID);
    } else {
      return "null";
    }
  }
}
