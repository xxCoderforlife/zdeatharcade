package dev.nullpointercoding.zdeatharcade.Utils.VaultHookFolder;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EcoTabCommands implements TabCompleter {
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd,
            @NotNull String arg2, @NotNull String[] args) {

        List<String> tab = new ArrayList<String>();
        if (cmd.getName().equalsIgnoreCase("economy") || cmd.getName().equalsIgnoreCase("token")) {
            if (args.length == 1) {
                tab.add("add");
                tab.add("remove");
                tab.add("set");
                if (cmd.getName().equalsIgnoreCase("economy")) {
                    tab.add("bank");
                }
            }
            if (args.length == 2) {
                if (args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("remove")
                        || args[0].equalsIgnoreCase("set")) {
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        tab.add(p.getName());
                    }
                }
                if (cmd.getName().equalsIgnoreCase("economy") && args[0].equalsIgnoreCase("bank")) {
                    tab.add("create");
                    tab.add("balance");
                    tab.add("add");
                    tab.add("remove");
                    tab.add("set");
                }
            }
            if (args.length == 3) {
                if (args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("remove")
                        || args[0].equalsIgnoreCase("set")) {
                    tab.add("1");
                    tab.add("10");
                    tab.add("100");
                    tab.add("1000");
                }
                if (cmd.getName().equalsIgnoreCase("economy") && args[0].equalsIgnoreCase("bank")) {
                    if (args[1].equalsIgnoreCase("create") || args[1].equalsIgnoreCase("balance")
                            || args[1].equalsIgnoreCase("add") || args[1].equalsIgnoreCase("remove")
                            || args[1].equalsIgnoreCase("set")) {
                        for (Player p : Bukkit.getOnlinePlayers()) {
                            tab.add(p.getName());
                        }
                    }
                }
            }
            if (args.length == 4) {
                if (cmd.getName().equalsIgnoreCase("economy") && args[0].equalsIgnoreCase("bank")) {
                    if (args[1].equalsIgnoreCase("add") || args[1].equalsIgnoreCase("remove")
                            || args[1].equalsIgnoreCase("set")) {
                        tab.add("1");
                        tab.add("10");
                        tab.add("100");
                        tab.add("1000");
                    }
                }
            }
        }
        return tab;
    }
}
