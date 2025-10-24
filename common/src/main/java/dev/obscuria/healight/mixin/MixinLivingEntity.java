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
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
@SuppressWarnings("WrongEntityDataParameterClass")
public abstract class MixinLivingEntity extends Entity implements LivingExtension {

    @Unique private static final EntityDataAccessor<Integer> DATA_HEAL_TIME;

    private MixinLivingEntity(EntityType<?> type, Level level) {
        super(type, level);
    }

    @Override
    public int healight$getHealTime() {
        return this.entityData.get(DATA_HEAL_TIME);
    }

    @Inject(method = "defineSynchedData", at = @At("RETURN"))
    private void defineCustomSynchedData(CallbackInfo info) {
        this.entityData.define(DATA_HEAL_TIME, 0);
    }

    @Inject(method = "baseTick", at = @At("HEAD"))
    private void tickHealTime(CallbackInfo info) {
        if (this.entityData.get(DATA_HEAL_TIME) <= 0) return;
        this.entityData.set(DATA_HEAL_TIME, this.entityData.get(DATA_HEAL_TIME) - 1);
    }

    @Inject(method = "setHealth", at = @At("HEAD"))
    private void onSetHealth(float health, CallbackInfo info) {
        final var self = (LivingEntity) (Object) this;
        if (this.level().isClientSide()) return;
        if (this.tickCount < 20) return;
        final var result = Mth.clamp(health, 0, self.getMaxHealth());
        if (result - 0.01f < self.getHealth()) return;
        this.entityData.set(DATA_HEAL_TIME, 6);
    }

    static {
        DATA_HEAL_TIME = SynchedEntityData.defineId(LivingEntity.class, EntityDataSerializers.INT);
    }
}
