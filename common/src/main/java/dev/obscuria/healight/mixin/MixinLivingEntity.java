package dev.obscuria.healight.mixin;

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

    @Inject(method = "tick", at = @At("TAIL"))
    private void customTick(CallbackInfo info) {

        final var self = (LivingEntity) (Object) this;
        if (healight$healTime > 0) {
            this.healight$healTime -= 1;
        }

        final var health = self.getHealth();
        if (self.tickCount > 20 && health > healight$lastHealth + 0.01f) {
            this.healight$healTime = 8;
        }
        this.healight$lastHealth = health;
    }
}
