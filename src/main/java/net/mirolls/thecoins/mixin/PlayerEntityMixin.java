package net.mirolls.thecoins.mixin;

import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.mirolls.thecoins.TheCoins;
import net.mirolls.thecoins.menu.Menu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin {
  @Shadow
  public abstract PlayerInventory getInventory();

  @Inject(method = "dropItem(Lnet/minecraft/item/ItemStack;ZZ)Lnet/minecraft/entity/ItemEntity;", at = @At("HEAD"), cancellable = true)
  private void dropItem(ItemStack stack, boolean throwRandomly, boolean retainOwnership, CallbackInfoReturnable<ItemEntity> cir) {
    TheCoins.LOGGER.info("Someone try to drop item, re-giving new menu");
    this.getInventory().setStack(8, Menu.getMenu((LivingEntity) (Object) this));
    
    if (Menu.isMenu(stack)) {
      cir.setReturnValue(null);
    }
  }
}
