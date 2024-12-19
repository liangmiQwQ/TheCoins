package net.mirolls.thecoins.libs;

import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.mirolls.thecoins.TheCoins;

import java.util.Objects;
import java.util.regex.Pattern;

public class StringLocation {

  public static String encodeLocationAsString(ServerPlayerEntity player) {
    TheCoins.LOGGER.info("location: " + player.getX() + " | " + player.getY() + " | " + player.getZ());

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
}
