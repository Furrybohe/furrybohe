package com.bulefire.furrybohe.common.render;

import com.bulefire.furrybohe.FurryBoHe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3d;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.model.GeoModel;

import java.util.HashMap;
import java.util.Optional;

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

    public final HashMap<String, GeoBone> geoBoneCache = new HashMap<>();

    public final @Nullable GeoBone getCachedGeoBone(String name) {
        GeoBone bone = geoBoneCache.get(name);
        if (bone == null) {
            Optional<GeoBone> boneOptional = this.getBone(name);
            if (boneOptional.isPresent()) {
                bone = boneOptional.get();
                geoBoneCache.put(name, bone);
            }
        }
        return bone;
    }

    public final GeoBone translatePositionForBone(String bone_name, Vec3 pos) {
        var b = this.getCachedGeoBone(bone_name);
        if (b == null) {
            return null;
        }
        var posOut = new Vec3(pos.x + b.getPosX(), (float)pos.y + b.getPosY(),(float)pos.z + b.getPosZ());
        return this.setPositionForBone(bone_name, posOut);
    }

    public final GeoBone setPositionForBone(String bone_name, Vec3 pos) {
        var b = this.getCachedGeoBone(bone_name);
        if (b == null) {
            return null;
        }
        b.setPosX((float)pos.x);
        b.setPosY((float)pos.y);
        b.setPosZ((float)pos.z);
        return (GeoBone) b;
    }

    public final GeoBone setRotationForBone(String bone_name, Vec3 rot) {
        var b = this.getCachedGeoBone(bone_name);
        if (b == null) {
            return null;
        }
        b.setRotX((float)rot.x);
        b.setRotY((float)rot.y);
        b.setRotZ((float)rot.z);
        return (GeoBone) b;
    }

    public final GeoBone setModelPositionForBone(String bone_name, Vec3 pos) {
        var b = this.getCachedGeoBone(bone_name);
        if (b == null) {
            return null;
        }
        b.setModelPosition(new Vector3d(pos.x, pos.y, pos.z));
        return (GeoBone) b;
    }

    public final GeoBone setScaleForBone(String bone_name, Vec3 scale) {
        var b = this.getCachedGeoBone(bone_name);
        if (b == null) {
            return null;
        }
        b.setScaleX((float)scale.x);
        b.setScaleY((float)scale.y);
        b.setScaleZ((float)scale.z);
        return (GeoBone) b;
    }

    public final GeoBone invertRotForPart(String bone_name, boolean x, boolean y, boolean z) {
        var b = getCachedGeoBone(bone_name);
        if (b == null) {return null;}
        var r =b.getRotationVector().mul(x ? -1 : 1, y ? -1 : 1, z ? -1 : 1);
        b.setRotX((float) r.x);
        b.setRotY((float) r.y);
        b.setRotZ((float) r.z);
        return b;
    }

    public final GeoBone setPivotForBone(String bone_name, Vec3 pivot) {
        var b = this.getCachedGeoBone(bone_name);
        if (b == null) {
            return null;
        }
        b.setPivotX((float)pivot.x);
        b.setPivotY((float)pivot.y);
        b.setPivotZ((float)pivot.z);
        return (GeoBone) b;
    }

    public final GeoBone resetBone(String bone_name) {
        setPositionForBone(bone_name, new Vec3(0,0,0));
        setRotationForBone(bone_name, new Vec3(0,0,0));
        setModelPositionForBone(bone_name, Vec3.ZERO);
        return setScaleForBone(bone_name, new Vec3(1,1,1));
    }
}
