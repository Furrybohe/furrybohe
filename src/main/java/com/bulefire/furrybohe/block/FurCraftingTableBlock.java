package com.bulefire.furrybohe.block;

import com.bulefire.furrybohe.menu.FurCraftingTableMenu;
import com.bulefire.furrybohe.register.FurryBoHeBlockEntityRegister;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;

public class FurCraftingTableBlock extends Block implements EntityBlock {
    public FurCraftingTableBlock(Properties properties) {
        super(properties);
    }
    
    @Override
    public @NotNull InteractionResult use(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, BlockHitResult hit) {
        if (! level.isClientSide()) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof FurCraftingTableBlockEntity myBE) {
                NetworkHooks.openScreen((ServerPlayer) player, myBE, pos);
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide());
    }
    
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new FurCraftingTableBlockEntity(pos, state);
    }
    
    public static class FurCraftingTableBlockEntity extends BlockEntity implements MenuProvider {
        private final ItemStackHandler itemHandler = new ItemStackHandler(4) {
            @Override
            protected void onContentsChanged(int slot) {
                setChanged();
            }
        };
        
        public FurCraftingTableBlockEntity(BlockPos pos, BlockState state) {
            super(FurryBoHeBlockEntityRegister.FUR_CRAFTING_TABLE_BLOCK_ENTITY_REGISTER.get(), pos, state);
        }
        
        @Override
        public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> cap, Direction side) {
            if (cap == ForgeCapabilities.ITEM_HANDLER) {
                return LazyOptional.of(() -> itemHandler).cast();
            }
            return super.getCapability(cap, side);
        }
        
        @Override
        public @NotNull Component getDisplayName() {
            return Component.translatable("container.furrybohe.fur_crafting_table");
        }
        
        @Override
        public AbstractContainerMenu createMenu(int id, @NotNull Inventory playerInventory, @NotNull Player player) {
            return new FurCraftingTableMenu(id, playerInventory, this); // 传入 BlockEntity
        }
    }
}
