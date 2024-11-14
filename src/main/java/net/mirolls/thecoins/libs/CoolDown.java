package net.mirolls.thecoins.libs;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.mirolls.thecoins.TheCoins;
import net.mirolls.thecoins.file.Translation;

import java.util.HashMap;

public class CoolDown {
  private static final HashMap<String, Long> lastActionTime = new HashMap<>();
  private static final HashMap<String, Long> lastTickTime = new HashMap<>();

  public static void ticksCoolDown(String playerUUID, long tick, CoolDownCallback coolDownCallback) {
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

  public static void ticksCoolDown(String playerUUID, long tick, CoolDownCallbackLoop coolDownCallbackLoop) {
    long now = System.currentTimeMillis();
    if (lastTickTime.containsKey(playerUUID)) {
      if (now - lastTickTime.get(playerUUID) > tick) {
        coolDownCallbackLoop.callback(now);
      }
      // else: don't do anything
    } else {
      coolDownCallbackLoop.callback(now);
    }
  }

  public static void commandCoolDown(PlayerEntity player, long coolDownTime, String playerUUID, long now, Translation translation, CoolDownCallback coolDownCallback) {
    lastTickTime.put(playerUUID, now);
    if (lastActionTime.containsKey(playerUUID)) {
      if (now - lastActionTime.get(playerUUID) > coolDownTime) {
        lastActionTime.put(playerUUID, now);
        coolDownCallback.callback();
      } else {
        long needToWaitTime = coolDownTime - (now - lastActionTime.get(playerUUID));

        // get translation
        String commandCoolDownRawString = translation.getTranslation("commandCoolDown");
        TheCoins.LOGGER.info(commandCoolDownRawString);
        player.sendMessage(Text.literal(
            commandCoolDownRawString.replace("${}", String.format("%.1f", (float) needToWaitTime / 1000))
        ).setStyle(Style.EMPTY.withColor(MinecraftColor.hexToRgb("#FF0000"))));
      }
    } else {
      lastActionTime.put(playerUUID, now);
      coolDownCallback.callback();
    }
  }

  public static void commandCoolDown(PlayerEntity player, long coolDownTime, String playerUUID, long now, Translation translation, CoolDownCallbackEnd coolDownCallbackEnd) {
    lastTickTime.put(playerUUID, now);
    if (lastActionTime.containsKey(playerUUID)) {
      if (now - lastActionTime.get(playerUUID) > coolDownTime) {
        lastActionTime.put(playerUUID, now);
        coolDownCallbackEnd.callback(translation);
      } else {
        long needToWaitTime = coolDownTime - (now - lastActionTime.get(playerUUID));

        // get translation
        String commandCoolDownRawString = translation.getTranslation("commandCoolDown");
        player.sendMessage(Text.literal(
            commandCoolDownRawString.replace("${}", String.format("%.1f", (float) needToWaitTime / 1000))
        ).setStyle(Style.EMPTY.withColor(MinecraftColor.hexToRgb("#FF0000"))));
      }
    } else {
      lastActionTime.put(playerUUID, now);
      coolDownCallbackEnd.callback(translation);
    }
  }
}
