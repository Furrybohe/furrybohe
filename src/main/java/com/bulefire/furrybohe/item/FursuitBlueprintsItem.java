package com.bulefire.furrybohe.item;

import com.bulefire.furrybohe.FurryBoHe;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class FursuitBlueprintsItem extends Item {
    private static final String STATUS_KEY = FurryBoHe.MODID+":status";
    public static final String EMPTY = "empty";
    public static final String DESIGNED = "designed";
    public static final String PRESET = "preset";
    
    public FursuitBlueprintsItem(Properties properties) {
        super(properties);
    }
    
    @Override
    public @NotNull ItemStack getDefaultInstance() {
        ItemStack stack = super.getDefaultInstance();
        
        CompoundTag tag = stack.getOrCreateTag();
        
        tag.putString(STATUS_KEY, EMPTY);
        
        return stack;
    }
    
    @Override
    public @NotNull String getDescriptionId(@NotNull ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if (tag == null) {
            tag = stack.getOrCreateTag();
            tag.putString(STATUS_KEY, EMPTY );
        }
        
        return switch (tag.getString(STATUS_KEY)) {
            case DESIGNED -> "item.furrybohe.fursuit_blueprints_designed";
            case PRESET -> "item.furrybohe.fursuit_blueprints_preset";
            default -> "item.furrybohe.fursuit_blueprints_empty";
        };
    }
}
