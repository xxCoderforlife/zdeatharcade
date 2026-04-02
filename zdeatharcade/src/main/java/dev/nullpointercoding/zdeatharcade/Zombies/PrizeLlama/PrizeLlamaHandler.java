//TODO: Create a PrizeLlama class that handles the spawning of the llama and the chest. or maybe might just some private methods.
package dev.nullpointercoding.zdeatharcade.Zombies.PrizeLlama;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Llama;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import dev.nullpointercoding.zdeatharcade.Main;
import dev.nullpointercoding.zdeatharcade.Zombies.ZombieDrops;
import eu.decentsoftware.holograms.api.DHAPI;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import net.kyori.adventure.text.Component;

public class PrizeLlamaHandler implements Listener {

    private Main plugin = Main.getInstance();

    private static Random r = new Random();
    private PrizeLlama pLlama = new PrizeLlama();
    private ZombieDrops zDrops = new ZombieDrops();
    private Integer countdown = 30;
    private Chest prizeChest;
    private Hologram fistHolo;
    private Boolean chestBeingUnlocked = false;
    private BukkitTask prizeLlamaTask;

    private static int randoInt(int min, int max) {
        int randoNum = r.nextInt((max - min) + 1) + min;
        return randoNum;
    }

    @EventHandler
    public void onLlamaSpawn(CreatureSpawnEvent e) {
        if (e.getEntity() instanceof Zombie) {
            //TODO: Check if the runnable is already running.
            Integer spawnChance = randoInt(1, 100);
            if (spawnChance <= 10) {
                if(prizeLlamaTask != null && !prizeLlamaTask.isCancelled()){
                    return;
                }
                prizeLlamaTask = new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (e.getEntity().getWorld().getEntitiesByClass(Llama.class).size() == 0) {
                            Location loc = e.getEntity().getLocation();
                            loc.setY(loc.getY() + 1);
                            Llama la  = pLlama.spawnPrizeLlama(loc);
                            la.teleportAsync(e.getLocation(), TeleportCause.PLUGIN);
                            Bukkit.broadcast(Component.text("A Prize Llama has spawned!"));
                        }
                    }
                }.runTaskTimer(plugin, 0, 20 * 30);
            }
        }
    }

    // TODO: Figure out a better way to do this.
    @EventHandler
    public void onLlamaDeath(EntityDeathEvent e) {
        if (!(e.getEntity() instanceof Llama))
            return;
        Llama llama = (Llama) e.getEntity();
        if (llama.customName() == null)
            return;
        if (llama.customName().equals(pLlama.name())) {
            List<String> lines = Arrays.asList("PRIZE CHEST", "Click to open!");
            Location loc = llama.getLocation();
            Block blockLoc = loc.getBlock();
            blockLoc.setType(Material.CHEST);
            fistHolo = DHAPI.createHologram("prizellama", loc.add(0, 3, 0), true, lines);
            prizeChest = (Chest) blockLoc.getState();
            //Add items to chest
            Inventory chestInv = prizeChest.getBlockInventory();
            Integer fillChance = randoInt(1, 100);
            //TODO: Add class for Chest and ChestDrops
            if (fillChance <= 50) {
                for(ItemStack s : zDrops.getLevel1Drops()){
                    s.setAmount(64);
                    chestInv.addItem(s);
                }
            } else if (fillChance > 50 && fillChance <= 75) {
                for(ItemStack s : zDrops.getLevel2Drops()){
                    s.setAmount(64);
                    chestInv.addItem(s);
                }
            } 
        }
    }

    @EventHandler
    public void onPrizeChestOpen(PlayerInteractEvent e) {
        if (e.getClickedBlock() == null || prizeChest == null)
            return;
        if (e.getClickedBlock().getType() == Material.CHEST) {
            if (e.getClickedBlock().getLocation().equals(prizeChest.getLocation())) {
                Block chestBlock = prizeChest.getBlock();
                Player p = (Player) e.getPlayer();
                if (chestBeingUnlocked) {
                    p.sendMessage("The Prize Chest is already being opened!");
                    e.setCancelled(true);
                    return;
                }
                Hologram holo = DHAPI.createHologram("chestTimer", fistHolo.getLocation(), true,
                        List.of("Prize Chest Unlocking...", "Time Left: 0:30"));
                holo.enable();
                fistHolo.delete();
                Bukkit.broadcast(p.displayName().append(Component.text(" is opening the Prize Chest!")));
                chestBeingUnlocked = true;
                new BukkitRunnable() {

                    @Override
                    public void run() {
                        if (countdown > 0) {
                            DHAPI.setHologramLine(holo, 1, "Time Left: 0:" + countdown);
                            countdown--;
                        } else if (countdown == 0) {
                            for(ItemStack s : prizeChest.getBlockInventory().getContents()){
                                if(s != null){
                                    Bukkit.getWorld(p.getWorld().getUID()).dropItemNaturally(chestBlock.getLocation().add(0, 2, 0), s);
                                }

                            }
                            chestBlock.setType(Material.AIR);
                            Bukkit.broadcast(Component.text("The Prize Chest has been opened!"));
                            chestBeingUnlocked = false;
                            this.cancel();
                            holo.delete();
                            countdown = 30;
                        }
                    }

                }.runTaskTimer(plugin, 0, 20);
                e.setCancelled(true);
            }
        }
    }


}
