package dev.obscuria.healight;

import net.minecraft.world.entity.LivingEntity;

public final class HealightHooks {

    public static void livingTick(LivingEntity entity) {

        final var healTime = LivingExtension.healTimeOf(entity);
        final var lastHealth = LivingExtension.lastHealthOf(entity);
        final var health = entity.getHealth();

        if (healTime > 0) {
            LivingExtension.setHealTime(entity, healTime - 1);
        }

        if (entity.tickCount > 20 && health > lastHealth + 0.01f) {
            LivingExtension.setHealTime(entity, 8);
        }

        LivingExtension.setLastHealth(entity, health);
    }
}
