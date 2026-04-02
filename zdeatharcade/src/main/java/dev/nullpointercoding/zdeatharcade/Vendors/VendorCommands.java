package dev.nullpointercoding.zdeatharcade.Vendors;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import net.kyori.adventure.text.Component;

public class VendorCommands implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String arg2,
            @NotNull String[] args) {
        if (cmd.getName().equalsIgnoreCase("vendor")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (args.length == 0) {
                    if (!(player.hasPermission("zdeatharcade.vendor"))) {
                        player.sendMessage("You do not have permission to use this command");
                    }
                    player.sendMessage(Component.text("Vendor Base Command"));
                }
                if (args.length == 1) {
                    if (args[0].equalsIgnoreCase("spawn")) {
                        player.sendMessage(Component.text("Please specify a vendor to spawn"));
                    }
                    if (args[0].equalsIgnoreCase("remove")) {
                        player.sendMessage(Component.text("Please specify a vendor to remove"));
                    }
                }
                if (args.length == 2) {
                    if (args[0].equalsIgnoreCase("spawn")) {
                        if (args[1].equalsIgnoreCase("farmer")) {
                            player.sendMessage(Component.text("Spawning Farmer Vendor"));
                            FarmerVendor.spawnFarmerVendor(player, 27536);
                        }
                        if (args[1].equalsIgnoreCase("miner")) {
                            player.sendMessage(Component.text("Spawning Miner Vendor"));
                            MinerVendor.spawnMinerVendor(player, 27636);
                        }
                        if (args[1].equalsIgnoreCase("blackmarket")) {
                            player.sendMessage(Component.text("Spawning BlackMarket Vendor"));
                            BlackMarketVendor.spawnblackMarketVendor(player, 27736);
                        }
                        if (args[1].equalsIgnoreCase("gunvendor")) {
                            player.sendMessage(Component.text("Spawning Gun Vendor"));
                            GunVendor.spawnGunVendor(player, 27836);
                        }
                        if (args[1].equalsIgnoreCase("snackvendor")) {
                            player.sendMessage(Component.text("Spawning Snack Vendor"));
                            SnackVendor.spawnsnackVen(player, 28936);
                        }
                    }
                    if (args[0].equalsIgnoreCase("remove")) {
                        if (args[1].equalsIgnoreCase("farmer")) {
                            player.sendMessage(Component.text("Removing Farmer Vendor"));
                            FarmerVendor.removeFarmerVendor(player);
                            ;
                        }
                        if (args[1].equalsIgnoreCase("miner")) {
                            player.sendMessage(Component.text("Removing Miner Vendor"));
                            MinerVendor.removeMinerVendor(player);
                        }
                        if (args[1].equalsIgnoreCase("blackmarket")) {
                            player.sendMessage(Component.text("Removing BlackMarket Vendor"));
                            BlackMarketVendor.removeblackMarketVendor(player);
                        }
                        if (args[1].equalsIgnoreCase("gunvendor")) {
                            player.sendMessage(Component.text("Removing Gun Vendor"));
                            GunVendor.removeGunVendor(player);
                        }
                        if (args[1].equalsIgnoreCase("snackvendor")) {
                            player.sendMessage(Component.text("Removing Snack Vendor"));
                            SnackVendor.removesnackVendor(player);
                        }

                    }
                }
            }
        }
        return true;
    }

}
