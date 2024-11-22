package net.mirolls.thecoins.skyblock;

import java.util.ArrayList;

public class PluginsRegistry {
  private static final ArrayList<Plugin> registeredPlugins = new ArrayList<>();
  private static final ArrayList<String> registeredPluginNames = new ArrayList<>();


  public static void registry(Plugin plugin, String pluginName) {
    registeredPlugins.add(plugin);
    registeredPluginNames.add(pluginName);
  }

  public static ArrayList<Plugin> getRegisteredPlugins() {
    return registeredPlugins;
  }

  public static ArrayList<String> getRegisteredPluginName() {
    return registeredPluginNames;
  }

}
