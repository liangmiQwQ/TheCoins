package net.mirolls.thecoins.libs;

import net.mirolls.thecoins.TheCoins;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SQLExecutor {
  public static int insert(String tableName, HashMap<String, Object> keyAndValue, Connection connection) {
    StringBuilder SQL = new StringBuilder("INSERT INTO ").append(tableName);

    // keys
    SQL.append("(");
    ArrayList<String> keyOrder = new ArrayList<>(keyAndValue.keySet());
    for (int i = 0; i < keyOrder.size(); i++) {
      SQL.append("`").append(keyOrder.get(i)).append("`");
      if (i < keyOrder.size() - 1) {
        SQL.append(", ");
      }
    }
    SQL.append(") VALUES (");
    for (int i = 0; i < keyOrder.size(); i++) {
      SQL.append("?");
      if (i < keyOrder.size() - 1) {
        SQL.append(", ");
      }
    }
    SQL.append(");");

    // execute
    try {
      PreparedStatement preparedStatement = connection.prepareStatement(SQL.toString());
      for (int i = 0; i < keyOrder.size(); i++) {
        preparedStatement.setObject(i + 1, keyAndValue.get(keyOrder.get(i)));
      }
      int rs = preparedStatement.executeUpdate();
      preparedStatement.close();
      return rs;
    } catch (SQLException e) {
      TheCoins.LOGGER.error("Cannot create profile for player because " + e);
      throw new RuntimeException(e);
    }
  }

  public static String selectSQL(String tableName, List<String> keys, Connection connection) {
    StringBuilder SQL = new StringBuilder("SELECT ");
    for (int i = 0; i < keys.size(); i++) {
      SQL.append("`").append(keys.get(i)).append("`");
      if (i < keys.size() - 1) {
        SQL.append(", ");
      }
    }
    return SQL.append(" FROM ").append("`").append(tableName).append("`").append(";").toString();
  }

  public static String selectSQL(String tableName, List<String> keys, Connection connection, String whereSQL) {
    StringBuilder SQL = new StringBuilder("SELECT ");
    for (int i = 0; i < keys.size(); i++) {
      SQL.append("`").append(keys.get(i)).append("`");
      if (i < keys.size() - 1) {
        SQL.append(", ");
      }
    }
    return SQL.append(" FROM ").append("`").append(tableName).append("`").append(" WHERE ").append(whereSQL).append(";").toString();
  }

  public static String selectSQLNoEnd(String tableName, List<String> keys, Connection connection, String whereSQL) {
    StringBuilder SQL = new StringBuilder("SELECT ");
    for (int i = 0; i < keys.size(); i++) {
      SQL.append("`").append(keys.get(i)).append("`");
      if (i < keys.size() - 1) {
        SQL.append(", ");
      }
    }
    return SQL.append(" FROM ").append("`").append(tableName).append("`").append(" WHERE ").append(whereSQL).toString();
  }
}
