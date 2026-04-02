package dev.nullpointercoding.zdeatharcade.Zombies.PrizeLlama;

import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Llama;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;

public class PrizeLlama {
    private final TextComponent name = Component.text("Prize Llama");

    public final LivingEntity convertToPrizeLlama(final Llama e) {
        Llama l1 = (Llama) e;
        l1.customName(name);
        l1.getAttribute(Attribute.MOVEMENT_SPEED).setBaseValue(1.2);
        l1.setCustomNameVisible(true);
        return e;
    }

    public TextComponent name() {
        return name;
    }

    public Llama spawnPrizeLlama(Location loc) {
        Llama la = (Llama) loc.getWorld().spawnEntity(loc, EntityType.LLAMA, SpawnReason.COMMAND);
        convertToPrizeLlama(la);
        return la;
    }
}
