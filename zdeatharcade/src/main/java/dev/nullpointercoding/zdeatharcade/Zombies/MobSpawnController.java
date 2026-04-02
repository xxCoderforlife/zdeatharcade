package dev.nullpointercoding.zdeatharcade.Zombies;

import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

import dev.nullpointercoding.zdeatharcade.Utils.MainConfigManager;

public class MobSpawnController implements Listener {

    public static boolean isMobSpawningEnabled() {
        return MainConfigManager.isMobSpawningEnabled();
    }

    public static boolean setMobSpawningEnabled(boolean enabled) {
        MainConfigManager mainConfigManager = new MainConfigManager();
        mainConfigManager.setMobSpawningEnabled(enabled);
        return enabled;
    }

    public static boolean toggleMobSpawning() {
        return setMobSpawningEnabled(!isMobSpawningEnabled());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onCreatureSpawn(CreatureSpawnEvent e) {
        if (isMobSpawningEnabled()) {
            return;
        }
        if (e.getEntityType() == EntityType.ARMOR_STAND) {
            return;
        }
        e.setCancelled(true);
    }
}