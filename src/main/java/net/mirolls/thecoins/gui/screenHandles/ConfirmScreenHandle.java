package net.mirolls.thecoins.gui.screenHandles;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import net.mirolls.thecoins.gui.ConfirmGUI;
import net.mirolls.thecoins.gui.ScreenHandlerRegistryHelper;
import net.mirolls.thecoins.libs.StringHelper;

public class ConfirmScreenHandle extends SKBMenuScreenHandle {

  public ConfirmScreenHandle(int syncId, Inventory inventory, PlayerInventory playerInventory) {
    super(ScreenHandlerType.GENERIC_9X3, syncId, inventory);
    inventory.onOpen(playerInventory.player);

    int rows = 3;

    int i = (rows - 4) * 18;
    int j;
    int k;
    for (j = 0; j < rows; ++j) {
      for (k = 0; k < 9; ++k) {
        this.addSlot(new Slot(inventory, k + j * 9, 8 + k * 18, 18 + j * 18));
      }
    }

    for (j = 0; j < 3; ++j) {
      for (k = 0; k < 9; ++k) {
        this.addSlot(new Slot(playerInventory, k + j * 9 + 9, 8 + k * 18, 103 + j * 18 + i));
      }
    }

    for (j = 0; j < 9; ++j) {
      this.addSlot(new Slot(playerInventory, j, 8 + j * 18, 161 + i));
    }
  }

  public ConfirmScreenHandle(int syncId, PlayerInventory playerInventory) {
    this(syncId, new SimpleInventory(27), playerInventory);
  }

  public static void registerScreenHandlers() {
    // 注册 ScreenHandler
    ScreenHandlerRegistryHelper.register(StringHelper.camelToSnake(ConfirmGUI.GUI_ID), new ScreenHandlerType<>(ConfirmScreenHandle::new, FeatureSet.empty()));
  }
}
