package net.mirolls.thecoins;

import net.fabricmc.api.ModInitializer;
import net.mirolls.thecoins.event.MenuHandle;
import net.mirolls.thecoins.file.LanguageConfig;
import net.mirolls.thecoins.file.Translation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

public class TheCoins implements ModInitializer {
  public static final String MOD_ID = "thecoins";
  public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
  public static final List<String> LANGUAGES_LIST = Arrays.asList("zh_cn", "en_us");

  @Override
  public void onInitialize() {
    LOGGER.info("The Coins mod is running");
    MenuHandle.menuGive();
    MenuHandle.menuRemover();

    // init config files
    LanguageConfig.initLanguageConfigFileAtFirst();
    Translation.initTranslationAtFirst();
  }
}
