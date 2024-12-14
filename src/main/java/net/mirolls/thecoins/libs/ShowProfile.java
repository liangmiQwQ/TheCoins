package net.mirolls.thecoins.libs;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.mirolls.thecoins.TheCoins;
import net.mirolls.thecoins.database.SkyBlockDB;
import net.mirolls.thecoins.database.TheCoinsDB;
import net.mirolls.thecoins.skyblock.Profile;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class ShowProfile {
  String profileName;
  String profileID;
  ArrayList<String> playerNames;
  Boolean playing;

  public ShowProfile(String profileName, String profileID, ArrayList<String> playerNames, Boolean playing) {
    this.profileName = profileName;
    this.profileID = profileID;
    this.playerNames = playerNames;
    this.playing = playing;
  }

  public static ArrayList<ShowProfile> getShowProfiles(Profile profile, MinecraftServer server) {
    return getShowProfiles(Objects.requireNonNull(server.getPlayerManager().getPlayer(UUID.fromString(profile.playerUUID()))));
  }

  public static ArrayList<ShowProfile> getShowProfiles(ServerPlayerEntity player) {
    String SQL = SQLExecutor.selectSQL(
        TheCoinsDB.PROFILE_TABLE_NAME,
        List.of("profileID", "profileName", "playerUUID", "coins", "enderChestInventory", "inventory", "playing"), SkyBlockDB.connection,
        "`profileID` IN ("
            + SQLExecutor.selectSQLNoEnd(TheCoinsDB.PROFILE_TABLE_NAME, List.of("profileID"),
            SkyBlockDB.connection,
            "`playerUUID`=?") + // 获取这个玩家下所有的档案
            ")"
    );
//    TheCoins.LOGGER.debug(SQL);

    try {
      PreparedStatement preparedStatement = SkyBlockDB.connection.prepareStatement(SQL);
      preparedStatement.setString(1, player.getUuidAsString());
      ResultSet result = preparedStatement.executeQuery();

      Map<String, ShowProfile> profileMap = new HashMap<>();
      ArrayList<ShowProfile> returnProfiles = new ArrayList<>();

      while (result.next()) {
        String profileID = result.getString("profileID");
        Profile profile = new Profile(
            result.getString("profileID"),
            result.getString("profileName"),
            result.getString("playerUUID"),
            result.getDouble("coins"),
            result.getString("enderChestInventory"),
            result.getString("inventory"),
            result.getBoolean("playing")
        );

        ShowProfile targetProfile = profileMap.get(profileID);

        if (targetProfile == null) {
          targetProfile = new ShowProfile(
              result.getString("profileName"),
              profileID,
              new ArrayList<>(),
              false
          );
          profileMap.put(profileID, targetProfile);
          returnProfiles.add(targetProfile);
        }

        // 更新玩家列表和游玩状态
        String playerName = Objects.requireNonNull(Objects.requireNonNull(player.getServer())
            .getPlayerManager()
            .getPlayer(UUID.fromString(profile.playerUUID()))).getName().getString();

//        if (playerName != null) {
        targetProfile.getPlayerNames().add(playerName);
//        }

        if (Objects.equals(profile.playerUUID(), player.getUuidAsString())) {
          targetProfile.setPlaying(profile.playing());
        }
      }
      // `returnProfiles` 和 `profiles` 都已完整处理

      // it's difficult for me to encode, so I note it in Chinese
      // I feel I am encoding in Rust🦀
      // thanks for using translate apps

      result.close();
      preparedStatement.close();
      return returnProfiles;
    } catch (SQLException e) {
      TheCoins.LOGGER.error("Cannot get ShowProfiles from profile");
      throw new RuntimeException(e);
    }
  }

  public Boolean getPlaying() {
    return playing;
  }

  public void setPlaying(Boolean playing) {
    this.playing = playing;
  }

  public String getProfileID() {
    return profileID;
  }

  public void setProfileID(String profileID) {
    this.profileID = profileID;
  }

  public String getProfileName() {
    return profileName;
  }

  public void setProfileName(String profileName) {
    this.profileName = profileName;
  }

  public ArrayList<String> getPlayerNames() {
    return playerNames;
  }

  public void setPlayerNames(ArrayList<String> playerNames) {
    this.playerNames = playerNames;
  }
}
