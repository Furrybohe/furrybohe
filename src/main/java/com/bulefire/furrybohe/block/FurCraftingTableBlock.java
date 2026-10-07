package com.bulefire.furrybohe.block;

import com.bulefire.furrybohe.menu.FurCraftingTableMenu;
import com.bulefire.furrybohe.register.FurryBoHeBlockEntityRegister;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
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
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FurCraftingTableBlock extends Block implements EntityBlock {
    public FurCraftingTableBlock(Properties properties) {
        super(properties);
    }
    
    @Override
    public @NotNull InteractionResult use(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hit) {
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
    
    @Override
    public void playerWillDestroy(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull Player player) {
        if (! level.isClientSide() && level.getBlockEntity(pos) instanceof FurCraftingTableBlockEntity be) {
            Containers.dropContents(level, pos, be.getInput());
            be.getInput().clearContent();
        }
        super.playerWillDestroy(level, pos, state, player);
    }
    
    public static class FurCraftingTableBlockEntity extends BlockEntity implements MenuProvider {
        private static final int INPUT_SIZE = 3;
        
        private final SimpleContainer input = new SimpleContainer(INPUT_SIZE);
        
        private LazyOptional<IItemHandler> itemHandler = LazyOptional.empty();
        
        public FurCraftingTableBlockEntity(BlockPos pos, BlockState state) {
            super(FurryBoHeBlockEntityRegister.FUR_CRAFTING_TABLE_BLOCK_ENTITY_REGISTER.get(), pos, state);
            this.input.addListener(container -> this.setChanged());
        }
        
        public SimpleContainer getInput() {
            return input;
        }
        
        private LazyOptional<IItemHandler> handler() {
            if (! itemHandler.isPresent()) {
                itemHandler = LazyOptional.of(() -> new InvWrapper(input));
            }
            return itemHandler;
        }
        
        @Override
        public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
            if (cap == ForgeCapabilities.ITEM_HANDLER) {
                return handler().cast();
            }
            return super.getCapability(cap, side);
        }
        
        @Override
        public void invalidateCaps() {
            super.invalidateCaps();
            itemHandler.invalidate();
        }
        
        @Override
        protected void saveAdditional(@NotNull CompoundTag tag) {
            super.saveAdditional(tag);
            tag.put("inventory", input.createTag());
        }
        
        @Override
        public void load(@NotNull CompoundTag tag) {
            super.load(tag);
            input.fromTag(tag.getList("inventory", Tag.TAG_COMPOUND));
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
