package net.mirolls.thecoins.item;

import net.minecraft.entity.player.PlayerEntity;

public interface ItemStackGUICallBack {
  void callback(PlayerEntity player, String[] args);
}
