package net.mirolls.thecoins.file;

import net.mirolls.thecoins.TheCoins;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class Translation {
  private static Map<String, String> cachedTranslation;
  private static Map<String, String> cachedPlayerConfig;

  private static void createConfig() {
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

}
