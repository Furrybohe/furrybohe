package com.bulefire.furrybohe.item;

import com.bulefire.furrybohe.FurryBoHe;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class FursuitManualItem extends Item {
    public static final String LEVEL_KEY = FurryBoHe.MODID + ":level";
    public static final String ENTRUSTMENT_KEY = FurryBoHe.MODID + ":entrustment";
    
    public FursuitManualItem(Properties properties) {
        super(properties);
    }
    
    @Override
    public @NotNull ItemStack getDefaultInstance() {
        ItemStack stack = super.getDefaultInstance();
        
        CompoundTag tag = stack.getOrCreateTag();
        
        tag.putInt(LEVEL_KEY, 0);
        tag.putBoolean(ENTRUSTMENT_KEY, false);
        
        return stack;
    }
}
