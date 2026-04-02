package dev.nullpointercoding.zdeatharcade.PlayerAccount;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import dev.nullpointercoding.zdeatharcade.Main;
import dev.nullpointercoding.zdeatharcade.Utils.PlayerConfigManager;
import dev.nullpointercoding.zdeatharcade.Utils.Packets.PacketHandler;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;

public class PlayerSettingsGUI implements Listener {

    private Main plugin = Main.getInstance();
    private final Inventory inv;
    private final Component title = Component.text("§c§lMONEY MANAGAMENT SYSTEM");
    private Component zombieVisName = Component.text("Zombie Visable", NamedTextColor.LIGHT_PURPLE,
            TextDecoration.ITALIC);
    private Component playerVisName = Component.text("Players Visable", NamedTextColor.LIGHT_PURPLE,
            TextDecoration.ITALIC);
    private PlayerConfigManager pcm;


    public PlayerSettingsGUI(Player p) {
        boolean isEventRegistered = HandlerList.getRegisteredListeners(plugin).stream()
                .anyMatch(handler -> handler.getListener() instanceof PlayerSettingsGUI);
        if (!isEventRegistered) {
            Bukkit.getPluginManager().registerEvents(this, plugin);
        }
        pcm = new PlayerConfigManager(p.getUniqueId());
        inv = Bukkit.createInventory(null, 36, title);
    }

    public Inventory getInventory() {
        return inv;
    }

    private void addItems() {
        inv.setItem(11, toggleZombieVisablity());
        inv.setItem(10, togglePlayerVisablity());
        inv.setItem(12,
                createGUIItem(Component.text("Reload Resource Pack", NamedTextColor.GREEN, TextDecoration.ITALIC),
                        List.of(Component.text("Click to reload", NamedTextColor.GRAY)), Material.WOODEN_AXE));

    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (!e.getView().title().equals(title))
            return;

        e.setCancelled(true);

        if (e.getCurrentItem() == null || !e.getCurrentItem().getItemMeta().hasDisplayName())
            return;

        // TODO Automate this method later
        Inventory inventory = e.getInventory();
        ItemStack clickedItem = e.getCurrentItem();

        if (inventory == null || clickedItem == null) {
            return;
        }

        // Check if the clicked item is the item you want to toggle
        if (clickedItem.getItemMeta().displayName().equals(playerVisName)) {
            // TODO Send Player Packet
            if(pcm.isPlayerVisable()){
                new PacketHandler().stopShowingZombie((Player)e.getWhoClicked());
            }else{
                new PacketHandler().showZombie((Player)e.getWhoClicked());
            }
            pcm.setPlayerVisable(!pcm.isPlayerVisable());
            inventory.setItem(e.getSlot(), togglePlayerVisablity());
        }
        if (clickedItem.getItemMeta().displayName().equals(zombieVisName)) {
            // TODO Send Zombie Packet
            pcm.setZombieVisable(!pcm.isZombieVisable());
            inventory.setItem(e.getSlot(), toggleZombieVisablity());
        }

    }

    public void onInventoryOpen(Player whoOpened) {
        addItems();
        whoOpened.openInventory(inv);
    }

    private ItemStack togglePlayerVisablity(){
        Material playerVisMat = pcm.isPlayerVisable() ? Material.GREEN_DYE : Material.RED_DYE;
        ItemStack playerVis = new ItemStack(playerVisMat);
        ItemMeta playerVisMeta = playerVis.getItemMeta();
        playerVisMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES,ItemFlag.HIDE_ENCHANTS);
        List<Component> lore = new ArrayList<Component>();
        lore.add(Component.text("Click to toggle", NamedTextColor.GRAY));
        lore.add(Component.text("Status: ", NamedTextColor.GRAY).append(Component.text(pcm.isPlayerVisable() ? "§aON" : "§cOFF")));
        playerVisMeta.lore(lore);
        playerVisMeta.displayName(playerVisName);
        playerVis.setItemMeta(playerVisMeta);
        return playerVis;
    }
    
    private ItemStack toggleZombieVisablity(){
        Material zombieVisMat = pcm.isZombieVisable() ? Material.GREEN_DYE : Material.RED_DYE;
        ItemStack zombieVis = new ItemStack(zombieVisMat);
        ItemMeta zombieVisMeta = zombieVis.getItemMeta();
        zombieVisMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES,ItemFlag.HIDE_ENCHANTS);
        List<Component> lore = new ArrayList<Component>();
        lore.add(Component.text("Click to toggle", NamedTextColor.GRAY));
        lore.add(Component.text("Status: ", NamedTextColor.GRAY).append(Component.text(pcm.isZombieVisable() ? "§aON" : "§cOFF")));
        zombieVisMeta.lore(lore);
        zombieVisMeta.displayName(zombieVisName);
        zombieVis.setItemMeta(zombieVisMeta);
        return zombieVis;
    }


    private ItemStack createGUIItem(Component displayName, List<Component> lore, Material mat) {
        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(displayName);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES,ItemFlag.HIDE_ENCHANTS);
        meta.lore(lore);
        item.setItemMeta(meta);
        return item;

    }
}
