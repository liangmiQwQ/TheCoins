package net.mirolls.thecoins.gui;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;

public class SKBMenu {
  public static void open(PlayerEntity player) {
    openGUI(player);
  }

  public static void open(LivingEntity entity) {
    if (entity.isPlayer()) {
      openGUI((PlayerEntity) entity);
    }

  }

  private static void openGUI(PlayerEntity player) {
    player.sendMessage(Text.literal("The GUI is not release now."));
  }
}
