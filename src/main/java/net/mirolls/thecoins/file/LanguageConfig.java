package net.mirolls.thecoins.file;

import java.util.HashMap;

import static net.mirolls.thecoins.TheCoins.LANGUAGES_LIST;

public class LanguageConfig {

  public static void initLanguageConfigFile() {
    if (!MinecraftFile.isFileExists("skyblock", "language.json")) {
      MinecraftFile.createFile("skyblock", "language.json");
    }
  }

  public static String getPlayerLanguage(String playerUUID) {
    initLanguageConfigFile();
    HashMap<String, String> playerLanguageMap = MinecraftFile.getJSON("skyblock", "language.json");
    if (playerLanguageMap != null) {
      if (playerLanguageMap.containsKey(playerUUID)) {
        return playerLanguageMap.get(playerUUID); // attention: there is UUID
      } else {
        setPlayerLanguage(playerUUID, "en_us");
        return "en_us";
      }
    } else {
      setPlayerLanguage(playerUUID, "en_us");
      return "en_us";
    }
  }

  public static void setPlayerLanguage(String playerUUID, String targetLanguage) {
    initLanguageConfigFile();

    HashMap<String, String> playerLanguageMap = MinecraftFile.getJSON("skyblock", "language.json");
    if (playerLanguageMap != null && LANGUAGES_LIST.contains(targetLanguage)) {
      playerLanguageMap.put(playerUUID, targetLanguage);
    } else {
      throw new Error("Unknown Language");
    }

    MinecraftFile.writeJSON("skyblock", "language.json", playerLanguageMap);
  }
}
