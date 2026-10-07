package com.bulefire.furrybohe.menu;

import com.bulefire.furrybohe.block.FurCraftingSlots;
import com.bulefire.furrybohe.block.FurCraftingTableBlock;
import com.bulefire.furrybohe.recipe.FurRecipe;
import com.bulefire.furrybohe.register.FurryBoHeMenuTypeRegister;
import com.bulefire.furrybohe.register.FurryBoHeRecipesRegister;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.ContainerListener;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FurCraftingTableMenu extends AbstractContainerMenu {
    private static final int SLOT_RESULT = 3;
    private static final int MENU_SLOTS = 4;
    private static final int INPUT_SLOTS = FurCraftingSlots.SIZE;
    
    private static final int FALLBACK_SIZE = FurCraftingSlots.SIZE;
    
    private final FurCraftingTableBlock.@Nullable FurCraftingTableBlockEntity entity;
    private final Player player;
    private final SimpleContainer input;
    private final SimpleContainer result = new SimpleContainer(1);
    private final ContainerListener inputListener = container -> refreshOutput();
    
    private boolean consuming = false;
    
    public FurCraftingTableMenu(int containerId, @NotNull Inventory playerInventory,
                                FurCraftingTableBlock.@Nullable FurCraftingTableBlockEntity entity) {
        super(FurryBoHeMenuTypeRegister.FUR_CRAFTING_TABLE_MENU_TYPE.get(), containerId);
        this.entity = entity;
        this.player = playerInventory.player;
        this.input = entity != null ? entity.getInput() : new SimpleContainer(FALLBACK_SIZE);
        
        this.addSlot(new Slot(this.input, FurCraftingSlots.LEATHER, 19, 36) {
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return FurCraftingSlots.accepts(FurCraftingSlots.LEATHER, stack);
            }
        });
        this.addSlot(new Slot(this.input, FurCraftingSlots.FILLER, 52, 36) {
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return FurCraftingSlots.accepts(FurCraftingSlots.FILLER, stack);
            }
        });
        this.addSlot(new Slot(this.input, FurCraftingSlots.DYE, 84, 36) {
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return FurCraftingSlots.accepts(FurCraftingSlots.DYE, stack);
            }
        });
        this.addSlot(new Slot(this.result, 0, 146, 36) {
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return false;
            }
            
            @Override
            public boolean mayPickup(@NotNull Player player) {
                return hasValidPlan();
            }
            
            @Override
            public void onTake(@NotNull Player player, @NotNull ItemStack stack) {
                super.onTake(player, stack);
                consumePlan();
            }
        });
        
        this.input.addListener(inputListener);
        
        addPlayerInventory(playerInventory, 8, 84);
        addPlayerHotbar(playerInventory, 8, 142);
        
        refreshOutput();
    }
    
    public FurCraftingTableMenu(int containerId, @NotNull Inventory playerInventory, @NotNull FriendlyByteBuf extraData) {
        this(containerId, playerInventory, resolve(playerInventory, extraData));
    }
    
    private static FurCraftingTableBlock.@Nullable FurCraftingTableBlockEntity resolve(
            @NotNull Inventory inv, @NotNull FriendlyByteBuf data) {
        BlockPos pos = data.readBlockPos();
        BlockEntity be = inv.player.level().getBlockEntity(pos);
        return be instanceof FurCraftingTableBlock.FurCraftingTableBlockEntity table ? table : null;
    }
    
    private @Nullable FurRecipe findRecipe() {
        Level level = player.level();
        return level.getRecipeManager()
                .getRecipeFor(FurryBoHeRecipesRegister.FUR_RECIPE_TYPE_REGISTER.get(), input, level)
                .orElse(null);
    }
    
    private FurRecipe.@Nullable CraftPlan currentPlan() {
        FurRecipe recipe = findRecipe();
        return recipe == null ? null : recipe.plan(input);
    }
    
    private boolean hasValidPlan() {
        return currentPlan() != null;
    }
    
    private void refreshOutput() {
        if (consuming) return;
        
        FurRecipe.CraftPlan plan = currentPlan();
        if (plan == null) {
            if (! result.getItem(0).isEmpty()) {
                result.setItem(0, ItemStack.EMPTY);
            }
            return;
        }
        
        ItemStack desired = plan.result();
        desired.setDamageValue((int) (desired.getMaxDamage() * plan.damageCount()*0.1));
        if (! ItemStack.matches(result.getItem(0), desired)) {
            result.setItem(0, desired);
        }
    }

    private void consumePlan() {
        FurRecipe.CraftPlan plan = currentPlan();
        if (plan == null) return;
        
        consuming = true;
        try {
            input.removeItem(FurCraftingSlots.LEATHER, plan.leather());
            input.removeItem(FurCraftingSlots.FILLER, plan.filler());
            input.removeItem(FurCraftingSlots.DYE, plan.dye());
        } finally {
            consuming = false;
        }
        refreshOutput();
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
        Slot slot = this.slots.get(index);
        if (! slot.hasItem()) return ItemStack.EMPTY;
        
        ItemStack stackInSlot = slot.getItem();
        ItemStack original = stackInSlot.copy();
        
        if (index == SLOT_RESULT) {
            if (! this.moveItemStackTo(stackInSlot, MENU_SLOTS, this.slots.size(), true)) {
                return ItemStack.EMPTY;
            }
            slot.onQuickCraft(stackInSlot, original);
        } else if (index < MENU_SLOTS) {
            if (! this.moveItemStackTo(stackInSlot, MENU_SLOTS, this.slots.size(), true)) {
                return ItemStack.EMPTY;
            }
        } else {
            if (! this.moveItemStackTo(stackInSlot, 0, INPUT_SLOTS, false)) {
                return ItemStack.EMPTY;
            }
        }
        
        if (stackInSlot.isEmpty()) {
            slot.setByPlayer(ItemStack.EMPTY);
        } else {
            slot.setChanged();
        }
        
        if (stackInSlot.getCount() == original.getCount()) {
            return ItemStack.EMPTY;
        }
        
        slot.onTake(player, stackInSlot);
        
        return original;
    }
    
    @Override
    public void removed(@NotNull Player player) {
        super.removed(player);
        this.input.removeListener(inputListener);
    }
    
    @Override
    public boolean stillValid(@NotNull Player player) {
        if (entity == null) {
            return true;
        }
        return ! entity.isRemoved()
                && player.distanceToSqr(entity.getBlockPos().getCenter()) <= 64.0;
    }
}
