package net.mirolls.thecoins.file;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.mirolls.thecoins.TheCoins;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class MinecraftFile {
  public static void createFile(String dirPath, String filename) {
    String defaultFile = ResourceReader.readResourceAsString(filename);

    // create final dir to save the last dir
    File dirPathFile = new File(new File("."), dirPath);


    if (!dirPathFile.exists() && !dirPathFile.mkdirs()) {
      TheCoins.LOGGER.error("Can't create folder to save the config file.");
      return;
    }

    File fileFile = new File(dirPathFile, filename);

    if (!fileFile.exists()) {
      try {
        if (fileFile.createNewFile() && defaultFile != null) {
          try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileFile), StandardCharsets.UTF_8))) {
            writer.write(defaultFile);
          } catch (IOException e) {
            TheCoins.LOGGER.error("Failed to write to config file.", e);
          }
        } else {
          if (defaultFile == null) {
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

  public static boolean isFileExists(String dirPath, String filename) {
    File dirFile = new File(new File("."), dirPath);
    if (!dirFile.exists()) {
      return false;
    }
    File fileFile = new File(dirFile, filename);
    return fileFile.exists();
  }

  public static HashMap<String, String> getJSON(String dirPath, String filename) {
    ObjectMapper objectMapper = new ObjectMapper();

    try {
      return objectMapper.readValue(
          new File(new File(new File("."), dirPath), filename),
          new TypeReference<>() {
          }
      );
    } catch (IOException e) {
      TheCoins.LOGGER.error("Cannot get " + filename + " in " + dirPath + e);
      throw new Error("Cannot read the file");
    }
  }

  public static void writeJSON(String dirPath, String filename, HashMap<String, String> data) {
    ObjectMapper objectMapper = new ObjectMapper();

    try {
      File file = new File(new File(new File("."), dirPath), filename);
      file.getParentFile().mkdirs();

      objectMapper.writeValue(file, data);

    } catch (IOException e) {
      TheCoins.LOGGER.error("Cannot write to " + filename + " in " + dirPath + " - " + e);
      throw new Error("Cannot write the file");
    }
  }

}
