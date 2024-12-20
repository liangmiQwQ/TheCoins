package net.mirolls.thecoins;

import net.fabricmc.api.ModInitializer;
import net.mirolls.thecoins.database.SkyBlockDB;
import net.mirolls.thecoins.database.thecoins.TheCoinsDBCreator;
import net.mirolls.thecoins.event.MenuHandle;
import net.mirolls.thecoins.event.ProfileHandle;
import net.mirolls.thecoins.file.LanguageConfig;
import net.mirolls.thecoins.file.Translation;
import net.mirolls.thecoins.gui.function.TheCoinsFunction;
import net.mirolls.thecoins.gui.screenHandles.ConfirmScreenHandle;
import net.mirolls.thecoins.gui.screenHandles.ProfileScreenHandle;
import net.mirolls.thecoins.gui.screenHandles.SKBMenuScreenHandle;
import net.mirolls.thecoins.skyblock.TheCoinsPlugin;
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

    // init database
    SkyBlockDB.initSQLite();

    // init theCoins table
    TheCoinsDBCreator.createPlayerProfileTable();

    // init MenuHandle
    MenuHandle.menuGive();
    MenuHandle.menuRemover();

    // init ProfileHandle
    ProfileHandle.firstProfileCreator();
    ProfileHandle.profileUpdater();

    // init config files
    LanguageConfig.initLanguageConfigFileAtFirst();
    Translation.initTranslationAtFirst();

    // registry GUI functions
    TheCoinsFunction.registerReturnFunction();
    TheCoinsFunction.registerSwapProfileFunction();
    TheCoinsFunction.registerCreateProfile();

    // registry GUI
    SKBMenuScreenHandle.registerScreenHandlers();
    ProfileScreenHandle.registerScreenHandlers();
    ConfirmScreenHandle.registerScreenHandlers();

    // registry plugins
    TheCoinsPlugin.registry(); // self-plugin
  }
}
