package net.mirolls.thecoins.gui;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import net.mirolls.thecoins.TheCoins;
import net.mirolls.thecoins.file.LanguageConfig;
import net.mirolls.thecoins.file.Translation;
import net.mirolls.thecoins.gui.screenHandles.SKBMenuScreenHandle;
import net.mirolls.thecoins.item.ItemStackGUI;
import net.mirolls.thecoins.libs.CoolDown;
import net.mirolls.thecoins.libs.SpecialItemClickedAction;
import org.jetbrains.annotations.Nullable;

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
      TheCoins.LOGGER.info("GUI has been opened");
    }
  }

  private SimpleInventory createGUI() {
    SimpleInventory inventoryGUI = new SimpleInventory(54);

    ItemStack closeButton;
    try {
      closeButton = ItemStackGUI.itemStackFactory(
          GUI_ID,
          Items.BARRIER,
          translation.getTranslation("GUIClose"),
          "#C35E12",
          List.of(),
          new ObjectMapper().writeValueAsString(new SpecialItemClickedAction("Close", "")),
          "Close",
          null,
          translation
      );
    } catch (JsonProcessingException e) {
      TheCoins.LOGGER.error("Cannot make button CloseButton");
      throw new RuntimeException(e);
    }
    TheCoins.LOGGER.info("CloseButton: " + closeButton);

    inventoryGUI.setStack(49, closeButton);
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
