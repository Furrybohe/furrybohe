package com.bulefire.furrybohe.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class FurItem extends Item {
    public FurItem(Properties properties) {
        super(properties);
    }
    
    @Override
    public boolean hasCraftingRemainingItem(ItemStack stack) {
        return true;
    }
    
    @Override
    public ItemStack getCraftingRemainingItem(@NotNull ItemStack stack) {
        ItemStack remainder = stack.copy();
        // TODO: 设置剩余物品栈的耐久度
        
        return remainder;
    }
}
