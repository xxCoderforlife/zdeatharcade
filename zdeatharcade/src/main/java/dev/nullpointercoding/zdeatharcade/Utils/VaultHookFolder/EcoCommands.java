package dev.nullpointercoding.zdeatharcade.Utils.VaultHookFolder;

import java.math.BigDecimal;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import dev.nullpointercoding.zdeatharcade.Main;
import dev.nullpointercoding.zdeatharcade.Utils.BankConfigManager;
import dev.nullpointercoding.zdeatharcade.Utils.PlayerConfigManager;
import net.milkbowl.vault2.economy.Economy;

public class EcoCommands implements CommandExecutor {

    private Main plugin = Main.getInstance();
    Economy econ = new VaultHook();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String arg2,
            @NotNull String[] args) {
        if (cmd.getName().equalsIgnoreCase("economy")) {
            if (args.length == 0) {
                sender.sendMessage("§cUsage: /economy <add|remove|set> <player> <amount>");
                sender.sendMessage("§cUsage: /economy bank <create|balance|add|remove|set> <player> [amount]");
                return true;
            }
            if (args[0].equalsIgnoreCase("bank")) {
                if (args.length < 3) {
                    sender.sendMessage("§cUsage: /economy bank <create|balance|add|remove|set> <player> [amount]");
                    return true;
                }

                Player target = plugin.getServer().getPlayer(args[2]);
                if (target == null) {
                    sender.sendMessage("§cPlayer not found!");
                    return true;
                }

                BankConfigManager bankConfigManager = new BankConfigManager(target.getUniqueId());

                if (args[1].equalsIgnoreCase("create")) {
                    if (bankConfigManager.exists()) {
                        sender.sendMessage("§c" + target.getName() + " already has a bank account.");
                        return true;
                    }
                    bankConfigManager.createConfig(target.getUniqueId());
                    sender.sendMessage("§aCreated a bank account for " + target.getName() + "!");
                    return true;
                }

                if (args[1].equalsIgnoreCase("balance")) {
                    if (!bankConfigManager.exists()) {
                        sender.sendMessage("§c" + target.getName() + " does not have a bank account yet.");
                        return true;
                    }
                    sender.sendMessage("§a" + target.getName() + "'s bank balance: "
                            + String.format("$%,.2f", bankConfigManager.getBankBalance()));
                    return true;
                }

                if (args.length < 4) {
                    sender.sendMessage("§cUsage: /economy bank <add|remove|set> <player> <amount>");
                    return true;
                }

                BigDecimal amount;
                try {
                    amount = new BigDecimal(args[3]);
                } catch (NumberFormatException ex) {
                    sender.sendMessage("§cInvalid amount!");
                    return true;
                }

                if (amount.compareTo(BigDecimal.ZERO) < 0) {
                    sender.sendMessage("§cAmount cannot be negative!");
                    return true;
                }

                if (!bankConfigManager.exists()) {
                    bankConfigManager.createConfig(target.getUniqueId());
                }

                if (args[1].equalsIgnoreCase("add")) {
                    bankConfigManager.setBankBalance(bankConfigManager.getBankBalance().add(amount));
                    sender.sendMessage("§aAdded " + String.format("$%,.2f", amount) + " to " + target.getName() + "'s bank account!");
                    return true;
                }
                if (args[1].equalsIgnoreCase("remove")) {
                    BigDecimal currentBalance = bankConfigManager.getBankBalance();
                    if (currentBalance.compareTo(amount) < 0) {
                        sender.sendMessage("§cInsufficient bank funds to remove that amount.");
                        return true;
                    }
                    bankConfigManager.setBankBalance(currentBalance.subtract(amount));
                    sender.sendMessage("§aRemoved " + String.format("$%,.2f", amount) + " from " + target.getName() + "'s bank account!");
                    return true;
                }
                if (args[1].equalsIgnoreCase("set")) {
                    bankConfigManager.setBankBalance(amount);
                    sender.sendMessage("§aSet " + target.getName() + "'s bank balance to " + String.format("$%,.2f", amount) + "!");
                    return true;
                }

                sender.sendMessage("§cUnknown bank subcommand. Use create, balance, add, remove, or set.");
                return true;
            }
            if (args.length == 1) {
                sender.sendMessage("§cUsage: /economy <add|remove|set> <player> <amount>");
                return true;
            }
            if (args.length == 2) {
                sender.sendMessage("§cUsage: /economy <add|remove|set> <player> <amount>");
                return true;
            }
            if (args.length == 3) {
                if (args[0].equalsIgnoreCase("add")) {
                    Player target = plugin.getServer().getPlayer(args[1]);
                    if (target == null) {
                        sender.sendMessage("§cPlayer not found!");
                        return true;
                    }
                    BigDecimal amount = new BigDecimal(args[2]);
                    econ.deposit("zDeathArcade", target.getUniqueId(), amount);
                    sender.sendMessage("§aAdded " + String.format("$%,.2f", amount) + " to " + target.getName() + "'s balance!");
                    return true;
                }
                if (args[0].equalsIgnoreCase("remove")) {
                    Player target = plugin.getServer().getPlayer(args[1]);
                    if (target == null) {
                        sender.sendMessage("§cPlayer not found!");
                        return true;
                    }
                    BigDecimal amount = new BigDecimal(args[2]);
                    econ.withdraw("zDeathArcade", target.getUniqueId(), amount);
                    sender.sendMessage("§aRemoved " + String.format("$%,.2f", amount) + " from " + target.getName() + "'s balance!");
                    return true;
                }
                if (args[0].equalsIgnoreCase("set")) {
                    Player target = plugin.getServer().getPlayer(args[1]);
                    if (target == null) {
                        sender.sendMessage("§cPlayer not found!");
                        return true;
                    }
                    BigDecimal amount = new BigDecimal(args[2]);
                    econ.withdraw("zDeathArcade", target.getUniqueId(), econ.balance("zDeathArcade", target.getUniqueId()));
                    econ.deposit("zDeathArcade", target.getUniqueId(), amount);
                    sender.sendMessage("§aSet " + target.getName() + "'s balance to " + String.format("$%,.2f", amount) + "!");
                    return true;
                }
            }
        }
        if (cmd.getName().equalsIgnoreCase("token")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (args.length == 0) {
                    player.sendMessage("§cUsage: /token <add|remove|set> <player> <amount>");
                    return true;
                }
                if (args.length == 1) {
                    player.sendMessage("§cUsage: /token <add|remove|set> <player> <amount>");
                    return true;
                }
                if (args.length == 2) {
                    player.sendMessage("§cUsage: /token <add|remove|set> <player> <amount>");
                    return true;
                }
                if (args.length == 3) {
                    if (args[0].equalsIgnoreCase("add")) {
                        Player target = plugin.getServer().getPlayer(args[1]);
                        if (target == null) {
                            player.sendMessage("§cPlayer not found!");
                            return true;
                        }
                        PlayerConfigManager pcm = new PlayerConfigManager(target.getUniqueId());
                        BigDecimal amount = new BigDecimal(args[2]);
                        pcm.setTokens(amount.add(new BigDecimal(pcm.getTokens())));
                        player.sendMessage("§aAdded " + String.format("$%,.2f", amount) + " to " + target.getName() + "'s tokens!");
                        return true;
                    }
                    if (args[0].equalsIgnoreCase("remove")) {
                        Player target = plugin.getServer().getPlayer(args[1]);
                        if (target == null) {
                            player.sendMessage("§cPlayer not found!");
                            return true;
                        }
                        PlayerConfigManager pcm = new PlayerConfigManager(target.getUniqueId());
                        BigDecimal amount = new BigDecimal(args[2]);
                        pcm.setTokens(new BigDecimal(pcm.getTokens()).subtract(amount));
                        player.sendMessage("§aRemoved " + String.format("$%,.2f", amount) + " from " + target.getName() + "'s tokens!");
                        return true;
                    }
                    if (args[0].equalsIgnoreCase("set")) {
                        Player target = plugin.getServer().getPlayer(args[1]);
                        if (target == null) {
                            player.sendMessage("§cPlayer not found!");
                            return true;
                        }
                        PlayerConfigManager pcm = new PlayerConfigManager(target.getUniqueId());
                        BigDecimal amount = new BigDecimal(args[2]);
                        pcm.setTokens(amount);
                        player.sendMessage("§aSet " + target.getName() + "'s tokens to " + String.format("$%,.2f", amount) + "!");
                        return true;
                    }
                }
            }
        }
        return true;
    }

}
