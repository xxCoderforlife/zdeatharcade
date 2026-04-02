package dev.nullpointercoding.zdeatharcade.Vendors.BlackMarketPages;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent.Reason;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import dev.nullpointercoding.zdeatharcade.Main;
import dev.nullpointercoding.zdeatharcade.Utils.PlayerConfigManager;
import dev.nullpointercoding.zdeatharcade.Utils.InventoryUtils.CustomInvFunctions;
import dev.nullpointercoding.zdeatharcade.Utils.InventoryUtils.Pages;
import dev.nullpointercoding.zdeatharcade.Vendors.BlackMarketVendor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public class SellPage implements Listener {
    private final Component title = Component.text("        Sell Screen", NamedTextColor.GREEN, TextDecoration.BOLD);
    private Main plugin = Main.getInstance();
    private ArrayList<ItemStack> customItems;
    private ArrayList<File> customItemFiles = new ArrayList<File>();
    private File dropItemFile;
    private FileConfiguration dropItemConfig;

    public SellPage() {
        boolean isEventRegistered = HandlerList.getRegisteredListeners(plugin).stream()
                .anyMatch(handler -> handler.getListener() instanceof SellPage);
        if (!isEventRegistered) {
            Bukkit.getPluginManager().registerEvents(this, plugin);
        }
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
            customItemFiles.add(f);

        }
    }

    public void openInventory(Player player) {
        customItems = new ArrayList<ItemStack>();
        customItems.add(createItemStackFromFile(dropItemConfig.getString("displayname"),
                dropItemConfig.getString("material"), dropItemConfig.getDouble("worth")));
        new Pages(player, customItems, title);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (!(e.getView().title().equals(title))) {
            return;
        }
        e.setCancelled(true);
        ItemStack clicked = e.getCurrentItem();
        TextComponent comName = LegacyComponentSerializer.legacyAmpersand()
                .deserialize(dropItemConfig.getString("displayname"));
        if (clicked.getItemMeta().displayName().equals(comName)) {
            checkForItems((Player) e.getWhoClicked(), clicked, dropItemConfig.getDouble("worth"));
        }
        if (clicked.getItemMeta().displayName()
                .equals(CustomInvFunctions.getBackButton().getItemMeta().displayName())) {
            e.getWhoClicked().closeInventory(Reason.PLUGIN);
            new BlackMarketVendor().openInventory((Player) e.getWhoClicked());
        }
    }

    private ItemStack createItemStackFromFile(String displayName, String material, double worth) {
        ItemStack item = new ItemStack(Material.valueOf(material));
        ItemMeta meta = item.getItemMeta();
        meta.displayName(LegacyComponentSerializer.legacyAmpersand().deserialize(displayName));
        List<Component> lore = new ArrayList<Component>();
        lore.add(Component.text("Sell for : " + worth + " tokens", NamedTextColor.GREEN));
        meta.lore(lore);
        item.setItemMeta(meta);
        return item;
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
