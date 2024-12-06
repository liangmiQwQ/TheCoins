package net.mirolls.thecoins.libs.inventory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.util.collection.DefaultedList;
import net.mirolls.thecoins.TheCoins;

import java.util.HashMap;
import java.util.Map;

public class InventoryTransfer {
  public static String playerInventoryAsJSON(PlayerInventory inventory) {
    // main items
    HashMap<Integer, String> mainItemsMap = (HashMap<Integer, String>) serializeItemStackList(inventory.main);

    // armor items
    HashMap<Integer, String> armor = (HashMap<Integer, String>) serializeItemStackList(inventory.armor);

    // offhand item
    String offhand = "{}";
    ItemStack offhandItem = inventory.main.get(0);
    if (offhandItem.isEmpty() || offhandItem.getCount() == 0) {
      if (offhandItem.getNbt() != null) {
        offhand = NbtHelper.toNbtProviderString(offhandItem.getNbt());
      }
    }

    PlayerInventoryJsonParser jsonParser = new PlayerInventoryJsonParser(mainItemsMap, armor, offhand);

    ObjectMapper objectMapper = new ObjectMapper();
    try {
      return objectMapper.writeValueAsString(jsonParser);
    } catch (JsonProcessingException e) {
      TheCoins.LOGGER.error("Cannot parse the player inventory from NBT tags");
      throw new RuntimeException(e);
    }
  }

//  public static String enderChestInventoryAsJSON(EnderChestInventory inventory) {
//  }

  // 通用方法: 序列化 DefaultedList<ItemStack> 为 Map
  private static Map<Integer, String> serializeItemStackList(DefaultedList<ItemStack> itemList) {
    Map<Integer, String> serializedItems = new HashMap<>();
    for (int i = 0; i < itemList.size(); i++) {
      ItemStack itemStack = itemList.get(i);
      if (!itemStack.isEmpty()) {
        if (itemStack.getNbt() != null) {
          serializedItems.put(i, NbtHelper.toNbtProviderString(itemStack.getNbt()));
        }
      }
    }
    return serializedItems;
  }
}
