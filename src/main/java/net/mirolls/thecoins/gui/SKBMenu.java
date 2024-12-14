package net.mirolls.thecoins.gui;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.mirolls.thecoins.file.LanguageConfig;
import net.mirolls.thecoins.file.Translation;
import net.mirolls.thecoins.gui.screenHandles.SKBMenuScreenHandle;
import net.mirolls.thecoins.item.ItemStackGUI;
import net.mirolls.thecoins.libs.CoolDown;
import net.mirolls.thecoins.libs.SpecialItemClickedAction;
import net.mirolls.thecoins.skyblock.Plugin;
import net.mirolls.thecoins.skyblock.PluginsRegistry;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SKBMenu implements NamedScreenHandlerFactory {
  public static final String GUI_ID = "PlayerPersonalMenu";
  private static final long COOL_DOWN_TIME = 3000;
  private static final long TICK = 200;
  private final Translation translation;
  private final PlayerEntity player;
  private final SimpleInventory GUI;

  public SKBMenu(PlayerEntity player) {
    this.player = player;
    this.translation = new Translation(LanguageConfig.getPlayerLanguage(player.getUuidAsString()));
    this.GUI = createGUI();
  }

  public SKBMenu(PlayerEntity player, Translation translation) {
    this.player = player;
    this.translation = translation;
    this.GUI = createGUI();
  }

  public static void open(PlayerEntity player) {
    String playerUUID = player.getUuidAsString();
    Translation translationGUI = new Translation(LanguageConfig.getPlayerLanguage(playerUUID));
    CoolDown.ticksCoolDown(playerUUID, TICK, (long now) -> {
      CoolDown.commandCoolDown(player, COOL_DOWN_TIME, playerUUID, now, translationGUI, (Translation translationEnd) -> {
        openGUI(player, translationEnd);
      });
    });
  }

  public static void open(LivingEntity entity) {
    if (entity.isPlayer()) {
      String playerUUID = entity.getUuidAsString();
      Translation translationGUI = new Translation(LanguageConfig.getPlayerLanguage(entity.getUuidAsString()));
      CoolDown.ticksCoolDown(playerUUID, TICK, (long now) -> {
        CoolDown.commandCoolDown((PlayerEntity) entity, COOL_DOWN_TIME, playerUUID, now, translationGUI, (Translation translationEnd) -> {
          openGUI((PlayerEntity) entity, translationEnd);
        });
      });
    }

  }

  private static void openGUI(PlayerEntity player) {
    player.openHandledScreen(new SKBMenu(player));
  }

  private static void openGUI(PlayerEntity player, Translation translation) {
    if (!player.getWorld().isClient) {
      ((ServerPlayerEntity) player).closeHandledScreen();
      player.openHandledScreen(new SKBMenu(player, translation));
//      TheCoins.LOGGER.info("GUI has been opened");
    }
  }

  private SimpleInventory createGUI() {
    SimpleInventory inventoryGUI = new SimpleInventory(54);

    ItemStack closeButton = ItemStackGUI.itemStackFactory(
        GUI_ID,
        Items.BARRIER,
        translation.getTranslation("GUIClose"),
        "#C35E12",
        List.of(),
        new SpecialItemClickedAction("Close", ""),
        "Close",
        null
    );

    ItemStack background = ItemStackGUI.itemStackFactory(
        GUI_ID,
        Items.BLACK_STAINED_GLASS_PANE,
        "",
        "",
        List.of(),
        new SpecialItemClickedAction("Background", ""),
        "Background",
        null
    );

    ArrayList<Plugin> pluginArrayList = PluginsRegistry.getRegisteredPlugins();
    int numberOfButton = 0;
    for (Plugin plugin : pluginArrayList) {
      numberOfButton += plugin.getMenuItemStack().size();
    }

    for (Plugin plugin : pluginArrayList) {
      if (plugin.getMenuItemStack().size() == plugin.getItemStackLocations().size()) {
        for (int pluginButtonIndex = 0; pluginButtonIndex < plugin.getMenuItemStack().size(); pluginButtonIndex++) {
          int buttonLocation = plugin.getItemStackLocations() //  ArrayList<PluginButtonLocation>
              .get(pluginButtonIndex). // PluginButtonLocation
                  getLocation(numberOfButton); // int
          if (inventoryGUI.getStack(buttonLocation).isEmpty() || inventoryGUI.getStack(buttonLocation).getCount() == 0 || inventoryGUI.getStack(buttonLocation).getItem() == Items.AIR) {
            // do not have button
            inventoryGUI.setStack(buttonLocation,
                plugin.getMenuItemStack().get(pluginButtonIndex).getItemStack(translation, this.player));
            // set itemStack at last
          }
        }
      }
    }


    for (int inventoryIndex = 0; inventoryIndex < 54; inventoryIndex++) {
      if (inventoryIndex == 49) { // if close button
        inventoryGUI.setStack(49, closeButton);
      }
      if (inventoryGUI.getStack(inventoryIndex).isEmpty() || inventoryGUI.getStack(inventoryIndex).getCount() == 0 || inventoryGUI.getStack(inventoryIndex).getItem() == Items.AIR) {
        inventoryGUI.setStack(inventoryIndex, background);
      }
    }

    return inventoryGUI;
  }

  @Override
  public Text getDisplayName() {
    return Text.literal(this.translation.getTranslation("menu"));
  }

  @Nullable
  @Override
  public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
//    return new GenericContainerScreenHandler(ScreenHandlerType.GENERIC_9X6, syncId, playerInventory, GUI, 6);
    return new SKBMenuScreenHandle(syncId, GUI, playerInventory);
  }

}
