package net.mirolls.thecoins.skyblock;

import net.mirolls.thecoins.TheCoins;

import java.util.ArrayList;

public class Plugin {
  // 用法: 直接new Plugin() 第一个参数里传个按钮 第二个参数传个lambda，里面一定要返回int，然后直接去PluginsRegistry里注册了
  private final ArrayList<PluginGetItemStack> menuItemStack; // menu上的itemStack，就是图标，通常使用factory构造
  private final ArrayList<PluginButtonLocation> itemStackLocations; // 数量应当和 `menuItemStack` 相同. 确保index对应，然后获取location直接调用方法
  // 这里不使用int而是PluginButtonLocation的原因主要考虑到不同用户安装插件的数量可能不同

  public Plugin(ArrayList<PluginGetItemStack> menuItemStack, ArrayList<PluginButtonLocation> itemStackLocations) {
    if (menuItemStack.size() == itemStackLocations.size()) {
      this.menuItemStack = menuItemStack;
      this.itemStackLocations = itemStackLocations;
    } else {
      TheCoins.LOGGER.error("The length of menuItemStack is not the same as the length of itemStackLocations");
      throw new RuntimeException("The length of menuItemStack is not the same as the length of itemStackLocations");
    }
  }

  public ArrayList<PluginGetItemStack> getMenuItemStack() {
    return menuItemStack;
  }

  public ArrayList<PluginButtonLocation> getItemStackLocations() {
    return itemStackLocations;
  }
}
