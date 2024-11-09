package net.mirolls.thecoins.event;

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.mirolls.thecoins.TheCoins;
import net.mirolls.thecoins.menu.Menu;

public class MenuHandle {
  public static void menuGive() {
    ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
      TheCoins.LOGGER.info(handler.getPlayer().getName().getString() + "has joined the game. Ready to replace menu");
      ServerPlayerEntity player = handler.getPlayer();
      Menu.replaceMenu(player);
    });

    ServerPlayerEvents.AFTER_RESPAWN.register((oldPlayer, newPlayer, alive) -> {
      TheCoins.LOGGER.info(newPlayer.getName().getString() + "respawned. Ready to replace menu");
      Menu.replaceMenu(newPlayer);
    });
  }

  public static void menuRemover() {
    ServerLivingEntityEvents.ALLOW_DEATH.register((handler, sender, server) -> {
      if (handler.isPlayer()) {
        ServerPlayerEntity player = (ServerPlayerEntity) handler;
        TheCoins.LOGGER.info(player.getName().getString() + "is dying, ready to remove his menu");
        ItemStack menu = Menu.getMenu(player);

        if (Menu.hasMenu(player, menu)) {
          player.getInventory().removeOne(menu);
          // remove this item
        }
      }
      return true;
    });
  }

  public static void menuProtector() {

  }


}
