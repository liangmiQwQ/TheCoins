package net.mirolls.thecoins.mixin;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.collection.DefaultedList;
import net.mirolls.thecoins.gui.SKBMenu;
import net.mirolls.thecoins.item.Menu;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ScreenHandler.class)
public abstract class ScreenHandlerMixin {

  @Shadow
  @Final
  public DefaultedList<Slot> slots;

  @Unique
  private int setStackButton;
  @Unique
  private ItemStack setStackItemStack;


  @Shadow
  public abstract Slot getSlot(int index);

  @Inject(method = "onSlotClick", at = @At("HEAD"), cancellable = true)
  public void onSlotClickHead(int slotIndex, int button, SlotActionType actionType, PlayerEntity player, CallbackInfo ci) {
    if (slotIndex != -999 && actionType != SlotActionType.SWAP) {
      ItemStack stack = getSlot(slotIndex).getStack();
      if (Menu.isMenu(stack)) {
        SKBMenu.open(player);
        ci.cancel();
      }
    }
  }

  @Inject(method = "onSlotClick", at = @At("TAIL"))
  public void onSlotClickReturn(int slotIndex, int button, SlotActionType actionType, PlayerEntity player, CallbackInfo ci) {
    if (actionType == SlotActionType.SWAP && !player.getWorld().isClient && this.setStackButton != -999 && this.setStackItemStack != null) {
      player.getInventory().setStack(this.setStackButton, this.setStackItemStack);
    }
  }


  @Inject(method = "internalOnSlotClick", at = @At("HEAD"), cancellable = true)
  private void internalOnSlotClick(int slotIndex, int button, SlotActionType actionType, PlayerEntity player, CallbackInfo ci) throws InterruptedException {
    if (actionType == SlotActionType.SWAP && (button >= 0 && button < 9 || button == 40) && !player.getWorld().isClient) { //0-9 bar or offhand
      ItemStack itemStackInSlot = player.getInventory().getStack(button);
      Slot slot = this.slots.get(slotIndex);
      ItemStack itemStackInHand = slot.getStack();
//      TheCoins.LOGGER.info("itemStackInSlot: " + itemStackInSlot + " itemStackInHand: " + itemStackInHand);
      if (Menu.isMenu(itemStackInSlot) || Menu.isMenu(itemStackInHand)) {
        // slot is menu
        // we should replace itemStackInSlot (replace a menu)
        ci.cancel();
        this.setStackButton = button;
        this.setStackItemStack = itemStackInSlot;
        return;
      }
    }
    this.setStackButton = -999;
  }

}

