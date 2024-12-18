package net.mirolls.thecoins.gui.function;

import net.minecraft.server.network.ServerPlayerEntity;
import net.mirolls.thecoins.database.thecoins.TheCoinsDBCreator;
import net.mirolls.thecoins.database.thecoins.TheCoinsDBUpdater;
import net.mirolls.thecoins.gui.SKBMenu;
import net.mirolls.thecoins.item.ItemStackGUI;
import net.mirolls.thecoins.skyblock.Profile;

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

  public static void registerCreateProfile() {
    ItemStackGUI.registryAction("createProfile", (player, args) -> {
      if (!player.getWorld().isClient) {
        Profile profile = TheCoinsDBCreator.createProfileForPlayer(Profile.generateProfile((ServerPlayerEntity) player, false));
        // 这里设置playing为false的原因非常简单 因为swap的时候会自动进行切换
        TheCoinsDBUpdater.swapProfile((ServerPlayerEntity) player, profile.profileID());
      }
    });
  }
}
