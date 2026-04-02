package dev.nullpointercoding.zdeatharcade.Vendors.BlackMarketPages;

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
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import dev.nullpointercoding.zdeatharcade.Main;
import dev.nullpointercoding.zdeatharcade.Utils.InventoryUtils.CustomInvFunctions;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;

public class BuyTokensPage implements Listener {

    private final Inventory buyTokensInv;
    private final Component title = Component.text("Buy Tokens", NamedTextColor.GOLD, TextDecoration.BOLD);
    private Main plugin = Main.getInstance();

    public enum Tokens {
        ONE_HUNDRED("100", 0.99),
        THREE_HUNDRED("300", 2.99),
        FIVE_HUNDRED("500", 4.99),
        ONE_THOUSAND("1000", 9.99),
        TWO_THOUSAND("2000", 19.99),
        FIVE_THOUSAND("5000", 49.99),
        TEN_THOUSAND("10000", 99.99),
        TWENTY_THOUSAND("20000", 199.99);

        private String name;
        private Double price;

        Tokens(String name, Double price) {
            this.name = name;
            this.price = price;
        }

        public String getTokenAmmount() {
            return name;
        }

        public Double getPrice() {
            return price;
        }
    }

    public BuyTokensPage() {
        boolean isEventRegistered = HandlerList.getRegisteredListeners(plugin).stream()
                .anyMatch(handler -> handler.getListener() instanceof BuyTokensPage);
        if (!isEventRegistered) {
            Bukkit.getPluginManager().registerEvents(this, plugin);
        }
        buyTokensInv = Bukkit.createInventory(null, 9, title);
    }

    public Inventory getBuyTokensInv() {
        return buyTokensInv;
    }

    public void openInventory(Player player) {
        buyTokensInv.setItem(0, createTokens(Tokens.ONE_HUNDRED));
        buyTokensInv.setItem(1, createTokens(Tokens.THREE_HUNDRED));
        buyTokensInv.setItem(2, createTokens(Tokens.FIVE_HUNDRED));
        buyTokensInv.setItem(3, createTokens(Tokens.ONE_THOUSAND));
        buyTokensInv.setItem(4, createTokens(Tokens.TWO_THOUSAND));
        buyTokensInv.setItem(5, createTokens(Tokens.FIVE_THOUSAND));
        buyTokensInv.setItem(6, createTokens(Tokens.TEN_THOUSAND));
        buyTokensInv.setItem(7, createTokens(Tokens.TWENTY_THOUSAND));
        buyTokensInv.setItem(8, CustomInvFunctions.getBackButton());

        player.openInventory(buyTokensInv);

    }

    @EventHandler
    public void onInvClick(InventoryClickEvent e) {
        if (e.getView().title().equals(title)) {
            e.setCancelled(true);
            Player whoClicked = (Player) e.getWhoClicked();
            if (e.getCurrentItem() != null) {
                String cleanName = PlainTextComponentSerializer.plainText()
                        .serialize(e.getCurrentItem().getItemMeta().displayName());
                String tokenAmmount = cleanName.split(" ")[0];
                if (tokenAmmount.equals(Tokens.ONE_HUNDRED.getTokenAmmount())) {
                    whoClicked.sendMessage("You bought 100 tokens");
                }
                if (tokenAmmount.equals(Tokens.THREE_HUNDRED.getTokenAmmount())) {
                    whoClicked.sendMessage("You bought 300 tokens");
                }
                if (tokenAmmount.equals(Tokens.FIVE_HUNDRED.getTokenAmmount())) {
                    whoClicked.sendMessage("You bought 500 tokens");
                }
                if (tokenAmmount.equals(Tokens.ONE_THOUSAND.getTokenAmmount())) {
                    whoClicked.sendMessage("You bought 1000 tokens");
                }
                if (tokenAmmount.equals(Tokens.TWO_THOUSAND.getTokenAmmount())) {
                    whoClicked.sendMessage("You bought 2000 tokens");
                }
                if (tokenAmmount.equals(Tokens.FIVE_THOUSAND.getTokenAmmount())) {
                    whoClicked.sendMessage("You bought 5000 tokens");
                }
                if (tokenAmmount.equals(Tokens.TEN_THOUSAND.getTokenAmmount())) {
                    whoClicked.sendMessage("You bought 10000 tokens");
                }
                if (tokenAmmount.equals(Tokens.TWENTY_THOUSAND.getTokenAmmount())) {
                    whoClicked.sendMessage("You bought 20000 tokens");
                }
                if (e.getCurrentItem().equals(CustomInvFunctions.getBackButton())) {
                    whoClicked.closeInventory();
                    whoClicked.sendMessage("You clicked the back button");
                }
            }
        }
    }

    private ItemStack createTokens(Tokens token) {
        ItemStack tokenItem = new ItemStack(Material.SUNFLOWER);
        ItemMeta tokenMeta = tokenItem.getItemMeta();
        tokenMeta.displayName(
                Component.text(token.getTokenAmmount() + " Tokens", NamedTextColor.GOLD, TextDecoration.BOLD));
        List<Component> lore = new ArrayList<Component>();
        lore.add(Component.text("Price: " + token.getPrice() + " USD", NamedTextColor.GOLD, TextDecoration.BOLD));
        lore.add(Component.text("Click to buy", NamedTextColor.GOLD, TextDecoration.BOLD));
        tokenMeta.lore(lore);
        tokenItem.setItemMeta(tokenMeta);
        return tokenItem;
    }

}
