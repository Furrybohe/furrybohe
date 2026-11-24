package com.bulefire.furrybohe.common.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoObjectRenderer;

import java.util.List;
import java.util.Optional;

/**
 * 第一人称手臂渲染器。
 * <p>
 * 只在双手都空时接管（见 {@link com.bulefire.furrybohe.common.mixin.ItemInHandRendererMixin}），
 * 只画主手。整条手臂的位置、朝向、挥拳动画全部套用原版
 * {@code ItemInHandRenderer#renderPlayerArm} 的参数，所以观感和原版空手手臂一致。
 * <p>
 * 注意 GeckoLib 载入 geo 时会把 X 轴镜像（见 {@code BakedModelFactory}：位置 X 取负、X/Y 旋转取负），
 * 所以模型空间里角色正面是 -Z、右手在 +X。
 */
public class FursuitArmGeoRenderer extends GeoObjectRenderer<FursuitArmGeoAnimatable> {
    /**
     * 第一人称手臂缩放。原版手臂从肩部到指尖约 0.625 格，本模型是 13.9 像素，
     * 0.7 正好对得上（也是 ysm.json 里的 height_scale / width_scale）。
     */
    private static final float FIRST_PERSON_SCALE = 0.7F;
    /** 右臂骨骼的 pivot（已按 GeckoLib 的规则对 X 取负；模型像素 → 格），用来把肩部对到原版手臂坐标系原点 */
    private static final float SHOULDER_X = 2.89341F / 16F;
    private static final float SHOULDER_Y = 27.40372F / 16F;
    private static final float SHOULDER_Z = -1.1564F / 16F;

    private static FursuitArmGeoRenderer instance;

    /** 这一趟渲染的是左手还是右手（一次只画一只手） */
    private boolean renderingLeftArm;

    private FursuitArmGeoRenderer() {
        super(new FursuitArmGeoModel());
    }

    /**
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
            // 和 YSM 的第一人称一样只画主手（默认右手），副手不渲染。
            this.renderingLeftArm = player.getMainArm() == HumanoidArm.LEFT;
            defaultRender(poseStack, animatable, bufferSource, null, null, 0, partialTick, packedLight);
        }
        finally {
            // 原版 renderHandsWithItems 末尾会刷新缓冲区，这里取消后需要自己刷新
            bufferSource.endBatch();
            animatable.setPlayer(null);
            this.animatable = null;
        }
    }

    @Override
    public void preRender(@NotNull PoseStack poseStack, @NotNull FursuitArmGeoAnimatable animatable,
                          @NotNull BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer,
                          boolean isReRender, float partialTick, int packedLight, int packedOverlay,
                          float red, float green, float blue, float alpha) {
        this.objectRenderTranslations = new Matrix4f(poseStack.last().pose());

        boolean leftArm = this.renderingLeftArm;
        float side = leftArm ? -1.0F : 1.0F;

        // 原版只让正在挥的那只手摆动，副手攻击时主手不动。
        LocalPlayer player = animatable.getPlayer();
        float swing = 0.0F;

        if (player != null) {
            InteractionHand swingingHand = player.swingingArm;

            if (swingingHand == null || swingingHand == InteractionHand.MAIN_HAND) {
                swing = player.getAttackAnim(partialTick);
            }
        }

        // 以下整段照搬原版 ItemInHandRenderer#renderPlayerArm（含空手挥拳的位移与旋转）。
        float f1 = Mth.sqrt(swing);
        float f2 = -0.3F * Mth.sin(f1 * Mth.PI);
        float f3 = 0.4F * Mth.sin(f1 * (Mth.PI * 2.0F));
        float f4 = -0.4F * Mth.sin(swing * Mth.PI);
        poseStack.translate(side * (f2 + 0.64F), f3 - 0.6F, f4 - 0.72F);
        poseStack.mulPose(Axis.YP.rotationDegrees(side * 45.0F));

        float f5 = Mth.sin(swing * swing * Mth.PI);
        float f6 = Mth.sin(f1 * Mth.PI);
        poseStack.mulPose(Axis.YP.rotationDegrees(side * f6 * 70.0F));
        poseStack.mulPose(Axis.ZP.rotationDegrees(side * f5 * -20.0F));

        poseStack.translate(side * -1.0F, 3.6F, 3.5F);
        poseStack.mulPose(Axis.ZP.rotationDegrees(side * 120.0F));
        poseStack.mulPose(Axis.XP.rotationDegrees(200.0F));
        poseStack.mulPose(Axis.YP.rotationDegrees(side * -135.0F));
        poseStack.translate(side * 5.6F, 0.0F, 0.0F);

        // 到这里就是原版玩家手臂件的肩部坐标系：+Y 是沿手臂指向手掌的方向。
        // 本模型手臂沿 -Y 伸展、掌心朝 -X，绕 Z 转 180° 就能对上（X/Y 取反、Z 不变）。
        poseStack.mulPose(Axis.ZP.rotationDegrees(180.0F));
        poseStack.scale(FIRST_PERSON_SCALE, FIRST_PERSON_SCALE, FIRST_PERSON_SCALE);
        poseStack.translate(leftArm ? SHOULDER_X : -SHOULDER_X, -SHOULDER_Y, -SHOULDER_Z);

        // 整条手臂都画（原版也是整只手臂）：不会挡物品，也不存在被截断的根部。
        setArmVisible(model.getBone("RightArm").orElse(null), !leftArm);
        setArmVisible(model.getBone("LeftArm").orElse(null), leftArm);
    }

    **/

    /** 传 false 时整条手臂（含前臂、手）都不画。 */
    private static void setArmVisible(@Nullable GeoBone bone, boolean visible) {
        if (bone != null) {
            bone.setHidden(!visible);
        }
    }

    public static Vec3 getPartPosition(ModelPart part) {
        var t = part.storePose();
        return new Vec3(-t.x, t.y, t.z);
    }

    public static Vec3 getPartRotation(ModelPart part) {
        var t = part.storePose();
        return new Vec3(-t.xRot, -t.yRot, t.zRot);
    }

    private static void renderGeoBone(FursuitArmGeoRenderer formRenderer, GeoBone geoBone, PoseStack matrixStack, FursuitArmGeoAnimatable formAnimatable, MultiBufferSource vertexConsumerProvider, RenderType renderLayer, VertexConsumer vertexConsumer, int packedLight, float R, float G, float B, float A) {
        FursuitArmGeoModel formModel = (FursuitArmGeoModel) formRenderer.getGeoModel();
        BakedGeoModel bakedGeoModel = formModel.getBakedModel(formModel.getModelResource(formAnimatable));
        float TickDelta = Minecraft.getInstance().getFrameTime();
        int packedOverlay = formRenderer.getPackedOverlay(formAnimatable, 0.0F, Minecraft.getInstance().getFrameTime());
        matrixStack.translate(-0.5, -0.51, -0.5); // 在 GeoObjectRenderer.preRender 中会 poseStack.translate(0.5, 0.51, 0.5) 因此需要手动调整
        formRenderer.preRender(matrixStack, formAnimatable, bakedGeoModel, vertexConsumerProvider, vertexConsumerProvider.getBuffer(renderLayer), false, TickDelta, packedLight, packedOverlay, R, G, B, A);
        if (formRenderer.firePreRenderEvent(matrixStack, bakedGeoModel, vertexConsumerProvider, TickDelta, packedLight)) {
            formRenderer.preApplyRenderLayers(matrixStack, formAnimatable, bakedGeoModel, renderLayer, vertexConsumerProvider, vertexConsumer, (float)packedLight, packedLight, packedOverlay);
            matrixStack.pushPose();
            formRenderer.updateAnimatedTextureFrame(formAnimatable);
            formRenderer.renderRecursively(matrixStack, formAnimatable, geoBone, renderLayer, vertexConsumerProvider, vertexConsumer, false, TickDelta, packedLight, packedOverlay, R, G, B, A);
            matrixStack.popPose();
            formRenderer.applyRenderLayers(matrixStack, formAnimatable, bakedGeoModel, renderLayer, vertexConsumerProvider, vertexConsumer, TickDelta, packedLight, packedOverlay);
            formRenderer.postRender(matrixStack, formAnimatable, bakedGeoModel, vertexConsumerProvider, vertexConsumer, false, TickDelta, packedLight, packedOverlay, R, G, B, A);
            formRenderer.firePostRenderEvent(matrixStack, bakedGeoModel, vertexConsumerProvider, TickDelta, packedLight);
        }
    }

    public static void rFPM_PartB(PlayerRenderer playerEntityRenderer, PoseStack matrices, MultiBufferSource vertexConsumers, int light, AbstractClientPlayer player, ModelPart arm, ModelPart sleeve) {
        boolean IsRenderRight = arm.equals(playerEntityRenderer.getModel().rightArm);
        if (instance == null) {
            instance = new FursuitArmGeoRenderer();
        }
        if (!(player instanceof LocalPlayer localPlayer)) return;
        @Nullable GeoBone geoBone = null;
        PlayerModel<AbstractClientPlayer> playerEntityModel = playerEntityRenderer.getModel();
        FursuitArmGeoAnimatable formAnimatable = FursuitArmGeoAnimatable.getInstance();
        FursuitArmGeoModel formModel = (FursuitArmGeoModel) FursuitArmGeoRenderer.instance.getGeoModel();
        formAnimatable.setPlayer(localPlayer);
        matrices.pushPose();
        matrices.mulPose(new Quaternionf().rotateY(180 * Mth.DEG_TO_RAD));
        matrices.mulPose(new Quaternionf().rotateX(180 * Mth.DEG_TO_RAD));
        matrices.translate(0, -1.51f, 0);

        BakedGeoModel bakedGeoModel = formModel.getBakedModel(formModel.getModelResource(formAnimatable));
        String GeoBoneName = IsRenderRight ? "RightArm" : "LeftArm";
        Optional<GeoBone> OptionalGeoBone = bakedGeoModel.getBone(GeoBoneName);
        if (OptionalGeoBone.isPresent()) {
            geoBone = OptionalGeoBone.get();
        } else {
            matrices.popPose();
            formAnimatable.setPlayer(null);
            return;
        }

        formModel.resetBone(GeoBoneName);
        formModel.translatePositionForBone(GeoBoneName, getPartPosition(arm));
        formModel.translatePositionForBone(GeoBoneName, new Vec3(5 * (IsRenderRight ? -1.0 : 1.0), -2, 0));
        // 感觉有点偏下 额外做个补偿 看上去好像又不用了
        // formModel.translatePositionForBone(GeoBoneName, new Vec3(0, -4, 0));
        formModel.setRotationForBone(GeoBoneName, getPartRotation(arm));

        RenderType renderLayerNormal = RenderType.entityTranslucent(formModel.getTextureResource(formAnimatable));
        renderGeoBone(FursuitArmGeoRenderer.instance, geoBone, matrices, formAnimatable, vertexConsumers, renderLayerNormal, vertexConsumers.getBuffer(renderLayerNormal), light, 1.0f, 1.0f, 1.0f, 1.0f);
        matrices.popPose();
        formAnimatable.setPlayer(null);
    }
}
