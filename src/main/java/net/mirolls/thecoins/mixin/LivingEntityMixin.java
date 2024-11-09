package net.mirolls.thecoins.mixin;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.mirolls.thecoins.menu.Menu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(LivingEntity.class)

public abstract class LivingEntityMixin {
  @Shadow
  public abstract ItemStack getEquippedStack(EquipmentSlot slot);

  @Shadow
  protected abstract void swapHandStacks();

  @Inject(method = "checkHandStackSwap", at = @At("HEAD"))
  public void checkHandStackSwap(Map<EquipmentSlot, ItemStack> equipmentChanges, CallbackInfo ci) {
    if (Menu.isMenu(getEquippedStack(EquipmentSlot.OFFHAND))) {
      swapHandStacks();
    }
  }
}
