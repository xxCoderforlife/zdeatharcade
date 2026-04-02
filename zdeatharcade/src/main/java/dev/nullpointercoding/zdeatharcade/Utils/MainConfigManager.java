package dev.nullpointercoding.zdeatharcade.Utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import org.bukkit.Location;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import dev.nullpointercoding.zdeatharcade.Main;

public class MainConfigManager {

    private Main plugin = Main.getInstance();

    private String configName;
    private File configFile;
    private FileConfiguration config;
    private static Integer zombieSpawnLimit;
    private static boolean mobSpawningEnabled;
    private final String configStart = "zDeathArcade.";

    public MainConfigManager() {
        this.configName = "config.yml";
        configHandler();
        if (!getConfig().contains(configStart + "world-mob-spawning-enabled")) {
            getConfig().set(configStart + "world-mob-spawning-enabled", true);
            saveConfig();
        }
        zombieSpawnLimit = getConfig().getInt(configStart + "world-zombie-spawnLimit");
        mobSpawningEnabled = getConfig().getBoolean(configStart + "world-mob-spawning-enabled", true);
    }

    public String getConfigName() {
        return configName;
    }

    public File getConfigFile() {
        return configFile;
    }

    public static Integer getZombieSpawnLimit() {
        return zombieSpawnLimit;
    }

    public static boolean isMobSpawningEnabled() {
        return mobSpawningEnabled;
    }

    public void setZombieSpawnLimit(Integer zombieSpawnLimit) {
        MainConfigManager.zombieSpawnLimit = zombieSpawnLimit;
        getConfig().set(configStart + "world-zombie-spawnLimit", zombieSpawnLimit);
        saveConfig();
    }

    public void setMobSpawningEnabled(boolean mobSpawningEnabled) {
        MainConfigManager.mobSpawningEnabled = mobSpawningEnabled;
        getConfig().set(configStart + "world-mob-spawning-enabled", mobSpawningEnabled);
        saveConfig();
    }

    public FileConfiguration getConfig() {
        return (YamlConfiguration) config;
    }

    public void saveConfig() {
        try {
            config.save(configFile);
        } catch (IOException e) {
            plugin.saveResource(configName, false);
        }
    }

    private void configHandler() {
        configFile = new File(plugin.getMainDataFolder() + File.separator + configName);
        if (!configFile.exists()) {
            configFile.getParentFile().mkdirs();
            try {
                configFile.createNewFile();
                Reader r = new InputStreamReader(this.getClass().getResourceAsStream("/config.yml"));
                config = YamlConfiguration.loadConfiguration(r);
                config.save(configFile);
                r.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        config = new YamlConfiguration();
        try {
            config.load(configFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public Location getAFKPoolSpawn() {
        return getConfig().getLocation(configStart + "AFKPool.Location");
    }
}
