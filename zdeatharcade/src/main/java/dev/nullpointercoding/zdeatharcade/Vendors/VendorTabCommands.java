package dev.nullpointercoding.zdeatharcade.Vendors;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class VendorTabCommands implements TabCompleter {

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd,
            @NotNull String arg2, @NotNull String[] args) {
        List<String> tab = new ArrayList<String>();
        if (cmd.getName().equalsIgnoreCase("vendor")) {
            if (args.length == 1) {
                tab.add("spawn");
                tab.add("remove");
            }
            if (args.length == 2) {
                if (args[0].equalsIgnoreCase("spawn")) {
                    tab.add("farmer");
                    tab.add("miner");
                    tab.add("blackmarket");
                    tab.add("gunvendor");
                    tab.add("snackvendor");
                }
                if (args[0].equalsIgnoreCase("remove")) {
                    tab.add("farmer");
                    tab.add("miner");
                    tab.add("blackmarket");
                    tab.add("gunvendor");
                    tab.add("snackvendor");
                }

            }
        }
        return tab;
    }

}
