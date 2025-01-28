package dev.obscuria.healight.mixin;

import dev.obscuria.healight.LivingExtension;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
@SuppressWarnings("WrongEntityDataParameterClass")
public abstract class MixinLivingEntity extends Entity implements LivingExtension
{
    @Unique
    private static final EntityDataAccessor<Integer> DATA_HEAL_TIME;

    private MixinLivingEntity(EntityType<?> type, Level level)
    {
        super(type, level);
    }

    @Inject(method = "defineSynchedData", at = @At("RETURN"))
    private void defineSynchedData_modify(SynchedEntityData.Builder builder, CallbackInfo info)
    {
        builder.define(DATA_HEAL_TIME, 0);
    }

    @Inject(method = "baseTick", at = @At("RETURN"))
    private void baseTick_listener(CallbackInfo info)
    {
        if (this.entityData.get(DATA_HEAL_TIME) <= 0) return;
        this.entityData.set(DATA_HEAL_TIME, this.entityData.get(DATA_HEAL_TIME) - 1);
    }

    @Inject(method = "setHealth", at = @At("HEAD"))
    private void setHealth_listener(float health, CallbackInfo info)
    {
        if (this.level().isClientSide()) return;
        if (this.tickCount < 10) return;
        final var result = Mth.clamp(health, 0, this.getMaxHealth());
        if (result - 0.01f < this.getHealth()) return;
        this.entityData.set(DATA_HEAL_TIME, 6);
    }

    @Override
    public int healight_getHealTime()
    {
        return this.entityData.get(DATA_HEAL_TIME);
    }

    @Shadow
    public abstract float getHealth();

    @Shadow
    public abstract float getMaxHealth();

    static
    {
        DATA_HEAL_TIME = SynchedEntityData.defineId(LivingEntity.class, EntityDataSerializers.INT);
    }
}
