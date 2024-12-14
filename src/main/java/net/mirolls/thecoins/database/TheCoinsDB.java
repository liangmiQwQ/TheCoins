package net.mirolls.thecoins.database;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.EnderChestInventory;
import net.minecraft.server.network.ServerPlayerEntity;
import net.mirolls.thecoins.TheCoins;
import net.mirolls.thecoins.libs.SQLExecutor;
import net.mirolls.thecoins.libs.inventory.InventoryTransfer;
import net.mirolls.thecoins.skyblock.Profile;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TheCoinsDB {
  public static final String PROFILE_TABLE_NAME = "playerProfile";

  public static void createPlayerProfileTable() {
    SkyBlockDB.createTable(PROFILE_TABLE_NAME, new DBKey[]{
        new DBKey("profileID", "TEXT", true),
        new DBKey("profileName", "TEXT", true),
        new DBKey("playerUUID", "TEXT", true), // 这里我采取一人一个 coop 的话允许填写同一个 profileID
        new DBKey("coins", "INTEGER", true),
        new DBKey("enderChestInventory", "TEXT", true), // use NBT tags
        new DBKey("inventory", "TEXT", true), // NBT tags
        new DBKey("playing", "BOOLEAN", true)
    });
  }
  // coins表就不需要了 因为profile里已经包含了coins信息

  public static void createProfileForPlayer(Profile playerProfile) {
    SQLExecutor.insert(PROFILE_TABLE_NAME, playerProfile.asHashMap(), SkyBlockDB.connection);
  }

  public static ArrayList<Profile> getProfilesPlayer(ServerPlayerEntity player) {
    String SQL = SQLExecutor.selectSQL(
        PROFILE_TABLE_NAME,
        List.of("profileID", "profileName", "playerUUID", "coins", "enderChestInventory", "inventory", "playing"), SkyBlockDB.connection,
        "`playerUUID`=?"
    );

    ArrayList<Profile> profiles = new ArrayList<>();
    try {
      PreparedStatement preparedStatement = SkyBlockDB.connection.prepareStatement(SQL);
      String playerUUID = player.getUuidAsString();
      preparedStatement.setString(1, playerUUID);
      ResultSet result = preparedStatement.executeQuery();

      while (result.next()) {
        profiles.add(new Profile(
            result.getString("profileID"),
            result.getString("profileName"),
            result.getString("playerUUID"),
            result.getDouble("coins"),
            result.getString("enderChestInventory"),
            result.getString("inventory"),
            result.getBoolean("playing")
        ));
      }

      result.close();
      preparedStatement.close();
    } catch (SQLException e) {
      TheCoins.LOGGER.error("Cannot query Players SQL");
      throw new RuntimeException(e);
    }
    return profiles;
  }

  public static Profile getOrCreateProfilePlaying(ServerPlayerEntity player) {
    ArrayList<Profile> profiles = getProfilesPlayer(player);
    Profile profile = null;
    if (profiles.isEmpty()) {
      // 没有就创建
      profile = Profile.generateProfile(player, true);
      createProfileForPlayer(profile);
    } else {
      for (Profile oneProfile : profiles) {
        if (oneProfile.playing()) {
          if (profile == null) {
            profile = oneProfile;
          } else {
            TheCoins.LOGGER.error("Cannot to get playing Profile for " + player.getName().getString());
            throw new RuntimeException("Player " + player.getName().getString() + " has a lot of Profiles but no one is playing now");
          }
        }
      }

      if (profile == null) {
        TheCoins.LOGGER.warn("Cannot to get Profile " + player.getName().getString() + " playing, creating a new profile");
        profile = Profile.generateProfile(player, true);
        createProfileForPlayer(profile);
      }
    }

    return profile;
  }

  public static int UpdateProfileEnderChestInventory(EnderChestInventory enderChestInventory, String playerUUID, Boolean playing) {
    String SQL = "UPDATE " + PROFILE_TABLE_NAME + " SET `enderChestInventory`=? WHERE `playerUUID`=? AND `playing`=?;";
    try {
      PreparedStatement preparedStatement = SkyBlockDB.connection.prepareStatement(SQL);
      preparedStatement.setString(1, InventoryTransfer.enderChestInventoryAsJSON(enderChestInventory));
      preparedStatement.setString(2, playerUUID);
      preparedStatement.setBoolean(3, playing);
      int result = preparedStatement.executeUpdate();
      preparedStatement.close();
      return result;
    } catch (SQLException e) {
      TheCoins.LOGGER.error("Cannot UPDATE the database when update one's EnderChestInventory " + e);
      throw new RuntimeException(e);
    }
  }

  public static int UpdateProfilePlayerInventory(PlayerInventory playerInventory, String playerUUID, Boolean playing) {
    String SQL = "UPDATE " + PROFILE_TABLE_NAME + " SET `inventory` = ? WHERE `playerUUID`=? AND `playing`=?;";
    try {
      PreparedStatement preparedStatement = SkyBlockDB.connection.prepareStatement(SQL);
      preparedStatement.setString(1, InventoryTransfer.playerInventoryAsJSON(playerInventory));
      preparedStatement.setString(2, playerUUID);
      preparedStatement.setBoolean(3, playing);
      int result = preparedStatement.executeUpdate();
      preparedStatement.close();
      return result;
    } catch (SQLException e) {
      TheCoins.LOGGER.error("Cannot UPDATE the database when update one's EnderChestInventory " + e);
      throw new RuntimeException(e);
    }
  }

  public static int UpdateProfile(ServerPlayerEntity player) {
    return TheCoinsDB.UpdateProfilePlayerInventory(player.getInventory(), player.getUuidAsString(), true)
        + TheCoinsDB.UpdateProfileEnderChestInventory(player.getEnderChestInventory(), player.getUuidAsString(), true);
  }

  public static Profile swapProfile(ServerPlayerEntity player, String profileID) {
    ArrayList<Profile> profiles = getProfilesPlayer(player);

    Profile targetProfile = null;
    for (Profile profile : profiles) {
      if (Objects.equals(profile.profileID(), profileID)) {
        // if the profile id equals each other
        // find the target profile
        targetProfile = profile;
        break;
      }
    }

    if (targetProfile == null) {
      TheCoins.LOGGER.error("Cannot swap the profile because couldn't find the profile that id equals " + profileID);
      throw new RuntimeException("Cannot swap the profile because couldn't find the profile that id equals " + profileID);
    }

    // 1. Save the profile playing now
    UpdateProfile(player);

    // clear the inventory
    player.getInventory().clear();
    player.getEnderChestInventory().clear();

    // 突然意识到没办法往下写了 因为player他的位置 xp 重生点都没存储 得稍微修改一下profile

    return targetProfile;
  }
}
