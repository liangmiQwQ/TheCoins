package net.mirolls.thecoins;

import net.fabricmc.api.ModInitializer;
import net.mirolls.thecoins.event.PlayJoinEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TheCoins implements ModInitializer {
  public static final String MOD_ID = "thecoins";
  public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

  @Override
  public void onInitialize() {
    LOGGER.info("The Coins mod is running");
    PlayJoinEvent.menuGive();
  }
}
