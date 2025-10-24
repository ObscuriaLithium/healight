package dev.obscuria.healight;

import net.minecraft.world.entity.LivingEntity;

public interface LivingExtension {

    int healight$getHealTime();

    static int healTimeOf(LivingEntity entity) {
        return ((LivingExtension) entity).healight$getHealTime();
    }
}
