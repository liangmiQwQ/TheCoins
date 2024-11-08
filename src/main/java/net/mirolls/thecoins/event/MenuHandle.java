package net.mirolls.thecoins.event;

import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.network.ServerPlayerEntity;
import net.mirolls.thecoins.menu.Menu;

public class MenuHandle {
  public static void menuGive() {
    ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
      ServerPlayerEntity player = handler.getPlayer();
      Menu.replaceMenu(player);
    });
  }

}
