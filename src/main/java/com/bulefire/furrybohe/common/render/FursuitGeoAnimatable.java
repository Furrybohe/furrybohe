package com.bulefire.furrybohe.common.render;

import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

/**
 * 玩家白膜的 geckolib 动画载体。
 * <p>
 * 玩家共用同一个实例，geckolib 会按实体 id 为每个玩家分配独立的 {@link AnimatableManager}。
 * 当前渲染的玩家通过 {@link software.bernie.geckolib.constant.DataTickets#ENTITY} 从动画状态中获取。
 */
public final class FursuitGeoAnimatable implements GeoAnimatable {
    private static final FursuitGeoAnimatable INSTANCE = new FursuitGeoAnimatable();

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    private FursuitGeoAnimatable() {
    }

    public static @NotNull FursuitGeoAnimatable getInstance() {
        return INSTANCE;
    }

    @Override
    public void registerControllers(AnimatableManager.@NotNull ControllerRegistrar controllers) {
        controllers.add(new FursuitGeoController(this));
        controllers.add(new FursuitGeoAttackController(this));
    }

    @Override
    public @NotNull AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public double getTick(Object object) {
        return object instanceof Entity entity ? entity.tickCount : 0;
    }
}
