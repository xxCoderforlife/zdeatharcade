package dev.nullpointercoding.zdeatharcade.PlayerAccount;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent.Reason;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import dev.nullpointercoding.zdeatharcade.Main;
import dev.nullpointercoding.zdeatharcade.Utils.InventoryUtils.BlankSpaceFiller;
import dev.nullpointercoding.zdeatharcade.Utils.InventoryUtils.CustomInvFunctions;
import dev.nullpointercoding.zdeatharcade.Utils.InventoryUtils.Pages;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.md_5.bungee.api.ChatColor;

public class OtherPlayerAccounts implements Listener {

    private Main plugin = Main.getInstance();
    private final Inventory inv;
    private final Component title = Component.text("§c§l    PLAYER ACCOUNTS");
    private ArrayList<ItemStack> playerHeads;

    public OtherPlayerAccounts() {
        boolean isEventRegistered = HandlerList.getRegisteredListeners(plugin).stream()
                .anyMatch(handler -> handler.getListener() instanceof OtherPlayerAccounts);
        if (!isEventRegistered) {
            Bukkit.getPluginManager().registerEvents(this, plugin);
        }
        inv = Bukkit.createInventory(null, 36, title);
    }

    private void addItems() {
        ItemStack filler = BlankSpaceFiller.fillerItem();
        inv.setItem(18, filler);
        inv.setItem(19, filler);
        inv.setItem(20, filler);
        inv.setItem(21, filler);
        inv.setItem(22, filler);
        inv.setItem(23, filler);
        inv.setItem(24, filler);
        inv.setItem(25, filler);
        inv.setItem(29, filler);
        inv.setItem(30, filler);
        inv.setItem(31, filler);
        inv.setItem(32, filler);
        inv.setItem(33, filler);
        inv.setItem(34, filler);
        for (Player p : Bukkit.getOnlinePlayers()) {
            ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta meta = (SkullMeta) skull.getItemMeta();
            meta.displayName(Component.text("§c§l" + p.getName()));
            meta.setOwningPlayer(p);
            skull.setItemMeta(meta);
            inv.addItem(skull);
            playerHeads = new ArrayList<>();
            playerHeads.add(skull);
        }
    }

    public void openGUI(Player p) {
        addItems();
        new Pages(p, playerHeads, title);
    }

    @EventHandler
    public void onClickEvent(InventoryClickEvent e) {
        if (!(e.getView().title().equals(title))) {
            return;
        }
        e.setCancelled(true);
        Player p = (Player) e.getWhoClicked();
        if (!(Pages.users.containsKey(p.getUniqueId()))) {
            return;
        }
        Pages inv = Pages.users.get(p.getUniqueId());
        ItemStack clicked = e.getCurrentItem();
        if (clicked == null || clicked.getItemMeta().displayName() == null) {
            return;
        }
        if (clicked.getType() == Material.PLAYER_HEAD) {
            String name = PlainTextComponentSerializer.plainText().serialize(clicked.getItemMeta().displayName());
            String cleanName = ChatColor.stripColor(name);
            Player target = Bukkit.getPlayer(cleanName);
            PlayerProfileManager playerProfile = new PlayerProfileManager(target);
            p.playSound(p, Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);
            playerProfile.openGUI(p);
        }
        if (clicked.getItemMeta().displayName()
                .equals(CustomInvFunctions.getBackButton().getItemMeta().displayName())) {
            p.closeInventory(Reason.PLUGIN);
            PlayerAccountGUI gui = new PlayerAccountGUI(p);
            gui.openGUI(p);
            p.playSound(p, Sound.ITEM_ARMOR_EQUIP_LEATHER, 1.0f, 1.0f);
        }
        if (clicked.getItemMeta().displayName().equals(Pages.nextPageName)) {
            if (inv.currpage >= inv.pages.size() - 1) {
                return;
            } else {
                // Next page exists, flip the page
                inv.currpage += 1;
                p.openInventory(inv.pages.get(inv.currpage));
            }
        }
        if (clicked.getItemMeta().displayName().equals(Pages.previousPageName)) {
            if (inv.currpage > 0) {
                // Flip to previous page
                inv.currpage -= 1;
                p.openInventory(inv.pages.get(inv.currpage));
            }
        }

    }

}
