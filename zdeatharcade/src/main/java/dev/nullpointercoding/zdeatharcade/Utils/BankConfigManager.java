package dev.nullpointercoding.zdeatharcade.Utils;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import dev.nullpointercoding.zdeatharcade.Main;

public class BankConfigManager {

    private Main plugin = Main.getInstance();
    private File configFile;
    private FileConfiguration config;
    private String configName;
    private Boolean isConfigCreated;
    private UUID accountNumber;
    private HashMap<String, UUID> members = new HashMap<String, UUID>();

    public BankConfigManager(UUID accountNumber) {
        this.accountNumber = accountNumber;
        this.configName = accountNumber.toString() + ".yml";
        this.configFile = new File(plugin.getBankDataFolder(), configName);
    }

    public String getConfigName() {
        return configName;
    }

    public FileConfiguration getConfig() {
        ensureConfigLoaded(accountNumber, true);
        return (YamlConfiguration) config;
    }

    public File getFile() {
        return configFile;
    }
    public enum configPaths{
        PLAYER("Player");

        private String path;

        configPaths(String path){
            this.path = path;
        }

        public String getPath(){
            return path;
        }
    }

private void configHandler(UUID ownerUUID, boolean createIfMissing) {
    if (configFile == null) {
        configFile = new File(plugin.getBankDataFolder(), configName);
    }

    // Ensure parent directory exists
    File parent = configFile.getParentFile();
    if (parent != null && !parent.exists()) {
        if (!parent.mkdirs()) {
            plugin.getLogger().severe("Failed to create directories for bank config: " + configFile);
            return;
        }
    }

    boolean newlyCreated = false;

    if (!configFile.exists()) {
        if (!createIfMissing) {
            config = null;
            isConfigCreated = false;
            return;
        }
        try {
            if (configFile.createNewFile()) {
                newlyCreated = true;
            }
        } catch (IOException e) {
            plugin.getLogger().severe("Failed to create bank config file: " + configFile);
            return;
        }
    }

    config = YamlConfiguration.loadConfiguration(configFile);

    // Optional but very recommended: set defaults on first creation or when missing keys
    if (newlyCreated || !config.isSet("Bank_Account.Balance")) {
        generateDefaults((YamlConfiguration) config, ownerUUID);
        saveConfig();  // save defaults immediately
    }

    isConfigCreated = true;  // only set if we actually loaded something
}

private void ensureConfigLoaded(UUID ownerUUID, boolean createIfMissing) {
    if (config == null) {
        configHandler(ownerUUID, createIfMissing);
    }
}

    public Boolean isConfigCreated() {
        return isConfigCreated;
    }
private void generateDefaults(YamlConfiguration cfg,UUID ownerUUID) {
    cfg.set("Bank_Account.Balance",      "0.00");
    cfg.set("Bank_Account.Interest",     "0.8200");
    cfg.set("Bank_Account.Created",      System.currentTimeMillis());
    cfg.set("Bank_Account.OwnerUUID",    ownerUUID.toString());          // or set later
    cfg.set("Bank_Account.Number",       accountNumber.toString());          // if using bank name
     // if supporting multiple members
    cfg.createSection("Bank_Account.Members", members); // if supporting multiple members
    // Add comments if you want (optional)
    cfg.setComments("Bank_Account", List.of(
        "Personal bank account data",
        "Balance is stored as string to preserve precision"
    ));
}

public void saveConfig() {
    if (config == null || configFile == null) return;
    try {
        config.save(configFile);
    } catch (IOException e) {
        plugin.getLogger().severe("Could not save bank config: " + configFile);
    }
}


    public void createConfig(UUID ownerUUID) {
        configHandler(ownerUUID, true);
    }


    /**
     * Gets the current bank balance for a player.
     * Returns BigDecimal.ZERO if no bank exists or value is invalid.
     */
    public BigDecimal getBankBalance() {
        BankConfigManager manager = new BankConfigManager(accountNumber);
        if (!manager.exists()) {
            return BigDecimal.ZERO.setScale(2, RoundingMode.UNNECESSARY);
        }
        FileConfiguration config = manager.getConfig();

        String path = "Bank_Account.Balance";

        if (!config.isSet(path)) {
            return BigDecimal.ZERO.setScale(2, RoundingMode.UNNECESSARY);
        }

        String valueStr = config.getString(path);
        if (valueStr == null || valueStr.trim().isEmpty()) {
            return BigDecimal.ZERO.setScale(2, RoundingMode.UNNECESSARY);
        }

        try {
            BigDecimal balance = new BigDecimal(valueStr.trim());
            // Always enforce 2 decimal places
            return balance.setScale(2, RoundingMode.HALF_EVEN);
        } catch (NumberFormatException | ArithmeticException e) {
            // Log corruption once if you want (avoid console spam)
            // getLogger().warning("Invalid balance format for " + uuid + ": " + valueStr);
            return BigDecimal.ZERO.setScale(2, RoundingMode.UNNECESSARY);
        }
    }

    /**
     * Sets a new bank balance for the player.
     * Automatically rounds to 2 decimal places using banker's rounding.
     * Saves the file immediately.
     * 
     * @return true if save succeeded, false on error
     */
    public boolean setBankBalance(BigDecimal newBalance) {
        if (newBalance == null) {
            newBalance = BigDecimal.ZERO;
        }

        // Always normalize to 2 decimal places
        BigDecimal safeBalance = newBalance.setScale(2, RoundingMode.HALF_EVEN);

        BankConfigManager manager = new BankConfigManager(accountNumber);
        manager.createConfig(accountNumber);
        FileConfiguration config = manager.getConfig();

        String path = "Bank_Account.Balance";

        // Store as plain string → no scientific notation, exact representation
        config.set(path, safeBalance.toPlainString());

        try {
            manager.saveConfig();
            return true;
        } catch (Exception e) {
            // getLogger().severe("Failed to save bank balance for " + uuid + ": " +
            // e.getMessage());
            return false;
        }
    }

    public boolean exists() {
        return configFile != null && configFile.exists() && configFile.isFile();
    }

    public void addMemeber(UUID memberUUID) {
        members.put(memberUUID.toString(), memberUUID);
        config.set("Bank_Account.Members", members);
        saveConfig();
    }
    public void removeMember(UUID memberUUID) {
        members.remove(memberUUID.toString());
        config.set("Bank_Account.Members", members);
        saveConfig();
    }
    public boolean isMember(UUID memberUUID) {
        return members.containsKey(memberUUID.toString());
    }
    public HashMap<String, UUID> getMembers() {
        return members;

    }
    public String getAccountNumber() {
        return config.getString("Bank_Account.Number");
    }

}
