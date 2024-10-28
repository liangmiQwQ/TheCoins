package net.mirolls.thecoins.file;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.mirolls.thecoins.TheCoins;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class Translation {
  private final String lang;
  private HashMap<String, String> cachedTranslation;

  Translation(String lang) {
    this.lang = lang;
  }

  private static void createTranslationFile() {
    String defaultZhCnConfigFile = ResourceReader.readResourceAsString("zh_cn.json");
    String defaultEnUsConfigFile = ResourceReader.readResourceAsString("en_us.json");

    File skyBlockDir = new File(new File("."), "skyblock");

    if (!skyBlockDir.exists() && !skyBlockDir.mkdirs()) {
      TheCoins.LOGGER.error("[BetterTips|ERROR] Can't create folder to save the config file.");
      return;
    }

    File zhCnFile = new File(skyBlockDir, "zh_cn.yaml");
    File enUsFile = new File(skyBlockDir, "en_us.json");

    if (!zhCnFile.exists()) {
      try {
        if (zhCnFile.createNewFile() && defaultZhCnConfigFile != null) {
          try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(zhCnFile), StandardCharsets.UTF_8))) {
            writer.write(defaultZhCnConfigFile);
          } catch (IOException e) {
            TheCoins.LOGGER.error("Failed to write to config file.", e);
          }
        } else {
          if (defaultZhCnConfigFile == null) {
            TheCoins.LOGGER.error("Internal error, cannot read default file");
          } else {
            TheCoins.LOGGER.error("Can't create file to save the config.");
          }
        }
      } catch (IOException e) {
        TheCoins.LOGGER.error("An error occurred while creating the config file.", e);
      }
    }

    if (!enUsFile.exists()) {
      try {
        if (enUsFile.createNewFile() && defaultEnUsConfigFile != null) {
          try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(enUsFile), StandardCharsets.UTF_8))) {
            writer.write(defaultEnUsConfigFile);
          } catch (IOException e) {
            TheCoins.LOGGER.error("Failed to write to config file.", e);
          }
        } else {
          if (defaultEnUsConfigFile == null) {
            TheCoins.LOGGER.error("Internal error, cannot read default file");
          } else {
            TheCoins.LOGGER.error("Can't create file to save the config.");
          }
        }
      } catch (IOException e) {
        TheCoins.LOGGER.error("An error occurred while creating the config file.", e);
      }
    }
  }

  private static boolean isTranslationFileHere() {
    File bettertipsFolder = new File(new File("."), "skyblock");
    if (!bettertipsFolder.exists()) {
      return false;
    }
    File zhCnFile = new File(bettertipsFolder, "zh_cn.json");
    File enUsFile = new File(bettertipsFolder, "en_us.json");
    return zhCnFile.exists() && enUsFile.exists();
  }

  public static void initTranslation() {
    if (!isTranslationFileHere()) {
      // 如果没有则创建，反正保证的就是一个有文件
      createTranslationFile();
    }
  }

  private static HashMap<String, String> getTranslationFile(String filename) throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();

    return objectMapper.readValue(
        new File(new File(new File("."), "skyblock"), filename),
        new TypeReference<>() {
        }
    );
  }

  public String getTranslation(String translateID) {
    initTranslation();

    if (cachedTranslation.isEmpty()) {
      try {
        cachedTranslation = getTranslationFile(lang + ".json");
      } catch (IOException e) {
        TheCoins.LOGGER.error("Cannot read language file" + e);
      }
    }
    return cachedTranslation.get(translateID);
  }
}
