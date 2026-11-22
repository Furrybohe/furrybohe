package com.bulefire.furrybohe.common.render;

import com.bulefire.furrybohe.FurryBoHe;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.model.GeoModel;

/**
 * 第一人称手臂模型，资源由 {@code resources/白膜/models/arm.json} 及对应的 arm 动画转换而来。
 */
public class FursuitArmGeoModel extends GeoModel<FursuitArmGeoAnimatable> {
    private static final ResourceLocation MODEL =
            ResourceLocation.fromNamespaceAndPath(FurryBoHe.MODID, "geo/fursuit_arm.geo.json");
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(FurryBoHe.MODID, "textures/entity/player.png");
    private static final ResourceLocation ANIMATION =
            ResourceLocation.fromNamespaceAndPath(FurryBoHe.MODID, "animations/fursuit_arm.animation.json");

    @Override
    public @NotNull ResourceLocation getModelResource(@NotNull FursuitArmGeoAnimatable animatable) {
        return MODEL;
    }

    @Override
    public @NotNull ResourceLocation getTextureResource(@NotNull FursuitArmGeoAnimatable animatable) {
        return TEXTURE;
    }

    @Override
    public @NotNull ResourceLocation getAnimationResource(@NotNull FursuitArmGeoAnimatable animatable) {
        return ANIMATION;
    }
}
