package dev.nullpointercoding.zdeatharcade.Bank;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent.Reason;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import com.destroystokyo.paper.profile.PlayerProfile;

import dev.nullpointercoding.zdeatharcade.Main;
import dev.nullpointercoding.zdeatharcade.Bank.BankAccountGUI.AccountType;
import dev.nullpointercoding.zdeatharcade.PlayerAccount.PlayerAccountGUI;
import dev.nullpointercoding.zdeatharcade.Utils.InventoryUtils.BlankSpaceFiller;
import dev.nullpointercoding.zdeatharcade.Utils.InventoryUtils.CustomInvFunctions;
import net.kyori.adventure.text.Component;

public class BankGUI implements Listener {

    private Main plugin = Main.getInstance();
    private Player p;
    private final Inventory inv;
    private Component title = Component.text("§c§l         BANK MENU");

    public BankGUI(Player p) {
        boolean isEventRegistered = HandlerList.getRegisteredListeners(plugin).stream()
                .anyMatch(handler -> handler.getListener() instanceof BankGUI);
        if (!isEventRegistered) {
            Bukkit.getPluginManager().registerEvents(this, plugin);
        }
        this.p = p;
        inv = Bukkit.getServer().createInventory(null, 9, title);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (!(e.getView().title().equals(title))) {
            return;
        }
        e.setCancelled(true);
        ItemStack clicked = e.getCurrentItem();
        if (clicked == null) {
            return;
        }
        BankAccountGUI bankAccountGUI = new BankAccountGUI(p);
        if (clicked.getItemMeta().displayName().equals(depositToBank().getItemMeta().displayName())) {
            bankAccountGUI.setIsDespoisting(true, AccountType.BANK);
            bankAccountGUI.openGUI(p);
            p.playSound(p, Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
        }
        if (clicked.getItemMeta().displayName().equals(withdrawFromBank().getItemMeta().displayName())) {
            bankAccountGUI.setIsDespoisting(false, AccountType.BANK);
            bankAccountGUI.openGUI(p);
            p.playSound(p, Sound.ENTITY_PLAYER_HURT, 1.0f, 1.0f);

        }
        if (clicked.getItemMeta().displayName().equals(back().getItemMeta().displayName())) {
            p.closeInventory(Reason.PLUGIN);
            PlayerAccountGUI playerAccountGUI = new PlayerAccountGUI(p);
            playerAccountGUI.openGUI(p);
            p.playSound(p, Sound.ITEM_ARMOR_EQUIP_LEATHER, 1.0f, 1.0f);
        }
    }

    private void addItems() {
        inv.setItem(0, depositToBank());
        inv.setItem(4, withdrawFromBank());
        inv.setItem(8, back());
    }

    public void openGUI(Player p) {
        addItems();
        BlankSpaceFiller.fillinBlankInv(inv, List.of(0));
        p.openInventory(inv);
    }

    private ItemStack depositToBank() {
        ItemStack is = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta im = (SkullMeta) is.getItemMeta();
        im.addEnchant(Enchantment.PUNCH, 1, false);
        im.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES);
        im.displayName(Component.text("§a§lDeposit to Bank"));
        PlayerProfile profile = CustomInvFunctions.getProfile(
                "https://textures.minecraft.net/texture/a79a5c95ee17abfef45c8dc224189964944d560f19a44f19f8a46aef3fee4756");
        im.setPlayerProfile(profile);
        is.setItemMeta(im);
        return is;

    }

    private ItemStack withdrawFromBank() {
        ItemStack is = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta im = (SkullMeta) is.getItemMeta();
        im.addEnchant(Enchantment.PUNCH, 1, false);
        im.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES);
        im.displayName(Component.text("§c§lWithdraw from Bank"));
        PlayerProfile profile = CustomInvFunctions.getProfile(
                "https://textures.minecraft.net/texture/beb588b21a6f98ad1ff4e085c552dcb050efc9cab427f46048f18fc803475f7");
        im.setPlayerProfile(profile);
        is.setItemMeta(im);
        return is;

    }

    private ItemStack back() {
        ItemStack is = new ItemStack(Material.BARRIER);
        ItemMeta im = is.getItemMeta();
        im.displayName(Component.text("§c§oBack"));
        List<Component> lore = List.of(Component.text("§7Go back to the main menu"));
        im.lore(lore);
        is.setItemMeta(im);
        return is;
    }
}
