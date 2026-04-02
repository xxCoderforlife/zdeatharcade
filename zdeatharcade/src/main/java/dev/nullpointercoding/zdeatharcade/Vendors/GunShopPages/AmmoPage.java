package dev.nullpointercoding.zdeatharcade.Vendors.GunShopPages;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import dev.nullpointercoding.zdeatharcade.Main;
import dev.nullpointercoding.zdeatharcade.Utils.InventoryUtils.BlankSpaceFiller;
import dev.nullpointercoding.zdeatharcade.Utils.InventoryUtils.CustomInvFunctions;
import dev.nullpointercoding.zdeatharcade.Vendors.GunVendor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;

public class AmmoPage implements Listener {
    private Main plugin = Main.getInstance();
    private final Inventory inv;
    private final Component title = Component.text("Ammo Page", NamedTextColor.GOLD, TextDecoration.ITALIC);
    private final Component star = Component.text('★', NamedTextColor.YELLOW, TextDecoration.BOLD);

    public AmmoPage() {
        boolean isEventRegistered = HandlerList.getRegisteredListeners(plugin).stream()
                .anyMatch(handler -> handler.getListener() instanceof AmmoPage);
        if (!isEventRegistered) {
            Bukkit.getPluginManager().registerEvents(this, plugin);
        }
        inv = Bukkit.createInventory(null, 27, title);
    }

    public void openInventory(Player whoOpened) {
        addItems();
        BlankSpaceFiller.fillinBlankInv(inv, List.of(10));
        whoOpened.openInventory(inv);
    }

    private void addItems() {
        inv.setItem(16, CustomInvFunctions.getBackButton());

    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getView().title().equals(title)) {
            e.setCancelled(true);
            ItemStack clicked = e.getCurrentItem();
            if (clicked == null || clicked.getItemMeta().displayName() == null) {
                return;
            }
            Player whoClicked = (Player) e.getWhoClicked();
            if (clicked.getItemMeta().displayName()
                    .equals(CustomInvFunctions.getBackButton().getItemMeta().displayName())) {
                whoClicked.closeInventory();
                new GunVendor().openInventory(whoClicked);
            }
        }
    }
}
