package net.mirolls.thecoins.mixin;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.PlayerScreenHandler;
import net.mirolls.thecoins.TheCoins;
import net.mirolls.thecoins.menu.Menu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerScreenHandler.class)
public abstract class PlayerScreenHandlerMixin extends AbstractRecipeScreenHandlerMixin<RecipeInputInventory> {

  @Inject(method = "quickMove", at = @At("HEAD"), cancellable = true)
  public void quickMove(PlayerEntity player, int slot, CallbackInfoReturnable<ItemStack> cir) {
    TheCoins.LOGGER.info("Someone try to move menu. Ready to cancel");
    ItemStack itemStack = this.slots.get(slot).getStack();

    if (Menu.isMenu(itemStack)) {
      cir.setReturnValue(null);
      cir.cancel();
    }
  }
}
