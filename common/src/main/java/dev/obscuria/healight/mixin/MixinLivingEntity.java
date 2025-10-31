package dev.obscuria.healight.mixin;

import dev.obscuria.healight.HealightHooks;
import dev.obscuria.healight.LivingExtension;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity implements LivingExtension {

    @Unique private float healight$lastHealth;
    @Unique private int healight$healTime;

    @Override
    public int healight$getHealTime() {
        return this.healight$healTime;
    }

    @Override
    public void healight$setHealTime(int healTime) {
        this.healight$healTime = healTime;
    }

    @Override
    public float healight$getLastHealth() {
        return this.healight$lastHealth;
    }

    @Override
    public void healight$setLastHealth(float lastHealth) {
        this.healight$lastHealth = lastHealth;
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void customTick(CallbackInfo info) {
        final var self = (LivingEntity) (Object) this;
        HealightHooks.livingTick(self);
    }
}
