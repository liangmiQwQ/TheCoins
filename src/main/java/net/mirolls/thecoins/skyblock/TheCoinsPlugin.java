package net.mirolls.thecoins.skyblock;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

import java.util.ArrayList;

public class TheCoinsPlugin {
  public static void registry() {
    ArrayList<PluginGetItemStack> pluginItemStacks = new ArrayList<>();
    ArrayList<PluginButtonLocation> pluginButtonLocations = new ArrayList<>();

    pluginItemStacks.add(((translation, player) -> {
//      if (translation != null) {
//        return ItemStackGUI.itemStackFactory(SKBMenu.GUI_ID, Items.PLAYER_HEAD, "");
      return new ItemStack(Items.PLAYER_HEAD);
//      }
    }));

    PluginsRegistry.registry(new Plugin(pluginItemStacks, pluginButtonLocations));
  }
}
