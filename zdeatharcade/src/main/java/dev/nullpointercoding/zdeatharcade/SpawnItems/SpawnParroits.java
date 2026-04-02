package dev.nullpointercoding.zdeatharcade.SpawnItems;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Parrot;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.SpawnerSpawnEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;

import dev.nullpointercoding.zdeatharcade.Main;
import io.papermc.paper.event.entity.EntityMoveEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;

public class SpawnParroits implements Listener {

    private Integer parroits = 0;
    private final Component parrotName = Component.text("Wild Parrot", NamedTextColor.BLUE, TextDecoration.ITALIC);
    private List<Parrot> parrotsThatGoBoom = new ArrayList<Parrot>();

    @EventHandler
    public void onSpawn(SpawnerSpawnEvent e) {
        parroits = e.getEntity().getWorld().getEntitiesByClass(Parrot.class).size();
        if (e.getEntity() instanceof Parrot) {
            if (parroits < 30) {
                Parrot parrot = (Parrot) e.getEntity();
                parrot.customName(parrotName);
                parrot.setCustomNameVisible(true);

            } else {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onParrotHit(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Parrot && e.getDamager() instanceof Player) {
            Player p = (Player) e.getDamager();
            if (e.getEntity().customName().equals(parrotName)) {
                e.setCancelled(true);
                p.damage(2);
                p.sendHurtAnimation(90.0f);
            }
        }
        if (e.getEntity() instanceof LivingEntity && e.getDamager() instanceof Parrot) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onParrotMove(EntityMoveEvent e) {
        if (!(e.getEntity() instanceof Parrot)) return;
        Parrot par = (Parrot) e.getEntity();
        double x = e.getFrom().getX();
        double y = e.getFrom().getY();
        double z = e.getFrom().getZ();

        double finalX = e.getTo().getX();
        double finalY = e.getTo().getY();
        double finalZ = e.getTo().getZ();
        if (x != finalX || y != finalY || z != finalZ) {
            if(!(isParrotinSpawn(par))){
                par.remove();
            }
        }
    }

    @EventHandler
    public void onParrotRightClick(PlayerInteractEntityEvent e) {
        if (e.getRightClicked() instanceof Parrot) {
            Parrot par = (Parrot) e.getRightClicked();
            Player p = (Player) e.getPlayer();
            ItemStack item = p.getInventory().getItemInMainHand();
            if (item.getType().name().endsWith("SEEDS")) {
                e.setCancelled(true);
                p.playSound(p, Sound.ENTITY_PLAYER_HURT, 1.0f, 1.0f);
                par.customName(Component.text("I don't like seeds!", NamedTextColor.RED, TextDecoration.BOLD));
                new BukkitRunnable() {

                    @Override
                    public void run() {
                        par.customName(parrotName);
                        par.setCustomNameVisible(true);
                    }

                }.runTaskLater(Main.getInstance(), 20 * 3);
                return;
            }
            if (par.customName().equals(parrotName)) {
                p.playSound(p, Sound.ENTITY_PARROT_IMITATE_SKELETON, 1.0f, 1.0f);
                par.customName(randomParrotName(p, par));
                par.setCustomNameVisible(true);
                new BukkitRunnable() {

                    @Override
                    public void run() {
                        if (parrotsThatGoBoom.contains(par)) {
                            par.getWorld().createExplosion(par, par.getLocation(), 2.0f, false, false);
                            parrotsThatGoBoom.remove(par);
                            par.remove();
                        } else {
                            par.customName(parrotName);
                            par.setCustomNameVisible(true);
                        }
                    }

                }.runTaskLater(Main.getInstance(), 20 * 3);
            }
        }
    }

    private Component randomParrotName(Player player, Parrot par) {
        Component parrotName;
        int random = (int) (Math.random() * 5) + 1;
        switch (random) {
            case 1:
                parrotName = Component.text("Hello!", NamedTextColor.BLUE, TextDecoration.ITALIC)
                        .append(Component.text(" " + player.getName(), NamedTextColor.GOLD, TextDecoration.ITALIC));
                return parrotName;
            case 2:
                parrotName = Component.text("Please stop touching me...", NamedTextColor.YELLOW, TextDecoration.BOLD);
                return parrotName;
            case 3:
                parrotName = Component.text("I may be wild but I really haven't really even left the Dome",
                        NamedTextColor.GREEN, TextDecoration.ITALIC);
                return parrotName;
            case 4:
                parrotName = Component
                        .text("Do you always touch wild animals?", NamedTextColor.RED, TextDecoration.BOLD)
                        .append(Component.text(" " + player.getName(), NamedTextColor.GOLD, TextDecoration.ITALIC));
                return parrotName;
            case 5:
                parrotName = Component.text("Okay that's it, I'm leaving", NamedTextColor.DARK_RED,
                        TextDecoration.BOLD);
                parrotsThatGoBoom.add(par);
                return parrotName;
        }
        return null;
    }

    private boolean isParrotinSpawn(Parrot par) {
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionQuery query = container.createQuery();
        ApplicableRegionSet set = query.getApplicableRegions(BukkitAdapter.adapt(par.getLocation()));
        for (ProtectedRegion pr : set)
            if (pr.getId().equalsIgnoreCase("innerspawn"))
                return true;
        return false;
    }

}
