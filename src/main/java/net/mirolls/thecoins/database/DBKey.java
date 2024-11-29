package net.mirolls.thecoins.database;

public class DBKey {

  private String name;
  private String type;
  private boolean isNotNull;

  public DBKey(String name, String type, boolean isNotNull) {
    this.name = name;
    this.type = type;
    this.isNotNull = isNotNull;
  }

  public boolean isNotNull() {
    return isNotNull;
  }

  public void setNotNull(boolean notNull) {
    isNotNull = notNull;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
