package dev.nullpointercoding.zdeatharcade.Zombies;

import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Zombie;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;

public class ZombieLevel2 {

    private final Component level = Component.text("LVL 2", TextColor.color(255, 255, 255))
            .decorate(TextDecoration.BOLD);
    private final Component name = Component.text("Zombie").color(TextColor.color(10, 214, 68)).appendSpace()
            .append(level);

    public final LivingEntity convertToLevel1Zombie(final Zombie z) {
        Zombie z2 = (Zombie) z;
        z2.customName(name);
        z2.setAge(1);
        z2.setSilent(true);
        z2.setCanPickupItems(false);
        z2.getEquipment().clear();
        z2.getAttribute(Attribute.MAX_HEALTH).setBaseValue(12.0);
        z2.getAttribute(Attribute.MOVEMENT_SPEED).setBaseValue(0.22);
        z2.getAttribute(Attribute.ATTACK_DAMAGE).setBaseValue((double) 0.8);
        z2.setCustomNameVisible(true);
        z2.setShouldBurnInDay(false);
        return z;
    }

    public Component name() {
        return name;
    }
}
