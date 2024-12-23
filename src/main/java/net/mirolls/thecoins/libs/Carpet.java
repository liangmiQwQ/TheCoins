package net.mirolls.thecoins.libs;

import net.minecraft.server.network.ServerPlayerEntity;
import net.mirolls.thecoins.TheCoins;

public class Carpet {
  private static final String FAKE_PLAYER_ENTITY_CLASS = "carpet.patches.EntityPlayerMPFake"; // 假人连接类的全路径

  // 这里是为了排除假人的干扰

  public static boolean isFakePlayer(ServerPlayerEntity player) {
    try {

      Class<?> EntityPlayerMPFake = Class.forName(FAKE_PLAYER_ENTITY_CLASS);

      return EntityPlayerMPFake.isInstance(player);
    } catch (ClassNotFoundException e) {
      TheCoins.LOGGER.warn("Cannot find the carpet mod.");
      return false;
    }
  }
}
