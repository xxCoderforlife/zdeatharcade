package dev.nullpointercoding.zdeatharcade.Vendors.BlackMarketPages;

import java.io.File;
import java.time.Duration;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent.Reason;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import dev.nullpointercoding.zdeatharcade.Main;
import dev.nullpointercoding.zdeatharcade.Utils.InventoryUtils.CustomInvFunctions;
import dev.nullpointercoding.zdeatharcade.Vendors.BlackMarketVendor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.title.Title;
import net.kyori.adventure.title.TitlePart;

public class DailyDealPage implements Listener {

    private final Inventory dailyDealInv;
    private Main plugin = Main.getInstance();
    private final Component title = Component.text("Daily Deal");
    private File dailyDealFile;
    private FileConfiguration dailyDealConfig;
    private Double newItemPrice;
    private static Random r;

    private static int randoInt(int min, int max) {
        int randoNum = r.nextInt((max - min) + 1) + min;
        return randoNum;
    }

    public DailyDealPage() {
        boolean isEventRegistered = HandlerList.getRegisteredListeners(plugin).stream()
                .anyMatch(handler -> handler.getListener() instanceof DailyDealPage);
        if (!isEventRegistered) {
            Bukkit.getPluginManager().registerEvents(this, plugin);
        }
        dailyDealInv = Bukkit.createInventory(null, 9, title);
        for (File f : plugin.getDailyDealFolder().listFiles()) {
            if (f.getName().endsWith(".yml")) {
                dailyDealFile = f;
            }
            try {
                dailyDealConfig = new YamlConfiguration();
                dailyDealConfig.load(dailyDealFile);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Bukkit.getConsoleSender().sendMessage("Loaded Daily Deal Config" + dailyDealFile.getName());

        }
    }

    public Inventory getDailyDealInv() {
        return dailyDealInv;
    }

    public void openInventory(Player player) {
        player.closeInventory();
        player.sendTitlePart(TitlePart.TITLE, Component.text("Loading Daily Deal...", NamedTextColor.LIGHT_PURPLE));
        player.sendTitlePart(TitlePart.SUBTITLE, Component.text("Please wait", NamedTextColor.DARK_PURPLE));
        player.sendTitlePart(TitlePart.TIMES,
                Title.Times.times(Duration.ofSeconds(1), Duration.ofSeconds(3), Duration.ofSeconds(1)));
        player.sendActionBar(Component.text("This might take a while....", NamedTextColor.LIGHT_PURPLE));
    }

    @EventHandler
    public void onInvClick(InventoryClickEvent e) {
        if (e.getView().title().equals(title)) {
            e.setCancelled(true);
            Player whoClicked = (Player) e.getWhoClicked();
            ItemStack clicked = e.getCurrentItem();
                if (clicked.getItemMeta().displayName()
                        .equals(CustomInvFunctions.getBackButton().getItemMeta().displayName())) {
                    whoClicked.closeInventory(Reason.PLUGIN);
                    new BlackMarketVendor().openInventory(whoClicked);
                }
        }
    }




}
