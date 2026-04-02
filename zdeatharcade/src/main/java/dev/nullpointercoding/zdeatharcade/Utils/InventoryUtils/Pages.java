package dev.nullpointercoding.zdeatharcade.Utils.InventoryUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import com.destroystokyo.paper.profile.PlayerProfile;

import net.kyori.adventure.text.Component;

public class Pages {
    public ArrayList<Inventory> pages = new ArrayList<Inventory>();
    public UUID id;
    public int currpage = 0;
    public static HashMap<UUID, Pages> users = new HashMap<UUID, Pages>();

    public Pages(Player p, ArrayList<ItemStack> items, Component name) {
        // Running this will open a paged inventory for the specified player, with the
        // items in the arraylist specified.
        this.id = UUID.randomUUID();
        // create new blank page
        Inventory page = getBlankPage(name);
        // According to the items in the arraylist, add items to the ScrollerInventory
        for (int i = 0; i < items.size(); i++) {
            // If the current page is full, add the page to the inventory's pages arraylist,
            // and create a new page to add the items.
            if (page.firstEmpty() == 46) {
                pages.add(page);
                page = getBlankPage(name);
                page.addItem(items.get(i));
            } else {
                // Add the item to the current page as per normal
                page.addItem(items.get(i));

            }
        }
        pages.add(page);
        // open page 0 for the specified player
        BlankSpaceFiller.fillinBlankInv(pages.get(currpage));
        p.openInventory(pages.get(currpage));
        users.put(p.getUniqueId(), this);
    }

    public static final Component nextPageName = Component.text("§a§oNext Page");
    public static final Component previousPageName = Component.text("§c§oPrevious Page");

    // This creates a blank page with the next and prev buttons
    private Inventory getBlankPage(Component name) {
        Inventory page = Bukkit.createInventory(null, 36, name);

        page.setItem(35, nextPage());
        page.setItem(28, prevPage());
        page.setItem(27, CustomInvFunctions.getBackButton());
        return page;
    }

    private ItemStack nextPage() {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) head.getItemMeta();
        meta.displayName(nextPageName);
        List<Component> lore = new ArrayList<Component>();
        lore.add(Component.text("§7Click to go to the next page"));
        meta.lore(lore);
        PlayerProfile profile = CustomInvFunctions.getProfile(
                "https://textures.minecraft.net/texture/18660691d1ca029f120a3ff0eabab93a2306b37a7d61119fcd141ff2f6fcd798");
        meta.setPlayerProfile(profile);
        head.setItemMeta(meta);
        return head;

    }

    private ItemStack prevPage() {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) head.getItemMeta();
        meta.displayName(previousPageName);
        List<Component> lore = new ArrayList<Component>();
        lore.add(Component.text("§7Click to go to the previous page"));
        meta.lore(lore);
        PlayerProfile profile = CustomInvFunctions.getProfile(
                "https://textures.minecraft.net/texture/52ba81b47d5ee06b484ea9bdf22934e6abca5e4ced7be3905d6ae6ecd6fcea2a");
        meta.setPlayerProfile(profile);
        head.setItemMeta(meta);
        return head;

    }

}
