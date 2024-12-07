package net.mirolls.thecoins.libs.inventory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.EnderChestInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
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

  public static void setPlayerInventoryFromJSON(String inventoryString, PlayerInventory inventory) {
    // parse inventory JSON
    ObjectMapper objectMapper = new ObjectMapper();
    PlayerInventoryJsonParser playerInventory;
    try {
      playerInventory = objectMapper.readValue(
          inventoryString,
          objectMapper.getTypeFactory().constructType(PlayerInventoryJsonParser.class)
      );
    } catch (JsonProcessingException e) {
      TheCoins.LOGGER.error("Cannot get the inventory of player " + e);
      throw new RuntimeException(e);
    }

    // clear
    inventory.clear();

    // start to set main
    for (int index : playerInventory.getMain().keySet()) {
      // get itemStack
      inventory.setStack(index, transferInventoryToItemStack(playerInventory.getMain().get(index)));
    }
    // set armor
    for (int index : playerInventory.getArmor().keySet()) {
      inventory.setStack(index, transferInventoryToItemStack(playerInventory.getArmor().get(index)));
    }
    // set offhand
    for (int index : playerInventory.getOffhand().keySet()) {
      inventory.setStack(index, transferInventoryToItemStack(playerInventory.getOffhand().get(index)));
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

  public static void setEnderChestInventoryFromJSON(String ECInventoryString, EnderChestInventory inventory) {
    // parse inventory JSON
    ObjectMapper objectMapper = new ObjectMapper();
    HashMap<Integer, InventoryItems> enderChestInventory;
    try {
      enderChestInventory = objectMapper.readValue(
          ECInventoryString,
          objectMapper.getTypeFactory().constructMapType(HashMap.class, Integer.class, InventoryItems.class)
      );
    } catch (JsonProcessingException e) {
      TheCoins.LOGGER.error("Cannot get the inventory of player " + e);
      throw new RuntimeException(e);
    }

    // clear
    inventory.clear();

    // set
    for (int index : enderChestInventory.keySet()) {
      inventory.setStack(index, transferInventoryToItemStack(enderChestInventory.get(index)));
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
              "{}") // no NBT at all
          );
        }
      }
    }

    return serializedItems;
  }

  // 反序列化 InventoryItems to ItemStack
  private static ItemStack transferInventoryToItemStack(InventoryItems inventoryItems) {
    // item type
    ItemStack itemStack = new ItemStack(Registries.ITEM.get(new Identifier(inventoryItems.getItemID())));

    // item count
    itemStack.setCount(inventoryItems.getCount());

    // item NBT
    try {
      NbtCompound nbtCompound = NbtHelper.fromNbtProviderString(inventoryItems.getItemNBT());
      itemStack.setNbt(nbtCompound);
    } catch (CommandSyntaxException e) {
      TheCoins.LOGGER.error("Cannot read the NBT from String " + inventoryItems.getItemID() + inventoryItems.getItemNBT() + e);
      throw new RuntimeException(e);
    }

    return itemStack;
  }
}
