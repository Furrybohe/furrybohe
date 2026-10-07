package com.bulefire.furrybohe.client.screnn;

import com.bulefire.furrybohe.FurryBoHe;
import com.bulefire.furrybohe.menu.FurCraftingTableMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class FurCraftingTableScreen extends AbstractContainerScreen<FurCraftingTableMenu> {
    private static final ResourceLocation BG_TEXTURE = new ResourceLocation(FurryBoHe.MODID, "textures/gui/fur_crafting_table.png");
    
    public FurCraftingTableScreen(FurCraftingTableMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
         this.imageWidth = 176;
         this.imageHeight = 166;
    }
    
    @Override
    protected void renderBg(@NotNull GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        guiGraphics.blit(
                BG_TEXTURE,
                this.leftPos,
                this.topPos,
                0, 0,
                this.imageWidth,
                this.imageHeight
        );
    }
    
    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }
}
