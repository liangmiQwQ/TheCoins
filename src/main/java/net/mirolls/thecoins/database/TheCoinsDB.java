package net.mirolls.thecoins.database;

import net.mirolls.thecoins.libs.SQLExecutor;
import net.mirolls.thecoins.skyblock.Profile;

public class TheCoinsDB {
  static final String PROFILE_TABLE_NAME = "playerProfile";

  public static void createPlayerProfileTable() {
    SkyBlockDB.createTable(PROFILE_TABLE_NAME, new DBKey[]{
        new DBKey("profileID", "TEXT", true),
        new DBKey("profileName", "TEXT", true),
        new DBKey("playerUUID", "TEXT", true), // use json
        new DBKey("coins", "INTEGER", true),
        new DBKey("enderChestInventory", "TEXT", true), // use NBT tags
        new DBKey("inventory", "TEXT", true) // NBT tags
    });
  }
  // coins表就不需要了 因为profile里已经包含了coins信息

  public static void createProfileForPlayer(Profile playerProfile) {
    SQLExecutor.insert(PROFILE_TABLE_NAME, playerProfile.asHashMap(), SkyBlockDB.connection);
  }
}
