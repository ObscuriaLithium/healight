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
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

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
            color = LivingExtension.getHealTime(entity) > 0 ? 0xFF00FF00 : color;
        }
        original.call(instance, pose, consumer, light, overlay, color);
    }

    @Inject(method = "getOverlayCoords", at = @At("RETURN"), cancellable = true)
    private static void getOverlayCoords_modify(LivingEntity entity, float u, CallbackInfoReturnable<Integer> info)
    {
        if (LivingExtension.getHealTime(entity) <= 0) return;
        info.setReturnValue(OverlayTexture.pack(OverlayTexture.u(u), OverlayTexture.v(true)));
    }
}
