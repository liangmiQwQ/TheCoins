package net.mirolls.thecoins.libs;

import net.mirolls.thecoins.TheCoins;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

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
      return preparedStatement.executeUpdate();
    } catch (SQLException e) {
      TheCoins.LOGGER.error("Cannot create profile for player because " + e);
      throw new RuntimeException(e);
    }
  }
}
