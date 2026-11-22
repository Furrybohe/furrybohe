package com.bulefire.furrybohe.common.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.player.Player;
import software.bernie.geckolib.renderer.GeoReplacedEntityRenderer;

/**
 * 用白膜模型替换原版玩家渲染。
 * <p>
 * 由 {@link com.bulefire.furrybohe.common.mixin.LivingEntityRendererMixin} 在玩家渲染阶段接管，
 * 不替换 {@code EntityType.PLAYER} 的渲染器，因此第一人称手臂仍使用原版渲染。
 */
public class FursuitGeoRenderer extends GeoReplacedEntityRenderer<Player, FursuitGeoAnimatable> {
    /** 对应 ysm.json 中的 height_scale / width_scale */
    private static final float MODEL_SCALE = 0.7F;

    private static FursuitGeoRenderer instance;

    private FursuitGeoRenderer(EntityRendererProvider.Context context) {
        super(context, new FursuitGeoModel(), FursuitGeoAnimatable.getInstance());
        withScale(MODEL_SCALE);
    }

    public static void renderPlayer(Player player, float entityYaw, float partialTick, PoseStack poseStack,
                                    MultiBufferSource bufferSource, int packedLight) {
        getInstance().render(player, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }

    private static FursuitGeoRenderer getInstance() {
        if (instance == null) {
            Minecraft minecraft = Minecraft.getInstance();
            EntityRendererProvider.Context context = new EntityRendererProvider.Context(
                    minecraft.getEntityRenderDispatcher(),
                    minecraft.getItemRenderer(),
                    minecraft.getBlockRenderer(),
                    minecraft.getEntityRenderDispatcher().getItemInHandRenderer(),
                    minecraft.getResourceManager(),
                    minecraft.getEntityModels(),
                    minecraft.font);
            instance = new FursuitGeoRenderer(context);
        }

        return instance;
    }
}
