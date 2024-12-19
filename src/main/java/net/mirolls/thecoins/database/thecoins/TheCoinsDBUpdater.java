package net.mirolls.thecoins.database.thecoins;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.mirolls.thecoins.TheCoins;
import net.mirolls.thecoins.database.SkyBlockDB;
import net.mirolls.thecoins.file.LanguageConfig;
import net.mirolls.thecoins.file.Translation;
import net.mirolls.thecoins.item.Menu;
import net.mirolls.thecoins.libs.MinecraftColor;
import net.mirolls.thecoins.libs.StringLocation;
import net.mirolls.thecoins.libs.inventory.InventoryTransfer;
import net.mirolls.thecoins.skyblock.Profile;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.regex.Pattern;

import static net.mirolls.thecoins.database.thecoins.TheCoinsDBCreator.PROFILE_TABLE_NAME;

public class TheCoinsDBUpdater {
  public static int updateProfile(ServerPlayerEntity player) {
    String SQL = "UPDATE " + PROFILE_TABLE_NAME + " SET " +
        "`enderChestInventory`=?, `inventory`=?, `exp`=?, `location`=?, `respawnLocation`=?" +
        " WHERE `playerUUID`=? AND `playing`=?;";

    try {
      PreparedStatement preparedStatement = SkyBlockDB.connection.prepareStatement(SQL);
      preparedStatement.setString(1, InventoryTransfer.enderChestInventoryAsJSON(player.getEnderChestInventory()));
      preparedStatement.setString(2, InventoryTransfer.playerInventoryAsJSON(player.getInventory()));
      preparedStatement.setInt(3, player.totalExperience);
      preparedStatement.setString(4, StringLocation.encodeLocationAsString(player));
      preparedStatement.setString(5, StringLocation.encodeRespawnAsString(player));
      preparedStatement.setString(6, player.getUuidAsString());
      preparedStatement.setBoolean(7, true);

      int result = preparedStatement.executeUpdate();
      preparedStatement.close();
      return result;
    } catch (SQLException e) {
      TheCoins.LOGGER.error("Cannot UPDATE the database when update one's EnderChestInventory " + e);
      throw new RuntimeException(e);
    }
  }

  public static int updateProfilePlaying(String playerUUID, String targetProfileID) {
    String SQL = "UPDATE " + PROFILE_TABLE_NAME + " SET " +
        "`playing`=?" +
        " WHERE `playerUUID`=? AND `playing`=?;";

    int result1;
    try {
      PreparedStatement preparedStatement = SkyBlockDB.connection.prepareStatement(SQL);
      preparedStatement.setBoolean(1, false);
      preparedStatement.setString(2, playerUUID);
      preparedStatement.setBoolean(3, true);

      result1 = preparedStatement.executeUpdate();

      preparedStatement.close();
    } catch (SQLException e) {
      TheCoins.LOGGER.error("Cannot UPDATE the database when update one's EnderChestInventory " + e);
      throw new RuntimeException(e);
    }


    String SQL1 = "UPDATE " + PROFILE_TABLE_NAME + " SET " +
        "`playing`=?" +
        " WHERE `profileID`=? AND `playerUUID`=?;";

    try {
      PreparedStatement preparedStatement = SkyBlockDB.connection.prepareStatement(SQL1);
      preparedStatement.setBoolean(1, true);
      preparedStatement.setString(2, targetProfileID);
      preparedStatement.setString(3, playerUUID);

      int result = preparedStatement.executeUpdate() + result1;
      preparedStatement.close();
      return result;
    } catch (SQLException e) {
      TheCoins.LOGGER.error("Cannot UPDATE the database when update one's EnderChestInventory " + e);
      throw new RuntimeException(e);
    }
  }

  public static Profile swapProfile(ServerPlayerEntity player, String targetProfileID) {
    ArrayList<Profile> profiles = TheCoinsDBCreator.getProfilesPlayer(player);

    Profile targetProfile = null;
    for (Profile profile : profiles) {
      if (Objects.equals(profile.profileID(), targetProfileID)) {
        // if the profile id equals each other
        // find the target profile
        targetProfile = profile;
        break;
      }
    }

    if (targetProfile == null) {
      TheCoins.LOGGER.error("Cannot swap the profile because couldn't find the profile that id equals " + targetProfileID);
      throw new RuntimeException("Cannot swap the profile because couldn't find the profile that id equals " + targetProfileID);
    }

    // 1. Save the profile playing now
    updateProfile(player);

    // clear the inventory
    player.getInventory().clear();
    player.getEnderChestInventory().clear();

    player.closeHandledScreen();

    // change the database 在数据库层面继续更改
    updateProfilePlaying(player.getUuidAsString(), targetProfileID);

    // change the exp location respawnLocation 在玩家层面进行修改
    player.totalExperience = targetProfile.exp();
    // 解析坐标
    StringLocation.setLocationFromString(player, targetProfile.location());
    StringLocation.setRespawnFromString(player, targetProfile.respawnLocation());

    Menu.replaceMenu(player);

    // 发送提示消息
    // 如果玩家进入的时候没有profile则首先创建profile
    Translation translation = new Translation(LanguageConfig.getPlayerLanguage(player.getUuidAsString()));

    String[] playingMessage = translation.getTranslation("YouAreNowPlaying").split(Pattern.quote("${}"));

    player.sendMessage(
        Text.literal(
                playingMessage[0]
            ).setStyle(Style.EMPTY.withColor(MinecraftColor.hexToRgb("#55FF55")))
            .append(Text.literal(
                targetProfile.profileName()
            ).setStyle(Style.EMPTY.withColor(MinecraftColor.hexToRgb("#FFAA00"))))
            .append(Text.literal(
                playingMessage[1]
            ).setStyle(Style.EMPTY.withColor(MinecraftColor.hexToRgb("#55FF55"))))
    );

    // 额外send一个profileID
    player.sendMessage(
        Text.literal(
            translation.getTranslation("YourProfileID").replace("${}", targetProfile.profileID())
        ).setStyle(Style.EMPTY.withColor(MinecraftColor.hexToRgb("#AAAAAA")))
    );

    return targetProfile;
  }
}
