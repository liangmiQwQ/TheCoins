package net.mirolls.thecoins.gui;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Items;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.mirolls.thecoins.file.LanguageConfig;
import net.mirolls.thecoins.file.Translation;
import net.mirolls.thecoins.gui.screenHandles.ConfirmScreenHandle;
import net.mirolls.thecoins.item.ItemStackGUI;
import net.mirolls.thecoins.libs.SpecialItemClickedAction;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public class ConfirmGUI implements NamedScreenHandlerFactory {
  public static final String GUI_ID = "confirmGUI";

  private final String displayNameTranslationID;
  private final Translation translation;
  private final SimpleInventory GUI;

  public ConfirmGUI(PlayerEntity player, String displayNameTranslationID, String action, String[] args) {
    this.translation = new Translation(LanguageConfig.getPlayerLanguage(player.getUuidAsString()));
    this.GUI = createGUI(action, args, player);
    this.displayNameTranslationID = displayNameTranslationID;
  }

  public ConfirmGUI(PlayerEntity player, Translation translation, String displayNameTranslationID, String action, String[] args) {
    this.translation = translation;
    this.GUI = createGUI(action, args, player);
    this.displayNameTranslationID = displayNameTranslationID;
  }

  public static void openGUI(PlayerEntity player, Translation translation, String displayNameTranslationID, String action, String[] args) {
    Translation realTranslation;
    realTranslation = Objects.requireNonNullElseGet(translation, () -> new Translation(LanguageConfig.getPlayerLanguage(player.getUuidAsString())));

    if (!player.getWorld().isClient) {
      ((ServerPlayerEntity) player).closeHandledScreen();
      player.openHandledScreen(new ConfirmGUI(player, realTranslation, displayNameTranslationID, action, args));
    }
  }

  private SimpleInventory createGUI(String action, String[] args, PlayerEntity player) {
    SimpleInventory inventory = new SimpleInventory(27);

    for (int inventoryIndex = 0; inventoryIndex < inventory.size(); inventoryIndex++) {
      if (inventoryIndex == 11) {
        inventory.setStack(inventoryIndex, ItemStackGUI.itemStackFactory(
            GUI_ID,
            Items.GREEN_CONCRETE,
            translation.getTranslation("GUIConfirm"),
            "#55FF55",
            List.of(),
            new SpecialItemClickedAction("Function", action, args),
            "Function",
            null
        ));
      } else if (inventoryIndex == 15) {
        inventory.setStack(inventoryIndex, ItemStackGUI.itemStackFactory(
            GUI_ID,
            Items.RED_CONCRETE,
            translation.getTranslation("GUIClose"),
            "#C35E12",
            List.of(),
            new SpecialItemClickedAction("Close", "", null),
            "Close",
            null
        ));
      } else {
        inventory.setStack(inventoryIndex, ItemStackGUI.itemStackFactory(
            GUI_ID,
            Items.BLACK_STAINED_GLASS_PANE,
            "",
            "",
            List.of(),
            new SpecialItemClickedAction("Background", ""),
            "Background",
            null
        ));
      }
    }

    return inventory;
  }

  @Override
  public Text getDisplayName() {
    return Text.literal(translation.getTranslation(displayNameTranslationID));
  }

  @Nullable
  @Override
  public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
    return new ConfirmScreenHandle(syncId, GUI, playerInventory);
  }
}
