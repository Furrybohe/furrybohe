package com.bulefire.furrybohe.datagen.recipes;

import com.bulefire.furrybohe.register.FurryBoHeItemsRegister;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Consumer;

public class FursuitEyes extends RecipeProvider implements IConditionBuilder {
    public FursuitEyes(PackOutput output) {
        super(output);
    }
    
    @Override
    protected void buildRecipes(@NotNull Consumer<FinishedRecipe> consumer) {
        for (DyeColor color : DyeColor.values()) {
            Item dye = DyeItem.byColor(color);
            Item eye = FurryBoHeItemsRegister.FURSUIT_EYES_REGISTER.get(color.getName()).get();
            Objects.requireNonNull(eye);
            
            ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, eye)
                    .requires(dye)
                    .requires(Items.PAPER)
                    .requires(FurryBoHeItemsRegister.RESIN_FRAGMENT_REGISTER.get())
                    .unlockedBy("has_" + color + "_dye", has(dye))
                    .save(consumer, "fursuit_" + color.getName() + "_eyes");
        }
    }
}
