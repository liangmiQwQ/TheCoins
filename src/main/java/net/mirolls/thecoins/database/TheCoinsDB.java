package net.mirolls.thecoins.database;

import net.mirolls.thecoins.libs.SQLExecutor;

import java.util.HashMap;
import java.util.UUID;

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

  public static void createProfileForPlayer() {
    HashMap<String, Object> data = new HashMap<>();

    data.put("profileID", UUID.randomUUID().toString());
    data.put("profileName", "");
    data.put("playerUUID", "");
    data.put("coins", "");
    data.put("enderChestInventory", "");
    data.put("inventory", "");

    SQLExecutor.insert(PROFILE_TABLE_NAME, data, SkyBlockDB.connection);
  }
}
