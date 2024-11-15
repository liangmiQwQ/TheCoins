package net.mirolls.thecoins.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.mirolls.thecoins.file.LanguageConfig;
import net.mirolls.thecoins.file.Translation;
import net.mirolls.thecoins.libs.MinecraftColor;

public class Menu {

  public static ItemStack getMenu(ServerPlayerEntity player) {
    String playerLanguage = LanguageConfig.getPlayerLanguage(player.getUuidAsString());
    Translation translation = new Translation(playerLanguage);
    return getMenuStack(translation);
  }

  public static ItemStack getMenu(LivingEntity player) {
    String playerLanguage = LanguageConfig.getPlayerLanguage(player.getUuidAsString());
    Translation translation = new Translation(playerLanguage);
    return getMenuStack(translation);
  }

  private static ItemStack getMenuStack(Translation translation) {
    ItemStack menu = new ItemStack(Items.NETHER_STAR);

    menu.setCustomName(Text.literal(translation.getTranslation("menu")).setStyle(
        Style.EMPTY.withColor(TextColor.fromRgb(MinecraftColor.hexToRgb("#FFAA00")))
    )).setCount(1);

    menu.setDamage(0);

    NbtList loreList = new NbtList();
    loreList.add(NbtString.of(
        Text.Serialization.toJsonString(
            Text.literal(
                    translation.getTranslation("menuDescription"))
                .setStyle(
                    Style.EMPTY.withColor(
                        MinecraftColor.hexToRgb(
                            "#AAAAAA"))))));

    NbtCompound displayTag = menu.getOrCreateSubNbt("display");
    displayTag.put("Lore", loreList);
    displayTag.put("SpecialItemID", NbtString.of("Menu"));

    return menu;

  }

  public static void replaceMenu(ServerPlayerEntity player) {
    ItemStack menu = getMenu(player);

    if (hasMenu(player)) {
      removeMenu(player);
    }

    player.getInventory().setStack(8, menu);
  }

  public static boolean hasMenu(ServerPlayerEntity player) {
    return player.getInventory().main.stream().filter(itemStack -> !itemStack.isEmpty()).anyMatch(Menu::isMenu);
  }

  public static void removeMenu(ServerPlayerEntity player) {
    player.getInventory().main.replaceAll(itemStack -> {
      if (isMenu(itemStack)) {
        return ItemStack.EMPTY;
      } else {
        return itemStack;
      }
    });
  }

  public static boolean isMenu(ItemStack itemStack) {
    if (itemStack.hasNbt()) {
      NbtCompound nbt = itemStack.getNbt();
      if (nbt != null && nbt.contains("display", 10)) {  // 检查 "display" 是否存在，且是一个 compound
        NbtCompound displayTag = nbt.getCompound("display");
        if (displayTag.contains("SpecialItemID", 8)) {  // 检查 "SpecialItemID" 是否存在，且是一个字符串类型
          if (displayTag.getString("SpecialItemID").equals("Menu")) {
            return true;
          }
          ;
        }
      }
    }
    return false;
  }
}
