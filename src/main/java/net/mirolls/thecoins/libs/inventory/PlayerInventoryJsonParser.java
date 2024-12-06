package net.mirolls.thecoins.libs.inventory;

import java.util.HashMap;

public class PlayerInventoryJsonParser {
  private HashMap<Integer, InventoryItems> main;
  private HashMap<Integer, InventoryItems> armor;
  private HashMap<Integer, InventoryItems> offhand;

  public PlayerInventoryJsonParser(HashMap<Integer, InventoryItems> main, HashMap<Integer, InventoryItems> armor, HashMap<Integer, InventoryItems> offhand) {
    this.main = main;
    this.armor = armor;
    this.offhand = offhand;
  }

  public HashMap<Integer, InventoryItems> getMain() {
    return main;
  }

  public void setMain(HashMap<Integer, InventoryItems> main) {
    this.main = main;
  }

  public HashMap<Integer, InventoryItems> getArmor() {
    return armor;
  }

  public void setArmor(HashMap<Integer, InventoryItems> armor) {
    this.armor = armor;
  }

  public HashMap<Integer, InventoryItems> getOffhand() {
    return offhand;
  }

  public void setOffhand(HashMap<Integer, InventoryItems> offhand) {
    this.offhand = offhand;
  }
}
