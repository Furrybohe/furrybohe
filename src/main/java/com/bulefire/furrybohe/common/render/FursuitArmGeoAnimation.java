package com.bulefire.furrybohe.common.render;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.FishingRodItem;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.ThrowablePotionItem;
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.item.UseAnim;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;

/**
 * 第一人称手臂的动画选择逻辑。
 * <p>
 * 动画名对应 {@code assets/furrybohe/animations/fursuit_arm.animation.json}。
 * 持物姿势与使用动作用两条控制器分别驱动左右手，挥臂动画最后注册以覆盖手臂骨骼。
 */
public final class FursuitArmGeoAnimation {
    private static final int HOLD_TRANSITION_TICKS = 5;
    private static final int USE_TRANSITION_TICKS = 3;

    private FursuitArmGeoAnimation() {
    }

    public static void registerControllers(AnimatableManager.ControllerRegistrar controllers, FursuitArmGeoAnimatable animatable) {
        controllers.add(new AnimationController<>(animatable, "arm_hold_main", HOLD_TRANSITION_TICKS,
                state -> hold(state, InteractionHand.MAIN_HAND)));
        controllers.add(new AnimationController<>(animatable, "arm_hold_off", HOLD_TRANSITION_TICKS,
                state -> hold(state, InteractionHand.OFF_HAND)));
        controllers.add(new AnimationController<>(animatable, "arm_use", USE_TRANSITION_TICKS,
                FursuitArmGeoAnimation::use));
        controllers.add(new AnimationController<>(animatable, "arm_swing_main", 0,
                state -> swing(state, InteractionHand.MAIN_HAND)));
        controllers.add(new AnimationController<>(animatable, "arm_swing_off", 0,
                state -> swing(state, InteractionHand.OFF_HAND)));
    }

    private static PlayState hold(@NotNull AnimationState<FursuitArmGeoAnimatable> state, InteractionHand hand) {
        LocalPlayer player = state.getAnimatable().getPlayer();

        if (player == null) {
            return PlayState.STOP;
        }

        String prefix = hand == InteractionHand.MAIN_HAND ? "hold_mainhand:" : "hold_offhand:";
        String kind = heldKind(player.getItemInHand(hand), hand);

        return state.setAndContinue(RawAnimation.begin().thenLoop(prefix + kind));
    }

    private static PlayState use(@NotNull AnimationState<FursuitArmGeoAnimatable> state) {
        LocalPlayer player = state.getAnimatable().getPlayer();

        if (player == null || !player.isUsingItem() || player.getUseItemRemainingTicks() <= 0) {
            return PlayState.STOP;
        }

        String kind = useKind(player.getUseItem());

        if (kind == null) {
            return PlayState.STOP;
        }

        String hand = player.getUsedItemHand() == InteractionHand.MAIN_HAND ? "mainhand" : "offhand";
        // 物品专属动画使用 use_mainhand$minecraft:xxx 形式，其余为 use_mainhand:xxx
        String name = kind.startsWith("$") ? "use_" + hand + kind : "use_" + hand + ":" + kind;

        return state.setAndContinue(RawAnimation.begin().thenLoop(name));
    }

    private static PlayState swing(@NotNull AnimationState<FursuitArmGeoAnimatable> state, InteractionHand hand) {
        LocalPlayer player = state.getAnimatable().getPlayer();

        if (player == null) {
            return PlayState.STOP;
        }

        String animation = swingAnimation(player.getItemInHand(hand), hand);

        if (animation == null) {
            return PlayState.STOP;
        }

        AnimationController<FursuitArmGeoAnimatable> controller = state.getController();

        if (player.swinging && player.swingingArm == hand) {
            // swingTime 在每次新的挥臂开始时被重置为 -1 / 0，据此重新触发动画
            if (player.swingTime <= 0) {
                controller.forceAnimationReset();
            }

            return state.setAndContinue(RawAnimation.begin().thenPlay(animation));
        }

        // 原版挥臂时间比动画短，挥臂结束后让动画继续播完再停止
        if (controller.getCurrentRawAnimation() == null || controller.hasAnimationFinished()) {
            return PlayState.STOP;
        }

        return PlayState.CONTINUE;
    }

    private static String heldKind(ItemStack stack, InteractionHand hand) {
        if (stack.isEmpty()) {
            return "empty";
        }

        Item item = stack.getItem();

        if (item instanceof SwordItem) return "sword";
        if (item instanceof PickaxeItem) return "pickaxe";
        if (item instanceof AxeItem) return "axe";
        if (item instanceof ShovelItem) return "shovel";
        if (item instanceof HoeItem) return "hoe";
        if (item instanceof TridentItem) return "spear";
        if (item instanceof BowItem) return "bow";
        if (item instanceof CrossbowItem) return CrossbowItem.isCharged(stack) ? "charged_crossbow" : "crossbow";
        if (stack.is(Items.SHIELD)) return "shield";
        // 鱼竿 / 投掷药水只有主手动作
        if (item instanceof FishingRodItem) return hand == InteractionHand.MAIN_HAND ? "fishing" : "empty";
        if (item instanceof ThrowablePotionItem) return hand == InteractionHand.MAIN_HAND ? "throwable_potion" : "empty";

        return "empty";
    }

    @Nullable
    private static String useKind(ItemStack stack) {
        UseAnim animation = stack.getUseAnimation();

        return switch (animation) {
            case EAT -> "eat";
            case DRINK -> "drink";
            case BOW -> "bow";
            case BLOCK -> "shield";
            case SPEAR -> "spear";
            case CROSSBOW -> "crossbow";
            case SPYGLASS -> "spyglass";
            case BRUSH -> "$minecraft:brush";
            case TOOT_HORN -> "$minecraft:goat_horn";
            default -> null;
        };
    }

    @Nullable
    private static String swingAnimation(ItemStack stack, InteractionHand hand) {
        if (stack.isEmpty()) {
            return null;
        }

        Item item = stack.getItem();

        if (item instanceof SwordItem) {
            return hand == InteractionHand.MAIN_HAND ? "swing:sword" : null;
        }
        if (item instanceof TridentItem) {
            return hand == InteractionHand.MAIN_HAND ? "swing:spear" : null;
        }
        if (item instanceof FishingRodItem) {
            return hand == InteractionHand.MAIN_HAND ? "swing:fishing_rod" : "swing_offhand:fishing_rod";
        }
        if (item instanceof ThrowablePotionItem) {
            return hand == InteractionHand.MAIN_HAND ? "swing:throwable_potion" : "swing_offhand:throwable_potion";
        }

        return null;
    }
}
