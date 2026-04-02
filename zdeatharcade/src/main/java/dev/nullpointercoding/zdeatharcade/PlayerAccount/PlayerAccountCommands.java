package dev.nullpointercoding.zdeatharcade.PlayerAccount;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;

public class PlayerAccountCommands implements CommandExecutor {

    //TODO: Add cache checking for Bank Account and Player Account to prevent loading from disk every time.

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String arg2,
            @NotNull String[] args) {
        if (cmd.getName().equalsIgnoreCase("account")) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                if (!(p.hasPermission("zdeatharcade.account"))) {
                    p.sendMessage("§cYou do not have permission to use this command!");
                    return true;
                }
                if (!(isPlayerinSpawn(p))) {
                    p.sendMessage("§cYou must be in spawn to use this command!");
                    return true;
                }
                new PlayerAccountGUI(p).openGUI(p);
            }
        }
        if (cmd.getName().equalsIgnoreCase("settings")) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                if (!(p.hasPermission("zdeatharcade.settings"))) {
                    p.sendMessage("§cYou do not have permission to use this command!");
                    return true;
                }
                if (!(isPlayerinSpawn(p))) {
                    p.sendMessage("§cYou must be in spawn to use this command!");
                    return true;
                }
                new PlayerSettingsGUI(p).onInventoryOpen(p);
            }
        }
        if(cmd.getName().equalsIgnoreCase("stats")){
            if(sender instanceof Player) {
                Player player = (Player) sender;
                if (!(player.hasPermission("zdeatharcade.stats"))) {
                    player.sendMessage("§cYou do not have permission to use this command!");
                    return true;
                }
                player.sendMessage("Health: " + player.getHealth());
                player.sendMessage("Food: " + player.getFoodLevel());
                player.sendMessage("Saturation: " + player.getSaturation());
            }
        }
        return true;
    }

    private boolean isPlayerinSpawn(Player p) {
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionQuery query = container.createQuery();
        ApplicableRegionSet set = query.getApplicableRegions(BukkitAdapter.adapt(p.getLocation()));
        for (ProtectedRegion pr : set)
            if (pr.getId().equalsIgnoreCase("spawn"))
                return true;
        return false;
    }

}
