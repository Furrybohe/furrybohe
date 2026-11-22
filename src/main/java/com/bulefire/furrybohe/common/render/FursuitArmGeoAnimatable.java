package com.bulefire.furrybohe.common.render;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

/**
 * 第一人称手臂模型的 geckolib 动画载体。
 * <p>
 * 同一时刻只会渲染本地玩家的一副手臂，因此这里直接持有当前玩家，
 * 由 {@link FursuitArmGeoRenderer} 在渲染前设置、渲染后清空。
 */
public final class FursuitArmGeoAnimatable implements GeoAnimatable {
    private static final FursuitArmGeoAnimatable INSTANCE = new FursuitArmGeoAnimatable();

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    @Nullable
    private LocalPlayer player;

    private FursuitArmGeoAnimatable() {
    }

    public static @NotNull FursuitArmGeoAnimatable getInstance() {
        return INSTANCE;
    }

    public @Nullable LocalPlayer getPlayer() {
        return this.player;
    }

    void setPlayer(@Nullable LocalPlayer player) {
        this.player = player;
    }

    @Override
    public void registerControllers(AnimatableManager.@NotNull ControllerRegistrar controllers) {
        FursuitArmGeoAnimation.registerControllers(controllers, this);
    }

    @Override
    public @NotNull AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public double getTick(Object object) {
        if (this.player != null) {
            return this.player.tickCount;
        }

        return object instanceof Entity entity ? entity.tickCount : 0;
    }
}
