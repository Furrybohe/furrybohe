package com.bulefire.furrybohe.common.mixin;

import com.bulefire.furrybohe.common.render.FursuitGeoRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin<T extends LivingEntity, M extends EntityModel<T>> {

    @Inject(method = "render(Lnet/minecraft/world/entity/LivingEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
            at = @At("HEAD"), cancellable = true)
    private void furrybohe$renderPlayerGeoModel(T entity, float entityYaw, float partialTick, PoseStack poseStack,
                                                MultiBufferSource bufferSource, int packedLight, CallbackInfo ci) {
        if (!(entity instanceof Player player)) return;
//        if (!((Object) this instanceof PlayerRenderer)) return;

        ci.cancel();
        FursuitGeoRenderer.renderPlayer(player, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }
}
