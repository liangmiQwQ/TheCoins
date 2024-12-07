package net.mirolls.thecoins.skyblock;

import net.minecraft.server.network.ServerPlayerEntity;
import net.mirolls.thecoins.libs.RandomCharacterPicker;
import net.mirolls.thecoins.libs.inventory.InventoryTransfer;

import java.util.HashMap;
import java.util.UUID;

public record Profile(
    String profileID,
    String profileName,
    String playerUUID,
    double coins,
    String enderChestInventory,
    String inventory, boolean playing) {
  // each player has to have at least one profile. different players can have the same profile(coop)

  public static Profile generateProfile(ServerPlayerEntity player, boolean playing) {
    return new Profile(
        UUID.randomUUID().toString(),
        RandomCharacterPicker.getRandomCharacter(),
        player.getUuidAsString(),
        0.0,
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
    data.put("enderChestInventory", this.enderChestInventory());
    data.put("inventory", this.inventory());
    data.put("isPlaying", this.playing());

    return data;
  }
}
