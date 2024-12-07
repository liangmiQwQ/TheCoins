package net.mirolls.thecoins.libs.inventory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.EnderChestInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.registry.Registries;
import net.minecraft.util.collection.DefaultedList;
import net.mirolls.thecoins.TheCoins;

import java.util.HashMap;
import java.util.Map;

public class InventoryTransfer {
  public static String playerInventoryAsJSON(PlayerInventory inventory) {
    // main items
    HashMap<Integer, InventoryItems> mainItemsMap = (HashMap<Integer, InventoryItems>) serializeItemStackList(inventory.main);

    // armor items
    HashMap<Integer, InventoryItems> armor = (HashMap<Integer, InventoryItems>) serializeItemStackList(inventory.armor);

    // offhand items
    HashMap<Integer, InventoryItems> offhand = (HashMap<Integer, InventoryItems>) serializeItemStackList(inventory.offHand);

    PlayerInventoryJsonParser jsonParser = new PlayerInventoryJsonParser(mainItemsMap, armor, offhand);

    ObjectMapper objectMapper = new ObjectMapper();
    try {
      return objectMapper.writeValueAsString(jsonParser);
    } catch (JsonProcessingException e) {
      TheCoins.LOGGER.error("Cannot encode the player inventory from NBT tags");
      throw new RuntimeException(e);
    }
  }

  public static String enderChestInventoryAsJSON(EnderChestInventory inventory) {
    HashMap<Integer, InventoryItems> mainItemsMap = (HashMap<Integer, InventoryItems>) serializeItemStackList(inventory.heldStacks);

    // 这里就不选择创建一个单独的类了 因为没有必要
    ObjectMapper objectMapper = new ObjectMapper();
    try {
      return objectMapper.writeValueAsString(mainItemsMap);
    } catch (JsonProcessingException e) {
      TheCoins.LOGGER.error("Cannot encode the player inventory from NBT tags");
      throw new RuntimeException(e);
    }
  }

  // 通用方法: 序列化 DefaultedList<ItemStack> 为 Map
  private static Map<Integer, InventoryItems> serializeItemStackList(DefaultedList<ItemStack> itemList) {
    Map<Integer, InventoryItems> serializedItems = new HashMap<>();
    for (int i = 0; i < itemList.size(); i++) {
      ItemStack itemStack = itemList.get(i);
      if (!itemStack.isEmpty()) {
        if (itemStack.getNbt() != null) {
          serializedItems.put(i, new InventoryItems(
              Registries.ITEM.getId(itemStack.getItem()).toString(),
              itemStack.getCount(),
              NbtHelper.toNbtProviderString(itemStack.getNbt()))
          );
        } else {
          serializedItems.put(i, new InventoryItems(
              Registries.ITEM.getId(itemStack.getItem()).toString(),
              itemStack.getCount(),
              null) // no NBT at all
          );
        }
      }
    }

    return serializedItems;
  }
}
