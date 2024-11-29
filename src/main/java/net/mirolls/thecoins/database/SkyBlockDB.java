package net.mirolls.thecoins.database;

import net.mirolls.thecoins.TheCoins;
import net.mirolls.thecoins.file.MinecraftFile;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SkyBlockDB {
  private static Connection connection;

  public static void initSQLite() {
    if (!MinecraftFile.isFileExists("skyblock", "SkyBlock.db")) {
      // 链接数据库 不存在则自动创建
      try {
        Connection conn = DriverManager.getConnection("jdbc:sqlite:skyblock/SkyBlock.db");
        if (conn != null) connection = conn;
        // 创建链接
      } catch (SQLException e) {
        throw new RuntimeException(e);
      }
    }
  }

  public static void createTable(String tableName, DBKey[] dbKeys) {
    if (connection != null) {
      try {
        StringBuilder SQL =
            new StringBuilder("CREATE TABLE IF NOT EXISTS ? (" + "id INTEGER PRIMARY KEY AUTOINCREMENT");

        for (DBKey dbKey : dbKeys) {
          SQL.append(", ").append(dbKey.getName()).append(" ").append(dbKey.getType()).append(dbKey.isNotNull() ? " NOT NULL" : "");

        }
        SQL.append(")");

        PreparedStatement preparedStatement = connection.prepareStatement(SQL.toString());
        preparedStatement.setString(1, tableName);
        preparedStatement.execute();

      } catch (SQLException e) {
        TheCoins.LOGGER.error("Cannot to create table " + tableName + " because " + e);
        throw new RuntimeException(e);
      }
    } else {
      // 插入到准备集合 然后一次性全部创建 when init
    }
  }
}
