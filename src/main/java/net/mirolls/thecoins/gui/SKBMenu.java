package net.mirolls.thecoins.gui;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.mirolls.thecoins.libs.CoolDown;

public class SKBMenu {
  private static final long COOL_DOWN_TIME = 3000;
  private static final long TICK = 200;


  public static void open(PlayerEntity player) {
    CoolDown.ticksCoolDown(player, TICK, (PlayerEntity playerEntity, String playerUUID, long now) -> {
      CoolDown.commandCoolDown(playerEntity, COOL_DOWN_TIME, playerUUID, now, () -> {
        openGUI(player);
      });
    });
  }

  public static void open(LivingEntity entity) {
    if (entity.isPlayer()) {
      CoolDown.ticksCoolDown((PlayerEntity) entity, TICK, (PlayerEntity playerEntity, String playerUUID, long now) -> {
        CoolDown.commandCoolDown(playerEntity, COOL_DOWN_TIME, playerUUID, now, () -> {
          openGUI((PlayerEntity) entity);
        });
      });
    }

  }

  private static void openGUI(PlayerEntity player) {
    player.sendMessage(Text.literal("The GUI is not release now."));
  }
}
