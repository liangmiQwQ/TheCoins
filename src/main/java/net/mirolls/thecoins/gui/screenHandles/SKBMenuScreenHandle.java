package net.mirolls.thecoins.gui.screenHandles;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.mirolls.thecoins.item.ItemStackGUI;

public class SKBMenuScreenHandle extends ScreenHandler {
  private final String GUI_ID;

  public SKBMenuScreenHandle(int syncId, Inventory inventory, String GUI_ID) {
    super(ScreenHandlerType.GENERIC_9X6, syncId); // 第一个参数是 ScreenHandlerType，可以自定义一个
    this.GUI_ID = GUI_ID;

    // Add GUI Slots with 6x9 inventory
    for (int i = 0; i < 6; ++i) {
      for (int j = 0; j < 9; ++j) {
        this.addSlot(new Slot(inventory, j + i * 3, 62 + j * 18, 17 + i * 18));
      }
    }

    // don't do anything because we need to remove player's inventory
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
    return true;
  }

  public String getGUI_ID() {
    return GUI_ID;
  }
}
