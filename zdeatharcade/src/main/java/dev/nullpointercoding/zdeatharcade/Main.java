package dev.nullpointercoding.zdeatharcade;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.profile.PlayerTextures;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.destroystokyo.paper.profile.PlayerProfile;
import dev.nullpointercoding.zdeatharcade.Commands.Commands;
import dev.nullpointercoding.zdeatharcade.Listeners.PlayerListeners;
import dev.nullpointercoding.zdeatharcade.PlayerAccount.PlayerAccountCommands;
import dev.nullpointercoding.zdeatharcade.SpawnItems.SpawnParroits;
import dev.nullpointercoding.zdeatharcade.SpawnItems.AFKPool.AFKPool;
import dev.nullpointercoding.zdeatharcade.Utils.MainConfigManager;
import dev.nullpointercoding.zdeatharcade.Utils.PlayerConfigManager;
import dev.nullpointercoding.zdeatharcade.Utils.Packets.PacketHandler;
import dev.nullpointercoding.zdeatharcade.Utils.PlaceHolders.Placeholders;
import dev.nullpointercoding.zdeatharcade.Utils.VaultHookFolder.EcoCommands;
import dev.nullpointercoding.zdeatharcade.Utils.VaultHookFolder.EcoTabCommands;
import dev.nullpointercoding.zdeatharcade.Utils.VaultHookFolder.VaultHook;
import dev.nullpointercoding.zdeatharcade.Vendors.BlackMarketVendor;
import dev.nullpointercoding.zdeatharcade.Vendors.FarmerVendor;
import dev.nullpointercoding.zdeatharcade.Vendors.GunVendor;
import dev.nullpointercoding.zdeatharcade.Vendors.MinerVendor;
import dev.nullpointercoding.zdeatharcade.Vendors.SnackVendor;
import dev.nullpointercoding.zdeatharcade.Vendors.VendorCommands;
import dev.nullpointercoding.zdeatharcade.Vendors.VendorTabCommands;
import dev.nullpointercoding.zdeatharcade.Vendors.Snacks.SnackEatEvent;
import dev.nullpointercoding.zdeatharcade.Zombies.MobSpawnController;
import dev.nullpointercoding.zdeatharcade.Zombies.MobSpawnToggleFile;
import dev.nullpointercoding.zdeatharcade.Zombies.ZombieCommands;
import dev.nullpointercoding.zdeatharcade.Zombies.ZombieHandler;
import dev.nullpointercoding.zdeatharcade.Zombies.PrizeLlama.PrizeLlamaCommands;
import dev.nullpointercoding.zdeatharcade.Zombies.PrizeLlama.PrizeLlamaHandler;
import net.milkbowl.vault2.economy.Economy;

public class Main extends JavaPlugin {

    private static Main plugin;
    private static Random r;
    private static Random spawmChance;
    private static Economy econ;
    private ProtocolManager protocolManager;
    private Boolean isRangeSpawnSet;
    private File zombieSpawnLocations = new File(getDataFolder() + File.separator + "Zombie Spawn Locations");
    private File rangeConfigFolder = new File(getDataFolder() + File.separator + "Shooting Range");
    private File playerInventoryDataFolder = new File(getDataFolder() + File.separator + "Player Inventory Data");
    private File NPCDataFolder = new File(getDataFolder() + File.separator + "NPC Data");
    private File PlayerDataFolder = new File(getDataFolder() + File.separator + "Player Data");
    private File bankDataFolder = new File(getDataFolder() + File.separator + "Bank Data");
    private File customGuns = new File(getDataFolder() + File.separator + "Custom Guns");
    private File customZombieDrops = new File(getDataFolder() + File.separator + "Custom Zombie Drops");
    private File dailyDealFolder = new File(getDataFolder() + File.separator + "Daily Deals");
    private File mainDataFolder = new File(getDataFolder() + File.separator + "Main Data");
    private PlayerProfile profile;

    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;
        new MainConfigManager();
        r = new Random();
        spawmChance = new Random();
        createConfigFolders();
        registerEcon();
        if (!setupEconomy()) {
            System.out.println("Vault not found! Disabling plugin!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        RegisterCommandsandEvents();
        loadKnownPlayers(); //Plan to nest somewhere but wanna test
        System.out.println("ZDeathArcade has been enabled!");

    }

    @Override
    public void onLoad() {
        System.out.println("ZDeathArcade has been loaded!");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        System.out.println("ZDeathArcade has been disabled!");
    }

    public static Main getInstance() {
        return plugin;
    }

    public static Random getRandom() {
        return r;
    }

    public static Random getSpawnChance() {
        return spawmChance;
    }

    private void registerEcon() {
        Bukkit.getServer().getServicesManager().register(Economy.class, new VaultHook(), plugin,
                ServicePriority.Highest);
                econ = new VaultHook();
    }

    private void RegisterCommandsandEvents() {
        protocolManager = ProtocolLibrary.getProtocolManager();
        new Placeholders(this).register();
        new PacketHandler().registerAllPackets();
        getCommand("zdeatharcade").setExecutor(new Commands());
        getCommand("spawn").setExecutor(new Commands());
        getCommand("afk").setExecutor(new Commands());
        getCommand("settings").setExecutor(new PlayerAccountCommands());
        getCommand("zdeatharcade").setTabCompleter(new Commands());
        getCommand("economy").setExecutor(new EcoCommands());
        getCommand("economy").setTabCompleter(new EcoTabCommands());
        getCommand("account").setExecutor(new PlayerAccountCommands());
        getCommand("zombie").setExecutor(new ZombieCommands());
        getCommand("vendor").setExecutor(new VendorCommands());
        getCommand("vendor").setTabCompleter(new VendorTabCommands());
        getCommand("token").setExecutor(new EcoCommands());
        getCommand("token").setTabCompleter(new EcoTabCommands());
        getCommand("prizellama").setExecutor(new PrizeLlamaCommands());
        getServer().getPluginManager().registerEvents(new MobSpawnController(), this);
        getServer().getPluginManager().registerEvents(new MobSpawnToggleFile(), this);
        getServer().getPluginManager().registerEvents(new ZombieHandler(), this);
        getServer().getPluginManager().registerEvents(new PlayerListeners(), this);
        getServer().getPluginManager().registerEvents(new FarmerVendor(), this);
        getServer().getPluginManager().registerEvents(new MinerVendor(), this);
        getServer().getPluginManager().registerEvents(new GunVendor(), this);
        getServer().getPluginManager().registerEvents(new BlackMarketVendor(), this);
        getServer().getPluginManager().registerEvents(new SnackVendor(), this);
        getServer().getPluginManager().registerEvents(new SnackEatEvent(), this);
        getServer().getPluginManager().registerEvents(new SpawnParroits(), this);
        getServer().getPluginManager().registerEvents(new AFKPool(), this);
        getServer().getPluginManager().registerEvents(new PrizeLlamaHandler(), this);
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    private void createConfigFolders() {
        if (!getDataFolder().exists()) {
            getDataFolder().mkdirs();
            Bukkit.getConsoleSender().sendMessage("§aZDeathArcade folder has been created!");
        }
        if (!zombieSpawnLocations.exists()) {
            zombieSpawnLocations.mkdirs();
            Bukkit.getConsoleSender().sendMessage("§aZombie Spawn Locations folder has been created!");

        }
        if (!playerInventoryDataFolder.exists()) {
            playerInventoryDataFolder.mkdirs();
            Bukkit.getConsoleSender().sendMessage("§aPlayer Inventory Data folder has been created!");

        }
        if (!NPCDataFolder.exists()) {
            NPCDataFolder.mkdirs();
            Bukkit.getConsoleSender().sendMessage("§aNPCData folder has been created!");

        }
        if (!PlayerDataFolder.exists()) {
            PlayerDataFolder.mkdirs();
            Bukkit.getConsoleSender().sendMessage("§aPlayerData folder has been created!");
        }
        if (!bankDataFolder.exists()) {
            bankDataFolder.mkdirs();
            Bukkit.getConsoleSender().sendMessage("§aBankData folder has been created!");
        }
        if (!customGuns.exists()) {
            customGuns.mkdirs();
            Bukkit.getConsoleSender().sendMessage("§aCustomGuns folder has been created!");
        }
        if (!customZombieDrops.exists()) {
            customZombieDrops.mkdirs();
            Bukkit.getConsoleSender().sendMessage("§aCustomZombieDrops folder has been created!");
        }
        if (!customZombieDrops.exists()) {
            customZombieDrops.mkdirs();
            Bukkit.getConsoleSender().sendMessage("§aCustomZombieDrops folder has been created!");
        }
        if (!dailyDealFolder.exists()) {
            dailyDealFolder.mkdirs();
            Bukkit.getConsoleSender().sendMessage("§aDailyDeal folder has been created!");
        }
        if (!mainDataFolder.exists()) {
            mainDataFolder.mkdirs();
            Bukkit.getConsoleSender().sendMessage("§aMainData folder has been created!");
        }

        if (!rangeConfigFolder.exists()) {
            rangeConfigFolder.mkdirs();
            Bukkit.getConsoleSender().sendMessage("§aShooting Range folder has been created!");
            isRangeSpawnSet = false;
        } else {
            for (File f : rangeConfigFolder.listFiles()) {
                if (f.getName().equalsIgnoreCase("config.yml")) {
                    Bukkit.getConsoleSender().sendMessage("§aShooting Range config.yml has been found!");
                    Bukkit.getConsoleSender().sendMessage("§aSetting ShootingRanage to true");
                    isRangeSpawnSet = true;
                    return;

                }
            }
        }

    }

    public File getZombieSpawnLocationFolder() {
        return zombieSpawnLocations;
    }

    public File getRangeConfigFolder() {
        return rangeConfigFolder;
    }

    public Boolean getIsRangeSpawnSet() {
        return isRangeSpawnSet;
    }

    public File getPlayerInventoryDataFolder() {
        return playerInventoryDataFolder;
    }

    public File getNPCDataFolder() {
        return NPCDataFolder;
    }

    public File getPlayerDataFolder() {
        return PlayerDataFolder;
    }

    public File getBankDataFolder() {
        return bankDataFolder;
    }

    public File getCustomGunsFolder() {
        return customGuns;
    }
    
    public File getMainDataFolder() {
        return mainDataFolder;
    }

    public File getCustomZombieDropsFolder() {
        return customZombieDrops;
    }

    public File getDailyDealFolder() {
        return dailyDealFolder;
    }

    public static Economy getEconomy() {
        return econ;
    }

    public ProtocolManager getProtocolManager() {
        return protocolManager;
    }

    public void loadKnownPlayers() {
    File folder = getPlayerDataFolder();
    if (folder == null) return;
    
    File[] files = folder.listFiles((dir, name) -> name.endsWith(".yml") && name.length() == 40); // rough UUID check
    if (files == null) return;
    
    for (File f : files) {
        try {
            UUID uuid = UUID.fromString(f.getName().substring(0, f.getName().length() - 4));
            new PlayerConfigManager(uuid).addKnownPlayer(uuid);
        } catch (IllegalArgumentException ignored) { }
    }
}
    private final UUID R_UUID = UUID.fromString("92864445-51c5-4c3b-9039-517c9927d1b4");
    private PlayerProfile getProfile(String url) {
        PlayerProfile profile = (PlayerProfile) Bukkit.createProfile(R_UUID); // Get a new player profile
        PlayerTextures textures = profile.getTextures();
        URL urlObject = null;
        try {
            urlObject = new URI(url).toURL(); // The URL to the skin, for example:
                                      // https://textures.minecraft.net/texture/18813764b2abc94ec3c3bc67b9147c21be850cdf996679703157f4555997ea63a
        } catch (MalformedURLException | URISyntaxException exception) {
            throw new RuntimeException("Invalid URL", exception);
        }
        textures.setSkin(urlObject); // Set the skin of the player profile to the URL
        profile.setTextures(textures); // Set the textures back to the profile
        return profile;
    }

}
