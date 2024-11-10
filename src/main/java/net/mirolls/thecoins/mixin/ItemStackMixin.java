package net.mirolls.thecoins.mixin;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.mirolls.thecoins.gui.SKBMenu;
import net.mirolls.thecoins.menu.Menu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public class ItemStackMixin {
  @Inject(method = "use", at = @At("HEAD"))
  public void use(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir) {
    if (Menu.isMenu((ItemStack) (Object) this)) {
      SKBMenu.open(user);
    }
  }
}
