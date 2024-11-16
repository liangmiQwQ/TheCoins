package net.mirolls.thecoins.gui.screenHandles;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.mirolls.thecoins.TheCoins;
import net.mirolls.thecoins.gui.SKBMenu;
import net.mirolls.thecoins.gui.ScreenHandlerRegistryHelper;
import net.mirolls.thecoins.item.ItemStackGUI;
import net.mirolls.thecoins.libs.StringChanger;

public class SKBMenuScreenHandle extends ScreenHandler {
  private final Inventory inventory;

  public SKBMenuScreenHandle(int syncId, Inventory inventory, PlayerInventory playerInventory) {
    super(ScreenHandlerType.GENERIC_9X6, syncId); // 第一个参数是 ScreenHandlerType，可以自定义一个
    inventory.onOpen(playerInventory.player);
    this.inventory = inventory;
    TheCoins.LOGGER.info("Created SKBMenuScreenHandle");


    // Add GUI Slots with 6x9 inventory
    for (int i = 0; i < 6; ++i) {
      for (int j = 0; j < 9; ++j) {
        this.addSlot(new Slot(inventory, j + i * 9, 62 + j * 18, 17 + i * 18));
      }
    }
    // don't do anything because we need to remove player's inventory
  }

  public SKBMenuScreenHandle(int syncId, PlayerInventory playerInventory) {
    this(syncId, new SimpleInventory(54), playerInventory);
  }


  public static void registerScreenHandlers() {
    // 注册 ScreenHandler
    ScreenHandlerRegistryHelper.register(StringChanger.camelToSnake(SKBMenu.GUI_ID), new ScreenHandlerType<>(SKBMenuScreenHandle::new, FeatureSet.empty()));
  }

  @Override
  public void onSlotClick(int slotIndex, int button, SlotActionType actionType, PlayerEntity player) {
    if (actionType == SlotActionType.PICKUP) { // left-click
      ItemStackGUI.itemStackExecutor(getSlot(slotIndex).getStack(), player);
    }
  }

  @Override
  public ItemStack quickMove(PlayerEntity player, int slot) {
    return null; // don't do anything because it doesn't need to move anything
  }

  @Override
  public boolean canUse(PlayerEntity player) {
    return this.inventory.canPlayerUse(player);
  }

  @Override
  public void onClosed(PlayerEntity player) {
    super.onClosed(player);
    this.inventory.onClose(player);
  }
}
