package net.mirolls.thecoins.gui;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.mirolls.thecoins.file.LanguageConfig;
import net.mirolls.thecoins.file.Translation;
import net.mirolls.thecoins.gui.screenHandles.ProfileScreenHandle;
import net.mirolls.thecoins.item.ItemStackGUI;
import net.mirolls.thecoins.libs.MinecraftColor;
import net.mirolls.thecoins.libs.ShowProfile;
import net.mirolls.thecoins.libs.SpecialItemClickedAction;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ProfileGUI implements NamedScreenHandlerFactory {
  public static final String GUI_ID = "PlayerProfiles";

  private final Translation translation;
  private final SimpleInventory GUI;

  public ProfileGUI(PlayerEntity player) {
    this.translation = new Translation(LanguageConfig.getPlayerLanguage(player.getUuidAsString()));
    if (!player.getWorld().isClient) {
      this.GUI = createGUI((ServerPlayerEntity) player);
    } else {
      throw new RuntimeException("Cannot create for player " + player.getName().getString() + " Because they're running at Client ");
    }

  }

  public ProfileGUI(PlayerEntity player, Translation translation) {
    this.translation = translation;
    if (!player.getWorld().isClient) {
      this.GUI = createGUI((ServerPlayerEntity) player);
    } else {
      throw new RuntimeException("Cannot create for player " + player.getName().getString() + " Because they're running at Client ");
    }

  }

  public static void openGUI(PlayerEntity player, Translation translation) {
    Translation realTranslation;
    realTranslation = Objects.requireNonNullElseGet(translation, () -> new Translation(LanguageConfig.getPlayerLanguage(player.getUuidAsString())));

    if (!player.getWorld().isClient) {
      ((ServerPlayerEntity) player).closeHandledScreen();
      player.openHandledScreen(new ProfileGUI(player, realTranslation));
//      TheCoins.LOGGER.info("GUI has been opened");
    }
  }

  private SimpleInventory createGUI(ServerPlayerEntity player) {
    SimpleInventory inventoryGUI = new SimpleInventory(54);

    ItemStack closeButton = ItemStackGUI.itemStackFactory(
        GUI_ID,
        Items.BARRIER,
        translation.getTranslation("GUIClose"),
        "#C35E12",
        List.of(),
        new SpecialItemClickedAction("Close", ""),
        "Close",
        null
    );

    ItemStack returnButton = ItemStackGUI.itemStackFactory(
        GUI_ID,
        Items.ARROW,
        translation.getTranslation("GUIReturn"),
        "#FFFFFF",
        List.of(),
        new SpecialItemClickedAction("Link", "returnToMenu"),
        "Link",
        null
    );

    ItemStack createButton = ItemStackGUI.itemStackFactory(
        GUI_ID,
        Items.OAK_BUTTON,
        translation.getTranslation("GUICreateProfile"),
        "#FFFFFF",
        List.of(),
        new SpecialItemClickedAction("Link", "confirm_createProfile"),
        "Link",
        null
    );

    ItemStack background = ItemStackGUI.itemStackFactory(
        GUI_ID,
        Items.BLACK_STAINED_GLASS_PANE,
        "",
        "",
        List.of(),
        new SpecialItemClickedAction("Background", ""),
        "Background",
        null
    );

    // 先填充一下
    ArrayList<Integer> emptySlots = new ArrayList<>();
    for (int i = 0; i < inventoryGUI.size(); i++) {
      if (i == 49) { // if close button
        inventoryGUI.setStack(i, closeButton);
      } else if (i == 48) {
        inventoryGUI.setStack(i, returnButton);
      } else if (i == 50) {
        inventoryGUI.setStack(i, createButton);
      } else if (i % 9 == 0 || (i + 1) % 9 == 0) {
        inventoryGUI.setStack(i, background);
      } else if (i < 8 || i > 44) {
        inventoryGUI.setStack(i, background);
      } else {
        emptySlots.add(i);
      }
    }

    // 处理各个profiles
    if (!player.getWorld().isClient) {
      ArrayList<ShowProfile> showProfiles = ShowProfile.getShowProfiles(player);
      for (int i = 0; i < showProfiles.size(); i++) {
        // every profile
        ShowProfile showProfile = showProfiles.get(i);
        Item itemTexture = showProfile.getPlaying() ? Items.MAP : Items.PAPER;

        List<MutableText> description = new ArrayList<>(List.of(
            showProfile.getPlaying() ?
                Text.literal(translation.getTranslation("playingProfile")).setStyle(
                    Style.EMPTY.withColor(MinecraftColor.hexToRgb("#AAAAAA"))
                ) :
                Text.literal(translation.getTranslation("clickToSwapProfile")).setStyle(
                    Style.EMPTY.withColor(MinecraftColor.hexToRgb("#AAAAAA"))
                ),
            Text.literal(" "),
            Text.literal(translation.getTranslation("players")).setStyle(
                Style.EMPTY.withColor(MinecraftColor.hexToRgb("#55FF55"))
            )
        ));


        for (String playerMembersName : showProfile.getPlayerNames()) {
          description.add(
              Text.literal(playerMembersName).setStyle(
                  Style.EMPTY.withColor(MinecraftColor.hexToRgb("#FFFFFF"))
              )
          );
        }

        ItemStack profileSelector = ItemStackGUI.itemStackFactory(
            GUI_ID,
            itemTexture,
            Text.literal(showProfile.getProfileName()).setStyle(
                showProfile.getPlaying() ? Style.EMPTY.withColor(MinecraftColor.hexToRgb("#55FF55"))
                    : Style.EMPTY.withColor(MinecraftColor.hexToRgb("#FFFF55"))

            ),
            description,
            // if click the selected profile, do not do anything
            showProfile.getPlaying() ? new SpecialItemClickedAction("Link", "")
                :
                new SpecialItemClickedAction("Link",
                    "confirm_swapProfile", new String[]{showProfile.getProfileID()}),
            // 仅仅需要创建swapProfile这一个函数就可以解决所有问题
            "Link",
            null
        );

        // 在此我也不使用 注册函数 => 销毁的逻辑了 修改原有代码 允许参杂额外参数

        inventoryGUI.setStack(emptySlots.get(i), profileSelector);
      }
    }

    return inventoryGUI;
  }

  @Override
  public Text getDisplayName() {
    return Text.literal(this.translation.getTranslation("profile"));
  }

  @Nullable
  @Override
  public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
    return new ProfileScreenHandle(syncId, GUI, playerInventory);
  }
}
