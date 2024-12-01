package net.mirolls.thecoins.database;

import net.mirolls.thecoins.TheCoins;
import net.mirolls.thecoins.file.MinecraftFile;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class SkyBlockDB {
  private static final ArrayList<TableCreateSQL> tablesBeforeInit = new ArrayList<>();
  public static Connection connection;

  public static void initSQLite() {
    if (!MinecraftFile.isFileExists("skyblock", "SkyBlock.db")) {
      // 链接数据库 不存在则自动创建
      try {
        Connection conn = DriverManager.getConnection("jdbc:sqlite:skyblock/SkyBlock.db");
        if (conn != null) connection = conn;
        for (TableCreateSQL tableBeforeInit : tablesBeforeInit) {
          PreparedStatement preparedStatement = connection.prepareStatement(tableBeforeInit.getCreateSQL());
          preparedStatement.setString(1, tableBeforeInit.getTableName());
          preparedStatement.execute();
          preparedStatement.close();
        }
        // 创建链接
      } catch (SQLException e) {
        throw new RuntimeException(e);
      }
    }
  }

  public static void createTable(String tableName, DBKey[] dbKeys) {
    StringBuilder SQL =
        new StringBuilder("CREATE TABLE IF NOT EXISTS ? (" + "`id` INTEGER PRIMARY KEY AUTOINCREMENT");

    for (DBKey dbKey : dbKeys) {
      SQL.append(", `").append(dbKey.getName()).append("` ").append(dbKey.getType().toUpperCase()).append(dbKey.isNotNull() ? " NOT NULL" : "");
    }
    SQL.append(")");

    if (connection != null) {
      try {
        PreparedStatement preparedStatement = connection.prepareStatement(SQL.toString());
        preparedStatement.setString(1, tableName);
        preparedStatement.execute();
        preparedStatement.close();

      } catch (SQLException e) {
        TheCoins.LOGGER.error("Cannot to create table " + tableName + " because " + e);
        throw new RuntimeException(e);
      }
    } else {
      // 插入到准备集合 然后一次性全部创建 when init
      tablesBeforeInit.add(new TableCreateSQL(SQL.toString(), tableName));
    }
  }
}

class TableCreateSQL {
  private String createSQL;
  private String tableName;

  public TableCreateSQL(String createSQL, String tableName) {
    this.createSQL = createSQL;
    this.tableName = tableName;
  }

  public String getCreateSQL() {
    return createSQL;
  }

  public void setCreateSQL(String createSQL) {
    this.createSQL = createSQL;
  }

  public String getTableName() {
    return tableName;
  }

  public void setTableName(String tableName) {
    this.tableName = tableName;
  }
}