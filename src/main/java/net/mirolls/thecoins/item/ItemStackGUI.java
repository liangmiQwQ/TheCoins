package net.mirolls.thecoins.item;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.mirolls.thecoins.TheCoins;
import net.mirolls.thecoins.libs.MinecraftColor;
import net.mirolls.thecoins.libs.SpecialItemClickedAction;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

public class ItemStackGUI {
  private static final HashMap<String, ItemStackGUICallBack> registeredActions = new HashMap<>();

  public static void itemStackExecutor(ItemStack itemStack, PlayerEntity player) {
    if (itemStack.hasNbt()) {
      NbtCompound nbt = itemStack.getNbt();
      if (nbt != null && nbt.contains("display", 10)) {  // 检查 "display" 是否存在，且是一个 compound
        NbtCompound displayTag = nbt.getCompound("display");
        if (displayTag.contains("SpecialItemID", 8) && displayTag.contains("SpecialItemClickedAction", 8)) {
          // 检查 "SpecialItemID" 是否存在，且是一个字符串类型{ // 检查 "SpecialItemClickedAction" 是否存在，并且是一个字符串类型
          String actionTypeAtNBT = displayTag.getString("SpecialItemID").split(Pattern.quote("_#&*&#_"))[1];
          String clickedActionJSON = displayTag.getString("SpecialItemClickedAction");


          SpecialItemClickedAction specialItemClickedAction = null;
          try {
            specialItemClickedAction = new ObjectMapper().readValue(clickedActionJSON, SpecialItemClickedAction.class);
          } catch (JsonProcessingException e) {
            TheCoins.LOGGER.error("Cannot read the SpecialItemClickedAction to SpecialItemClickedAction.class while reading value");
            throw new RuntimeException(e);
          }

          if (specialItemClickedAction.getActionType().equals(actionTypeAtNBT)) {
            // check successfully
            switch (specialItemClickedAction.getActionType()) {
              case "Close" -> {
                if (specialItemClickedAction.getActionFunction().isEmpty()) { // == ""
                  if (!player.getWorld().isClient) {
                    ((ServerPlayerEntity) player).closeHandledScreen(); // "ActionType:Close": "Player.closeHandledScreen()"
                  }
                } else { // not empty
                  registeredActions.get(specialItemClickedAction.getActionFunction()).callback(player);
                }  // type: close
              }
              case "Link" -> {
                if (!specialItemClickedAction.getActionFunction().isEmpty()) { // not empty
                  registeredActions.get(specialItemClickedAction.getActionFunction()).callback(player);
                }  // type: link
              }
              case "Function" -> {
                if (specialItemClickedAction.getActionFunction().isEmpty()) { // == ""
                  TheCoins.LOGGER.error("The type of the ItemStack " + itemStack + "is Function but the `actionFunction` in JSON is empty");
                } else { // not empty
                  registeredActions.get(specialItemClickedAction.getActionFunction()).callback(player);
                }  // type: function
              }
              case "Background" -> {
              }
              default ->
                  TheCoins.LOGGER.error("The type of the ItemStack " + itemStack + "is " + specialItemClickedAction.getActionType() + " is undefined");
            }
          } else {
            TheCoins.LOGGER.error("The type of the ItemStack " + itemStack + "is not the same as the `actionType` in JSON (" + specialItemClickedAction.getActionType() + ")");
          }

        }
      }
    }
  }

  public static void registryAction(String actionID, ItemStackGUICallBack action) {
    registeredActions.put(actionID, action);
  }

  public static ItemStack itemStackFactory(
      String GUI_ID,
      ItemConvertible items,
      String itemName,
      String hexTitleColor,
      List<String> itemLore,
      SpecialItemClickedAction clickedAction,
      String itemStackActionType,
      NbtCompound itemOtherNBT
  ) {

    Text itemNameText = Text.literal(itemName).setStyle(Style.EMPTY.withColor(MinecraftColor.hexToRgb(hexTitleColor)));

    List<MutableText> itemLoreText = itemLore.stream().map(lore -> {
      return Text.literal(
          lore
      ).setStyle(
          Style.EMPTY.withColor(
              MinecraftColor.hexToRgb(
                  "#AAAAAA")));
    }).toList();

    return itemStackFactoryWithTranslation(
        GUI_ID,
        items,
        itemNameText,
        itemLoreText,
        itemStackActionType,
        itemOtherNBT,
        clickedAction
    );
  }

  public static ItemStack itemStackFactory(
      String GUI_ID,
      ItemConvertible items,
      Text itemName,
      List<MutableText> itemLore,
      SpecialItemClickedAction clickedAction,
      String itemStackActionType,
      NbtCompound itemOtherNBT
  ) {
    return itemStackFactoryWithTranslation(
        GUI_ID,
        items,
        itemName,
        itemLore,
        itemStackActionType,
        itemOtherNBT,
        clickedAction
    );
  }

  private static ItemStack itemStackFactoryWithTranslation(
      String GUI_ID,
      ItemConvertible items,
      Text itemName,
      List<MutableText> itemLore,
      String itemStackActionType,
      NbtCompound itemOtherNBT,
      SpecialItemClickedAction clickedAction) {
    ItemStack itemStack = new ItemStack(items);

    itemStack.setNbt(itemOtherNBT);

    itemStack.setCustomName(itemName).setCount(1);

    NbtCompound displayTag = itemStack.getOrCreateSubNbt("display");

    if (Objects.equals(itemStackActionType, "Background") && Objects.equals(clickedAction.getActionType(), "Background")) {
      displayTag.putInt("HideFlags", 127);
    }

    if (itemLore != null && !itemLore.isEmpty()) {
      NbtList loreList = new NbtList();
      for (MutableText mutableText : itemLore) {
        loreList.add(NbtString.of(
            Text.Serialization.toJsonString(
                mutableText
            )));
      }
      displayTag.put("Lore", loreList);
    }

    displayTag.put("SpecialItemID", NbtString.of(GUI_ID + "_#&*&#_" + itemStackActionType));


    try {
      displayTag.put("SpecialItemClickedAction", NbtString.of(new ObjectMapper().writeValueAsString(clickedAction))); // use JSON to parse
    } catch (JsonProcessingException e) {
      TheCoins.LOGGER.error("Cannot make button " + GUI_ID + "_#&*&#_");
      throw new RuntimeException(e);
    }


    return itemStack;
  }
}
