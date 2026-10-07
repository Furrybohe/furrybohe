package com.bulefire.furrybohe.block;

import com.bulefire.furrybohe.item.FurryBoHeItems;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.Tags;
import org.jetbrains.annotations.NotNull;

/**
 * 毛布制作台的槽位约定：下标含义与放入白名单的唯一权威定义。
 * <p>
 * 方块实体容器、菜单槽位、配方索引三方都从这里取值，避免"菜单里写一遍白名单、
 * 能力侧没有校验"导致两条路径行为不一致。
 */
public final class FurCraftingSlots {
    /** 皮革槽 */
    public static final int LEATHER = 0;
    /** 填充物槽（羊毛 / 棉花） */
    public static final int FILLER = 1;
    /** 染料槽 */
    public static final int DYE = 2;
    /** 输入槽总数 */
    public static final int SIZE = 3;
    
    private FurCraftingSlots() {
    }
    
    /**
     * 该输入槽是否接受这个物品栈。
     * <p>
     * 同时被两条路径调用：
     * <ul>
     *     <li>{@code SimpleContainer#canPlaceItem} —— 漏斗/管道等自动化路径。
     *         Forge 的 {@code InvWrapper#insertItem} 内部会转调 {@code Container#canPlaceItem}，
     *         所以白名单在这里生效。</li>
     *     <li>菜单 {@code Slot#mayPlace} —— 玩家手动放置与 shift 点击路径。</li>
     * </ul>
     */
    public static boolean accepts(int slot, @NotNull ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        }
        return switch (slot) {
            case LEATHER -> stack.is(Items.LEATHER);
            case FILLER -> stack.is(ItemTags.WOOL) || stack.is(FurryBoHeItems.COTTON);
            case DYE -> stack.is(Tags.Items.DYES);
            default -> false;
        };
    }
}
