package com.bulefire.furrybohe.common.mixin;

import com.bulefire.furrybohe.common.render.FursuitArmGeoRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemInHandRenderer.class)
public abstract class ItemInHandRendererMixin {

    @Inject(method = "renderHandsWithItems", at = @At("HEAD"), cancellable = true)
    private void furrybohe$renderFursuitArms(float partialTick, PoseStack poseStack,
                                             MultiBufferSource.BufferSource bufferSource, LocalPlayer player,
                                             int packedLight, CallbackInfo ci) {
        ci.cancel();

        // 使用望远镜时原版会用全屏遮罩代替手部
        if (player.isScoping()) return;

        FursuitArmGeoRenderer.renderFirstPerson(player, partialTick, poseStack, bufferSource, packedLight);
    }
}
