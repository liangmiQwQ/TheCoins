package net.mirolls.thecoins.libs.inventory;

import java.util.HashMap;

public class PlayerInventoryJsonParser {
  private HashMap<Integer, String> main;
  private HashMap<Integer, String> armor;
  private String offhand;

  public PlayerInventoryJsonParser(HashMap<Integer, String> main, HashMap<Integer, String> armor, String offhand) {
    this.main = main;
    this.armor = armor;
    this.offhand = offhand;
  }

  public HashMap<Integer, String> getMain() {
    return main;
  }

  public void setMain(HashMap<Integer, String> main) {
    this.main = main;
  }

  public HashMap<Integer, String> getArmor() {
    return armor;
  }

  public void setArmor(HashMap<Integer, String> armor) {
    this.armor = armor;
  }

  public String getOffhand() {
    return offhand;
  }

  public void setOffhand(String offhand) {
    this.offhand = offhand;
  }
}
