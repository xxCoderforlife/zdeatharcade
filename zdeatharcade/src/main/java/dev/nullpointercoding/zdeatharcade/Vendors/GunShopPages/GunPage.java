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

public class GunPage implements Listener {

    private final Inventory inv;
    private Main plugin = Main.getInstance();
    private final Component title = Component.text("Guns Guns Guns");
    private Double fn57Price = 5000.0;
    private Double aa12Price = 8000.0;
    private Double ak47Price = 6000.0;
    private Double ak47uPrice = 5000.0;
    private Double glockPrice = 3000.0;
    private Double m4a1sPrice = 10000.0;
    private Double mac10Price = 8000.0;
    private Double uziPrice = 5000.0;
    private Double stenPrice = 6000.0;
    private Double awpPrice = 8000.0;
    private Double henryriflePrice = 25000.0;
    private Double mp5kPrice = 9000.0;
    private Double mp40Price = 7000.0;
    private Double umpPrice = 6000.0;
    private Double p30Price = 10000.0;
    private Double fnfalPrice = 12000.0;
    private final Component star = Component.text('★', NamedTextColor.YELLOW, TextDecoration.BOLD);

    public GunPage() {
        boolean isEventRegistered = HandlerList.getRegisteredListeners(plugin).stream()
                .anyMatch(handler -> handler.getListener() instanceof GunPage);
        if (!isEventRegistered) {
            Bukkit.getPluginManager().registerEvents(this, plugin);
        }
        inv = Bukkit.createInventory(null, 45, title);
    }

    public void openInventory(Player whoOpened) {
        addItems();
        BlankSpaceFiller.fillinBlankInv(inv, List.of(10));
        whoOpened.openInventory(inv);
    }

    private void addItems() {
        inv.setItem(34, CustomInvFunctions.getBackButton());

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
