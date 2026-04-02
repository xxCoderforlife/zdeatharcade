package dev.nullpointercoding.zdeatharcade.Utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import dev.nullpointercoding.zdeatharcade.Main;
import dev.nullpointercoding.zdeatharcade.Zombies.ZombieHandler;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public class AutoBroadcast {

    private Main plugin = Main.getInstance();
    private BukkitTask newMessage;
    private File configFile;
    private YamlConfiguration config;
    private Integer currentID = 0;
    private final String configName = "auto-messages";
    private Integer zombieUpdate = 0;
    private Component newHeader;
    // private ArrayList<String> lastMessage = new ArrayList<String>();

    public void startBroadcast() {
        checkforMessages();
        newHeader = LegacyComponentSerializer.legacyAmpersand()
                .deserialize(config.getString("Auto-Messages.Header"))
                .hoverEvent(Component.text("The Number One Auto-Message System in the World!", NamedTextColor.GREEN)
                        .append(Component.newline())
                        .append(Component.text("Provided by: The Dead Broadcasting System", NamedTextColor.GOLD))
                        .append(Component.newline()).append(Component.text("Version: 0.0.1", NamedTextColor.RED)));
        Bukkit.getConsoleSender().sendMessage(Component.text("Starting Auto-Messages...", NamedTextColor.GREEN));
        newMessage = new BukkitRunnable() {
            @Override
            public void run() {
                if (zombieUpdate == 3) {
                    sendZombieUpdate();
                    return;
                }
                List<String> messages = config.getStringList("Auto-Messages.Messages");
                if (messages.size() == 0) {
                    Bukkit.getConsoleSender()
                            .sendMessage(Component.text("No Messages Found!", NamedTextColor.DARK_RED));
                    return;
                }
                if (messages.size() == currentID) {
                    currentID = 0;
                }
                String mainMessage = messages.get(currentID).substring(0, messages.get(currentID).indexOf(","));
                String hoverMessage = messages.get(currentID).substring(messages.get(currentID).indexOf(",") + 1);
                sendMessage(mainMessage, hoverMessage);
                zombieUpdate++;
                currentID++;
            }

        }.runTaskTimer(plugin, (long) 20, (long) config.getLong("Auto-Messages.Timer") * 20);

    }

    public void stopBroadcast() {
        Bukkit.getConsoleSender()
                .sendMessage(Component.text("Shutting Down Auto-Messages...", NamedTextColor.DARK_RED));
        newMessage.cancel();
    }

    public BukkitTask getMessageSch() {
        return newMessage;
    }

    private void checkforMessages() {
        configFile = new File(plugin.getMainDataFolder() + File.separator + configName + ".yml");
        if (!configFile.exists()) {
            configFile.getParentFile().mkdirs();
            try {
                configFile.createNewFile();
                Reader r = new InputStreamReader(this.getClass().getResourceAsStream("/" + configName + ".yml"));
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

    private void sendMessage(String message, String hoverMessage) {
        Component newHoverMessage = LegacyComponentSerializer.legacyAmpersand().deserialize(hoverMessage);
        Component newMessage = LegacyComponentSerializer.legacyAmpersand().deserialize(message)
                .hoverEvent(newHoverMessage);
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.sendMessage(Component.newline());
            p.sendMessage(Component.text("           ").append(newHeader));
            p.sendMessage(Component.newline());
            p.sendMessage(Component.text("   ").append(newMessage));
            p.sendMessage(Component.newline());

        }

    }

    private void sendZombieUpdate() {
        zombieUpdate = 0;
        Component zombiesAlive = Component.text(ZombieHandler.getSpawnCount().toString(), NamedTextColor.GOLD,
                TextDecoration.ITALIC);
        Component newHeader = Component.text("           [", NamedTextColor.DARK_RED).append(Component
                .text("Zombie Update", NamedTextColor.RED, TextDecoration.ITALIC)
                .append(Component.text("]", NamedTextColor.DARK_RED))
                .hoverEvent(Component.text("The Number One Auto-Message System in the World!", NamedTextColor.GREEN)
                        .append(Component.newline())
                        .append(Component.text("Provided by: The Dead Broadcasting System", NamedTextColor.GOLD))
                        .append(Component.newline()).append(Component.text("Version: 0.0.1", NamedTextColor.RED))));
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.sendMessage(Component.newline());
            p.sendMessage(Component.text("           ").append(newHeader));
            p.sendMessage(Component.newline());
            p.sendMessage(Component.text("   " + "There are currently ", NamedTextColor.RED, TextDecoration.ITALIC)
                    .append(zombiesAlive)
                    .append(Component.text(" zombies alive!", NamedTextColor.RED, TextDecoration.ITALIC)
                            .hoverEvent(Component.text("Go out there and get some kills!",NamedTextColor.DARK_RED,TextDecoration.ITALIC))));
            p.sendMessage(Component.newline());

        }
    }

}
