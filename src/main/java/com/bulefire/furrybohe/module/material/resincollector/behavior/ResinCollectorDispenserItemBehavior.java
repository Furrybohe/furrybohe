package com.bulefire.furrybohe.module.material.resincollector.behavior;

import com.bulefire.furrybohe.common.registry.FurryBoHeBlocksRegister;
import com.bulefire.furrybohe.module.material.resincollector.block.ResinCollectorBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class ResinCollectorDispenserItemBehavior extends DefaultDispenseItemBehavior {
    @Override
    protected @NotNull ItemStack execute(@NotNull BlockSource source, @NotNull ItemStack itemStack) {
        BlockPos targetPos = source.getPos().relative(source.getBlockState().getValue(DispenserBlock.FACING));
        BlockState targetState = source.getLevel().getBlockState(targetPos);
        Block targetBlock = targetState.getBlock();
        
        if (targetBlock != FurryBoHeBlocksRegister.RESIN_COLLECTOR_REGISTER.get()) return super.execute(source, itemStack);
        ResinCollectorBlock block = (ResinCollectorBlock) targetBlock;
        
        if (itemStack.is(Items.GLASS_BOTTLE)) return dispenseGlassBottle(source, targetPos, targetState, block, itemStack);
        if (itemStack.is(Items.SLIME_BALL)) return dispenseSlimeBall(source, targetPos, targetState, block, itemStack);
        return super.execute(source, itemStack);
    }
    
    private ItemStack dispenseGlassBottle(@NotNull BlockSource source, BlockPos pos, BlockState state, ResinCollectorBlock block, ItemStack itemStack) {
        BlockEntity blockEntity = source.getLevel().getBlockEntity(pos);
        if (blockEntity == null) return itemStack;
        if (!(blockEntity instanceof ResinCollectorBlock.ResinCollectorBlockEntity be)) return itemStack;
        
        ItemStack result = block.collect(be, true, itemStack, state, source.getLevel(), pos);
        
        ItemEntity itemEntity = new ItemEntity(source.getLevel(), pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, result);
        itemEntity.setDeltaMovement(
                source.getLevel().random.nextGaussian() * 0.05,
                0.1,
                source.getLevel().random.nextGaussian() * 0.05
        );
        source.getLevel().addFreshEntity(itemEntity);
        
        
        return itemStack;
    }
    
    private ItemStack dispenseSlimeBall(@NotNull BlockSource source, BlockPos pos, BlockState state, ResinCollectorBlock block, ItemStack itemStack) {
        BlockEntity blockEntity = source.getLevel().getBlockEntity(pos);
        if (blockEntity == null) return itemStack;
        if (!(blockEntity instanceof ResinCollectorBlock.ResinCollectorBlockEntity be)) return itemStack;
        
        ItemStack result = block.fill(be, true, itemStack, state, source.getLevel(), pos);
        
        return itemStack;
    }
}
