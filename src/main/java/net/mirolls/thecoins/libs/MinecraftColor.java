package net.mirolls.thecoins.libs;

import java.awt.*;
import java.util.Random;

public class MinecraftColor {
  private static final Random random = new Random();


  public static int hexToRgb(String hexColor) {
    Color color = Color.decode(hexColor);
    return color.getRGB();
  }
}
