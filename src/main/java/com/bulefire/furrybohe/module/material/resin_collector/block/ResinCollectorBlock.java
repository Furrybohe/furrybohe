package com.bulefire.furrybohe.module.material.resin_collector.block;

import com.bulefire.furrybohe.common.registry.FurryBoHeBlockEntityRegister;
import com.bulefire.furrybohe.common.registry.FurryBoHeItemsRegister;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

public class ResinCollectorBlock extends BaseEntityBlock {
    public ResinCollectorBlock(Properties properties) {
        super(properties);
    }
    
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new ResinCollectorBlockEntity(pos, state);
    }
    
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level level, @NotNull BlockState state, @NotNull BlockEntityType<T> type) {
        return type == FurryBoHeBlockEntityRegister.RESIN_COLLECTOR_ENTITY_REGISTER.get()
                ? ResinCollectorBlockEntity::tick
                : null;
    }
    
    @Override
    public @NotNull InteractionResult use(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hit) {
        if (!(level.getBlockEntity(pos) instanceof ResinCollectorBlockEntity be)) return InteractionResult.PASS;
        
        ItemStack held = player.getItemInHand(hand);
        
        if (!held.is(Items.GLASS_BOTTLE)) return InteractionResult.PASS;
        
        if (level.isClientSide) return InteractionResult.SUCCESS;
        
        if (!be.isReady()) {
            //TODO: 提示失败
            return InteractionResult.CONSUME;
        }
        
        if (!player.getAbilities().instabuild) held.shrink(1);
        
        ItemStack result = new ItemStack(FurryBoHeItemsRegister.BOTTLE_OF_RESIN_REGISTER.get(), 1);
        if (!player.getInventory().add(result)) player.drop(result, false);
        
        be.consumeResin();
        level.playSound(null, pos, SoundEvents.BOTTLE_FILL,
                        SoundSource.BLOCKS, 1.0F, 1.0F);
        
        return InteractionResult.CONSUME;
    }
    
    public static class ResinCollectorBlockEntity extends BlockEntity {
        
        private static final int TICKS_PER_RESIN = 20*60;
        private static final int MAX_RESIN = 1;
        
        private int resinAmount = 0;
        private int progress = 0;
        
        public ResinCollectorBlockEntity(BlockPos pos, BlockState state) {
            super(FurryBoHeBlockEntityRegister.RESIN_COLLECTOR_ENTITY_REGISTER.get(), pos, state);
        }
        
        public static <T extends BlockEntity> void tick(@NotNull Level level, BlockPos pos, BlockState blockState, T t) {
            if (level.isClientSide) return;
            
            ResinCollectorBlockEntity be = null;
            if (t instanceof ResinCollectorBlockEntity) be = (ResinCollectorBlockEntity) t;
            else return;
            
            if (be.resinAmount >= MAX_RESIN) {
                be.progress = 0;
                return;
            }
            
            if (!canCollectResin(level, pos)) {
                be.progress = 0;
                return;
            }
            
            be.progress++;
            
            if (be.progress >= TICKS_PER_RESIN) {
                be.progress = 0;
                be.resinAmount++;
                be.setChanged();
            }
        }
        
        private static boolean canCollectResin(Level level, BlockPos pos) {
            return false;
        }
        
        public boolean isReady() {
            return resinAmount >= MAX_RESIN;
        }
        
        public void consumeResin() {
            if (resinAmount > 0) {
                resinAmount--;
                setChanged();
            }
        }
        
        @Override
        protected void saveAdditional(@NotNull CompoundTag tag) {
            super.saveAdditional(tag);
            tag.putInt("ResinAmount", resinAmount);
            tag.putInt("Progress", progress);
        }
        
        @Override
        public void load(@NotNull CompoundTag tag) {
            super.load(tag);
            resinAmount = tag.getInt("ResinAmount");
            progress = tag.getInt("Progress");
        }
    }
}
