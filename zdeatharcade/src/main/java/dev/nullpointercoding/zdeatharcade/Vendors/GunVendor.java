package dev.nullpointercoding.zdeatharcade.Vendors;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
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
import dev.nullpointercoding.zdeatharcade.Utils.InventoryUtils.BlankSpaceFiller;
import dev.nullpointercoding.zdeatharcade.Utils.InventoryUtils.CustomInvFunctions;
import dev.nullpointercoding.zdeatharcade.Vendors.GunShopPages.AmmoPage;
import dev.nullpointercoding.zdeatharcade.Vendors.GunShopPages.EquipmentPage;
import dev.nullpointercoding.zdeatharcade.Vendors.GunShopPages.GunPage;
import dev.nullpointercoding.zdeatharcade.Zombies.ZombieDrops;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;

public class GunVendor implements Listener {
    private Inventory inv;
    private Double coalOrePrice = 1.0;
    private Double ironOrePrice = 1.5;
    private Double redstoneOrePrice = 1.8;
    private Double emerabldPrice = 2.0;
    private static Villager gunVendor;
    ZombieDrops zDrops = new ZombieDrops();
    private static final Component name = Component.text(" Gun Shop ", NamedTextColor.BLUE, TextDecoration.ITALIC);
    private static final Component gunVendorSyb = Component.text('☤', NamedTextColor.DARK_BLUE, TextDecoration.BOLD);
    private static final Component fullName = gunVendorSyb.append(name).append(gunVendorSyb);

    public GunVendor() {
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
            if (clickedMeta.displayName().equals(gunPage().getItemMeta().displayName())) {
                whoClicked.closeInventory();
                new GunPage().openInventory(whoClicked);
            }
            if (clickedMeta.displayName().equals(ammoPage().getItemMeta().displayName())) {
                whoClicked.closeInventory();
                new AmmoPage().openInventory(whoClicked);
            }
            if (clickedMeta.displayName().equals(eqiupmentPage().getItemMeta().displayName())) {
                new EquipmentPage().openInventory(whoClicked);
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
        inv.setItem(10, gunPage());
        inv.setItem(11, ammoPage());
        inv.setItem(12, eqiupmentPage());
        inv.setItem(16, CustomInvFunctions.getBackButton());
    }

    public static Villager spawnGunVendor(Player player, Integer npcID) {
        for (File f : Main.getInstance().getNPCDataFolder().listFiles()) {
            if (f.getName().equalsIgnoreCase("gunvendor.yml")) {
                player.sendMessage(Component.text("GunVendor Spawned already spawned!"));
                return gunVendor;
            }
        }
        Location spawnLoc = player.getLocation().add(1, 0.0, 0);
        gunVendor = (Villager) player.getWorld().spawnEntity(spawnLoc, EntityType.VILLAGER);
        gunVendor.setVillagerType(Type.TAIGA);
        gunVendor.setProfession(Profession.CLERIC);
        gunVendor.setPersistent(true);
        gunVendor.setCollidable(false);
        gunVendor.setSilent(true);
        gunVendor.setAI(false);
        gunVendor.customName(gunVendorSyb.append(name).append(gunVendorSyb));
        gunVendor.setCustomNameVisible(true);
        new NPCConfigManager("gunvendor", player.getWorld(), npcID, spawnLoc.getX(), spawnLoc.getY(),
                spawnLoc.getZ());
        return gunVendor;
    }

    public static void removeGunVendor(Player p) {
        for (File f : Main.getInstance().getNPCDataFolder().listFiles()) {
            if (f.getName().equalsIgnoreCase("gunvendor.yml")) {
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
        return gunVendor;
    }

    private ItemStack gunPage() {
        ItemStack rpg = new ItemStack(Material.CROSSBOW);
        ItemMeta meta = rpg.getItemMeta();
        meta.displayName(gunVendorSyb.append(Component.text(" Gun Page", NamedTextColor.RED, TextDecoration.ITALIC)));
        meta.addEnchant(Enchantment.POWER, 1, false);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES);
        List<Component> lore = new ArrayList<Component>();
        lore.add(Component.text("Click to open Gun Page", NamedTextColor.GRAY, TextDecoration.ITALIC));
        meta.lore(lore);
        rpg.setItemMeta(meta);
        return rpg;
    }

    private ItemStack ammoPage() {
        ItemStack ammo = new ItemStack(Material.TNT);
        ItemMeta meta = ammo.getItemMeta();
        meta.addEnchant(Enchantment.POWER, 1, false);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES);
        meta.displayName(gunVendorSyb.append(Component.text(" Ammo", NamedTextColor.GOLD, TextDecoration.ITALIC)));
        List<Component> lore = new ArrayList<Component>();
        lore.add(Component.text("Click to open Ammo Page", NamedTextColor.GRAY, TextDecoration.ITALIC));
        meta.lore(lore);
        ammo.setItemMeta(meta);
        return ammo;
    }

    private ItemStack eqiupmentPage() {
        ItemStack grenade = new ItemStack(Material.RED_CONCRETE);
        ItemMeta meta = grenade.getItemMeta();
        meta.addEnchant(Enchantment.POWER, 1, false);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES);
        meta.displayName(
                gunVendorSyb.append(Component.text(" Equipment", NamedTextColor.GREEN, TextDecoration.ITALIC)));
        List<Component> lore = new ArrayList<Component>();
        lore.add(Component.text("Click to open Equipment Page", NamedTextColor.GRAY, TextDecoration.ITALIC));
        meta.lore(lore);
        grenade.setItemMeta(meta);
        return grenade;
    }

    private ItemStack vendorHead() {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) head.getItemMeta();
        meta.displayName(gunVendorSyb.append(name).append(gunVendorSyb));
        List<Component> lore = new ArrayList<Component>();
        lore.add(Component.text("Sells Guns", NamedTextColor.GRAY, TextDecoration.ITALIC));
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
}
