package com.bulefire.furrybohe.common.render;

import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;

/**
 * 玩家基础动作控制器。
 * <p>
 * 动画名称对应 {@code assets/furrybohe/animations/player.animation.json} 中的键，
 * 未在模型中定义的骨骼会被 geckolib 自动忽略。
 */
public class FursuitGeoController extends AnimationController<FursuitGeoAnimatable> {
    private static final int TRANSITION_TICKS = 5;

    private static final RawAnimation IDLE = RawAnimation.begin().thenLoop("idle");
    private static final RawAnimation WALK = RawAnimation.begin().thenLoop("walk");
    private static final RawAnimation RUN = RawAnimation.begin().thenLoop("run");
    /** 潜行移动 */
    private static final RawAnimation SNEAK = RawAnimation.begin().thenLoop("sneak");
    /** 潜行待机 */
    private static final RawAnimation SNEAKING = RawAnimation.begin().thenLoop("sneaking");
    private static final RawAnimation SWIM = RawAnimation.begin().thenLoop("swim");
    private static final RawAnimation FLY = RawAnimation.begin().thenLoop("fly");
    private static final RawAnimation JUMP = RawAnimation.begin().thenLoop("jump");
    private static final RawAnimation RIDE = RawAnimation.begin().thenLoop("ride");
    private static final RawAnimation SLEEP = RawAnimation.begin().thenLoop("sleep");
    private static final RawAnimation ELYTRA_FLY = RawAnimation.begin().thenLoop("elytra_fly");

    public FursuitGeoController(FursuitGeoAnimatable animatable) {
        super(animatable, "player", TRANSITION_TICKS, FursuitGeoController::handle);
    }

    private static PlayState handle(@NotNull AnimationState<FursuitGeoAnimatable> state) {
        if (!(state.getData(DataTickets.ENTITY) instanceof Player player)) {
            return PlayState.STOP;
        }

        if (player.isFallFlying()) {
            return state.setAndContinue(ELYTRA_FLY);
        }
        if (player.isPassenger()) {
            return state.setAndContinue(RIDE);
        }
        if (player.isSleeping()) {
            return state.setAndContinue(SLEEP);
        }
        if (player.isSwimming()) {
            return state.setAndContinue(SWIM);
        }
        if (player.getAbilities().flying) {
            return state.setAndContinue(FLY);
        }
        if (!player.onGround()) {
            return state.setAndContinue(player.isInWater() ? SWIM : JUMP);
        }
        if (player.isCrouching()) {
            return state.setAndContinue(state.isMoving() ? SNEAK : SNEAKING);
        }
        if (state.isMoving()) {
            return state.setAndContinue(player.isSprinting() ? RUN : WALK);
        }

        return state.setAndContinue(IDLE);
    }
}
