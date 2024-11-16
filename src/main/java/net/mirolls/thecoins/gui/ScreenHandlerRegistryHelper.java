package net.mirolls.thecoins.gui;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.mirolls.thecoins.TheCoins;

public class ScreenHandlerRegistryHelper {
  public static <T extends ScreenHandlerType<?>> T register(String name, T type) {
    return Registry.register(Registries.SCREEN_HANDLER, new Identifier(TheCoins.MOD_ID, name), type);
  }
}