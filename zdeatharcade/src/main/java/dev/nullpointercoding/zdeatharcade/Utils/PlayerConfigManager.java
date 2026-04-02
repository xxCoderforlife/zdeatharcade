package dev.nullpointercoding.zdeatharcade.Utils;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Statistic;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import dev.nullpointercoding.zdeatharcade.Main;
import dev.nullpointercoding.zdeatharcade.Utils.VaultHookFolder.VaultHook;

public class PlayerConfigManager {

    private Main plugin = Main.getInstance();
    private File configFile;
    private FileConfiguration config;
    private String configName;
    private String configPath;
    private final UUID playerUUID;
    private final ConcurrentHashMap<UUID, Boolean> knownPlayersMap = new ConcurrentHashMap<>();
    private final Set<UUID> knownPlayers = knownPlayersMap.keySet(true);

    
    public enum configPaths{
        PLAYER("Player"),
        UUID("UUID"),
        CLIENTBRAND("Player-Client-Brand"),
        LASTLOGIN("LastLogin"),
        ZOMBIEKILLS("Zombie-Kills"),
        DEATHS("Deaths"),
        CASH("Cash"),
        TOKENS("Tokens"),
        BOUNTY("Bounty"),
        BOUNTYHASBOUNTY("Bounty.Has-Bounty"),
        BOUNTYAMOUNT("Bounty.Bounty-Amount"),
        SETTINGS("Settings"),
        SETTINGS_PLAYER_VISABLE(".Player-Visable"),
        SETTINGS_ZOMBIE_VISABLE(".Zombie-Visable");

        private String path;

        configPaths(String path){
            this.path = path;
        }

        public String getPath(){
            return path;
        }
    }

    public PlayerConfigManager(UUID playerUUID) {
        this.playerUUID = playerUUID;
        this.configName = playerUUID.toString();
        this.configPath = configName + '.';
        this.configFile = new File(plugin.getPlayerDataFolder(), configName + ".yml");
    }

    public String getConfigName() {
        return configName;
    }

    public FileConfiguration getConfig() {
        ensureConfigLoaded();
        return (YamlConfiguration) config;
    }

    public void saveConfig() {
        ensureConfigLoaded();
        try {
            config.save(configFile);
        } catch (IOException e) {
            plugin.saveResource(configName, false);
        }

    }

    public boolean exists() {
        return configFile != null && configFile.exists() && configFile.isFile();
    }

    private void ensureConfigLoaded() {
        if (config == null) {
            configHandler();
        }
    }

    private void configHandler() {
        if (configFile == null) {
            configFile = new File(plugin.getPlayerDataFolder() + File.separator + configName + ".yml");
        }
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

    public void updatePlayerDataFile() {
        configHandler();
        YamlConfiguration playerConfig = (YamlConfiguration) config;
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String formattedDate = now.format(myFormatObj);

        playerConfig.set(configPath + configPaths.PLAYER.getPath(), Main.getInstance().getServer().getPlayer(playerUUID).getName());
        playerConfig.set(configPath + configPaths.UUID.getPath(), playerUUID.toString());
        playerConfig.set(configPath + configPaths.CLIENTBRAND.getPath(), Main.getInstance().getServer().getPlayer(playerUUID).getClientBrandName());
        playerConfig.set(configPath + configPaths.LASTLOGIN.getPath(), formattedDate);
        playerConfig.set(configPath + configPaths.ZOMBIEKILLS.getPath(), 0);
        playerConfig.set(configPath + configPaths.DEATHS.getPath(), 0);
        playerConfig.set(configPath + configPaths.CASH.getPath(), 1500);
        playerConfig.set(configPath + configPaths.TOKENS.getPath(), 0);
        playerConfig.set(configPath + configPaths.BOUNTYHASBOUNTY.getPath(), false);
        playerConfig.set(configPath + configPaths.BOUNTYAMOUNT.getPath(), 0);
        playerConfig.set(configPath + configPaths.SETTINGS.getPath() + configPaths.SETTINGS_PLAYER_VISABLE.getPath(), true);
        playerConfig.set(configPath + configPaths.SETTINGS.getPath() + configPaths.SETTINGS_ZOMBIE_VISABLE.getPath(), true);

        saveConfig();

    }

    public String getBalance() {
        Double balance = getConfig().getDouble(configPath + configPaths.CASH.getPath());
        BigDecimal balanceBD = BigDecimal.valueOf(balance);
        return new VaultHook().format(balanceBD);
    }

    public void setBalance(BigDecimal balance) {
        new VaultHook().format(balance);
        getConfig().set(configPath + configPaths.CASH.getPath(), balance.doubleValue());
        saveConfig();
    }

    public void addBalance(BigDecimal balance) {
        new VaultHook().format(balance);
        BigDecimal currentBalance = BigDecimal.valueOf(getConfig().getDouble(configPath + configPaths.CASH.getPath()));
        getConfig().set(configPath + configPaths.CASH.getPath(), currentBalance.add(balance).doubleValue());
        saveConfig();
    }

    public void setLastLogin(String dateandtime) {
        getConfig().set(configPath + configPaths.LASTLOGIN.getPath(), dateandtime);
        saveConfig();
    }

    public void setClientBrand(String clientBrand) {
        getConfig().set(configPath + configPaths.CLIENTBRAND.getPath(), clientBrand);
        saveConfig();
    }

    public String getTokens() {
        return new VaultHook().format(BigDecimal.valueOf(getConfig().getDouble(configPath + configPaths.TOKENS.getPath())));
    }

    public void addTokens(BigDecimal tokenToAdd){
        new VaultHook().format(tokenToAdd);
        BigDecimal currentTokens = BigDecimal.valueOf(getConfig().getDouble(configPath + configPaths.TOKENS.getPath()));
        getConfig().set(configPath + configPaths.TOKENS.getPath(), currentTokens.add(tokenToAdd).doubleValue());
        saveConfig();
    }

    public void setTokens(BigDecimal tokens) {
        getConfig().set(configPath + configPaths.TOKENS.getPath(), tokens.doubleValue());
        saveConfig();
    }

    public Double getKills() {
        return getConfig().getDouble(configPath + configPaths.ZOMBIEKILLS.getPath());
    }

    public void setKills(Player p) {
        getConfig().set(configPath + configPaths.ZOMBIEKILLS.getPath(), p.getStatistic(Statistic.KILL_ENTITY, EntityType.ZOMBIE));
        saveConfig();
    }

    public Double getDeaths() {
        return getConfig().getDouble(configPath + configPaths.DEATHS.getPath());
    }

    public void setDeaths(Double deaths) {
        getConfig().set(configPath + configPaths.DEATHS.getPath(), deaths);
        saveConfig();
    }

    public Boolean hasBounty() {
        return getConfig().getBoolean(configPath + configPaths.BOUNTYHASBOUNTY.getPath());
    }

    public void setHasBounty(Boolean hasBounty, Double bountyAmount) {
        new VaultHook().format(BigDecimal.valueOf(bountyAmount));
        getConfig().set(configPath + configPaths.BOUNTYHASBOUNTY.getPath(), hasBounty);
        getConfig().set(configPath + configPaths.BOUNTYAMOUNT.getPath(), bountyAmount);
        saveConfig();
    }

    public String getBounty() {
        return new VaultHook().format(BigDecimal.valueOf(getConfig().getDouble(configPath + configPaths.CASH.getPath())));
    }
    public Boolean isPlayerVisable() {
        return getConfig().getBoolean(configPath + configPaths.SETTINGS.getPath() + configPaths.SETTINGS_PLAYER_VISABLE.getPath());
    }
    public Boolean isZombieVisable() {
        return getConfig().getBoolean(configPath + configPaths.SETTINGS.getPath() + configPaths.SETTINGS_ZOMBIE_VISABLE.getPath());
    }
    public void setPlayerVisable(Boolean visable) {
        getConfig().set(configPath + configPaths.SETTINGS.getPath() + configPaths.SETTINGS_PLAYER_VISABLE.getPath(), visable);
        saveConfig();
    }
    public void setZombieVisable(Boolean visable) {
        getConfig().set(configPath + configPaths.SETTINGS.getPath() + configPaths.SETTINGS_ZOMBIE_VISABLE.getPath(), visable);
        saveConfig();
    }


    public Set<UUID> getKnownPlayers() {
        return knownPlayers;
    }

    public void addKnownPlayer(UUID uuid) {
        knownPlayers.add(uuid);
    }
}
