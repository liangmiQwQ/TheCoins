package net.mirolls.thecoins.mixin;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.collection.DefaultedList;
import net.mirolls.thecoins.gui.SKBMenu;
import net.mirolls.thecoins.menu.Menu;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ScreenHandler.class)
public abstract class ScreenHandlerMixin {

  @Shadow
  @Final
  public DefaultedList<Slot> slots;

  @Shadow
  public abstract Slot getSlot(int index);

  @Shadow
  protected abstract void internalOnSlotClick(int slotIndex, int button, SlotActionType actionType, PlayerEntity player);

  @Inject(method = "onSlotClick", at = @At("HEAD"), cancellable = true)
  public void onSlotClick(int slotIndex, int button, SlotActionType actionType, PlayerEntity player, CallbackInfo ci) {
    if (slotIndex != -999) {
      ItemStack stack = getSlot(slotIndex).getStack();
      if (Menu.isMenu(stack)) {
        SKBMenu.open();
        ci.cancel();
      }
    }
  }

  @Inject(method = "internalOnSlotClick", at = @At("HEAD"))
  private void internalOnSlotClick(int slotIndex, int button, SlotActionType actionType, PlayerEntity player, CallbackInfo ci) {
    if (actionType == SlotActionType.SWAP && (button >= 0 && button < 9 || button == 40)) { //0-9 bar or offhand
      ItemStack itemStackInSlot = player.getInventory().getStack(button);
      if (Menu.isMenu(itemStackInSlot)) {
        internalOnSlotClick(button, slotIndex, SlotActionType.SWAP, player);
      }
    }
  }

}

