package dev.nullpointercoding.zdeatharcade.Zombies;

import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Zombie;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;

public class ZombieLevel1 {

    private final Component level = Component.text("LVL 1", TextColor.color(255, 255, 255))
            .decorate(TextDecoration.BOLD);
    private final Component name = Component.text("Zombie").color(TextColor.color(10, 214, 68)).appendSpace()
            .append(level);

    public final LivingEntity convertToLevel1Zombie(final Zombie z) {
        Zombie z1 = (Zombie) z;
        z1.customName(name);
        z1.setAge(1);
        z1.setSilent(true);
        z1.setCanPickupItems(false);
        z1.getEquipment().clear();
        z1.getAttribute(Attribute.MAX_HEALTH).setBaseValue(4.0);
        z1.getAttribute(Attribute.MOVEMENT_SPEED).setBaseValue(0.18);
        z1.getAttribute(Attribute.ATTACK_DAMAGE).setBaseValue((double) 0.5);
        z1.setCustomNameVisible(true);
        z1.setShouldBurnInDay(false);
        return z;
    }

    public Component name() {
        return name;
    }
}
