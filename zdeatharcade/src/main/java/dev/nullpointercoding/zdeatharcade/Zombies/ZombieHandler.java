//TODO: Rewrite this so that you can actually read it and not have to pray to 6 of the 10 GODS that you know what in the world is going on.
package dev.nullpointercoding.zdeatharcade.Zombies;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import com.destroystokyo.paper.event.entity.EntityRemoveFromWorldEvent;

import dev.nullpointercoding.zdeatharcade.Main;
import dev.nullpointercoding.zdeatharcade.Utils.MainConfigManager;
import dev.nullpointercoding.zdeatharcade.Utils.PlayerConfigManager;
import dev.nullpointercoding.zdeatharcade.Utils.VaultHookFolder.VaultHook;
import net.milkbowl.vault2.economy.Economy;

public class ZombieHandler implements Listener {

    private Main plugin = Main.getInstance();
    Economy econ = new VaultHook();
    private File ZSLM = plugin.getZombieSpawnLocationFolder();
    private FileConfiguration config = new YamlConfiguration();
    private HashMap<File, Integer> zombieSpawnLocations = new HashMap<File, Integer>();
    private HashMap<ItemStack, Integer> zombieDropsLvl1 = new HashMap<ItemStack, Integer>();
    private HashMap<ItemStack, Integer> zombieDropsLvl2 = new HashMap<ItemStack, Integer>();
    private HashMap<ItemStack, Integer> zombieDropsLvl3 = new HashMap<ItemStack, Integer>();
    private static Random r = Main.getRandom();
    private ZombieDrops zDrops = new ZombieDrops();
    private static Random spawnChance = Main.getSpawnChance();
    private static Integer spawnCount = 0;
    private String[] mobList = { "SPIDER", "SKELETON", "CREEPER", "ENDERMAN", "SLIME", "WANDERING_TRADER", "PIG", "COW",
            "SHEEP","CHICKEN" };
    // Zombies
    private ZombieLevel1 zl1 = new ZombieLevel1();
    private ZombieLevel2 zl2 = new ZombieLevel2();
    private ZombieLevel3 zl3 = new ZombieLevel3();

    private static int randoInt(int min, int max) {
        int randoNum = r.nextInt((max - min) + 1) + min;
        return randoNum;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onZombieDeath(EntityDeathEvent e) {
        if (!(e.getEntity() instanceof Zombie)) {
            return;
        }/**
        TODO: create a switch method to handle the different zombie levels
        */
        spawnCount--;
        Zombie z = (Zombie) e.getEntity();
        e.getDrops().clear();
        if (z.name().equals(zl1.name())) {
            for (ItemStack s : zDrops.getLevel1Drops()) {
                zombieDropsLvl1.put(s, randoInt(1, 100));
                zombieDropsLvl1.put(new ItemStack(Material.AIR), randoInt(1, 100));
            }
            List<Integer> nums = new ArrayList<Integer>(zombieDropsLvl1.values());
            for (ItemStack is : zombieDropsLvl1.keySet()) {
                if (zombieDropsLvl1.get(is) == Collections.max(nums)) {
                    e.getDrops().add(is);
                    zombieDropsLvl1.clear();
                    return;
                }
            }
        }
        if (z.name().equals(zl2.name())) {
            for (ItemStack s : zDrops.getLevel2Drops()) {
                zombieDropsLvl2.put(s, randoInt(1, 100));
                zombieDropsLvl2.put(new ItemStack(Material.AIR), randoInt(1, 100));
            }
            List<Integer> nums = new ArrayList<Integer>(zombieDropsLvl2.values());
            for (ItemStack is : zombieDropsLvl2.keySet()) {
                if (zombieDropsLvl2.get(is) == Collections.max(nums)) {
                    e.getDrops().add(is);
                    zombieDropsLvl2.clear();
                    return;
                }
            }
        }
        if (z.name().equals(zl3.name())) {
            for (ItemStack s : zDrops.getCustomItems()) {
                zombieDropsLvl3.put(s, randoInt(1, 100));
                zombieDropsLvl3.put(new ItemStack(Material.AIR), randoInt(1, 100));
                Integer ran = randoInt(1, 100000);
                if (ran == 3) {
                    List<Integer> nums = new ArrayList<Integer>(zombieDropsLvl3.values());
                    for (ItemStack is : zombieDropsLvl3.keySet()) {
                        if (zombieDropsLvl3.get(is) == Collections.max(nums)) {
                            e.getDrops().add(is);
                            zombieDropsLvl3.clear();
                            return;
                        }
                    }

                } else {
                    e.getDrops().add(new ItemStack(Material.AIR));
                    zombieDropsLvl3.clear();
                    return;
                }
            }

        }
    }

    @EventHandler
    public void onZombieKill(EntityDamageByEntityEvent e) {
        Player p = (Player) e.getDamager();
        if (!(e.getEntity() instanceof Zombie)) {
            return;
        }
        Zombie z = (Zombie) e.getEntity();
        PlayerConfigManager pcm = new PlayerConfigManager(p.getUniqueId());
        if (z.name().equals(zl1.name())) {
            pcm.addBalance(BigDecimal.valueOf(0.2d));
            pcm.setKills(p);
            p.playSound(p, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.AMBIENT, 1, 1);
            return;
        }
        if (z.name().equals(zl2.name())) {
            pcm.addBalance(BigDecimal.valueOf(0.4d));
            pcm.setKills(p);
            p.playSound(p, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.AMBIENT, 1, 1);
            return;
        }
        if (z.name().equals(zl3.name())) {
            pcm.addBalance(BigDecimal.valueOf(0.6d));
            pcm.setKills(p);
            p.playSound(p, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.AMBIENT, 1, 1);
            return;
        }

    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void disableAllOtherMobSpawns(CreatureSpawnEvent e) {
        if (e.isCancelled() || !MobSpawnController.isMobSpawningEnabled()) {
            e.setCancelled(true);
            return;
        }
        LivingEntity c = e.getEntity();
        if (c.getType() == EntityType.ZOMBIE) {
            spawnCount = e.getEntity().getWorld().getEntitiesByClass(Zombie.class).size();
            if (spawnCount >= MainConfigManager.getZombieSpawnLimit()) {
                e.setCancelled(true);
                return;
            }
            Zombie z = (Zombie) c;
            if (!(z.getChunk().isLoaded())) {
                e.setCancelled(true);
                return;
            }
            if (ZSLM.listFiles().length == 0) {
                Bukkit.getConsoleSender().sendMessage(
                        "No zombie spawn locations found! Please create some in the ZombieSpawnLocations folder!");
                return;
            }
            for (File f : ZSLM.listFiles()) {
                zombieSpawnLocations.put(f, randoInt(1, 1000));
            }
            List<Integer> nums = new ArrayList<Integer>(zombieSpawnLocations.values());
            for (File f : ZSLM.listFiles()) {
                if (zombieSpawnLocations.get(f) == Collections.max(nums)) {
                    try {
                        config.load(f);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    Location locToTP = new Location(z.getWorld(), config.getDouble("X"), config.getDouble("Y") + 1,
                            config.getDouble("Z"));
                    if (spawnChance.nextInt(100) > 50) {
                        zl1.convertToLevel1Zombie(z);
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                z.teleportAsync(locToTP);
                            }
                        }.runTaskLater(plugin, 6);
                        zombieSpawnLocations.clear();
                        return;
                    }
                    if (spawnChance.nextInt(100) > 40) {
                        zl2.convertToLevel1Zombie(z);
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                z.teleportAsync(locToTP);
                            }
                        }.runTaskLater(plugin, 6);
                        zombieSpawnLocations.clear();
                        return;
                    }
                    if (spawnChance.nextInt(100) > 30) {
                        zl3.convertToLevel3Zombie(z);
                        new BukkitRunnable() {

                            @Override
                            public void run() {
                                z.teleportAsync(locToTP);
                            }

                        }.runTaskLater(plugin, 6);
                        zombieSpawnLocations.clear();
                        return;
                    }
                }
            }

        }
        for (String type : mobList) {
            hijackOtherMobSpawn(e, type);
        }

    }

    @EventHandler
    public void onZombieTarget(EntityTargetLivingEntityEvent e) {
        if (e.getEntity() instanceof Zombie) {
            Zombie z = (Zombie) e.getEntity();
            if (z.getTarget() instanceof Player) {
                if (!z.isCustomNameVisible()) {
                    zl1.convertToLevel1Zombie(z);
                }
            }
        }
    }

    @EventHandler
    public void onZombieRemove(EntityRemoveFromWorldEvent e) {
        if (e.getEntity() instanceof Zombie) {
            spawnCount--;
        }
    }

    @EventHandler
    public void onDropSpawnUtil(ItemSpawnEvent e) {
        Item i = e.getEntity();
        ItemStack is = i.getItemStack();

        if (e.getEntity().getItemStack().getType() == Material.ROTTEN_FLESH) {
            e.setCancelled(true);
        }
        for (ItemStack s : zDrops.getAllZombieDrops()) {
            if (s.getType() == is.getType()) {
                i.customName(s.getItemMeta().displayName());
                i.setCustomNameVisible(true);
            }
        }
    }

    public static Integer getSpawnCount() {
        return spawnCount;
    }

    public static void resetSpawnCount() {
        spawnCount = 0;
    }

    public void setSpawnCount(Integer spawnCount) {
        ZombieHandler.spawnCount = spawnCount;
    }

    private void hijackOtherMobSpawn(CreatureSpawnEvent e, String type) {
        if (e.getEntity().getType() == EntityType.valueOf(type)) {
            LivingEntity c = (LivingEntity) e.getEntity();
            Zombie z = (Zombie) c.getWorld().spawnEntity(c.getLocation(), EntityType.ZOMBIE);
            disableAllOtherMobSpawns(new CreatureSpawnEvent(z, SpawnReason.CUSTOM));
            e.setCancelled(true);
        }
    }

}
