package dev.nullpointercoding.zdeatharcade.Zombies;

import java.io.File;
import java.io.IOException;
import java.util.EnumSet;
import java.util.Set;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

import dev.nullpointercoding.zdeatharcade.Main;

public class MobSpawnToggleFile implements Listener {

    private static final String ROOT_PATH = "DisabledMobs";
    private final Main plugin = Main.getInstance();
    private final File configFile;
    private final FileConfiguration config;

    public MobSpawnToggleFile() {
        this.configFile = new File(plugin.getMainDataFolder(), "mob-spawn-toggles.yml");
        this.config = YamlConfiguration.loadConfiguration(configFile);
        ensureConfigExists();
        ensureDefaults();
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onCreatureSpawn(CreatureSpawnEvent e) {
        if (!MobSpawnController.isMobSpawningEnabled()) {
            return;
        }
        if (isMobDisabled(e.getEntityType())) {
            e.setCancelled(true);
        }
    }

    public boolean isMobDisabled(EntityType entityType) {
        if (!isToggleableMob(entityType)) {
            return false;
        }
        return config.getBoolean(getPath(entityType), getDefaultDisabledState(entityType));
    }

    public boolean setMobDisabled(EntityType entityType, boolean disabled) {
        if (!isToggleableMob(entityType)) {
            return false;
        }
        config.set(getPath(entityType), disabled);
        saveConfig();
        return true;
    }

    public boolean toggleMob(EntityType entityType) {
        if (!isToggleableMob(entityType)) {
            return false;
        }
        boolean updatedState = !isMobDisabled(entityType);
        setMobDisabled(entityType, updatedState);
        return updatedState;
    }

    public Set<EntityType> getManagedMobs() {
        EnumSet<EntityType> managedMobs = EnumSet.noneOf(EntityType.class);
        for (EntityType entityType : EntityType.values()) {
            if (isToggleableMob(entityType)) {
                managedMobs.add(entityType);
            }
        }
        return managedMobs;
    }

    private boolean isToggleableMob(EntityType entityType) {
        return entityType != null && entityType.isAlive() && entityType.isSpawnable();
    }

    private String getPath(EntityType entityType) {
        return ROOT_PATH + "." + entityType.name();
    }

    private boolean getDefaultDisabledState(EntityType entityType) {
        return entityType != EntityType.ZOMBIE;
    }

    private void ensureConfigExists() {
        if (configFile.exists()) {
            return;
        }
        File parent = configFile.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }
        saveConfig();
    }

    private void ensureDefaults() {
        boolean updated = false;
        for (EntityType entityType : EntityType.values()) {
            if (!isToggleableMob(entityType)) {
                continue;
            }
            String path = getPath(entityType);
            if (!config.contains(path)) {
                config.set(path, getDefaultDisabledState(entityType));
                updated = true;
            }
        }
        if (updated) {
            saveConfig();
        }
    }

    private void saveConfig() {
        try {
            config.save(configFile);
        } catch (IOException e) {
            plugin.getLogger().severe("Could not save mob spawn toggle config: " + configFile.getAbsolutePath());
        }
    }
}