package net.mirolls.thecoins.libs;

import net.minecraft.entity.player.PlayerEntity;

public interface CoolDownCallbackLoop {
  void callback(PlayerEntity player, String playerUUID, long now);
}
