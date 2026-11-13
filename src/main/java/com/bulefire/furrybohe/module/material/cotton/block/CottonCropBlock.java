package com.bulefire.furrybohe.module.material.cotton.block;

import com.bulefire.furrybohe.common.registry.FurryBoHeItemsRegister;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.jetbrains.annotations.NotNull;

public class CottonCropBlock extends CropBlock {
    private static final int MX_AGE = 3;
    
    public static final IntegerProperty AGE = IntegerProperty.create("age", 0, MX_AGE);
    
    public CottonCropBlock(Properties properties) {
        super(properties);
    }
    
    @Override
    protected @NotNull IntegerProperty getAgeProperty() {
        return AGE;
    }
    
    @Override
    public int getMaxAge() {
        return MX_AGE;
    }
    
    @Override
    protected @NotNull ItemLike getBaseSeedId() {
        return FurryBoHeItemsRegister.COTTON_SEEDS_REGISTER.get();
    }
    
    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
        builder.add(AGE);
    }
}
