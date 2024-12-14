package net.mirolls.thecoins.database.thecoins;

import net.minecraft.server.network.ServerPlayerEntity;
import net.mirolls.thecoins.TheCoins;
import net.mirolls.thecoins.database.DBKey;
import net.mirolls.thecoins.database.SkyBlockDB;
import net.mirolls.thecoins.libs.SQLExecutor;
import net.mirolls.thecoins.skyblock.Profile;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TheCoinsDBCreator {
  public static final String PROFILE_TABLE_NAME = "playerProfile";

  public static void createPlayerProfileTable() {
    SkyBlockDB.createTable(PROFILE_TABLE_NAME, new DBKey[]{
        new DBKey("profileID", "TEXT", true),
        new DBKey("profileName", "TEXT", true),
        new DBKey("playerUUID", "TEXT", true), // 这里我采取一人一个 coop 的话允许填写同一个 profileID
        new DBKey("coins", "INTEGER", true),
        new DBKey("exp", "INTEGER", true),
        new DBKey("location", "TEXT", true),
        new DBKey("respawnLocation", "TEXT", false),
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
        List.of("profileID", "profileName", "playerUUID", "coins", "exp", "location", "respawnLocation", "enderChestInventory", "inventory", "playing"), SkyBlockDB.connection,
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
            result.getInt("exp"),
            result.getString("location"),
            result.getString("respawnLocation"),
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

}
