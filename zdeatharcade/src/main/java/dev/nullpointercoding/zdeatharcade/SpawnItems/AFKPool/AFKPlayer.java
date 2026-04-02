package dev.nullpointercoding.zdeatharcade.SpawnItems.AFKPool;

import org.bukkit.entity.Player;

public class AFKPlayer{

    private Double cash;
    private Double tokens;
    private Player player;

    public AFKPlayer(Player player, Double cash, Double tokens) {
        this.player = player;
        this.cash = cash;
        this.tokens = tokens;
    }

    public Player getPlayer() {
        return player;
    }

    public Double getCash() {
        return cash;
    }

    public Double getTokens() {
        return tokens;
    }

    public void setCash(Double cash) {
        this.cash = cash;
    }

    public void setTokens(Double tokens) {
        this.tokens = tokens;
    }

}
