package com.bulefire.furrybohe.common.render;

import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;

/**
 * 玩家挥臂动作控制器。
 * <p>
 * 攻击、破坏方块、放置方块以及大部分使用物品都会调用 {@link net.minecraft.world.entity.LivingEntity#swing}，
 * 这里在每次挥臂开始时重新播放一次 {@code attacked} 动画，与基础动作控制器并行，由后注册的控制器覆盖手臂骨骼。
 */
public class FursuitGeoAttackController extends AnimationController<FursuitGeoAnimatable> {
    private static final RawAnimation ATTACKED = RawAnimation.begin().thenPlay("attacked");

    public FursuitGeoAttackController(FursuitGeoAnimatable animatable) {
        super(animatable, "attack", 0, FursuitGeoAttackController::handle);
    }

    private static PlayState handle(@NotNull AnimationState<FursuitGeoAnimatable> state) {
        if (!(state.getData(DataTickets.ENTITY) instanceof Player player)) {
            return PlayState.STOP;
        }

        if (player.swinging) {
            // swingTime 在每次新的挥臂开始时被重置为 -1 / 0，据此重新触发动画
            if (player.swingTime <= 0) {
                state.getController().forceAnimationReset();
            }

            return state.setAndContinue(ATTACKED);
        }

        // 原版挥臂时间比 attacked 动画短，挥臂结束后让动画继续播完再停止
        if (state.getController().getCurrentRawAnimation() == null || state.getController().hasAnimationFinished()) {
            return PlayState.STOP;
        }

        return PlayState.CONTINUE;
    }
}
