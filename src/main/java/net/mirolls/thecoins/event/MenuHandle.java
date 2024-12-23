package net.mirolls.thecoins.event;

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.network.ServerPlayerEntity;
import net.mirolls.thecoins.TheCoins;
import net.mirolls.thecoins.item.Menu;
import net.mirolls.thecoins.libs.Carpet;

public class MenuHandle {
  public static void menuGive() {
    ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
      if (!Carpet.isFakePlayer(handler.getPlayer())) {
        TheCoins.LOGGER.info(handler.getPlayer().getName().getString() + "has joined the game. Ready to replace menu");
        Menu.replaceMenu(handler.getPlayer());
      }
    });

    ServerPlayerEvents.AFTER_RESPAWN.register((oldPlayer, newPlayer, alive) -> {
      if (!Carpet.isFakePlayer(newPlayer)) {
        TheCoins.LOGGER.info(newPlayer.getName().getString() + "respawned. Ready to replace menu");
        Menu.replaceMenu(newPlayer);
      }
    });
  }

  public static void menuRemover() {
    ServerLivingEntityEvents.ALLOW_DEATH.register((handler, sender, server) -> {
      if (handler.isPlayer()) {
        ServerPlayerEntity player = (ServerPlayerEntity) handler;
        if (Carpet.isFakePlayer(player)) {
          TheCoins.LOGGER.info(player.getName().getString() + "is dying, ready to remove his menu");

          if (Menu.hasMenu(player)) {
            Menu.removeMenu(player);
          }
        }
      }
      return true;
    });
  }
}
