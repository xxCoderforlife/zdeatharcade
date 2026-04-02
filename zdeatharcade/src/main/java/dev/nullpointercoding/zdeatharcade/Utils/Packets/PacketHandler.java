package dev.nullpointercoding.zdeatharcade.Utils.Packets;

import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.PlayerInfoData;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedGameProfile;
import dev.nullpointercoding.zdeatharcade.Main;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.md_5.bungee.api.ChatColor;

public class PacketHandler {

    private ProtocolManager protocolManager = Main.getInstance().getProtocolManager();
    private Main plugin = Main.getInstance();
    private HashMap<Player,UUID> zombiePacket = new HashMap<Player,UUID>();
    private HashMap<Player,UUID> playerPacket = new HashMap<Player,UUID>();

    public void stopShowingPlayer(Player p) {
        protocolManager.addPacketListener(new PacketAdapter(Main.getInstance(), ListenerPriority.NORMAL,
            PacketType.Play.Server.SPAWN_ENTITY, PacketType.Play.Server.ENTITY_METADATA,
            PacketType.Play.Server.REL_ENTITY_MOVE) {
            @Override
            public void onPacketSending(PacketEvent event) {
                Player player = event.getPlayer();
                if (player == p) {
                    event.setCancelled(true);
                }
            }
        });
    }

    public void hidePlayers(Player hideFromPlayer) {
        for (Player targetPlayer : Bukkit.getOnlinePlayers()) {
            if (targetPlayer != hideFromPlayer) {
                hideFromPlayer.hidePlayer(Main.getInstance(), targetPlayer); // Hide the player using the Bukkit API
                sendPlayerInfoPacket(hideFromPlayer, targetPlayer, false);
            }
        }
    }

    public void showPlayers(Player showToPlayer) {
        for (Player targetPlayer : Bukkit.getOnlinePlayers()) {
            if (targetPlayer != showToPlayer) {
                showToPlayer.showPlayer(Main.getInstance(), targetPlayer); // Show the player using the Bukkit API
                sendPlayerInfoPacket(showToPlayer, targetPlayer, true);

            }
        }
    }

    private void sendPlayerInfoPacket(Player viewer, Player targetPlayer, Boolean isVisable) {
        if (isVisable) {
            PacketContainer packet = protocolManager.createPacket(PacketType.Play.Server.PLAYER_INFO);
            String cleanName = PlainTextComponentSerializer.plainText().serialize(targetPlayer.displayName());
            String newName = ChatColor.stripColor(cleanName);
            packet.getPlayerInfoActions().write(0, EnumSet.of(EnumWrappers.PlayerInfoAction.ADD_PLAYER));
            packet.getPlayerInfoDataLists().write(1, Collections.singletonList(new PlayerInfoData(
                    WrappedGameProfile.fromPlayer(targetPlayer), 0,
                    EnumWrappers.NativeGameMode.fromBukkit(targetPlayer.getGameMode()),
                    WrappedChatComponent.fromText(newName))));
            try {
                ProtocolLibrary.getProtocolManager().sendServerPacket(viewer, packet);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            PacketContainer packet = protocolManager.createPacket(PacketType.Play.Server.PLAYER_INFO_REMOVE);
            packet.getUUIDLists().write(0, Collections.singletonList(targetPlayer.getUniqueId()));
            try {
                ProtocolLibrary.getProtocolManager().sendServerPacket(viewer, packet);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void registerZombiePacket() {
        protocolManager.addPacketListener(
            new PacketAdapter(plugin, ListenerPriority.HIGHEST, PacketType.Play.Server.SPAWN_ENTITY) {
                    @Override
                    public void onPacketSending(PacketEvent event) {
                        EntityType entityType = event.getPacket().getEntityModifier(event).read(0).getType();
                        if (entityType == EntityType.ZOMBIE) {
                            if(zombiePacket.containsKey(event.getPlayer())){
                                event.setCancelled(true);
                            }
                        }
                    }
                });
        PacketContainer removeZombie = protocolManager.createPacket(PacketType.Play.Server.ENTITY_DESTROY);

            }

    public void stopShowingZombie(Player player){
        zombiePacket.put(player, player.getUniqueId());
    }
    public void showZombie(Player player){
        zombiePacket.remove(player);
    }

    public void registerAllPackets(){
        registerZombiePacket();
    }
}
