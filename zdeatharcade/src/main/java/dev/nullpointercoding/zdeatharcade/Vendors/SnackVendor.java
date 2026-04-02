package dev.nullpointercoding.zdeatharcade.Vendors;

import java.io.File;
import java.math.BigDecimal;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Villager.Profession;
import org.bukkit.entity.Villager.Type;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.potion.PotionEffectType;

import dev.nullpointercoding.zdeatharcade.Main;
import dev.nullpointercoding.zdeatharcade.Utils.NPCConfigManager;
import dev.nullpointercoding.zdeatharcade.Utils.InventoryUtils.BlankSpaceFiller;
import dev.nullpointercoding.zdeatharcade.Vendors.Snacks.Snack;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.milkbowl.vault2.economy.Economy;

public class SnackVendor implements Listener {

    private final Inventory inv;
    private final Component title = Component.text("Snack Shop", NamedTextColor.GOLD, TextDecoration.ITALIC);
    private static final Component npcName = Component.text("Snack Shop", NamedTextColor.GOLD, TextDecoration.ITALIC);
    private static Villager snackVen;
    private Economy econ = Main.getEconomy();

    public SnackVendor() {
        inv = Bukkit.createInventory(null, 36, title);
        addSnacks();
    }

    @EventHandler
    public void onIventoryClick(InventoryClickEvent e) {
        if (!e.getView().title().equals(title)) {
            return;
        }
        e.setCancelled(true);
        Player p = (Player) e.getWhoClicked();
        if (Snack.isSnack(e.getCurrentItem())) {
            Snack s = Snack.ItemStackToSnack(e.getCurrentItem());
            // Check if the Player has enough money to buy a Snack
            if (econ.balance("zdeatharcade", p.getUniqueId()).doubleValue() >= s.getWorth()) {
                // Remove the money from the Player
                econ.withdraw("zdeatharcade", p.getUniqueId(), BigDecimal.valueOf(s.getWorth()));
                // Add the Snack to the Player's Inventory
                p.getInventory().addItem(s.getSnackItem());
            } else {
                p.sendMessage(Component.text("You don't have enough money to buy this Snack!"));
            }
        }
    }

    public void openInventory(Player whoOpened) {
        BlankSpaceFiller.fillinBlankInv(inv, List.of(10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25));
        whoOpened.openInventory(inv);
    }

    @EventHandler
    public void onVendorClick(PlayerInteractEntityEvent e) {
        if (e.getRightClicked().getType() == EntityType.VILLAGER) {
            e.setCancelled(true);
            LivingEntity entity = (LivingEntity) e.getRightClicked();
            if (entity.customName() == null) {
                return;
            }
            if (entity.customName().equals(npcName)) {
                openInventory(e.getPlayer());
            }
        }
    }

    public static Villager spawnsnackVen(Player player, Integer npcID) {
        for (File f : Main.getInstance().getNPCDataFolder().listFiles()) {
            if (f.getName().equalsIgnoreCase("snackvendor.yml")) {
                player.sendMessage(Component.text("snackVen Spawned already spawned!"));
                return snackVen;
            }
        }
        Location spawnLoc = player.getLocation().add(1, 0.0, 0);
        snackVen = (Villager) player.getWorld().spawnEntity(spawnLoc, EntityType.VILLAGER);
        snackVen.setVillagerType(Type.TAIGA);
        snackVen.setProfession(Profession.CLERIC);
        snackVen.setPersistent(true);
        snackVen.setCollidable(false);
        snackVen.setSilent(true);
        snackVen.setAI(false);
        snackVen.customName(npcName);
        snackVen.setCustomNameVisible(true);
        new NPCConfigManager("snackvendor", player.getWorld(), npcID, spawnLoc.getX(), spawnLoc.getY(),
                spawnLoc.getZ());
        return snackVen;
    }

    public static void removesnackVendor(Player p) {
        for (File f : Main.getInstance().getNPCDataFolder().listFiles()) {
            if (f.getName().equalsIgnoreCase("snackvendor.yml")) {
                f.delete();
            }
        }
        for (LivingEntity le : p.getWorld().getLivingEntities()) {
            Component name = le.customName();
            if (name != null) {
                if (name.equals(npcName)) {
                    le.remove();
                }
            }
        }
    }

    private void addSnacks() {
        Snack cookie = new Snack("cookie", Material.COOKIE, Component.text("Cookie", NamedTextColor.BLUE),
                PotionEffectType.ABSORPTION, 5, 1,
                1.0, 0.5, 0.2f, 10.0);
        Snack cake = new Snack("cake", Material.CAKE, Component.text("Cake", NamedTextColor.BLUE),
                PotionEffectType.RESISTANCE, 5, 1, 1.5, 1.5, 1.2f, 120.00);
        Snack apple = new Snack("apple", Material.APPLE, Component.text("Apple", NamedTextColor.BLUE),
                PotionEffectType.SLOW_FALLING, 5, 1,
                1.2, 1.2, 1.6f, 50.00);
        Snack carrot = new Snack("carrot", Material.CARROT, Component.text("Carrot", NamedTextColor.BLUE),
                PotionEffectType.SPEED, 5, 1,
                1.2, 1.2, 1.6f, 50.00);
        Snack potato = new Snack("potato", Material.POTATO, Component.text("Potato", NamedTextColor.BLUE),
                PotionEffectType.STRENGTH, 5, 1,
                1.2, 1.2, 1.6f, 50.00);
        Snack bread = new Snack("bread", Material.BREAD, Component.text("Bread", NamedTextColor.BLUE),
                PotionEffectType.INSTANT_HEALTH, 5, 1,
                1.2, 1.2, 1.6f, 50.00);
        Snack steak = new Snack("steak", Material.COOKED_BEEF, Component.text("Steak", NamedTextColor.BLUE),
                PotionEffectType.ABSORPTION, 5, 1,
                1.2, 1.2, 1.6f, 50.00);
        Snack porkchop = new Snack("porkchop", Material.COOKED_PORKCHOP,
                Component.text("Porkchop", NamedTextColor.BLUE),
                PotionEffectType.ABSORPTION, 5, 1,
                1.2, 1.2, 1.6f, 50.00);
        inv.setItem(10, cookie.getSnack());
        inv.setItem(11, cake.getSnack());
        inv.setItem(12, apple.getSnack());
        inv.setItem(13, carrot.getSnack());
        inv.setItem(14, potato.getSnack());
        inv.setItem(15, bread.getSnack());
        inv.setItem(16, steak.getSnack());
        inv.setItem(19, porkchop.getSnack());

    }

}
