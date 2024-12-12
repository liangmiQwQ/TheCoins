package net.mirolls.thecoins.database;

import net.minecraft.util.crash.CrashException;
import net.minecraft.util.crash.CrashReport;
import net.mirolls.thecoins.TheCoins;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class SkyBlockDB {
  private static final ArrayList<TableCreateSQL> tablesBeforeInit = new ArrayList<>();
  public static Connection connection;

  public static void initSQLite() {
//    if (!MinecraftFile.isFileExists("skyblock", "SkyBlock.db")) {
    // 链接数据库 不存在则自动创建
    try {
      Class.forName("org.sqlite.JDBC");
      Connection conn = DriverManager.getConnection("jdbc:sqlite:skyblock/SkyBlock.db");
      if (conn != null) {
        connection = conn;
        TheCoins.LOGGER.info("Successfully connected to the database");
      } else {
        CrashReport crashReport = CrashReport.create(new RuntimeException("Got conn is null!"), "Crash");
        throw new CrashException(crashReport);
      }
      for (TableCreateSQL tableBeforeInit : tablesBeforeInit) {
        PreparedStatement preparedStatement = connection.prepareStatement(tableBeforeInit.getCreateSQL());
        preparedStatement.setString(1, tableBeforeInit.getTableName());
        preparedStatement.execute();
        preparedStatement.close();
      }
      // 创建链接
    } catch (SQLException | ClassNotFoundException e) {
      TheCoins.LOGGER.error("Cannot connect to the Database");
      throw new RuntimeException(e);
    }
//    }
  }

  public static void createTable(String tableName, DBKey[] dbKeys) {
    StringBuilder SQL =
        new StringBuilder("CREATE TABLE IF NOT EXISTS `").append(tableName).append("`").append(" (`id` INTEGER PRIMARY KEY AUTOINCREMENT, ");

    for (int i = 0; i < dbKeys.length; i++) {
      DBKey dbKey = dbKeys[i];
      if (i != 0) {
        SQL.append(", ");
      }
      SQL.append("`").append(dbKey.getName()).append("` ").append(dbKey.getType().toUpperCase()).append(dbKey.isNotNull() ? " NOT NULL" : "");
    }
    SQL.append(")");

    if (connection != null) {
      try {
        PreparedStatement preparedStatement = connection.prepareStatement(SQL.toString());
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