package com.bulefire.furrybohe.common.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.InteractionHand;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoObjectRenderer;

/**
 * 第一人称手臂渲染器。
 * <p>
 * 手臂模型使用与第三人称身体相同的坐标系（脚底为原点），渲染时把模型根骨骼
 * {@code Arm} 的 pivot 对齐到相机，并应用与身体一致的 0.7 缩放。
 */
public class FursuitArmGeoRenderer extends GeoObjectRenderer<FursuitArmGeoAnimatable> {
    /** 对应 ysm.json 中的 height_scale / width_scale */
    private static final float MODEL_SCALE = 0.9F;
    /** 根骨骼 Arm 的 pivot（单位：模型像素），用于把手臂放到相机位置 */
    private static final float ROOT_X = 0.09846F / 16F;
    private static final float ROOT_Y = 33.09755F / 16F;
    private static final float ROOT_Z = -3.31044F / 16F;

    private static FursuitArmGeoRenderer instance;

    private FursuitArmGeoRenderer() {
        super(new FursuitArmGeoModel());
        addRenderLayer(new FursuitArmItemLayer(this, InteractionHand.MAIN_HAND, "RightHandLocator"));
        addRenderLayer(new FursuitArmItemLayer(this, InteractionHand.OFF_HAND, "LeftHandLocator"));
    }

    public static void renderFirstPerson(@NotNull LocalPlayer player, float partialTick, PoseStack poseStack,
                                         MultiBufferSource.BufferSource bufferSource, int packedLight) {
        if (instance == null) {
            instance = new FursuitArmGeoRenderer();
        }

        instance.renderArms(player, partialTick, poseStack, bufferSource, packedLight);
    }

    private void renderArms(@NotNull LocalPlayer player, float partialTick, PoseStack poseStack,
                            MultiBufferSource.BufferSource bufferSource, int packedLight) {
        FursuitArmGeoAnimatable animatable = FursuitArmGeoAnimatable.getInstance();
        animatable.setPlayer(player);
        this.animatable = animatable;

        try {
            defaultRender(poseStack, animatable, bufferSource, null, null, 0, partialTick, packedLight);
        }
        finally {
            // 原版 renderHandsWithItems 末尾会刷新缓冲区，这里取消后需要自己刷新
            bufferSource.endBatch();
            animatable.setPlayer(null);
            this.animatable = null;
        }
    }

    private static void hideCubesOnly(software.bernie.geckolib.cache.object.GeoBone bone) {
        bone.setHidden(true);
        bone.setChildrenHidden(false);
    }

    @Override
    public void preRender(@NotNull PoseStack poseStack, @NotNull FursuitArmGeoAnimatable animatable,
                          @NotNull BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer,
                          boolean isReRender, float partialTick, int packedLight, int packedOverlay,
                          float red, float green, float blue, float alpha) {
        this.objectRenderTranslations = new Matrix4f(poseStack.last().pose());
        // Arm 根骨骼的 pivot 对齐到相机原点（无额外偏移）
        poseStack.translate(ROOT_X * MODEL_SCALE,
                ROOT_Z * MODEL_SCALE,
                -ROOT_Y * MODEL_SCALE);
        poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
        poseStack.scale(MODEL_SCALE, MODEL_SCALE, MODEL_SCALE);

        // 第一人称默认只显示主手；副手只有在被使用时才显示。
        // 上臂离相机太近会占满画面，所以只隐藏上臂自身的方块，保留前臂和手。
        LocalPlayer player = animatable.getPlayer();
        boolean showOffhand = player != null && player.isUsingItem()
                && player.getUsedItemHand() == InteractionHand.OFF_HAND;
        model.getBone("RightArm").ifPresent(FursuitArmGeoRenderer::hideCubesOnly);
        model.getBone("LeftArm").ifPresent(bone -> {
            bone.setHidden(!showOffhand);
            if (showOffhand) hideCubesOnly(bone);
        });
    }
}
