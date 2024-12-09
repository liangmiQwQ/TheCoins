package net.mirolls.thecoins.event;

import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.mirolls.thecoins.database.TheCoinsDB;
import net.mirolls.thecoins.file.LanguageConfig;
import net.mirolls.thecoins.file.Translation;
import net.mirolls.thecoins.libs.MinecraftColor;
import net.mirolls.thecoins.skyblock.Profile;

import java.util.regex.Pattern;

public class ProfileHandle {
  public static void firstProfileCreator() {
    ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
      // 如果玩家进入的时候没有profile则首先创建profile
      Profile profile = TheCoinsDB.getOrCreateProfilePlaying(handler.getPlayer());
      // 发送提示消息
      Translation translation = new Translation(LanguageConfig.getPlayerLanguage(handler.getPlayer().getUuidAsString()));

      String[] playingMessage = translation.getTranslation("YouAreNowPlaying").split(Pattern.quote("$[]]"));

      handler.getPlayer().sendMessage(
          Text.literal(
                  playingMessage[0]
              ).setStyle(Style.EMPTY.withColor(MinecraftColor.hexToRgb("#55FF55")))
              .append(Text.literal(
                  profile.profileName()
              ).setStyle(Style.EMPTY.withColor(MinecraftColor.hexToRgb("#FFAA00"))))
              .append(Text.literal(
                  playingMessage[1]
              ).setStyle(Style.EMPTY.withColor(MinecraftColor.hexToRgb("#55FF55"))))
      );

      // 额外send一个profileID
      handler.getPlayer().sendMessage(
          Text.literal(
              translation.getTranslation("YourProfileID").replace("${}", profile.profileID())
          ).setStyle(Style.EMPTY.withColor(MinecraftColor.hexToRgb("#AAAAAA")))
      );
    });
  }
}
