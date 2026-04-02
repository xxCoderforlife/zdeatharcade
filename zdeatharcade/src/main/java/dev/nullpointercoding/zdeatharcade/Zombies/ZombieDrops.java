package dev.nullpointercoding.zdeatharcade.Zombies;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import dev.nullpointercoding.zdeatharcade.Main;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public class ZombieDrops {

    private Main plugin = Main.getInstance();
    private File dropItemFile;
    private YamlConfiguration dropItemConfig;

    public ZombieDrops() {
        for (File f : plugin.getCustomZombieDropsFolder().listFiles()) {
            if (f.getName().endsWith(".yml")) {
                dropItemFile = f;
            }
            try {
                dropItemConfig = new YamlConfiguration();
                dropItemConfig.load(dropItemFile);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // Level 1 Drops
    public final ItemStack hayDrop() {
        ItemStack hay = new ItemStack(Material.HAY_BLOCK, 1);
        ItemMeta hayMeta = hay.getItemMeta();
        hayMeta.displayName(Component.text("Hay Block", NamedTextColor.AQUA, TextDecoration.ITALIC));
        hayMeta.addEnchant(Enchantment.PUNCH, 1, false);
        hayMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES);
        List<Component> lore = new ArrayList<Component>();
        lore.add(Component.text("Sell this at the Farmer for money!", NamedTextColor.GRAY, TextDecoration.ITALIC));
        hayMeta.lore(lore);
        hay.setItemMeta(hayMeta);
        return hay;
    }

    public final ItemStack wheatDrop() {
        ItemStack wheat = new ItemStack(Material.WHEAT);
        ItemMeta wheatMeta = wheat.getItemMeta();
        wheatMeta.displayName(Component.text("Wheat", NamedTextColor.AQUA, TextDecoration.ITALIC));
        wheatMeta.addEnchant(Enchantment.PUNCH, 1, false);
        wheatMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES);
        List<Component> lore = new ArrayList<Component>();
        lore.add(Component.text("Sell this at the Farmer for money!", NamedTextColor.GRAY, TextDecoration.ITALIC));
        wheatMeta.lore(lore);
        wheat.setItemMeta(wheatMeta);
        return wheat;
    }

    public final ItemStack grassBlockDrop() {
        ItemStack grassBlock = new ItemStack(Material.GRASS_BLOCK);
        ItemMeta grassBlockMeta = grassBlock.getItemMeta();
        grassBlockMeta.displayName(Component.text("Grass Block", NamedTextColor.AQUA, TextDecoration.ITALIC));
        grassBlockMeta.addEnchant(Enchantment.PUNCH, 1, false);
        grassBlockMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES);
        List<Component> lore = new ArrayList<Component>();
        lore.add(Component.text("Sell this at the Farmer for money!", NamedTextColor.GRAY, TextDecoration.ITALIC));
        grassBlockMeta.lore(lore);
        grassBlock.setItemMeta(grassBlockMeta);
        return grassBlock;
    }

    public final ItemStack seedsDrop() {
        ItemStack seeds = new ItemStack(Material.WHEAT_SEEDS);
        ItemMeta seedsMeta = seeds.getItemMeta();
        seedsMeta.displayName(Component.text("Wheat Seeds", NamedTextColor.AQUA, TextDecoration.ITALIC));
        seedsMeta.addEnchant(Enchantment.PUNCH, 1, false);
        seedsMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES);
        List<Component> lore = new ArrayList<Component>();
        lore.add(Component.text("Sell this at the Farmer for money!", NamedTextColor.GRAY, TextDecoration.ITALIC));
        seedsMeta.lore(lore);
        seeds.setItemMeta(seedsMeta);
        return seeds;
    }

    public ItemStack[] getLevel1Drops() {
        ItemStack[] level1Drops = { hayDrop(), wheatDrop(), grassBlockDrop(), seedsDrop() };
        return level1Drops;
    }

    // Level 2 Drops

    public final ItemStack coalOreDrop() {
        ItemStack coalOre = new ItemStack(Material.COAL_ORE);
        ItemMeta coalOreMeta = coalOre.getItemMeta();
        coalOreMeta.displayName(Component.text("Coal Ore", NamedTextColor.GREEN, TextDecoration.ITALIC));
        coalOreMeta.addEnchant(Enchantment.PUNCH, 1, false);
        coalOreMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES);
        List<Component> lore = new ArrayList<Component>();
        lore.add(Component.text("Sell this at the Miner for money!", NamedTextColor.GRAY, TextDecoration.ITALIC));
        coalOreMeta.lore(lore);
        coalOre.setItemMeta(coalOreMeta);
        return coalOre;
    }

    public final ItemStack ironOreDrop() {
        ItemStack ironOre = new ItemStack(Material.IRON_ORE);
        ItemMeta ironOreMeta = ironOre.getItemMeta();
        ironOreMeta.displayName(Component.text("Iron Ore", NamedTextColor.GREEN, TextDecoration.ITALIC));
        ironOreMeta.addEnchant(Enchantment.PUNCH, 1, false);
        ironOreMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES);
        List<Component> lore = new ArrayList<Component>();
        lore.add(Component.text("Sell this at the Miner for money!", NamedTextColor.GRAY, TextDecoration.ITALIC));
        ironOreMeta.lore(lore);
        ironOre.setItemMeta(ironOreMeta);
        return ironOre;
    }

    public final ItemStack redstoneOreDrop() {
        ItemStack redstone = new ItemStack(Material.REDSTONE_ORE);
        ItemMeta redstoneMeta = redstone.getItemMeta();
        redstoneMeta.displayName(Component.text("Redstone Ore", NamedTextColor.GREEN, TextDecoration.ITALIC));
        redstoneMeta.addEnchant(Enchantment.PUNCH, 1, false);
        redstoneMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES);
        List<Component> lore = new ArrayList<Component>();
        lore.add(Component.text("Sell this at the Miner for money!", NamedTextColor.GRAY, TextDecoration.ITALIC));
        redstoneMeta.lore(lore);
        redstone.setItemMeta(redstoneMeta);
        return redstone;
    }

    public final ItemStack emeraldOreDrop() {
        ItemStack emerald = new ItemStack(Material.EMERALD_ORE);
        ItemMeta emeraldMeta = emerald.getItemMeta();
        emeraldMeta.displayName(Component.text("Emerald Ore", NamedTextColor.GREEN, TextDecoration.ITALIC));
        emeraldMeta.addEnchant(Enchantment.PUNCH, 1, false);
        emeraldMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES);
        List<Component> lore = new ArrayList<Component>();
        lore.add(Component.text("Sell this at the Miner for money!", NamedTextColor.GRAY, TextDecoration.ITALIC));
        emeraldMeta.lore(lore);
        emerald.setItemMeta(emeraldMeta);
        return emerald;
    }

    public ItemStack[] getLevel2Drops() {
        ItemStack[] level2Drops = { coalOreDrop(), ironOreDrop(), redstoneOreDrop(), emeraldOreDrop() };
        return level2Drops;
    }

    public ItemStack[] getAllZombieDrops() {
        ItemStack[] allZombieDrops = { hayDrop(), wheatDrop(), grassBlockDrop(), seedsDrop(), coalOreDrop(),
                ironOreDrop(), redstoneOreDrop(), emeraldOreDrop() };
        return allZombieDrops;
    }
    // Zombie Drops Level 3

    public ItemStack createDropFromFile(String material, String displayName, double worth) {
        ItemStack item = new ItemStack(Material.valueOf(material));
        ItemMeta meta = item.getItemMeta();
        meta.displayName(LegacyComponentSerializer.legacyAmpersand().deserialize(displayName));
        List<Component> lore = new ArrayList<Component>();
        lore.add(Component.text("Sell at the Black Market Dealer", NamedTextColor.GRAY, TextDecoration.ITALIC)
                .append(Component.text(" for ", NamedTextColor.GRAY, TextDecoration.ITALIC))
                .append(Component.text("$" + worth, NamedTextColor.GOLD, TextDecoration.ITALIC)));
        meta.lore(lore);
        item.setItemMeta(meta);
        return item;
    }

    public ItemStack[] getCustomItems() {
        ItemStack[] customItems = { createDropFromFile(dropItemConfig.getString("material"),
                dropItemConfig.getString("displayname"), dropItemConfig.getDouble("worth")) };
        return customItems;
    }
}
