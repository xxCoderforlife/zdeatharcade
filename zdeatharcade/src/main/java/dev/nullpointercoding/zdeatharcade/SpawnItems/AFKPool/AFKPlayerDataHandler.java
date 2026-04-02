package dev.nullpointercoding.zdeatharcade.SpawnItems.AFKPool;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;

public class AFKPlayerDataHandler {
    private Map<Player, AFKPlayer> playerDataMap;

    public AFKPlayerDataHandler() {
        playerDataMap = new HashMap<Player, AFKPlayer>();
    }

    public void addPlayerData(Player player, Double cash, Double tokens) {
        AFKPlayer afkPlayer = new AFKPlayer(player, cash, tokens);
        playerDataMap.put(player, afkPlayer);
    }
    public void removePlayerData(Player player) {
        playerDataMap.remove(player);
    }

    public AFKPlayer getAFKPlayer(Player player) {
        return playerDataMap.get(player);
    }

    public void updatePlayerCash(Player player, Double cash) {
        AFKPlayer afkPlayer = playerDataMap.get(player);
        if (afkPlayer != null) {
            afkPlayer.setCash(afkPlayer.getCash() + cash);
        }
    }

    public void updatePlayerTokens(Player player, Double tokens) {
        AFKPlayer afkPlayer = playerDataMap.get(player);
        if (afkPlayer != null) {
            afkPlayer.setTokens(afkPlayer.getTokens() + tokens);
        }
    }
}
