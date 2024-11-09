package net.mirolls.thecoins.mixin;

import net.minecraft.inventory.Inventory;
import net.minecraft.screen.AbstractRecipeScreenHandler;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(AbstractRecipeScreenHandler.class)
public abstract class AbstractRecipeScreenHandlerMixin<C extends Inventory> extends ScreenHandlerMixin {
}
