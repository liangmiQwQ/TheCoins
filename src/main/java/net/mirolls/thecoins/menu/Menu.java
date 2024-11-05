package net.mirolls.thecoins.menu;

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
    ItemStack menu = new ItemStack(Items.NETHER_STAR);
    Translation translation = new Translation(LanguageConfig.getPlayerLanguage(player.getUuidAsString()));

    menu.setCustomName(Text.literal(translation.getTranslation("menu")).setStyle(
        Style.EMPTY.withColor(TextColor.fromRgb(MinecraftColor.hexToRgb("#FFAA00")))
    )).setCount(1);

    menu.setDamage(0);

    NbtList loreList = new NbtList();
    loreList.add(new NbtCompound());
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

    if (hasMenu(player, menu)) {
      player.getInventory().removeOne(menu);
    }

    player.getInventory().setStack(9, menu);
  }

  public static boolean hasMenu(ServerPlayerEntity player) {
    return player.getInventory().contains(getMenu(player));
  }

  public static boolean hasMenu(ServerPlayerEntity player, ItemStack menu) {
    return player.getInventory().contains(menu);
    // players don't need read json file again
  }
}
