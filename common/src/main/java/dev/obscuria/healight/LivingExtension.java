package dev.obscuria.healight;

import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.ApiStatus;

public interface LivingExtension
{
    static int getHealTime(LivingEntity entity)
    {
        return ((LivingExtension) entity).healight_getHealTime();
    }

    @ApiStatus.Internal
    int healight_getHealTime();
}
