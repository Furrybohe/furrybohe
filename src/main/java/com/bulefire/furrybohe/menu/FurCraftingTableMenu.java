package com.bulefire.furrybohe.menu;

import com.bulefire.furrybohe.block.FurCraftingTableBlock;
import com.bulefire.furrybohe.item.FurryBoHeItems;
import com.bulefire.furrybohe.register.FurryBoHeMenuTypeRegister;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class FurCraftingTableMenu extends AbstractContainerMenu {
    private final FurCraftingTableBlock.FurCraftingTableBlockEntity entity;
    
    public FurCraftingTableMenu(int containerId, @NotNull Inventory playerInventory, FurCraftingTableBlock.@NotNull FurCraftingTableBlockEntity entity) {
        super(FurryBoHeMenuTypeRegister.FUR_CRAFTING_TABLE_MENU_TYPE.get(), containerId);
        this.entity = entity;
        
        this.entity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(handler -> {
            this.addSlot(new SlotItemHandler(handler, 0, 19, 36) {
                @Override
                public boolean mayPlace(@NotNull ItemStack stack) {
                    return stack.is(Items.LEATHER);
                }
            });
            this.addSlot(new SlotItemHandler(handler, 1, 52, 36) {
                @Override
                public boolean mayPlace(@NotNull ItemStack stack) {
                    return stack.is(ItemTags.WOOL) || stack.is(FurryBoHeItems.COTTON);
                }
            });
            this.addSlot(new SlotItemHandler(handler, 2, 84, 36) {
                @Override
                public boolean mayPlace(@NotNull ItemStack stack) {
                    return stack.is(Tags.Items.DYES);
                }
            });
            this.addSlot(new SlotItemHandler(handler, 3, 146, 36) {
                @Override
                public boolean mayPlace(@NotNull ItemStack stack) {
                    return false;
                }
            });
        });
        
        addPlayerInventory(playerInventory, 8, 84);
        addPlayerHotbar(playerInventory, 8, 142);
    }
    
    public FurCraftingTableMenu(int containerId, Inventory playerInventory, @NotNull FriendlyByteBuf extraData) {
        this(containerId, playerInventory, resolve(playerInventory, extraData));
    }
    
    private static FurCraftingTableBlock.FurCraftingTableBlockEntity resolve(@NotNull Inventory inv, @NotNull FriendlyByteBuf data) {
        BlockEntity be = inv.player.level().getBlockEntity(data.readBlockPos());
        if (!(be instanceof FurCraftingTableBlock.FurCraftingTableBlockEntity table)) {
            throw new IllegalStateException("fur_crafting_table block entity missing on client");
        }
        return table;
    }
    
    private void addPlayerInventory(Inventory inv, int leftCol, int topRow) {
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                this.addSlot(new Slot(inv, col + row * 9 + 9,
                                      leftCol + col * 18, topRow + row * 18));
            }
        }
    }
    
    private void addPlayerHotbar(Inventory inv, int leftCol, int topRow) {
        for (int col = 0; col < 9; ++col) {
            this.addSlot(new Slot(inv, col, leftCol + col * 18, topRow));
        }
    }
    
    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int index) {
        ItemStack result = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (!slot.hasItem()) return result;
        
        ItemStack stackInSlot = slot.getItem();
        result = stackInSlot.copy();
        
        if (index < 4) {
            if (!this.moveItemStackTo(stackInSlot, 4, this.slots.size(), true)) {
                return ItemStack.EMPTY;
            }
        }else {
            if (!this.moveItemStackTo(stackInSlot, 0, 4, false)) {
                return ItemStack.EMPTY;
            }
        }
        
        if (stackInSlot.isEmpty()) {
            slot.setByPlayer(ItemStack.EMPTY);
        } else {
            slot.setChanged();
        }
        
        if (stackInSlot.getCount() == result.getCount()) {
            return ItemStack.EMPTY;
        }
        
        slot.onTake(player, stackInSlot);
        
        return result;
    }
    
    @Override
    public boolean stillValid(@NotNull Player player) {
        return entity != null && !entity.isRemoved()
                && player.distanceToSqr(entity.getBlockPos().getCenter()) <= 64.0;
    }
}
