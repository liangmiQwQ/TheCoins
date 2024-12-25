package net.mirolls.thecoins.event;

import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.mirolls.thecoins.database.thecoins.TheCoinsDBCreator;
import net.mirolls.thecoins.database.thecoins.TheCoinsDBUpdater;
import net.mirolls.thecoins.file.LanguageConfig;
import net.mirolls.thecoins.file.Translation;
import net.mirolls.thecoins.libs.Carpet;
import net.mirolls.thecoins.libs.MinecraftColor;
import net.mirolls.thecoins.skyblock.Profile;

import java.util.regex.Pattern;

public class ProfileHandle {
  public static void firstProfileCreator() {
    ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
      if (!Carpet.isFakePlayer(handler.getPlayer())) {
        // 如果玩家进入的时候没有profile则首先创建profile
        Profile profile = TheCoinsDBCreator.getOrCreateProfilePlaying(handler.getPlayer());
        // 发送提示消息
        Translation translation = new Translation(LanguageConfig.getPlayerLanguage(handler.getPlayer().getUuidAsString()));

        String[] playingMessage = translation.getTranslation("YouAreNowPlaying").split(Pattern.quote("${}"));

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
      }
    });
  }

  public static void profileUpdater() {
    // update players inventory data
    // 死后 退游戏的时候
    // disconnect
    ServerPlayConnectionEvents.DISCONNECT.register(((handler, server) -> {
      if (Carpet.isFakePlayer(handler.getPlayer())) {
        TheCoinsDBUpdater.updateProfile(handler.getPlayer());
      }
    }));

    // after spawn
    ServerPlayerEvents.AFTER_RESPAWN.register((oldPlayer, newPlayer, alive) -> {
      if (Carpet.isFakePlayer(newPlayer)) {
        TheCoinsDBUpdater.updateProfile(newPlayer);
      }
    });
  }
}
