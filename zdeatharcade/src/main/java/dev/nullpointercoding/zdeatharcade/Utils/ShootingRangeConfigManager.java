package dev.nullpointercoding.zdeatharcade.Utils;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import dev.nullpointercoding.zdeatharcade.Main;

public class ShootingRangeConfigManager {

    private Main plugin = Main.getInstance();

    private String configName;
    private File configFile;
    private FileConfiguration config;
    private Boolean isRangeSpawnSet;

    public ShootingRangeConfigManager(String configName) {
        this.configName = configName;
        configHandler();
    }

    public String getConfigName() {
        return configName;
    }

    public FileConfiguration getConfig() {
        return (YamlConfiguration) config;
    }

    public Boolean getIsRangeSpawnSet() {
        return isRangeSpawnSet;
    }

    public void saveConfig() {
        try {
            config.save(configFile);
        } catch (IOException e) {
            plugin.saveResource(configName, false);
        }
    }

    private void configHandler() {
        configFile = new File(plugin.getRangeConfigFolder() + File.separator + configName);
        if (!configFile.exists()) {
            configFile.getParentFile().mkdirs();
            try {
                configFile.createNewFile();
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
}
