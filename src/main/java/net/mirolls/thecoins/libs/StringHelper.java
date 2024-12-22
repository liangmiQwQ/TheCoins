package net.mirolls.thecoins.libs;

import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

public class StringHelper {

  public static String encodeHungryAsString(ServerPlayerEntity player) {
    return player.getHungerManager().getFoodLevel() + "|"
        + player.getHungerManager().getSaturationLevel() + "|"
        + player.getHungerManager().getExhaustion();
  }

  public static String encodeLocationAsString(ServerPlayerEntity player) {
//    TheCoins.LOGGER.info("location: " + player.getX() + " | " + player.getY() + " | " + player.getZ());

    return
        "[" + player.getWorld().getRegistryKey().getValue().toString()
            + "]&("
            + player.getX() + "," + player.getY() + "," + player.getZ() + ")";
  }

  public static String encodeRespawnAsString(ServerPlayerEntity player) {
    BlockPos playerSpawnPointPosition = player.getSpawnPointPosition();

    if (playerSpawnPointPosition != null) {
      return
          "[" + player.getSpawnPointDimension().getValue().toString()
              + "]&("
              + playerSpawnPointPosition.getX() + "," + playerSpawnPointPosition.getY() + "," + playerSpawnPointPosition.getZ() + ")";
    } else {
      return "UNKNOWN";
    }
  }

  public static void setHungryAsString(ServerPlayerEntity player, String hunger) {
    String[] hungerData = hunger.split(Pattern.quote("|"));

    player.getHungerManager().setFoodLevel(Integer.getInteger(hungerData[0]));
    player.getHungerManager().setSaturationLevel(Float.parseFloat(hungerData[1]));
    player.getHungerManager().setExhaustion(Float.parseFloat(hungerData[2]));
  }

  public static void setLocationFromString(ServerPlayerEntity player, String location) {
    // 分解location
    String[] locationStrings = location.split(Pattern.quote("&"));

    // 纬度
    String dimensionKey = locationStrings[0].substring(1, locationStrings[0].length() - 1);
    Identifier identifier = new Identifier(dimensionKey);
    // 创建 RegistryKey<World>
    RegistryKey<World> keyWorld = RegistryKey.of(RegistryKeys.WORLD, identifier);
    ServerWorld serverWorld = Objects.requireNonNull(player.getServer()).getWorld(keyWorld);

    // 坐标
    String[] XYZ = locationStrings[1].substring(1, locationStrings[1].length() - 1).split(Pattern.quote(","));

    player.teleport(serverWorld, Double.parseDouble(XYZ[0]), Double.parseDouble(XYZ[1]), Double.parseDouble(XYZ[2]), 0.0f, 0.0f);
  }

  public static void setRespawnFromString(ServerPlayerEntity player, String location) {
    if (Objects.equals(location, "UNKNOWN")) {
      player.setSpawnPoint(
          World.OVERWORLD,
          null,
          0.0f,
          false,
          false
      );
      return;
    }

    // 分解location
    String[] locationStrings = location.split(Pattern.quote("&"));

    // 纬度
    String dimensionKey = locationStrings[0].substring(1, locationStrings[0].length() - 1);
    Identifier identifier = new Identifier(dimensionKey);
    // 创建 RegistryKey<World>
    RegistryKey<World> keyWorld = RegistryKey.of(RegistryKeys.WORLD, identifier);

    // 坐标
    String[] XYZ = locationStrings[1].substring(1, locationStrings[1].length() - 1).split(Pattern.quote(","));

//    player.teleport(serverWorld, Double.parseDouble(XYZ[0]), Double.parseDouble(XYZ[1]), Double.parseDouble(XYZ[2]), 0.0f, 0.0f);
    player.setSpawnPoint(
        keyWorld,
        BlockPos.ofFloored(Double.parseDouble(XYZ[0]), Double.parseDouble(XYZ[1]), Double.parseDouble(XYZ[2])),
        0.0f,
        false,
        false
    );
  }

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
