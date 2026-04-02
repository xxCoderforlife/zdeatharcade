package dev.nullpointercoding.zdeatharcade.Vendors.BlackMarketPages;

import java.io.File;
import java.time.Duration;
import java.util.ArrayList;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent.Reason;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import dev.nullpointercoding.zdeatharcade.Main;
import dev.nullpointercoding.zdeatharcade.Utils.InventoryUtils.CustomInvFunctions;
import dev.nullpointercoding.zdeatharcade.Vendors.BlackMarketVendor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.title.Title;
import net.kyori.adventure.title.TitlePart;

public class BuyPage implements Listener {

    private final Component title = Component.text("        Buy Screen", NamedTextColor.GREEN, TextDecoration.BOLD);
    private Main plugin = Main.getInstance();
    private File gunConfigFile;
    private FileConfiguration gunConfig;
    private String itemName;
    private ArrayList<ItemStack> customGuns;
    private ArrayList<File> customGunFiles = new ArrayList<File>();

    public BuyPage() {
        boolean isEventRegistered = HandlerList.getRegisteredListeners(plugin).stream()
                .anyMatch(handler -> handler.getListener() instanceof BuyPage);
        if (!isEventRegistered) {
            Bukkit.getPluginManager().registerEvents(this, plugin);
        }

        for (File f : plugin.getCustomGunsFolder().listFiles()) {
            if (f.getName().endsWith(".yml")) {
                gunConfigFile = f;
            }
            try {
                gunConfig = new YamlConfiguration();
                gunConfig.load(gunConfigFile);
            } catch (Exception e) {
                e.printStackTrace();
            }
            itemName = gunConfig.getString("name");
            customGunFiles.add(f);

        }
    }

    public void openInventory(Player player) {
        player.sendTitlePart(TitlePart.TITLE, Component.text("Loading Black Market....", NamedTextColor.LIGHT_PURPLE));
        player.sendTitlePart(TitlePart.SUBTITLE, Component.text("Please wait", NamedTextColor.DARK_PURPLE));
        player.sendTitlePart(TitlePart.TIMES,
                Title.Times.times(Duration.ofSeconds(1), Duration.ofSeconds(3), Duration.ofSeconds(1)));
        new BukkitRunnable() {

            @Override
            public void run() {
            }
        }.runTaskLater(plugin, 20 * 4);

    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (!(e.getView().title().equals(title))) {
            return;
        }
        e.setCancelled(true);
        ItemStack clicked = e.getCurrentItem();
        if (clicked.getItemMeta().displayName()
                .equals(CustomInvFunctions.getBackButton().getItemMeta().displayName())) {
            Player whoClicked = (Player) e.getWhoClicked();
            whoClicked.closeInventory(Reason.PLUGIN);
            new BlackMarketVendor().openInventory(whoClicked);
        }
    }

}
