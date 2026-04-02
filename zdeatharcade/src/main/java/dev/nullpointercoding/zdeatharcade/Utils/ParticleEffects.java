package dev.nullpointercoding.zdeatharcade.Utils;

import java.io.File;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import dev.nullpointercoding.zdeatharcade.Main;

public class ParticleEffects {

    private static BukkitTask bmparticleTask;
    private static BukkitTask npcParticleTask;

    private static void spawnForBlackMarketParticle() {
        NPCConfigManager npcConfig = new NPCConfigManager("blackmarketvendor");
        Location spawnLoc = new Location(npcConfig.getWorld(), npcConfig.getNPCxLoc(), npcConfig.getNPCyLoc(),
                npcConfig.getNPCzLoc());
        double radius = 2;
        for (double t = 0; t <= 2 * Math.PI * radius; t += 0.05) {
            double x = (radius * Math.cos(t)) + spawnLoc.getX();
            double z = (spawnLoc.getZ() + radius * Math.sin(t));
            Location particleLoc = new Location(npcConfig.getWorld(), x - 0.8d, spawnLoc.getY() - 1.0d, z - 0.6d);
            bmparticleTask = new BukkitRunnable() {
                @Override
                public void run() {
                    double red = 174 / 255D;
                    double green = 33 / 255D;
                    double blue = 218 / 255D;
                    npcConfig.getWorld().spawnParticle(Particle.ELECTRIC_SPARK, particleLoc, 0, red, green, blue, 1);
                }

            }.runTaskTimer(Main.getInstance(), 10, 10);
        }
    }

    public static void startEffects() {
        if (bmparticleTask == null) {
            spawnForBlackMarketParticle();
        }
        if (npcParticleTask == null) {
            for (File f : Main.getInstance().getNPCDataFolder().listFiles()) {
                if (f.getName().equals("blackmarketvendor.yml")) {
                    return;
                }
                spawnNPCParticle(f.getName().replace(".yml", ""));

            }
        }
    }

    private static void spawnNPCParticle(String npcName) {
        NPCConfigManager npcConfig = new NPCConfigManager(npcName);
        Location spawnLoc = new Location(npcConfig.getWorld(), npcConfig.getNPCxLoc(), npcConfig.getNPCyLoc(),
                npcConfig.getNPCzLoc());
        Location particleLoc = new Location(npcConfig.getWorld(), spawnLoc.getX(), spawnLoc.getY() + 3,
                spawnLoc.getZ());
        npcParticleTask = new BukkitRunnable() {
            @Override
            public void run() {
                if (npcName.equalsIgnoreCase("blackmarketvendor")) {
                    return;
                }
                if (npcName.equalsIgnoreCase("farmervendor")) {
                    DustOptions dustOptions = new DustOptions(Color.fromRGB(0, 255, 205), 1);
                    npcConfig.getWorld().spawnParticle(Particle.DUST, particleLoc, 50, dustOptions);
                }
                if (npcName.equalsIgnoreCase("gunvendor")) {
                    DustOptions dustOptions = new DustOptions(Color.fromRGB(0, 51, 255), 1);
                    npcConfig.getWorld().spawnParticle(Particle.DUST, particleLoc, 50, dustOptions);
                }
                if (npcName.equalsIgnoreCase("minervendor")) {
                    DustOptions dustOptions = new DustOptions(Color.fromRGB(0, 255, 0), 1);
                    npcConfig.getWorld().spawnParticle(Particle.DUST, particleLoc, 50, dustOptions);
                }
                if (npcName.equalsIgnoreCase("gunsmithvendor")) {
                    DustOptions dustOptions = new DustOptions(Color.fromRGB(255, 0, 0), 1);
                    npcConfig.getWorld().spawnParticle(Particle.DUST, particleLoc, 50, dustOptions);
                }
                if(npcName.equalsIgnoreCase("snackvendor")){
                    DustOptions dustOptions = new DustOptions(Color.fromRGB(255, 255, 0), 1);
                    npcConfig.getWorld().spawnParticle(Particle.DUST, particleLoc, 50, dustOptions);
                }
            }
        }.runTaskTimer(Main.getInstance(), 10, 10);

    }
}
