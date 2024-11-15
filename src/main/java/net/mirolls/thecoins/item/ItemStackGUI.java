package net.mirolls.thecoins.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.mirolls.thecoins.file.LanguageConfig;
import net.mirolls.thecoins.file.Translation;
import net.mirolls.thecoins.libs.MinecraftColor;

public class ItemStackGUI {
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
            ))).setCount(0);

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
