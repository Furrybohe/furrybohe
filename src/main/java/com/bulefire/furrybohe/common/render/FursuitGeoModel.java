package com.bulefire.furrybohe.common.render;

import com.bulefire.furrybohe.FurryBoHe;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.model.GeoModel;

/**
 * 玩家白膜模型，资源由 {@code resources/白膜} 转换而来（Bedrock 格式，geckolib 可直接加载）。
 */
public class FursuitGeoModel extends GeoModel<FursuitGeoAnimatable> {
    private static final ResourceLocation MODEL =
            ResourceLocation.fromNamespaceAndPath(FurryBoHe.MODID, "geo/player.geo.json");
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(FurryBoHe.MODID, "textures/entity/player.png");
    private static final ResourceLocation ANIMATION =
            ResourceLocation.fromNamespaceAndPath(FurryBoHe.MODID, "animations/player.animation.json");

    @Override
    public @NotNull ResourceLocation getModelResource(@NotNull FursuitGeoAnimatable animatable) {
        return MODEL;
    }

    @Override
    public @NotNull ResourceLocation getTextureResource(@NotNull FursuitGeoAnimatable animatable) {
        return TEXTURE;
    }

    @Override
    public @NotNull ResourceLocation getAnimationResource(@NotNull FursuitGeoAnimatable animatable) {
        return ANIMATION;
    }
}
