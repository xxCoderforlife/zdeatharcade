package dev.nullpointercoding.zdeatharcade.Vendors;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Villager.Profession;
import org.bukkit.entity.Villager.Type;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.Inventory;
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

public class FarmerVendor implements Listener {

    private final Inventory inv;
    private Double hayBlockPrice = 0.80;
    private Double wheatPrice = 0.60;
    private Double grassBlockPrice = 0.20;
    private Double seedsPrice = 0.40;
    private static Villager farmerVendor;
    private static final Component name = Component.text(" Farmer ", NamedTextColor.AQUA, TextDecoration.ITALIC);
    private static final Component farmerSyb = Component.text('☼', NamedTextColor.DARK_AQUA, TextDecoration.BOLD);
    private static final Component fullName = farmerSyb.append(name).append(farmerSyb);
    private ZombieDrops zDrops = new ZombieDrops();

    public FarmerVendor() {
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
            if (clickedMeta.displayName().equals(sellHayBlock().getItemMeta().displayName())) {
                checkForItems(whoClicked, zDrops.hayDrop(), hayBlockPrice);
            }
            if (clickedMeta.displayName().equals(sellWheat().getItemMeta().displayName())) {
                checkForItems(whoClicked, zDrops.wheatDrop(), wheatPrice);
            }
            if (clickedMeta.displayName().equals(sellGrassBlock().getItemMeta().displayName())) {
                checkForItems(whoClicked, zDrops.grassBlockDrop(), grassBlockPrice);
            }
            if (clickedMeta.displayName().equals(sellSeeds().getItemMeta().displayName())) {
                checkForItems(whoClicked, zDrops.seedsDrop(), seedsPrice);

            }
        }
    }

    @EventHandler
    public void onVillagerHit(EntityDamageByEntityEvent e) {
        if (e.getEntity().getType() == EntityType.VILLAGER) {
            e.setCancelled(true);
            if (e.getDamager() instanceof Player) {
                Player player = (Player) e.getDamager();
                player.sendMessage(
                        Component.text("Please don't punch me ", NamedTextColor.DARK_RED, TextDecoration.ITALIC)
                                .append(player.displayName()));
                player.sendHurtAnimation(90.0f);
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
        inv.setItem(10, sellHayBlock());
        inv.setItem(11, sellWheat());
        inv.setItem(12, sellGrassBlock());
        inv.setItem(13, sellSeeds());
        inv.setItem(16, CustomInvFunctions.getBackButton());
    }

    public static Villager spawnFarmerVendor(Player player, Integer npcID) {
        for (File f : Main.getInstance().getNPCDataFolder().listFiles()) {
            if (f.getName().equalsIgnoreCase("farmervendor.yml")) {
                player.sendMessage(Component.text("Farmer Vendor Spawned already spawned!"));
                return farmerVendor;
            }
        }
        Location spawnLoc = player.getLocation().add(1, 0.0, 0);
        farmerVendor = (Villager) player.getWorld().spawnEntity(spawnLoc, EntityType.VILLAGER);
        farmerVendor.setVillagerType(Type.JUNGLE);
        farmerVendor.setProfession(Profession.FARMER);
        farmerVendor.setPersistent(true);
        farmerVendor.setCollidable(false);
        farmerVendor.setSilent(true);
        farmerVendor.setAI(false);
        farmerVendor.customName(farmerSyb.append(name).append(farmerSyb));
        farmerVendor.setCustomNameVisible(true);
        new NPCConfigManager("farmervendor", player.getWorld(), npcID, spawnLoc.getX(), spawnLoc.getY(),
                spawnLoc.getZ());
        return farmerVendor;
    }

    public static void removeFarmerVendor(Player p) {
        for (File f : Main.getInstance().getNPCDataFolder().listFiles()) {
            if (f.getName().equalsIgnoreCase("farmervendor.yml")) {
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
        return farmerVendor;
    }

    private ItemStack sellHayBlock() {
        ItemStack hayBlock = new ItemStack(Material.HAY_BLOCK);
        ItemMeta meta = hayBlock.getItemMeta();
        meta.displayName(farmerSyb.append(Component.text(" Hay Block", NamedTextColor.AQUA, TextDecoration.ITALIC)));
        List<Component> lore = new ArrayList<Component>();
        lore.add(Component.text("Click to Sell for: ", NamedTextColor.GRAY)
                .append(Component.text("$", NamedTextColor.GREEN))
                .append(Component.text("0.80", NamedTextColor.GREEN)));
        meta.lore(lore);
        hayBlock.setItemMeta(meta);
        return hayBlock;
    }

    private ItemStack sellWheat() {
        ItemStack wheat = new ItemStack(Material.WHEAT);
        ItemMeta meta = wheat.getItemMeta();
        meta.displayName(farmerSyb.append(Component.text(" Wheat", NamedTextColor.AQUA, TextDecoration.ITALIC)));
        List<Component> lore = new ArrayList<Component>();
        lore.add(Component.text("Click to Sell for: ", NamedTextColor.GRAY)
                .append(Component.text("$", NamedTextColor.GREEN))
                .append(Component.text(wheatPrice.doubleValue(), NamedTextColor.GREEN)));
        meta.lore(lore);
        wheat.setItemMeta(meta);
        return wheat;
    }

    private ItemStack sellGrassBlock() {
        ItemStack grassBlock = new ItemStack(Material.GRASS_BLOCK);
        ItemMeta meta = grassBlock.getItemMeta();
        meta.displayName(farmerSyb.append(Component.text(" Grass Block", NamedTextColor.AQUA, TextDecoration.ITALIC)));
        List<Component> lore = new ArrayList<Component>();
        lore.add(Component.text("Click to Sell for: ", NamedTextColor.GRAY)
                .append(Component.text("$", NamedTextColor.GREEN))
                .append(Component.text(grassBlockPrice.doubleValue(), NamedTextColor.GREEN)));
        meta.lore(lore);
        grassBlock.setItemMeta(meta);
        return grassBlock;
    }

    private ItemStack sellSeeds() {
        ItemStack seeds = new ItemStack(Material.WHEAT_SEEDS);
        ItemMeta meta = seeds.getItemMeta();
        meta.displayName(farmerSyb.append(Component.text(" Wheat Seeds", NamedTextColor.AQUA, TextDecoration.ITALIC)));
        List<Component> lore = new ArrayList<Component>();
        lore.add(Component.text("Click to Sell for: ", NamedTextColor.GRAY)
                .append(Component.text("$", NamedTextColor.GREEN))
                .append(Component.text(seedsPrice.doubleValue(), NamedTextColor.GREEN)));
        meta.lore(lore);
        seeds.setItemMeta(meta);
        return seeds;
    }

    private ItemStack vendorHead() {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) head.getItemMeta();
        meta.displayName(farmerSyb.append(name).append(farmerSyb));
        List<Component> lore = new ArrayList<Component>();
        lore.add(Component.text("Buys Grass and Stuff", NamedTextColor.GRAY, TextDecoration.ITALIC));
        meta.lore(lore);
        PlayerProfile profile = CustomInvFunctions.getProfile(
                "https://textures.minecraft.net/texture/d01e035a3d8d6126072bcbe52a97913ace93552a99995b5d4070d6783a31e909");
        meta.setPlayerProfile(profile);
        head.setItemMeta(meta);
        return head;
    }

    public Double getHayBlockPrice() {
        return hayBlockPrice;
    }

    public Double getWheatPrice() {
        return wheatPrice;
    }

    public Double getGrassBlockPrice() {
        return grassBlockPrice;
    }

    public Double getSeedsPrice() {
        return seedsPrice;
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
