package com.bulefire.furrybohe.menu;

import com.bulefire.furrybohe.block.FurCraftingTableBlock;
import com.bulefire.furrybohe.item.FurryBoHeItems;
import com.bulefire.furrybohe.recipe.FurRecipe;
import com.bulefire.furrybohe.register.FurryBoHeMenuTypeRegister;
import com.bulefire.furrybohe.register.FurryBoHeRecipesRegister;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.ContainerListener;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.Tags;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FurCraftingTableMenu extends AbstractContainerMenu {
    private static final int INPUT_LEATHER = 0;
    private static final int INPUT_FILLER = 1;
    private static final int INPUT_DYE = 2;
    private static final int SLOT_RESULT = 3;
    private static final int MENU_SLOTS = 4;
    private static final int INPUT_SLOTS = 3;
    
    private final FurCraftingTableBlock.FurCraftingTableBlockEntity entity;
    private final Player player;
    private final SimpleContainer input;
    private final SimpleContainer result = new SimpleContainer(1);
    private final ContainerListener inputListener = container -> updateResult();
    
    private int pendingLeather = 0;
    private int pendingFiller = 0;
    private int pendingDye = 0;
    
    public FurCraftingTableMenu(int containerId, @NotNull Inventory playerInventory, FurCraftingTableBlock.@NotNull FurCraftingTableBlockEntity entity) {
        super(FurryBoHeMenuTypeRegister.FUR_CRAFTING_TABLE_MENU_TYPE.get(), containerId);
        this.entity = entity;
        this.player = playerInventory.player;
        this.input = entity.getInput();
        
        this.addSlot(new Slot(this.input, INPUT_LEATHER, 19, 36) {
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return stack.is(Items.LEATHER);
            }
        });
        this.addSlot(new Slot(this.input, INPUT_FILLER, 52, 36) {
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return stack.is(ItemTags.WOOL) || stack.is(FurryBoHeItems.COTTON);
            }
        });
        this.addSlot(new Slot(this.input, INPUT_DYE, 84, 36) {
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return stack.is(Tags.Items.DYES);
            }
        });
        this.addSlot(new Slot(this.result, 0, 146, 36) {
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return false;
            }
            
            @Override
            public void onTake(@NotNull Player player, @NotNull ItemStack stack) {
                super.onTake(player, stack);
                consumePending();
            }
        });
        
        this.input.addListener(inputListener);
        
        addPlayerInventory(playerInventory, 8, 84);
        addPlayerHotbar(playerInventory, 8, 142);
        
        updateResult();
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
    
    private @Nullable FurRecipe findRecipe() {
        Level level = player.level();
        return level.getRecipeManager()
                .getRecipeFor(FurryBoHeRecipesRegister.FUR_RECIPE_TYPE_REGISTER.get(), input, level)
                .orElse(null);
    }
    
    private void updateResult() {
        if (player.level().isClientSide()) return;
        
        FurRecipe recipe = findRecipe();
        if (recipe == null) {
            if (! result.getItem(0).isEmpty()) {
                result.setItem(0, ItemStack.EMPTY);
            }
            clearPending();
            return;
        }
        
        int fillerCost = recipe.fillerCost(input.getItem(INPUT_FILLER));
        if (fillerCost <= 0) {
            if (! result.getItem(0).isEmpty()) {
                result.setItem(0, ItemStack.EMPTY);
            }
            clearPending();
            return;
        }
        
        ItemStack desired = recipe.getResultItem(player.level().registryAccess()).copy();
        if (! ItemStack.matches(result.getItem(0), desired)) {
            result.setItem(0, desired);
        }
        
        pendingLeather = 1;
        pendingDye = 1;
        pendingFiller = fillerCost;
    }
    
    private void clearPending() {
        pendingLeather = 0;
        pendingDye = 0;
        pendingFiller = 0;
    }
    
    private void consumePending() {
        if (pendingLeather > 0) input.removeItem(INPUT_LEATHER, pendingLeather);
        if (pendingDye > 0) input.removeItem(INPUT_DYE, pendingDye);
        if (pendingFiller > 0) input.removeItem(INPUT_FILLER, pendingFiller);
        clearPending();
        updateResult();
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
        
        // 结果槽 shift 点击：搬进背包（onTake 由 moveItemStackTo 之后的逻辑触发不了，需手动走一遍）
        if (index == SLOT_RESULT) {
            ItemStack taken = slot.getItem().copy();
            if (! player.level().isClientSide()) {
                if (! this.moveItemStackTo(slot.getItem(), MENU_SLOTS, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
                slot.onTake(player, taken);   // 手动触发扣料
            }
            return taken;
        }
        
        ItemStack stackInSlot = slot.getItem();
        ItemStack result = stackInSlot.copy();
        
        if (index < MENU_SLOTS) {
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
        
        if (stackInSlot.getCount() == result.getCount()) {
            return ItemStack.EMPTY;
        }
        
        slot.onTake(player, stackInSlot);
        
        return result;
    }
    
    @Override
    public void removed(@NotNull Player player) {
        super.removed(player);
        this.input.removeListener(inputListener);
    }
    
    @Override
    public boolean stillValid(@NotNull Player player) {
        return entity != null && !entity.isRemoved()
                && player.distanceToSqr(entity.getBlockPos().getCenter()) <= 64.0;
    }
}
