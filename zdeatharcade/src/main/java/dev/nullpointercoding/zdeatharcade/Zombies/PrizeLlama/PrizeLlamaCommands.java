package dev.nullpointercoding.zdeatharcade.Zombies.PrizeLlama;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public class PrizeLlamaCommands implements TabExecutor{

    private PrizeLlama prizeLlama = new PrizeLlama();

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd,
            @NotNull String arg2, @NotNull String[] args) {
        List<String> tab = new ArrayList<String>();
        if(cmd.getName().equalsIgnoreCase("prizellama")){
            if(args.length == 1){
                tab.add("spawn");
            }
        }
        return tab;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String arg2,
            @NotNull String[] args) {
        if(cmd.getName().equalsIgnoreCase("prizellama")){
            if(sender instanceof Player){
                Player player = (Player) sender;
                if(args.length == 0){
                    player.sendMessage("§eUsage: §6/prizellama spawn");
                    return true;
                }
                if(args.length == 1){
                    if(args[0].equalsIgnoreCase("spawn")){
                        prizeLlama.spawnPrizeLlama(player.getLocation());
                        return true;
                    }
                }
            }
        }
        return true;
    }
    
}
