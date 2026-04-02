package dev.nullpointercoding.zdeatharcade.Vendors;

import java.io.File;
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
import dev.nullpointercoding.zdeatharcade.Utils.InventoryUtils.BlankSpaceFiller;
import dev.nullpointercoding.zdeatharcade.Utils.InventoryUtils.CustomInvFunctions;
import dev.nullpointercoding.zdeatharcade.Vendors.BlackMarketPages.BuyPage;
import dev.nullpointercoding.zdeatharcade.Vendors.BlackMarketPages.BuyTokensPage;
import dev.nullpointercoding.zdeatharcade.Vendors.BlackMarketPages.DailyDealPage;
import dev.nullpointercoding.zdeatharcade.Vendors.BlackMarketPages.SellPage;
import dev.nullpointercoding.zdeatharcade.Zombies.ZombieDrops;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;

public class BlackMarketVendor implements Listener {
    private Inventory inv;
    private static Villager blackMarketVendor;
    ZombieDrops zDrops = new ZombieDrops();

    private final static Component key = Component.text('♛', NamedTextColor.YELLOW, TextDecoration.BOLD);

    private static final Component name = Component.text(" Black Market Dealer ", NamedTextColor.LIGHT_PURPLE,
            TextDecoration.ITALIC);
    private final static Component fullName = Component.text('♛', NamedTextColor.YELLOW, TextDecoration.BOLD)
            .append(Component.text(" Black Market Dealer ", NamedTextColor.LIGHT_PURPLE, TextDecoration.ITALIC))
            .append(Component.text('♛', NamedTextColor.YELLOW, TextDecoration.BOLD));

    public BlackMarketVendor() {
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
            if (clickedMeta.displayName().equals(buyGUI().getItemMeta().displayName())) {
                whoClicked.closeInventory();
                whoClicked.playSound(whoClicked, Sound.BLOCK_BEACON_ACTIVATE, 1.0f, 1.0f);
                new BuyPage().openInventory(whoClicked);
            }
            if (clickedMeta.displayName().equals(sellGUI().getItemMeta().displayName())) {
                whoClicked.playSound(whoClicked, Sound.ENTITY_VILLAGER_YES, 1.0f, 1.0f);
                new SellPage().openInventory(whoClicked);
            }
            if (clickedMeta.displayName().equals(dailyDeal().getItemMeta().displayName())) {
                whoClicked.playSound(whoClicked, Sound.MUSIC_DISC_CHIRP, 1.0f, 1.0f);
                new DailyDealPage().openInventory(whoClicked);
            }
            if (clickedMeta.displayName().equals(buyTokens().getItemMeta().displayName())) {
                whoClicked.playSound(whoClicked, Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
                new BuyTokensPage().openInventory(whoClicked);
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
        inv.setItem(10, buyGUI());
        inv.setItem(12, sellGUI());
        inv.setItem(14, dailyDeal());
        inv.setItem(16, CustomInvFunctions.getBackButton());
        inv.setItem(4, buyTokens());
    }

    public static Villager spawnblackMarketVendor(Player player, Integer npcID) {
        for (File f : Main.getInstance().getNPCDataFolder().listFiles()) {
            if (f.getName().equalsIgnoreCase("blackmarketvendor.yml")) {
                player.sendMessage(Component.text("Black Market Vendor Spawned already spawned!"));
                return blackMarketVendor;
            }
        }
        Location spawnLoc = player.getLocation().add(1, 0.0, 0);
        blackMarketVendor = (Villager) player.getWorld().spawnEntity(spawnLoc, EntityType.VILLAGER);
        blackMarketVendor.setVillagerType(Type.JUNGLE);
        blackMarketVendor.setProfession(Profession.WEAPONSMITH);
        blackMarketVendor.setPersistent(true);
        blackMarketVendor.setCollidable(false);
        blackMarketVendor.setSilent(true);
        blackMarketVendor.setAI(false);
        blackMarketVendor.customName(fullName);
        blackMarketVendor.setCustomNameVisible(true);
        new NPCConfigManager("blackmarketvendor", player.getWorld(), npcID, spawnLoc.getX(), spawnLoc.getY(),
                spawnLoc.getZ());
        return blackMarketVendor;
    }

    public static void removeblackMarketVendor(Player p) {
        for (File f : Main.getInstance().getNPCDataFolder().listFiles()) {
            if (f.getName().equalsIgnoreCase("blackmarketvendor.yml")) {
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
        return blackMarketVendor;
    }

    private ItemStack buyGUI() {
        ItemStack buyHead = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) buyHead.getItemMeta();
        meta.displayName(
                key.append(Component.text("Buy Items", NamedTextColor.GREEN, TextDecoration.ITALIC)).append(key));
        meta.addEnchant(Enchantment.POWER, 1, false);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES);
        List<Component> lore = new ArrayList<Component>();
        lore.add(Component.text("Buy Exctoic Items", NamedTextColor.LIGHT_PURPLE, TextDecoration.ITALIC));
        meta.lore(lore);
        PlayerProfile profile = CustomInvFunctions.getProfile(
                "https://textures.minecraft.net/texture/89e23af2797f10f588fa4c5a8fa9c515785258e459baac02f6963156a4babe25");
        meta.setPlayerProfile(profile);
        buyHead.setItemMeta(meta);
        return buyHead;
    }

    private ItemStack sellGUI() {
        ItemStack sellHead = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) sellHead.getItemMeta();
        meta.addEnchant(Enchantment.POWER, 1, false);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES);
        meta.displayName(
                key.append(Component.text("Sell Items", NamedTextColor.GREEN, TextDecoration.ITALIC)).append(key));
        List<Component> lore = new ArrayList<Component>();
        lore.add(Component.text("Sell Exctoic Items", NamedTextColor.LIGHT_PURPLE, TextDecoration.ITALIC));
        meta.lore(lore);
        PlayerProfile profile = CustomInvFunctions.getProfile(
                "https://textures.minecraft.net/texture/b423289510c54b67df023580979c465d0481c769c865bf4b465cf478749f1c4f");
        meta.setPlayerProfile(profile);
        sellHead.setItemMeta(meta);
        return sellHead;
    }

    private ItemStack dailyDeal() {
        ItemStack beacon = new ItemStack(Material.BEACON);
        ItemMeta meta = beacon.getItemMeta();
        meta.addEnchant(Enchantment.POWER, 1, false);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES);
        meta.displayName(
                key.append(Component.text("Daily Deal", NamedTextColor.GREEN, TextDecoration.ITALIC)).append(key));
        List<Component> lore = new ArrayList<Component>();
        lore.add(Component.text("Click to see the Daily Deal", NamedTextColor.GRAY, TextDecoration.ITALIC));
        meta.lore(lore);
        beacon.setItemMeta(meta);
        return beacon;
    }

    private ItemStack buyTokens() {
        ItemStack token = new ItemStack(Material.SUNFLOWER);
        ItemMeta meta = token.getItemMeta();
        meta.displayName(Component.text("Buy Tokens", NamedTextColor.GREEN, TextDecoration.ITALIC));
        List<Component> lore = new ArrayList<Component>();
        lore.add(Component.text("Click to buy Tokens", NamedTextColor.GRAY, TextDecoration.ITALIC));
        meta.lore(lore);
        token.setItemMeta(meta);
        return token;
    }

    private ItemStack vendorHead() {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) head.getItemMeta();
        meta.displayName(key.append(name).append(key));
        List<Component> lore = new ArrayList<Component>();
        lore.add(Component.text("Buy and Sell Excotic Items", NamedTextColor.GRAY, TextDecoration.ITALIC));
        meta.lore(lore);
        PlayerProfile profile = CustomInvFunctions.getProfile(
                "https://textures.minecraft.net/texture/25fafa2be55bd15aea6e2925f5d24f8068e0f4a2616f3b92b380d94912f0ec5f");
        meta.setPlayerProfile(profile);
        head.setItemMeta(meta);
        return head;
    }
}
