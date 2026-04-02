package dev.nullpointercoding.zdeatharcade.Utils.PlaceHolders;

import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import dev.nullpointercoding.zdeatharcade.Main;
import dev.nullpointercoding.zdeatharcade.SpawnItems.AFKPool.AFKPool;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;

public class Placeholders extends PlaceholderExpansion{

    @SuppressWarnings("unused")
    private final Main plugin;

    public Placeholders(Main plugin) {
        this.plugin = plugin;
    }
    @Override
    public @NotNull String getAuthor() {
        return new String("NullPointerCoding");
    }

    @Override
    public @NotNull String getIdentifier() {
        return new String("zda");
    }

    @Override
    public @NotNull String getVersion() {
        return new String("0.0.1");
    }

    @Override
    public String onRequest(OfflinePlayer player, String params) {
        if(params.equalsIgnoreCase("playersinafkpool")){
            if(AFKPool.getPlayersInPool().size() == 0){
                return new String("§c§oCurrently no one is AFK");
            }
            return new String("§a§o" + AFKPool.getPlayersInPool().size() + " players are AFK");
        }
        return params;
    }
    
}
