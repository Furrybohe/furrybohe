package com.bulefire.furrybohe.common.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

/**
 * 把手持物品渲染到手部定位骨骼（{@code RightHandLocator} / {@code LeftHandLocator}）上。
 * <p>
 * 物品使用 {@link ItemDisplayContext#NONE}，位置、朝向与缩放完全由定位骨骼的动画控制。
 */
public class FursuitArmItemLayer extends GeoRenderLayer<FursuitArmGeoAnimatable> {
    private final InteractionHand hand;
    private final String locator;

    public FursuitArmItemLayer(@NotNull FursuitArmGeoRenderer renderer, InteractionHand hand, String locator) {
        super(renderer);
        this.hand = hand;
        this.locator = locator;
    }

    @Override
    public void renderForBone(@NotNull PoseStack poseStack, @NotNull FursuitArmGeoAnimatable animatable, GeoBone bone,
                              RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer,
                              float partialTick, int packedLight, int packedOverlay) {
        if (!this.locator.equals(bone.getName())) {
            return;
        }

        LocalPlayer player = animatable.getPlayer();

        if (player == null) {
            return;
        }

        ItemStack stack = player.getItemInHand(this.hand);

        if (stack.isEmpty()) {
            return;
        }

        poseStack.pushPose();
        Minecraft.getInstance().getItemRenderer().renderStatic(stack, ItemDisplayContext.NONE, packedLight,
                OverlayTexture.NO_OVERLAY, poseStack, bufferSource, player.level(), player.getId());
        poseStack.popPose();
    }
}
