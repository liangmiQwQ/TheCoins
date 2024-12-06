package net.mirolls.thecoins.libs.inventory;

public class InventoryItems {
  private String itemID;
  private int count;
  private String itemNBT;

  public InventoryItems(String itemID, int count, String itemNBT) {
    this.itemID = itemID;
    this.count = count;
    this.itemNBT = itemNBT;
  }

  public String getItemID() {
    return itemID;
  }

  public void setItemID(String itemID) {
    this.itemID = itemID;
  }

  public int getCount() {
    return count;
  }

  public void setCount(int count) {
    this.count = count;
  }

  public String getItemNBT() {
    return itemNBT;
  }

  public void setItemNBT(String itemNBT) {
    this.itemNBT = itemNBT;
  }
}
