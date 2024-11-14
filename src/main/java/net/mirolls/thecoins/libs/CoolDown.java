package net.mirolls.thecoins.libs;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Style;
import net.minecraft.text.Text;

import java.util.HashMap;

public class CoolDown {
  private static final HashMap<String, Long> lastActionTime = new HashMap<>();
  private static final HashMap<String, Long> lastTickTime = new HashMap<>();

  public static void ticksCoolDown(PlayerEntity player, long tick, CoolDownCallback coolDownCallback) {
    String playerUUID = player.getUuidAsString();
    long now = System.currentTimeMillis();
    if (lastTickTime.containsKey(playerUUID)) {
      if (now - lastTickTime.get(playerUUID) > tick) {
        coolDownCallback.callback();
      }
      // else: don't do anything
    } else {
      coolDownCallback.callback();
    }
  }

  public static void ticksCoolDown(PlayerEntity player, long tick, CoolDownCallbackLoop coolDownCallbackLoop) {
    String playerUUID = player.getUuidAsString();
    long now = System.currentTimeMillis();
    if (lastTickTime.containsKey(playerUUID)) {
      if (now - lastTickTime.get(playerUUID) > tick) {
        coolDownCallbackLoop.callback(playerUUID, now);
      }
      // else: don't do anything
    } else {
      coolDownCallbackLoop.callback(playerUUID, now);
    }
  }

  public static void commandCoolDown(PlayerEntity player, long coolDownTime, String playerUUID, long now, CoolDownCallback coolDownCallback) {
    lastTickTime.put(playerUUID, now);
    if (lastActionTime.containsKey(playerUUID)) {
      if (now - lastActionTime.get(playerUUID) > coolDownTime) {
        lastActionTime.put(playerUUID, now);
        coolDownCallback.callback();
      } else {
        long needToWaitTime = coolDownTime - (now - lastActionTime.get(playerUUID));
        player.sendMessage(Text.literal(
            "The menu is still cooling down for "
                + String.format("%.1f", (float) needToWaitTime / 1000)
                + "s"
        ).setStyle(Style.EMPTY.withColor(MinecraftColor.hexToRgb("#FF0000"))));
      }
    } else {
      lastActionTime.put(playerUUID, now);
      coolDownCallback.callback();
    }
  }
}
