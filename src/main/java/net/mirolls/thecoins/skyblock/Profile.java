package net.mirolls.thecoins.skyblock;

import net.minecraft.server.network.ServerPlayerEntity;
import net.mirolls.thecoins.libs.RandomCharacterPicker;
import net.mirolls.thecoins.libs.StringHelper;
import net.mirolls.thecoins.libs.inventory.InventoryTransfer;

import java.util.HashMap;
import java.util.UUID;

public record Profile(
    String profileID,
    String profileName,
    String playerUUID,
    double coins,
    int exp,
    String location, // [NETHER] (123,123,123)
    String respawnLocation,
    String enderChestInventory,
    String inventory,
    float health, // player.getHealth()
    String hunger, // StringHelper.encodeHungryAsString(player)
    String gameMode, // using player.interactionManager.getGameMode().getName(),
    // player.changeGameMode(net.minecraft.world.GameMode.byName(gameMode));

    // String advancement, 写代码给我写脑溢血了 这个功能删了 改成按玩家为单位了

//    String effect, 存档也给我写红文了 删了 切换的时候直接全部清空
    boolean playing) {
  // each player has to have at least one profile. different players can have the same profile(coop)

  public static Profile generateProfile(ServerPlayerEntity player, boolean playing) {
//    player.getServer().getAdvancementLoader().get()
    return new Profile(
        UUID.randomUUID().toString(),
        RandomCharacterPicker.getRandomCharacter(),
        player.getUuidAsString(),
        0.0,
        player.totalExperience,
        StringHelper.encodeLocationAsString(player),
        StringHelper.encodeRespawnAsString(player),
        InventoryTransfer.enderChestInventoryAsJSON(player.getEnderChestInventory()),
        InventoryTransfer.playerInventoryAsJSON(player.getInventory()),
        player.getHealth(),
        StringHelper.encodeHungryAsString(player),
        player.interactionManager.getGameMode().getName(),
        playing
    );
  }

  public HashMap<String, Object> asHashMap() {
    HashMap<String, Object> data = new HashMap<>();

    data.put("profileID", this.profileID());
    data.put("profileName", this.profileName());
    data.put("playerUUID", this.playerUUID());
    data.put("coins", this.coins());
    data.put("exp", this.exp());
    data.put("location", this.location);
    data.put("respawnLocation", this.respawnLocation);
    data.put("enderChestInventory", this.enderChestInventory());
    data.put("inventory", this.inventory());
    data.put("health", this.health());
    data.put("hunger", this.hunger());
    data.put("gameMode", this.gameMode());
    data.put("playing", this.playing());


    return data;
  }
}
