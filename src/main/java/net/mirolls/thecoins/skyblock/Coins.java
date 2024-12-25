package net.mirolls.thecoins.skyblock;

import net.minecraft.server.network.ServerPlayerEntity;
import net.mirolls.thecoins.TheCoins;
import net.mirolls.thecoins.database.SkyBlockDB;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import static net.mirolls.thecoins.database.thecoins.TheCoinsDBCreator.PROFILE_TABLE_NAME;

public class Coins {
  public static void addCoins(ServerPlayerEntity player, Double modification) {
    updateDBCoins("UPDATE " + PROFILE_TABLE_NAME + " SET " +
        "`coins`=`coins`+?" +
        " WHERE `playerUUID`=? AND `playing`=?;", player, modification);

  }

  public static void setCoins(ServerPlayerEntity player, Double coins) {
    updateDBCoins("UPDATE " + PROFILE_TABLE_NAME + " SET " +
        "`coins`=?" +
        " WHERE `playerUUID`=? AND `playing`=?;", player, coins);
  }

  public static void increaseCoins(ServerPlayerEntity player, Double modification) {
    addCoins(player, -modification);
  }

  public static void clearCoins(ServerPlayerEntity player) {
    addCoins(player, 0.0);
  }

  private static void updateDBCoins(String SQL, ServerPlayerEntity player, Double modification) {
    try {
      PreparedStatement preparedStatement = SkyBlockDB.connection.prepareStatement(SQL);

      preparedStatement.setDouble(1, modification);
      preparedStatement.setString(2, player.getUuidAsString());
      preparedStatement.setBoolean(3, true);

      preparedStatement.executeUpdate();

      preparedStatement.close();
    } catch (SQLException e) {
      TheCoins.LOGGER.info("Cannot update player's coins data");
      throw new RuntimeException(e);
    }
  }
}
