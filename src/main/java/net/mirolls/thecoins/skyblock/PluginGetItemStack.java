package net.mirolls.thecoins.skyblock;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.mirolls.thecoins.file.Translation;

public interface PluginGetItemStack {
  ItemStack getItemStack(Translation translation, PlayerEntity player);
}
