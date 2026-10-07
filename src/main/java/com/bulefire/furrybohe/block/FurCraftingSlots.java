package com.bulefire.furrybohe.block;

import com.bulefire.furrybohe.item.FurryBoHeItems;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.Tags;
import org.jetbrains.annotations.NotNull;

public final class FurCraftingSlots {
    public static final int LEATHER = 0;
    public static final int FILLER = 1;
    public static final int DYE = 2;
    public static final int SIZE = 3;
    
    private FurCraftingSlots() {
    }
    
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
