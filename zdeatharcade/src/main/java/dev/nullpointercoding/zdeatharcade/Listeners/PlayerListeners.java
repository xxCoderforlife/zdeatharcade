package dev.nullpointercoding.zdeatharcade.Listeners;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerTextures;
import org.bukkit.scheduler.BukkitScheduler;

import com.destroystokyo.paper.profile.PlayerProfile;

import dev.nullpointercoding.zdeatharcade.Main;
import dev.nullpointercoding.zdeatharcade.Bank.BankAccountGUI;
import dev.nullpointercoding.zdeatharcade.Utils.AutoBroadcast;
import dev.nullpointercoding.zdeatharcade.Utils.PlayerConfigManager;
import dev.nullpointercoding.zdeatharcade.Utils.VaultHookFolder.VaultHook;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.title.TitlePart;
import net.milkbowl.vault2.economy.Economy;

public class PlayerListeners implements Listener {

    private HashMap<Player, Inventory> playerInv = new HashMap<Player, Inventory>();
    private Economy econ = new VaultHook();
    private AutoBroadcast AB = new AutoBroadcast();
    private Main plugin = Main.getInstance();

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        
       // ParticleEffects.startEffects();
        Player p = e.getPlayer();
        Economy econ = Main.getEconomy();
        PlayerConfigManager PCM = new PlayerConfigManager(p.getUniqueId());
        PCM.addKnownPlayer(p.getUniqueId());

        if (econ != null) { // add null check here
            if (econ.createAccount(p.getUniqueId(), p.getName(),true)) {
                p.sendMessage(Component.text("Creating your account!"));
            } else {
                p.sendMessage(Component.text("Logging into to " + p.getName()).color(NamedTextColor.GREEN));
            }
        } else {
            p.sendMessage(Component.text("Economy is null."));

        }
        // Check if the Player who joined has a bounty
        final TextComponent joinMessage = Component.text().content("{").color(TextColor.color(0x00FF00))
                .append(Component.text('✔').color(TextColor.color(0xFF0000)))
                .append(Component.text().content("} ").color(TextColor.color(0x00FF00)))
                .append(p.displayName().color(NamedTextColor.GREEN)).build();
        e.joinMessage(joinMessage.hoverEvent(randomJoinMessageLore(p)));
        //joinedWithBounty(p);
        AB.startBroadcast();
        // ✘

    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        final TextComponent leaveMessage = Component.text().content("{").color(TextColor.color(0x00FF00))
                .append(Component.text('✘').color(TextColor.color(0xFF0000)))
                .append(Component.text().content("} ").color(TextColor.color(0x00FF00)))
                .append(p.displayName().color(NamedTextColor.RED)).build();
        e.quitMessage(leaveMessage);
        leftWithBounty(p);
        setLastLoginPlayer(p);
        final BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        scheduler.scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                if (Bukkit.getOnlinePlayers().isEmpty()) {
                    AB.stopBroadcast();
                }
            }
        }, 60L);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        Player p = (Player) e.getPlayer();
        if (p.getLastDamageCause() instanceof EntityDamageByEntityEvent) {
            EntityDamageByEntityEvent playerKiller = (EntityDamageByEntityEvent) p.getLastDamageCause();
            PlayerConfigManager PCM = new PlayerConfigManager(p.getUniqueId());
            if (playerKiller.getDamager() instanceof Zombie) {
                PCM.setDeaths(PCM.getDeaths() + 1);
                PCM.saveConfig();
                final Component deathMessage = Component
                        .text("§c§l" + p.getName() + " §7was killed by §c§l" + playerKiller.getDamager().getName()
                                + " and has died " + "§e§o" + PCM.getDeaths().intValue() + " times.");
                playerInv.put(p, p.getInventory());
                e.deathMessage(deathMessage);
            }
            if (playerKiller.getDamager() instanceof Player) {
                PCM.setDeaths(PCM.getDeaths() + 1);
                PCM.saveConfig();
                final Component deathMessage = Component
                        .text("§c§l" + p.getName() + " §7was killed by §c§l" + playerKiller.getDamager().getName()
                                + " and has died " + "§e§o" + PCM.getDeaths().intValue() + " times.");
                playerInv.put(p, p.getInventory());
                e.deathMessage(deathMessage);
                checkForBounites(p);
                List<ItemStack> d = e.getDrops();
                d.clear();
            }
        }
    }

    @EventHandler
    public void onPlayerReSpawn(PlayerRespawnEvent e) {
        Player p = (Player) e.getPlayer();
        BigDecimal toTake = econ.balance("zdeatharcade", p.getUniqueId()).multiply(BigDecimal.valueOf(0.08));
        if (playerInv.containsKey(p)) {
            econ.withdraw("zdeatharcade", p.getUniqueId(), toTake);
            p.sendTitlePart(TitlePart.TITLE, Component.text("§4§lYOU DIED"));
            p.sendTitlePart(TitlePart.SUBTITLE, Component.text("You lost §e§o" + toTake.toString() + "§4§l$"));
            p.teleportAsync(p.getWorld().getSpawnLocation(), TeleportCause.PLUGIN);
            p.getInventory().clear();
            p.getInventory().setContents(playerInv.get(p).getContents());
            playerInv.remove(p);
        }
    }

    private void checkForBounites(Player p) {
        for (Entry<Player, HashMap<Player, Double>> m : BankAccountGUI.getBountyList().entrySet()) {
            if (m.getValue().containsKey(p)) {
                econ.withdraw("zdeathradio",p.getUniqueId(),BigDecimal.valueOf(m.getValue().get(p).doubleValue()));
                m.getValue().remove(p);
                Bukkit.broadcast(Component.text(p.getKiller() + " has claimed the bounty on " + p.getName()
                        + " and has been rewarded with " + m.getValue().get(p).doubleValue() + "$"));
                econ.deposit("zdeathradio", p.getKiller().getUniqueId(), BigDecimal.valueOf(m.getValue().get(p).doubleValue()));
                p.getKiller().getInventory().addItem(bountyHead());
            }
        }
    }

    private void setLastLoginPlayer(Player player) {
        PlayerConfigManager pcm = new PlayerConfigManager(player.getUniqueId());
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        pcm.setLastLogin(myFormatObj.format(now));
        pcm.setClientBrand(player.getClientBrandName());
        pcm.saveConfig();
    }

    private void joinedWithBounty(Player playerWithBounty) {
        PlayerConfigManager pcm = new PlayerConfigManager(playerWithBounty.getUniqueId());
        if (pcm.hasBounty()) {
            Bukkit.broadcast(Component.text(playerWithBounty.getName() + " has joined and still has a bounty of "
                    + pcm.getBounty() + "$"));

        }
    }

    private void leftWithBounty(Player playerWithBounty) {
        PlayerConfigManager pcm = new PlayerConfigManager(playerWithBounty.getUniqueId());
        if (pcm.hasBounty()) {
            Bukkit.broadcast(Component.text(playerWithBounty.getName() + " has left and still has a bounty of "
                    + pcm.getBounty() + "$"));
        }
    }

    private ItemStack bountyHead() {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) head.getItemMeta();
        meta.displayName(Component.text("§c§lBounty Head"));
        List<Component> lore = new ArrayList<Component>();
        lore.add(Component.text("Sell this at the Bounty Board"));
        PlayerProfile profile = getProfile(
                "https://textures.minecraft.net/texture/2c57e391e36801da12714cf7bcaed71e2c57fde4815afb692445f2b1393cd520");
        meta.setPlayerProfile(profile);
        head.setItemMeta(meta);
        return head;
    }

    // Get a player profile with a skin from a URL
    private static final UUID RANDOM_UUID = UUID.fromString("92864445-51c5-4c3b-9039-517c9927d1b4");

    private static PlayerProfile getProfile(String url) {
        PlayerProfile profile = (PlayerProfile) Bukkit.createProfile(RANDOM_UUID); // Get a new player profile
        PlayerTextures textures = profile.getTextures();
        URL urlObject;
        try {
            urlObject = new URI(url).toURL(); // The URL to the skin, for example:
                                      // https://textures.minecraft.net/texture/18813764b2abc94ec3c3bc67b9147c21be850cdf996679703157f4555997ea63a
        } catch (MalformedURLException | java.net.URISyntaxException exception) {
            throw new RuntimeException("Invalid URL", exception);
        }
        textures.setSkin(urlObject); // Set the skin of the player profile to the URL
        profile.setTextures(textures); // Set the textures back to the profile
        return profile;
    }

    private Component randomJoinMessageLore(Player player) {
        Component lore;
        int random = (int) (Math.random() * 5) + 1;
        switch (random) {
            case 1:
                lore = Component.text("Oh no, not this guy again...", NamedTextColor.RED, TextDecoration.BOLD);
                player.getWorld().spawnParticle(Particle.HAPPY_VILLAGER, player.getEyeLocation().add(0, 1, 0), 20, 2.0, 0.0, 2.0);
                return lore;
            case 2:
                lore = Component.text("Just one more login...", NamedTextColor.YELLOW, TextDecoration.BOLD);
                player.getWorld().spawnParticle(Particle.HAPPY_VILLAGER, player.getEyeLocation().add(0, 1, 0), 20, 2.0, 0.0, 2.0);
                return lore;
            case 3:
                lore = Component.text("The Choosen One has Returned!",NamedTextColor.LIGHT_PURPLE, TextDecoration.ITALIC);
                player.getWorld().spawnParticle(Particle.HAPPY_VILLAGER, player.getEyeLocation().add(0, 1, 0), 20, 2.0, 0.0, 2.0);
                return lore;
            case 4:
                lore = Component.text("yo yo welcome back", NamedTextColor.GOLD, TextDecoration.BOLD);
                player.getWorld().spawnParticle(Particle.HAPPY_VILLAGER, player.getEyeLocation().add(0, 1, 0), 20, 2.0, 0.0, 2.0);
                return lore;
            case 5:
                lore = Component.text("THE ONE AND ONLY", NamedTextColor.DARK_RED,
                        TextDecoration.BOLD);
                player.getWorld().spawnParticle(Particle.HAPPY_VILLAGER, player.getEyeLocation().add(0, 1, 0), 20, 2.0, 0.0, 2.0);
                return lore;
        }
        return null;
    }

}
