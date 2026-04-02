package dev.nullpointercoding.zdeatharcade.Vendors.Snacks;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;

public class SnackEatEvent implements Listener {

    /**
     * @param e
     */
    @EventHandler
    public void onSnackEat(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        Action pa = e.getAction();

        if (!pa.equals(Action.RIGHT_CLICK_AIR) && !pa.equals(Action.RIGHT_CLICK_BLOCK)) {
            return;
        }
        if (e.getHand().equals(EquipmentSlot.OFF_HAND)) {
            return;
        }
        ItemStack mainHand = p.getInventory().getItemInMainHand();
        if (Snack.isSnack(mainHand)) {
            e.setCancelled(true);
            Bukkit.getConsoleSender().sendMessage(Double.toString(p.getHealth()));
            Bukkit.getConsoleSender().sendMessage(Double.toString(p.getFoodLevel()));
            if (p.getGameMode() == GameMode.CREATIVE) {
                p.sendMessage(Component.text("Change to Adventure Mode to use Snacks")
                        .clickEvent(ClickEvent.runCommand("gamemode adventure " + p.getName()))
                        .hoverEvent(HoverEvent.showText(Component.text("Click to Change to Adventure Mode"))));
                return;
            }
            Snack.eatSnack(p);

            }
        }
    }
