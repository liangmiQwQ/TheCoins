package net.mirolls.thecoins.gui;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.mirolls.thecoins.libs.MinecraftColor;

import java.util.HashMap;

public class SKBMenu {
  private static final long COOL_DOWN_TIME = 3000;
  private static final long TICK = 200;
  private static final HashMap<String, Long> lastActionTime = new HashMap<>();
  private static final HashMap<String, Long> lastTickTime = new HashMap<>();


  public static void open(PlayerEntity player) {
    ticksCoolDown(player);
  }

  public static void open(LivingEntity entity) {
    if (entity.isPlayer()) {
      ticksCoolDown((PlayerEntity) entity);
    }

  }

  private static void ticksCoolDown(PlayerEntity player) {
    String playerUUID = player.getUuidAsString();
    long now = System.currentTimeMillis();
    if (lastTickTime.containsKey(playerUUID)) {
      if (now - lastTickTime.get(playerUUID) > TICK) {
        menuCoolDown(player, playerUUID, now);
      }
      // else: don't do anything
    } else {
      menuCoolDown(player, playerUUID, now);
    }

  }

  private static void menuCoolDown(PlayerEntity player, String playerUUID, long now) {
    lastTickTime.put(playerUUID, now);
    if (lastActionTime.containsKey(playerUUID)) {
      if (now - lastActionTime.get(playerUUID) > COOL_DOWN_TIME) {
        openGUI(player, playerUUID, now);
      } else {
        long needToWaitTime = COOL_DOWN_TIME - (now - lastActionTime.get(playerUUID));
        player.sendMessage(Text.literal(
            "The menu is still cooling down for "
                + String.format("%.1f", (float) needToWaitTime / 1000)
                + "s"
        ).setStyle(Style.EMPTY.withColor(MinecraftColor.hexToRgb("#FF0000"))));
      }
    } else {
      openGUI(player, playerUUID, now);
    }
  }

  private static void openGUI(PlayerEntity player, String playerUUID, long now) {
    lastActionTime.put(playerUUID, now);
    // do something there
    player.sendMessage(Text.literal("The GUI is not release now."));
  }
}
