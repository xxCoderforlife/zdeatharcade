package dev.nullpointercoding.zdeatharcade.Vendors;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
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
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import com.destroystokyo.paper.profile.PlayerProfile;

import dev.nullpointercoding.zdeatharcade.Main;
import dev.nullpointercoding.zdeatharcade.Utils.NPCConfigManager;
import dev.nullpointercoding.zdeatharcade.Utils.PlayerConfigManager;
import dev.nullpointercoding.zdeatharcade.Utils.InventoryUtils.BlankSpaceFiller;
import dev.nullpointercoding.zdeatharcade.Utils.InventoryUtils.CustomInvFunctions;
import dev.nullpointercoding.zdeatharcade.Zombies.ZombieDrops;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;

public class MinerVendor implements Listener {
    private Inventory inv;
    private Double coalOrePrice = 1.0;
    private Double ironOrePrice = 1.5;
    private Double redstoneOrePrice = 1.8;
    private Double emerabldPrice = 2.0;
    private static Villager minerVendor;
    ZombieDrops zDrops = new ZombieDrops();
    private static final Component minerSyb = Component.text('⛏', NamedTextColor.DARK_GREEN, TextDecoration.BOLD);
    private static final Component name = Component.text(" Miner ", NamedTextColor.GREEN, TextDecoration.ITALIC);
    private static final Component fullName = minerSyb.append(name).append(minerSyb);

    public MinerVendor() {
        inv = Bukkit.createInventory(null, 27, fullName);
    }

    @EventHandler
    public void onVendorClick(PlayerInteractEntityEvent e) {
        if (e.getRightClicked().getType() == EntityType.VILLAGER) {
            e.setCancelled(true);
            LivingEntity entity = (LivingEntity) e.getRightClicked();
            if (entity.customName() == null) {
                return;
            }
            if (entity.customName().equals(fullName)) {
                openInventory(e.getPlayer());
            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (e.getView().title().equals(fullName)) {
            e.setCancelled(true);
            if (e.getCurrentItem() == null) {
                return;
            }
            ItemMeta clickedMeta = e.getCurrentItem().getItemMeta();
            Player whoClicked = (Player) e.getWhoClicked();
            if (clickedMeta.displayName().equals(CustomInvFunctions.getBackButton().getItemMeta().displayName())) {
                whoClicked.closeInventory();
            }
            if (clickedMeta.displayName().equals(sellCoalOre().getItemMeta().displayName())) {
                checkForItems(whoClicked, zDrops.coalOreDrop(), coalOrePrice);
            }
            if (clickedMeta.displayName().equals(sellIronOre().getItemMeta().displayName())) {
                checkForItems(whoClicked, zDrops.ironOreDrop(), ironOrePrice);
            }
            if (clickedMeta.displayName().equals(sellRedstoneOre().getItemMeta().displayName())) {
                checkForItems(whoClicked, zDrops.redstoneOreDrop(), redstoneOrePrice);
            }
            if (clickedMeta.displayName().equals(sellEmeramldOre().getItemMeta().displayName())) {
                checkForItems(whoClicked, zDrops.emeraldOreDrop(), emerabldPrice);

            }
        }
    }

    public Inventory getInventory() {
        return inv;
    }

    public void openInventory(Player player) {
        addItem();
        BlankSpaceFiller.fillinBlankInv(inv, List.of(0));
        player.openInventory(inv);
    }

    private void addItem() {
        inv.setItem(0, vendorHead());
        inv.setItem(10, sellCoalOre());
        inv.setItem(11, sellIronOre());
        inv.setItem(12, sellRedstoneOre());
        inv.setItem(13, sellEmeramldOre());
        inv.setItem(16, CustomInvFunctions.getBackButton());
    }

    public static Villager spawnMinerVendor(Player player, Integer npcID) {
        for (File f : Main.getInstance().getNPCDataFolder().listFiles()) {
            if (f.getName().equalsIgnoreCase("minervendor.yml")) {
                player.sendMessage(Component.text("Mine Vendor Spawned already spawned!"));
                return minerVendor;
            }
        }
        Location spawnLoc = player.getLocation().add(1, 0.0, 0);
        minerVendor = (Villager) player.getWorld().spawnEntity(spawnLoc, EntityType.VILLAGER);
        minerVendor.setVillagerType(Type.TAIGA);
        minerVendor.setProfession(Profession.MASON);
        minerVendor.setPersistent(true);
        minerVendor.setCollidable(false);
        minerVendor.setSilent(true);
        minerVendor.setAI(false);
        minerVendor.customName(minerSyb.append(name).append(minerSyb));
        minerVendor.setCustomNameVisible(true);
        new NPCConfigManager("minervendor", player.getWorld(), npcID, spawnLoc.getX(), spawnLoc.getY(),
                spawnLoc.getZ());
        return minerVendor;
    }

    public static void removeMinerVendor(Player p) {
        for (File f : Main.getInstance().getNPCDataFolder().listFiles()) {
            if (f.getName().equalsIgnoreCase("minervendor.yml")) {
                f.delete();
            }
        }
        for (LivingEntity le : p.getWorld().getLivingEntities()) {
            Component name = le.customName();
            if (name != null) {
                if (name.equals(fullName)) {
                    le.remove();
                }
            }
        }
    }

    public static Villager getLevel1Vendor() {
        return minerVendor;
    }

    private ItemStack sellCoalOre() {
        ItemStack coalOre = new ItemStack(Material.COAL_ORE);
        ItemMeta meta = coalOre.getItemMeta();
        meta.displayName(minerSyb.append(Component.text(" Coal Ore", NamedTextColor.GREEN, TextDecoration.ITALIC)));
        meta.addEnchant(Enchantment.PUNCH, 1, false);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES);
        List<Component> lore = new ArrayList<Component>();
        lore.add(Component.text("Click to Sell for: ", NamedTextColor.GRAY)
                .append(Component.text("$", NamedTextColor.GREEN))
                .append(Component.text(coalOrePrice.doubleValue(), NamedTextColor.GREEN)));
        meta.lore(lore);
        coalOre.setItemMeta(meta);
        return coalOre;
    }

    private ItemStack sellIronOre() {
        ItemStack ironOre = new ItemStack(Material.IRON_ORE);
        ItemMeta meta = ironOre.getItemMeta();
        meta.addEnchant(Enchantment.PUNCH, 1, false);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES);
        meta.displayName(minerSyb.append(Component.text(" Iron Ore", NamedTextColor.GREEN, TextDecoration.ITALIC)));
        List<Component> lore = new ArrayList<Component>();
        lore.add(Component.text("Click to Sell for: ", NamedTextColor.GRAY)
                .append(Component.text("$", NamedTextColor.GREEN))
                .append(Component.text(ironOrePrice.doubleValue(), NamedTextColor.GREEN)));
        meta.lore(lore);
        ironOre.setItemMeta(meta);
        return ironOre;
    }

    private ItemStack sellRedstoneOre() {
        ItemStack redstoneOre = new ItemStack(Material.REDSTONE_ORE);
        ItemMeta meta = redstoneOre.getItemMeta();
        meta.addEnchant(Enchantment.PUNCH, 1, false);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES);
        meta.displayName(minerSyb.append(Component.text(" Redstone Ore", NamedTextColor.GREEN, TextDecoration.ITALIC))
                .append(minerSyb));
        List<Component> lore = new ArrayList<Component>();
        lore.add(Component.text("Click to Sell for: ", NamedTextColor.GRAY)
                .append(Component.text("$", NamedTextColor.GREEN))
                .append(Component.text(redstoneOrePrice.doubleValue(), NamedTextColor.GREEN)));
        meta.lore(lore);
        redstoneOre.setItemMeta(meta);
        return redstoneOre;
    }

    private ItemStack sellEmeramldOre() {
        ItemStack emeraldOre = new ItemStack(Material.EMERALD_ORE);
        ItemMeta meta = emeraldOre.getItemMeta();
        meta.addEnchant(Enchantment.PUNCH, 1, false);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES);
        meta.displayName(minerSyb.append(Component.text(" Emeramld Ore", NamedTextColor.GREEN, TextDecoration.ITALIC)));
        List<Component> lore = new ArrayList<Component>();
        lore.add(Component.text("Click to Sell for: ", NamedTextColor.GRAY)
                .append(Component.text("$", NamedTextColor.GREEN))
                .append(Component.text(emerabldPrice.doubleValue(), NamedTextColor.GREEN)));
        meta.lore(lore);
        emeraldOre.setItemMeta(meta);
        return emeraldOre;
    }

    private ItemStack vendorHead() {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) head.getItemMeta();
        meta.displayName(minerSyb.append(name).append(minerSyb));
        List<Component> lore = new ArrayList<Component>();
        lore.add(Component.text("Buys Ores", NamedTextColor.GRAY, TextDecoration.ITALIC));
        meta.lore(lore);
        PlayerProfile profile = CustomInvFunctions.getProfile(
                "https://textures.minecraft.net/texture/621ec612f8f78984a08f8290bd3f1c1892b4f7827b524dbab7eaacc9dd9e22b2");
        meta.setPlayerProfile(profile);
        head.setItemMeta(meta);
        return head;
    }

    public Double getCoalOrePrice() {
        return coalOrePrice;
    }

    public Double getIronOrePrice() {
        return ironOrePrice;
    }

    public Double getRedStoneOrePrice() {
        return redstoneOrePrice;
    }

    public Double getEmeramldOrePrice() {
        return emerabldPrice;
    }

    private void checkForItems(Player whoToCheck, ItemStack itemToCheckFor, Double price) {
        Boolean doesPlayerHaveItem = false;
        for (ItemStack s : whoToCheck.getInventory().getContents()) {
            if (s == null || s.getItemMeta() == null || !s.getItemMeta().hasDisplayName()) {
                continue;
            }
            if (s.getItemMeta().displayName() != null
                    && s.getItemMeta().displayName().equals(itemToCheckFor.getItemMeta().displayName())) {
                doesPlayerHaveItem = true;
                break;
            }
        }
        if (doesPlayerHaveItem) {
            whoToCheck.getInventory().removeItem(itemToCheckFor);
            whoToCheck.sendMessage(Component.text("You sold ").append(itemToCheckFor.getItemMeta().displayName())
                    .append(Component.text(" for ", NamedTextColor.GREEN))
                    .append(Component.text("$", NamedTextColor.GREEN))
                    .append(Component.text(price.doubleValue(), NamedTextColor.GREEN)));
            whoToCheck.playSound(whoToCheck.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
            PlayerConfigManager config = new PlayerConfigManager(whoToCheck.getUniqueId());
            config.addBalance(BigDecimal.valueOf(price));
        } else {
            whoToCheck.sendMessage(Component.text("You do not have any ", NamedTextColor.RED, TextDecoration.ITALIC)
                    .append(itemToCheckFor.getItemMeta().displayName()
                            .hoverEvent(Component.text("Go get some!").color(NamedTextColor.DARK_PURPLE)
                                    .decorate(TextDecoration.ITALIC)))
                    .append(Component.text(" to sell!", NamedTextColor.RED)));
            whoToCheck.playSound(whoToCheck, Sound.ENTITY_PLAYER_HURT, 1.0f, 1.0f);
        }
    }
}
