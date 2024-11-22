package net.mirolls.thecoins.skyblock;

import net.minecraft.item.Items;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.text.Text;
import net.mirolls.thecoins.file.LanguageConfig;
import net.mirolls.thecoins.file.Translation;
import net.mirolls.thecoins.gui.SKBMenu;
import net.mirolls.thecoins.item.ItemStackGUI;
import net.mirolls.thecoins.libs.SpecialItemClickedAction;
import net.mirolls.thecoins.libs.StringChanger;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TheCoinsPlugin {
  public static void registry() {
    ArrayList<PluginGetItemStack> pluginItemStacks = new ArrayList<>();
    ArrayList<PluginButtonLocation> pluginButtonLocations = new ArrayList<>();
    ItemStackGUI.registryAction("openCraft", player -> {
      if (!player.getWorld().isClient) {
        player.openHandledScreen(new SimpleNamedScreenHandlerFactory(
            (syncId, playerInventory, playerEntity) -> ScreenHandlerType.CRAFTING.create(syncId, playerInventory),
            Text.translatable("container.crafting")
        ));
      }
    });
    ItemStackGUI.registryAction("openEnderChest", player -> {
      if (!player.getWorld().isClient) {
        player.getEnderChestInventory();
        player.openHandledScreen(new SimpleNamedScreenHandlerFactory(
            (syncId, playerInventory, playerEntity) -> new GenericContainerScreenHandler(
                ScreenHandlerType.GENERIC_9X3,
                syncId, playerInventory,
                playerEntity.getEnderChestInventory(), 3
            ),
            Text.translatable("container.enderchest")
        ));
      }
    });

    { // player head
      pluginItemStacks.add((translation, player) -> {
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

      });

      pluginButtonLocations.add(numberOfButton -> 13);
    }

    { // crafting table
      pluginItemStacks.add((translation, player) -> {
        Translation trulyTranslation;
        trulyTranslation = Objects.requireNonNullElseGet(translation, () -> new Translation(LanguageConfig.getPlayerLanguage(player.getUuidAsString())));

        if (player != null) {
          return ItemStackGUI.itemStackFactory(
              SKBMenu.GUI_ID,
              Items.CRAFTING_TABLE,
              trulyTranslation.getTranslation("CraftingTable"),
              "#23FF07",
              List.of(translation.getTranslation("CraftingTableLore")),
              new SpecialItemClickedAction("Link", "openCraft"),
              "Link",
              null

          );
        } else {
          throw new NullPointerException("Getting null when registry TheCoins plugin");
        }

      });

      pluginButtonLocations.add(numberOfButton -> {
        if (numberOfButton == 8) {
          return 32;
        } else {
          return 22;
        }
      });
    }

    { // ender chest
      pluginItemStacks.add((translation, player) -> {
        Translation trulyTranslation;
        trulyTranslation = Objects.requireNonNullElseGet(translation, () -> new Translation(LanguageConfig.getPlayerLanguage(player.getUuidAsString())));

        if (player != null) {
          return ItemStackGUI.itemStackFactory(
              SKBMenu.GUI_ID,
              Items.ENDER_CHEST,
              trulyTranslation.getTranslation("EnderChest"),
              "#23FF07",
              StringChanger.splittingString(translation.getTranslation("EnderChestLore")),
              new SpecialItemClickedAction("Link", "openEnderChest"),
              "Link",
              null
          );
        } else {
          throw new NullPointerException("Getting null when registry TheCoins plugin");
        }

      });

      pluginButtonLocations.add(numberOfButton -> {
        if (numberOfButton == 8) {
          return 33;
        } else {
          return 31;
        }
      });
    }

    PluginsRegistry.registry(new Plugin(pluginItemStacks, pluginButtonLocations), "TheCoins");
  }
}
