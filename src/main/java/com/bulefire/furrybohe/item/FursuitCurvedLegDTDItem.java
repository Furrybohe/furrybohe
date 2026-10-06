package com.bulefire.furrybohe.item;

import com.bulefire.furrybohe.FurryBoHe;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class FursuitCurvedLegDTDItem extends Item {
    private static final String SHAPED_KEY = FurryBoHe.MODID+":shaped";
    
    public FursuitCurvedLegDTDItem(Properties properties) {
        super(properties);
    }
    
    @Override
    public @NotNull ItemStack getDefaultInstance() {
        ItemStack stack = super.getDefaultInstance();
        
        CompoundTag tag = stack.getOrCreateTag();
        
        tag.putBoolean(SHAPED_KEY, false);
        
        return stack;
    }
    
    @Override
    public @NotNull String getDescriptionId(@NotNull ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if (tag == null) {
            tag = stack.getOrCreateTag();
            tag.putBoolean(SHAPED_KEY, false);
        }
        
        return tag.getBoolean(SHAPED_KEY) ?
                "item.furrybohe.fursuit_curved_leg_dtd_shaped" :
                "item.furrybohe.fursuit_curved_leg_dtd_unshaped";
    }
}
