package net.mirolls.thecoins.mixin;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.mirolls.thecoins.gui.SKBMenu;
import net.mirolls.thecoins.item.Menu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(ItemStack.class)
public class ItemStackMixin {
  @Inject(method = "use", at = @At("TAIL"))
  public void use(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<ItemStack> cir) {
    if (Menu.isMenu((ItemStack) (Object) this) && !world.isClient) {
      SKBMenu.open(user);
    }
  }

  @Inject(method = "useOnBlock", at = @At("TAIL"))
  public void useOnBlock(ItemUsageContext context, CallbackInfoReturnable<ItemStack> cir) {
    PlayerEntity player = context.getPlayer();
    if (player != null && Menu.isMenu((ItemStack) (Object) this) && !player.getWorld().isClient) {
      SKBMenu.open(Objects.requireNonNull(context.getPlayer()));
    }
  }
}
