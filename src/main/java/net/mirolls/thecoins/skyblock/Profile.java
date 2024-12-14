package net.mirolls.thecoins.skyblock;

import net.minecraft.server.network.ServerPlayerEntity;
import net.mirolls.thecoins.libs.RandomCharacterPicker;
import net.mirolls.thecoins.libs.StringLocation;
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
    String inventory, boolean playing) {
  // each player has to have at least one profile. different players can have the same profile(coop)

  public static Profile generateProfile(ServerPlayerEntity player, boolean playing) {


//    ServerWorld serverWorld = player.getServer()

    return new Profile(
        UUID.randomUUID().toString(),
        RandomCharacterPicker.getRandomCharacter(),
        player.getUuidAsString(),
        0.0,
        player.totalExperience,
        StringLocation.encodeLocationAsString(player),
        StringLocation.encodeRespawnAsString(player),
        InventoryTransfer.enderChestInventoryAsJSON(player.getEnderChestInventory()),
        InventoryTransfer.playerInventoryAsJSON(player.getInventory()),
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
    data.put("playing", this.playing());

    return data;
  }
}
