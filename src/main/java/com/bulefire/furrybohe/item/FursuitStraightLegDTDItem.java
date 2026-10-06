package com.bulefire.furrybohe.item;

import com.bulefire.furrybohe.FurryBoHe;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class FursuitStraightLegDTDItem extends Item {
    private static final String SHAPED_KEY = FurryBoHe.MODID+":shaped";
    
    public FursuitStraightLegDTDItem(Properties properties) {
        super(properties);
    }
    
    @Override
    public @NotNull ItemStack getDefaultInstance() {
        ItemStack stack = super.getDefaultInstance();
        
        CompoundTag tag = stack.getOrCreateTag();
        
        tag.putBoolean(SHAPED_KEY, false);
        
        return stack;
    }
}
