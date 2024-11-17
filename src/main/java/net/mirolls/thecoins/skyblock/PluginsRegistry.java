package net.mirolls.thecoins.skyblock;

import java.util.ArrayList;

public class PluginsRegistry {
  private static final ArrayList<Plugin> registeredPlugins = new ArrayList<>();

  public static void registry(Plugin plugin) {
    registeredPlugins.add(plugin);
  }

  public static ArrayList<Plugin> getRegisteredPlugins() {
    return registeredPlugins;
  }

}
