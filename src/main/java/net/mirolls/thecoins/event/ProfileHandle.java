package net.mirolls.thecoins.event;

import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;

public class ProfileHandle {
  public static void firstProfileCreator() {
    ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
    });
  }
}
