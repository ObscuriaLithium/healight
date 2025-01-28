package dev.obscuria.healight.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.obscuria.healight.LivingExtension;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LivingEntityRenderer.class)
public abstract class MixinLivingEntityRenderer
{
    @WrapOperation(method = "render(Lnet/minecraft/world/entity/LivingEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/EntityModel;renderToBuffer(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;III)V"))
    private void render_modify(EntityModel<?> instance, PoseStack pose, VertexConsumer consumer,
                               int light, int overlay, int color, Operation<Void> original,
                               @Local(argsOnly = true) LivingEntity entity)
    {
        if (LivingExtension.getHealTime(entity) > 0)
        {
            final var blockLight = Math.min(15, LightTexture.block(light) + 3);
            final var skyLight = Math.min(15, LightTexture.sky(light) + 3);
            light = LightTexture.pack(blockLight, skyLight);
            color = 0xFF3FFF3F;
        }
        original.call(instance, pose, consumer, light, overlay, color);
    }
}
