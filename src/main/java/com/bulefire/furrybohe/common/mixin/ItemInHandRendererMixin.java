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
        // 望远镜：原版本来就不渲染手部，直接让它走完（包括结尾的 buffer 刷新）。
        if (player.isScoping()) {
            return;
        }

        // 只要有任何一只手拿着东西，就整帧交给原版渲染：物品的位置 / 大小 / 使用动画
        // 都和原版完全一致，而且原版在显示手持物品时本来就不显示手臂。
        if (!player.getMainHandItem().isEmpty() || !player.getOffhandItem().isEmpty()) {
            return;
        }

        // 双手都空：用 geckolib 手臂替换原版手臂。
        ci.cancel();
        FursuitArmGeoRenderer.renderFirstPerson(player, partialTick, poseStack, bufferSource, packedLight);
    }
}
