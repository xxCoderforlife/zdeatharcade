package dev.nullpointercoding.zdeatharcade.Utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import dev.nullpointercoding.zdeatharcade.Main;

public class NPCConfigManager {

    private Main plugin = Main.getInstance();

    private String configName;
    private World npcWorld;
    private Integer npcID;
    private Double npcxLoc;
    private Double npcyLoc;
    private Double npczLoc;
    private File configFile;
    private FileConfiguration config;

    public NPCConfigManager(String configName, World world, Integer npcID, Double xLoc, Double yLoc, Double zLoc) {
        this.configName = configName;
        this.npcWorld = world;
        this.npcID = npcID;
        this.npcxLoc = xLoc;
        this.npcyLoc = yLoc;
        this.npczLoc = zLoc;
        configHandler();
    }

    public NPCConfigManager(String configName) {
        this.configName = configName;
        configHandler();
    }

    public String getConfigName() {
        return configName;
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

    public static List<String> getNPCs() {
        List<String> npcList = new ArrayList<>();
        for (File f : Main.getInstance().getNPCDataFolder().listFiles()) {
            if (f.getName().endsWith(".yml")) {
                String name = f.getName().replace(".yml", "");
                npcList.add(name);
            }
        }
        return npcList;
    }

    private void configHandler() {
        configFile = new File(plugin.getNPCDataFolder() + File.separator + configName.toLowerCase() + ".yml");
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
        if (!config.contains("NPC.World")) {
            config.set("NPC.Name", configName);
            config.set("NPC.World", npcWorld.getName());
            config.set("NPC.id", npcID);
            config.set("NPC.xLoc", npcxLoc);
            config.set("NPC.yLoc", npcyLoc);
            config.set("NPC.zLoc", npczLoc);
            config.set("NPC.Effect", "FLAME");
        }
        saveConfig();
    }

    public void reloadConfig() {

        config = new YamlConfiguration();
        try {
            config.load(configFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public void updateConfig(File f) {
        f.delete();
        configHandler();
    }

    public void setNPCWorld(World world) {
        config.set("NPC.World", world.getName());
        saveConfig();
    }

    public void setNPCID(Integer npcID) {
        config.set("NPC.id", npcID);
        saveConfig();
    }

    public void setNPCxLoc(Double xLoc) {
        config.set("NPC.xLoc", xLoc);
        saveConfig();
    }

    public void setNPCyLoc(Double yLoc) {
        config.set("NPC.yLoc", yLoc);
        saveConfig();
    }

    public void setNPCzLoc(Double zLoc) {
        config.set("NPC.zLoc", zLoc);
        saveConfig();
    }

    public Particle getNPCEffect() {
        Particle effect = Particle.valueOf(config.getString("NPC.Effect").toUpperCase());
        return effect;
    }

    public World getWorld() {
        World npcWorld = Bukkit.getWorld(config.getString("NPC.World"));
        return npcWorld;
    }

    public Integer getNPCID() {
        return config.getInt("NPC.id");
    }

    public Double getNPCxLoc() {
        return config.getDouble("NPC.xLoc");
    }

    public Double getNPCyLoc() {
        return config.getDouble("NPC.yLoc");
    }

    public Double getNPCzLoc() {
        return config.getDouble("NPC.zLoc");
    }

    public List<Float> getNPCyaw() {
        return config.getFloatList("NPC.yaw");
    }

    public List<Float> getNPCpitch() {
        return config.getFloatList("NPC.pitch");
    }
}
