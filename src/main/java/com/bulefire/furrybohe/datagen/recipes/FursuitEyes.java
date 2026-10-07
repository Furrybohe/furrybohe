package com.bulefire.furrybohe.datagen.recipes;

import com.bulefire.furrybohe.register.FurryBoHeItemsRegister;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

public class FursuitEyes {
    public static void build(Consumer<FinishedRecipe> consumer, Function<ItemLike, CriterionTriggerInstance> has) {
        for (DyeColor color : DyeColor.values()) {
            Item dye = DyeItem.byColor(color);
            Item eye = FurryBoHeItemsRegister.FURSUIT_EYES_REGISTER.get(color.getName()).get();
            Objects.requireNonNull(eye);
            
            ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, eye)
                    .requires(dye)
                    .requires(Items.PAPER)
                    .requires(FurryBoHeItemsRegister.RESIN_FRAGMENT_REGISTER.get())
                    .unlockedBy("has_" + color + "_dye", has.apply(dye))
                    .save(consumer, new ResourceLocation("furrybohe", "fursuit_" + color.getName() + "_eyes"));
        }
    }
}
