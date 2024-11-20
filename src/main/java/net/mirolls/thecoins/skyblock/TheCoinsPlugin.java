package net.mirolls.thecoins.skyblock;

import net.minecraft.item.Items;
import net.mirolls.thecoins.file.LanguageConfig;
import net.mirolls.thecoins.file.Translation;
import net.mirolls.thecoins.gui.SKBMenu;
import net.mirolls.thecoins.item.ItemStackGUI;
import net.mirolls.thecoins.libs.SpecialItemClickedAction;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TheCoinsPlugin {
  public static void registry() {
    ArrayList<PluginGetItemStack> pluginItemStacks = new ArrayList<>();
    ArrayList<PluginButtonLocation> pluginButtonLocations = new ArrayList<>();

    pluginItemStacks.add(((translation, player) -> {
      Translation trulyTranslation;
      // player head
      //        return new ItemStack(Items.PLAYER_HEAD);
      trulyTranslation = Objects.requireNonNullElseGet(translation, () -> new Translation(LanguageConfig.getPlayerLanguage(player.getUuidAsString())));

      if (player != null) {
        return ItemStackGUI.itemStackFactory(
            SKBMenu.GUI_ID,
            Items.PLAYER_HEAD,
            trulyTranslation.getTranslation("SKBMenuHead").replace("${}", player.getName().getString()),
            "#23FF07",
            List.of("1"),
            new SpecialItemClickedAction("Link", ""),
            "Link",
            null

        );
      } else {
        throw new NullPointerException("Getting null when registry TheCoins plugin");
      }

    }));

    pluginButtonLocations.add(numberOfButton -> 13);

    PluginsRegistry.registry(new Plugin(pluginItemStacks, pluginButtonLocations));
  }
}
