package net.mirolls.thecoins.mixin;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.SlotActionType;
import net.mirolls.thecoins.gui.SKBMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ScreenHandler.class)
public class ScreenHandlerMixin {
  @Inject(method = "onSlotClick", at = @At("HEAD"), cancellable = true)
  public void onSlotClick(int slotIndex, int button, SlotActionType actionType, PlayerEntity player, CallbackInfo ci) {
    if (slotIndex == 8) { // n - 1
      SKBMenu.open();
      ci.cancel();
    }
  }
}
