package dev.nullpointercoding.zdeatharcade.Utils.InventoryUtils;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import net.kyori.adventure.text.Component;

public class BlankSpaceFiller {

    @SuppressWarnings("null")
    public static void fillinBlankInv(@NotNull Inventory inv, @NotNull List<Integer> slotsToIgnore) {
        for (int i = 0; i < inv.getSize(); i++) {
            if (slotsToIgnore == null) {
                inv.setItem(i, fillerItem());
            }
            if (!slotsToIgnore.contains(i) && inv.getItem(i) == null) {
                inv.setItem(i, fillerItem());
            }
        }
    }

    public static void fillinBlankInv(@NotNull Inventory inv) {
        for (int i = 0; i < inv.getSize(); i++) {
            if (inv.getItem(i) == null) {
                inv.setItem(i, fillerItem());
            }
        }
    }

    public static ItemStack fillerItem() {
        ItemStack filler = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta fillerMeta = filler.getItemMeta();
        fillerMeta.displayName(Component.empty());
        filler.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        filler.setItemMeta(fillerMeta);
        return filler;
    }
}
