package dev.obscuria.healight;

import net.minecraft.world.entity.LivingEntity;

public interface LivingExtension {

    int healight$getHealTime();

    void healight$setHealTime(int healTime);

    float healight$getLastHealth();

    void healight$setLastHealth(float lastHealth);

    static int healTimeOf(LivingEntity entity) {
        return ((LivingExtension) entity).healight$getHealTime();
    }

    static void setHealTime(LivingEntity entity, int healTime) {
        ((LivingExtension) entity).healight$setHealTime(healTime);
    }

    static float lastHealthOf(LivingEntity entity) {
        return ((LivingExtension) entity).healight$getLastHealth();
    }

    static void setLastHealth(LivingEntity entity, float lastHealth) {
        ((LivingExtension) entity).healight$setLastHealth(lastHealth);
    }
}
