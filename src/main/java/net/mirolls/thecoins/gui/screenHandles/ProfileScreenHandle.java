package net.mirolls.thecoins.gui.screenHandles;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.screen.ScreenHandlerType;
import net.mirolls.thecoins.gui.ProfileGUI;
import net.mirolls.thecoins.gui.ScreenHandlerRegistryHelper;
import net.mirolls.thecoins.libs.StringChanger;

public class ProfileScreenHandle extends SKBMenuScreenHandle {
  public ProfileScreenHandle(int syncId, Inventory inventory, PlayerInventory playerInventory) {
    super(syncId, inventory, playerInventory);
  }

  public ProfileScreenHandle(int syncId, PlayerInventory playerInventory) {
    super(syncId, playerInventory);
  }

  public static void registerScreenHandlers() {
    // 注册 ScreenHandler
    ScreenHandlerRegistryHelper.register(StringChanger.camelToSnake(ProfileGUI.GUI_ID), new ScreenHandlerType<>(ProfileScreenHandle::new, FeatureSet.empty()));
  }
}
