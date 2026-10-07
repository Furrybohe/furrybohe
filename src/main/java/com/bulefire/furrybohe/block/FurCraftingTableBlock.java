package com.bulefire.furrybohe.block;

import com.bulefire.furrybohe.menu.FurCraftingTableMenu;
import com.bulefire.furrybohe.register.FurryBoHeBlockEntityRegister;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
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
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.InvWrapper;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

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
    public void onRemove(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos,
                         @NotNull BlockState newState, boolean isMoving) {
        if (! state.is(newState.getBlock())
                && ! level.isClientSide()
                && level.getBlockEntity(pos) instanceof FurCraftingTableBlockEntity be) {
            Containers.dropContents(level, pos, be.getInput());
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }
    
    public static class FurCraftingTableBlockEntity extends BlockEntity implements MenuProvider {
        private static final String TAG_ITEMS = "Items";
        
        private final SimpleContainer input = new SimpleContainer(FurCraftingSlots.SIZE) {
            @Override
            public boolean canPlaceItem(int slot, @NotNull ItemStack stack) {
                return FurCraftingSlots.accepts(slot, stack);
            }
        };
        
        private final Map<Direction, LazyOptional<IItemHandler>> sideHandlers = new HashMap<>();
        
        public FurCraftingTableBlockEntity(BlockPos pos, BlockState state) {
            super(FurryBoHeBlockEntityRegister.FUR_CRAFTING_TABLE_BLOCK_ENTITY_REGISTER.get(), pos, state);
            this.input.addListener(container -> this.setChanged());
        }
        
        public SimpleContainer getInput() {
            return input;
        }
        
        private LazyOptional<IItemHandler> handler(@Nullable Direction side) {
            return sideHandlers.computeIfAbsent(side, s -> LazyOptional.of(() -> new SidedHandler(input, s)));
        }
        
        @Override
        public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
            if (cap == ForgeCapabilities.ITEM_HANDLER) {
                return handler(side).cast();
            }
            return super.getCapability(cap, side);
        }
        
        @Override
        public void invalidateCaps() {
            super.invalidateCaps();
            sideHandlers.values().forEach(LazyOptional::invalidate);
            sideHandlers.clear();
        }
        
        @Override
        protected void saveAdditional(@NotNull CompoundTag tag) {
            super.saveAdditional(tag);
            tag.put(TAG_ITEMS, input.createTag());
        }
        
        @Override
        public void load(@NotNull CompoundTag tag) {
            super.load(tag);
            input.fromTag(tag.getList("Items", Tag.TAG_COMPOUND));
        }
        
        @Override
        public @NotNull CompoundTag getUpdateTag() {
            CompoundTag tag = super.getUpdateTag();
            tag.put(TAG_ITEMS, input.createTag());
            return tag;
        }
        
        @Override
        public @NotNull Component getDisplayName() {
            return Component.translatable("container.furrybohe.fur_crafting_table");
        }
        
        @Override
        public AbstractContainerMenu createMenu(int id, @NotNull Inventory playerInventory, @NotNull Player player) {
            return new FurCraftingTableMenu(id, playerInventory, this);
        }
        
        private record SidedHandler(IItemHandlerModifiable base,
                                    boolean allowInsert) implements IItemHandlerModifiable {
                    private SidedHandler(Container container, @Nullable Direction side) {
                        this(new InvWrapper(container), side == null);
                    }
                    
                    @Override
                    public void setStackInSlot(int slot, @NotNull ItemStack stack) {
                        base.setStackInSlot(slot, stack);
                    }
                    
                    @Override
                    public int getSlots() {
                        return base.getSlots();
                    }
                    
                    @Override
                    public @NotNull ItemStack getStackInSlot(int slot) {
                        return base.getStackInSlot(slot);
                    }
                    
                    @Override
                    public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
                        if (! allowInsert) {
                            return stack;
                        }
                        return base.insertItem(slot, stack, simulate);
                    }
                    
                    @Override
                    public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
                        return base.extractItem(slot, amount, simulate);
                    }
                    
                    @Override
                    public int getSlotLimit(int slot) {
                        return base.getSlotLimit(slot);
                    }
                    
                    @Override
                    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
                        return allowInsert && base.isItemValid(slot, stack);
                    }
                }
    }
}
