package net.mirolls.thecoins.libs;

public interface CoolDownCallbackLoop {
  void callback(String playerUUID, long now);
}
