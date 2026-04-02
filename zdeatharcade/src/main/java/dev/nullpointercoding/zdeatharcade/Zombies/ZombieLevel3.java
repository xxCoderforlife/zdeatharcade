package dev.nullpointercoding.zdeatharcade.Zombies;

import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Zombie;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;

public class ZombieLevel3 {
    private final Component level = Component.text("LVL 3", TextColor.color(255, 255, 255))
            .decorate(TextDecoration.BOLD);
    private final Component name = Component.text("Zombie").color(TextColor.color(10, 214, 68)).appendSpace()
            .append(level);

    public final LivingEntity convertToLevel3Zombie(final Zombie z) {
        Zombie z3 = (Zombie) z;
        z3.customName(name);
        z3.setAge(1);
        z3.getEquipment().clear();
        z3.setSilent(true);
        z3.setCanPickupItems(false);
        z3.getAttribute(Attribute.MAX_HEALTH).setBaseValue(18.0);
        z3.getAttribute(Attribute.MOVEMENT_SPEED).setBaseValue(0.28);
        z3.getAttribute(Attribute.ATTACK_DAMAGE).setBaseValue((double) 1.2);
        z3.setCustomNameVisible(true);
        z3.setShouldBurnInDay(false);
        return z;
    }

    public Component name() {
        return name;
    }
}
