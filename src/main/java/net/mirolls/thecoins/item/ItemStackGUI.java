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
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.mirolls.thecoins.TheCoins;
import net.mirolls.thecoins.file.LanguageConfig;
import net.mirolls.thecoins.file.Translation;
import net.mirolls.thecoins.libs.MinecraftColor;
import net.mirolls.thecoins.libs.SpecialItemClickedAction;

import java.util.HashMap;
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

  public static ItemStack itemStackFactory(String GUI_ID, ItemConvertible items, String nameTranslationID, String hexTitleColor, String loreTranslationID, String clickedAction, String itemStackActionType, PlayerEntity player) {
    return itemStackFactoryWithTranslation(
        GUI_ID,
        new Translation(LanguageConfig.getPlayerLanguage(player.getUuidAsString())),
        items,
        nameTranslationID,
        hexTitleColor,
        loreTranslationID,
        itemStackActionType,
        clickedAction
    );
  }

  public static ItemStack itemStackFactory(String GUI_ID, ItemConvertible items, String nameTranslationID, String hexTitleColor, String loreTranslationID, String clickedAction, String itemStackActionType, Translation translation) {
    return itemStackFactoryWithTranslation(
        GUI_ID,
        translation,
        items,
        nameTranslationID,
        hexTitleColor,
        loreTranslationID,
        itemStackActionType,
        clickedAction
    );
  }

  private static ItemStack itemStackFactoryWithTranslation(String GUI_ID, Translation translation, ItemConvertible items, String nameTranslationID, String hexTitleColor, String loreTranslationID, String itemStackActionType, String clickedAction) {
    ItemStack itemStack = new ItemStack(items);

    itemStack.setCustomName(
        Text.literal(
            translation.getTranslation(nameTranslationID)
        ).setStyle(
            Style.EMPTY.withColor(
                MinecraftColor.hexToRgb(hexTitleColor)
            ))).setCount(1);

    NbtCompound displayTag = itemStack.getOrCreateSubNbt("display");

    if (loreTranslationID != null) {
      NbtList loreList = new NbtList();
      loreList.add(NbtString.of(
          Text.Serialization.toJsonString(
              Text.literal(
                      translation.getTranslation(loreTranslationID))
                  .setStyle(
                      Style.EMPTY.withColor(
                          MinecraftColor.hexToRgb(
                              "#AAAAAA"))))));

      displayTag.put("Lore", loreList);
    }

    displayTag.put("SpecialItemID", NbtString.of(GUI_ID + "_#&*&#_" + itemStackActionType));
    displayTag.put("SpecialItemClickedAction", NbtString.of(clickedAction)); // use JSON to parse

    return itemStack;
  }
}
