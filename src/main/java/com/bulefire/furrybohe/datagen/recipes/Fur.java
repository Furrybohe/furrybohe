package com.bulefire.furrybohe.datagen.recipes;

import com.bulefire.furrybohe.FurryBoHe;
import com.bulefire.furrybohe.recipe.FurRecipe;
import com.bulefire.furrybohe.register.FurryBoHeItemsRegister;
import com.bulefire.furrybohe.register.FurryBoHeRecipeSerializerRegister;
import com.google.gson.JsonObject;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class Fur {
    public static void build(Consumer<FinishedRecipe> consumer) {
        Ingredient leather = Ingredient.of(Items.LEATHER);
        Ingredient cotton = Ingredient.of(FurryBoHeItemsRegister.COTTON_REGISTER.get());
        Ingredient wool = Ingredient.of(ItemTags.WOOL);
        
        for (DyeColor color : DyeColor.values()) {
            Item dye = DyeItem.byColor(color);
            Item fur = FurryBoHeItemsRegister.COLOR_FUR_REGISTER.get(color.getName()).get();
            
            consumer.accept(new FurFinishedRecipe(
                    new ResourceLocation(FurryBoHe.MODID, color.getName() + "_fur"),
                    leather, cotton, wool, Ingredient.of(dye),
                    FurRecipe.DEFAULT_COTTON_COUNT, FurRecipe.DEFAULT_WOOL_COUNT,
                    new ItemStack(fur)
            ));
        }
    }
    
    public record FurFinishedRecipe(ResourceLocation id, Ingredient leather, Ingredient cotton, Ingredient wool,
                                    Ingredient dye, int cottonCount, int woolCount, ItemStack result
    ) implements FinishedRecipe {
        
        @Override
        public void serializeRecipeData(@NotNull JsonObject json) {
            json.add("leather", leather.toJson());
            json.add("cotton", cotton.toJson());
            json.add("wool", wool.toJson());
            json.add("dye", dye.toJson());
            json.addProperty("cotton_count", cottonCount);
            json.addProperty("wool_count", woolCount);
            
            JsonObject resultJson = new JsonObject();
            ResourceLocation key = ForgeRegistries.ITEMS.getKey(result.getItem());
            resultJson.addProperty("item", key == null ? "minecraft:air" : key.toString());
            if (result.getCount() > 1) {
                resultJson.addProperty("count", result.getCount());
            }
            json.add("result", resultJson);
        }
        
        @Override
        public @NotNull ResourceLocation getId() {
            return id;
        }
        
        @Override
        public @NotNull RecipeSerializer<?> getType() {
            return FurryBoHeRecipeSerializerRegister.FUR_RECIPE_SERIALIZER.get();
        }
        
        @Override
        public @Nullable JsonObject serializeAdvancement() {
            return null;
        }
        
        @Override
        public @NotNull ResourceLocation getAdvancementId() {
            return new ResourceLocation(id.getNamespace(), "recipes/" + id.getPath());
        }
    }
}
