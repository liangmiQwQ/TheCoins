package net.mirolls.thecoins.database.thecoins;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.mirolls.thecoins.TheCoins;
import net.mirolls.thecoins.database.SkyBlockDB;
import net.mirolls.thecoins.libs.inventory.InventoryTransfer;
import net.mirolls.thecoins.skyblock.Profile;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;

import static net.mirolls.thecoins.database.thecoins.TheCoinsDBCreator.PROFILE_TABLE_NAME;

public class TheCoinsDBUpdater {
  public static int UpdateProfile(ServerPlayerEntity player) {
    String SQL = "UPDATE " + PROFILE_TABLE_NAME + " SET " +
        "`enderChestInventory`=?, `inventory`=?, `exp`=?, `location`=?, `respawnLocation`=?" +
        " WHERE `playerUUID`=? AND `playing`=?;";

    String playerLocation =
        "[" + player.getWorld().getRegistryKey().getValue().toString()
            + "]&("
            + player.getX() + "," + player.getY() + "," + player.getZ() + ")";

    BlockPos playerSpawnPointPosition = player.getSpawnPointPosition();

    String playerRespawn = "UNKNOWN";
    if (playerSpawnPointPosition != null) {
      playerRespawn =
          "[" + player.getSpawnPointDimension().getValue().toString()
              + "]&("
              + playerSpawnPointPosition.getX() + "," + playerSpawnPointPosition.getY() + "," + playerSpawnPointPosition.getZ() + ")";
    }

    try {
      PreparedStatement preparedStatement = SkyBlockDB.connection.prepareStatement(SQL);
      preparedStatement.setString(1, InventoryTransfer.enderChestInventoryAsJSON(player.getEnderChestInventory()));
      preparedStatement.setString(2, InventoryTransfer.playerInventoryAsJSON(player.getInventory()));
      preparedStatement.setInt(3, player.totalExperience);
      preparedStatement.setString(4, playerLocation);
      preparedStatement.setString(5, playerRespawn);
      preparedStatement.setString(5, player.getUuidAsString());
      preparedStatement.setBoolean(6, true);

      int result = preparedStatement.executeUpdate();
      preparedStatement.close();
      return result;
    } catch (SQLException e) {
      TheCoins.LOGGER.error("Cannot UPDATE the database when update one's EnderChestInventory " + e);
      throw new RuntimeException(e);
    }

  }

  public static Profile swapProfile(ServerPlayerEntity player, String profileID) {
    ArrayList<Profile> profiles = TheCoinsDBCreator.getProfilesPlayer(player);

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

    // 可以继续往下写了

    return targetProfile;
  }
}
