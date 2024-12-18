package net.mirolls.thecoins.gui.function;

import net.minecraft.server.network.ServerPlayerEntity;
import net.mirolls.thecoins.database.thecoins.TheCoinsDBUpdater;
import net.mirolls.thecoins.gui.SKBMenu;
import net.mirolls.thecoins.item.ItemStackGUI;

public class TheCoinsFunction {
  public static void registerReturnFunction() {
    ItemStackGUI.registryAction("returnToMenu", (player, args) -> {
      if (!player.getWorld().isClient) { // is server
        ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;
        serverPlayer.closeHandledScreen();
        SKBMenu.open(player);
      }
    });
  }

  public static void registerSwapProfileFunction() {
    ItemStackGUI.registryAction("swapProfile", (player, args) -> {
      if (!player.getWorld().isClient) {
        TheCoinsDBUpdater.swapProfile((ServerPlayerEntity) player, args[0]);
      }
    });
  }
}
