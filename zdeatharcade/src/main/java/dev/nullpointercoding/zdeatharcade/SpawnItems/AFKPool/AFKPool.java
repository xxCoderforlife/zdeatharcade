package dev.nullpointercoding.zdeatharcade.SpawnItems.AFKPool;

import java.math.BigDecimal;
import java.util.ArrayList;

//import java.util.HashMap;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.block.sign.SignSide;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;

import dev.nullpointercoding.zdeatharcade.Main;
import dev.nullpointercoding.zdeatharcade.Utils.PlayerConfigManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.title.TitlePart;

public class AFKPool implements Listener {

    // TODO: Find a faster way to check and get the PlayerRewards, I hate this
    // method.
    private static ArrayList<Player> playersInPool = new ArrayList<Player>();
    private ArrayList<Player> leavingPool = new ArrayList<Player>();
    // private HashMap<Player, Integer> playerAFKTime = new HashMap<Player,
    // Integer>();
    private AFKPlayerDataHandler playerDataHandler = new AFKPlayerDataHandler();
    private BukkitTask akfTask;
    private Main plugin = Main.getInstance();

    public AFKPool() {

    }

    @EventHandler
    public void onPlayerEnterPool(PlayerMoveEvent e) {
        Player player = (Player) e.getPlayer();
        Location from = e.getFrom();
        Location to = e.getTo();

        if (from.getBlockX() != to.getBlockX() || from.getBlockY() != to.getBlockY()
                || from.getBlockZ() != to.getBlockZ()) {
            /*
             * Check if the player is in water
             * If the player is in water, check if the player is in the AFK Pool
             */
            Location loc = player.getLocation().subtract(e.getTo(), 0, 1, 0);
            if (loc.getBlock().getType() == Material.WATER && isPlayerinAFKPool(player)) {
                addPlayerToPool(player);
            }
        }
    }

    private void addPlayerToPool(Player player) {
        if (!(playersInPool.contains(player))) {
            playerDataHandler.addPlayerData(player, 0.0, 0.0);
            playersInPool.add(player);
            checkForPlayersInPool(player);
            player.sendTitlePart(TitlePart.TITLE,
                    Component.text("You are now AFK", NamedTextColor.LIGHT_PURPLE, TextDecoration.ITALIC));
            player.sendTitlePart(TitlePart.SUBTITLE, Component.text("You will earn cash and tokens for being AFK",
                    NamedTextColor.GREEN, TextDecoration.ITALIC));

        }
    }

    private boolean isPlayerinAFKPool(Player p) {
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionQuery query = container.createQuery();
        ApplicableRegionSet set = query.getApplicableRegions(BukkitAdapter.adapt(p.getLocation()));
        for (ProtectedRegion pr : set)
            if (pr.getId().equalsIgnoreCase("afkpool"))
                return true;
        return false;
    }

    @EventHandler
    public void onRightClickEvent(PlayerInteractEvent e) {
        if (e.getClickedBlock() == null) {
            return;
        }

        if (e.getClickedBlock().getType().toString().contains("SIGN")) {
            Player whoClicked = (Player) e.getPlayer();
            Sign sign = (Sign) e.getClickedBlock().getState();
            SignSide side = sign.getTargetSide(whoClicked);
            if (side.line(0).equals(Component.text("AFK POOL", NamedTextColor.AQUA, TextDecoration.BOLD))) {
                if (!(leavingPool.contains(e.getPlayer()))) {
                    AFKPlayer afkPlayer = playerDataHandler.getAFKPlayer(whoClicked);
                    leavingPool.add(whoClicked);
                    whoClicked.sendActionBar(Component.text("Calculating earning....", NamedTextColor.GREEN));

                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            PlayerConfigManager PCM = new PlayerConfigManager(whoClicked.getUniqueId());
                            whoClicked.teleportAsync(whoClicked.getWorld().getSpawnLocation(), TeleportCause.PLUGIN);
                            playersInPool.remove(e.getPlayer());
                            leavingPool.remove(e.getPlayer());
                            whoClicked.sendTitlePart(TitlePart.TITLE, Component.text("You are no longer AFK",
                                    NamedTextColor.LIGHT_PURPLE, TextDecoration.ITALIC));
                            if (afkPlayer.getCash() + afkPlayer.getTokens() == 0) {
                                whoClicked.sendMessage(Component.text("You did not earn any rewards for being AFK",
                                        NamedTextColor.DARK_RED, TextDecoration.ITALIC).hoverEvent(
                                                Component.text("You need to stay in the pool longer to earn rewards",
                                                        NamedTextColor.GRAY, TextDecoration.ITALIC)));
                                playerDataHandler.removePlayerData(whoClicked);

                            } else {
                                whoClicked.sendMessage(Component.text("You earned ")
                                        .append(Component.text(afkPlayer.getCash() + " Cash", NamedTextColor.GREEN,
                                                TextDecoration.ITALIC)
                                                .hoverEvent(Component.text("Cash is used at the Shop",
                                                        NamedTextColor.GRAY, TextDecoration.ITALIC)))
                                        .append(Component.text(" and "))
                                        .append(Component.text(afkPlayer.getTokens() + " Tokens",
                                                NamedTextColor.GOLD,
                                                TextDecoration.ITALIC)
                                                .hoverEvent(Component.text("Tokens are used at the BlackMarket Dealer",
                                                        NamedTextColor.GRAY, TextDecoration.ITALIC))));
                                PCM.addBalance(BigDecimal.valueOf(afkPlayer.getCash()));
                                PCM.addTokens(BigDecimal.valueOf(afkPlayer.getTokens()));
                                playerDataHandler.removePlayerData(whoClicked);
                            }
                        }

                    }.runTaskLater(plugin, 20 * 5);
                } else {
                    whoClicked.sendMessage(Component.text("You are already leaving the pool",
                            NamedTextColor.DARK_RED, TextDecoration.ITALIC)
                            .hoverEvent(Component.text("We are getting all the Money and Tokens ready!",
                                    NamedTextColor.GRAY, TextDecoration.ITALIC)));
                }
            }
        }
    }

    @EventHandler
    public void onLeaveSignCreate(SignChangeEvent e) {
        Player whoCreated = (Player) e.getPlayer();

        if (e.lines().get(0).equals(Component.text("[afk]"))) {
            e.line(0, Component.text("AFK POOL", NamedTextColor.AQUA, TextDecoration.BOLD));
            e.line(2, Component.text("EXIT", NamedTextColor.DARK_RED, TextDecoration.ITALIC));
            whoCreated.sendMessage(
                Component.text("AFK Pool Exit Sign Created!", NamedTextColor.GREEN, TextDecoration.BOLD));
        }
    }

    private void checkForPlayersInPool(Player afkplayer) {
        if (akfTask == null || akfTask.isCancelled()) {

            akfTask = new BukkitRunnable() {

                @Override
                public void run() {

                    if (playersInPool.isEmpty()) {
                        this.cancel();
                        Bukkit.getConsoleSender().sendMessage("No players in pool, stopping task.");
                    }
                    for (Player p : playersInPool) {
                        playerDataHandler.updatePlayerCash(afkplayer, 0.25);
                        playerDataHandler.updatePlayerTokens(afkplayer, 0.02);
                        p.sendMessage(Component.text("+ $0.25", NamedTextColor.GREEN,
                                TextDecoration.ITALIC)
                                .hoverEvent(Component.text("Cash can be used to buy most items.", NamedTextColor.GREEN,
                                        TextDecoration.ITALIC)));
                        p.sendMessage(Component.text("+ 0.02 Tokens", NamedTextColor.GOLD,
                                TextDecoration.ITALIC).hoverEvent(
                                        Component.text("Tokens can be used to buy items at the Black Martket Vendor.",
                                                NamedTextColor.GREEN, TextDecoration.ITALIC)));

                    }
                }

            }.runTaskTimer(plugin, (long) 20 * 60, (long) 20 * 60);
        }
    }

    public static ArrayList<Player> getPlayersInPool() {
        return playersInPool;
    }

}
