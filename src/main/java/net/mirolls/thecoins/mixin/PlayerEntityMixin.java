package net.mirolls.thecoins.mixin;

import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.mirolls.thecoins.TheCoins;
import net.mirolls.thecoins.menu.Menu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {
  @Inject(method = "dropItem*", at = @At("HEAD"))
  private void dropItem(ItemStack stack, boolean retainOwnership, CallbackInfoReturnable<ItemEntity> cir) {
    TheCoins.LOGGER.info("Someone try to drop item." + stack.toString());
    if (Menu.isMenu(stack)) {
      cir.cancel();
    }
  }
}
