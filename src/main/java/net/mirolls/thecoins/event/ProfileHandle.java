package net.mirolls.thecoins.event;

import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.mirolls.thecoins.TheCoins;
import net.mirolls.thecoins.libs.inventory.InventoryTransfer;

public class ProfileHandle {
  public static void firstProfileCreator() {
    ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
      TheCoins.LOGGER.info(InventoryTransfer.playerInventoryAsJSON(handler.player.getInventory()));
    });
  }
}
