package com.bulefire.furrybohe.recipe;

import com.bulefire.furrybohe.register.FurryBoHeRecipeSerializerRegister;
import com.bulefire.furrybohe.register.FurryBoHeRecipesRegister;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.crafting.CraftingHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class FurRecipe implements Recipe<Container> {
    private final List<Ingredient> inputs; // size == 3
    private final ItemStack result;
    
    public FurRecipe(List<Ingredient> inputs, ItemStack result) {
        this.inputs = inputs;
        this.result = result;
    }
    
    @Override
    public boolean matches(@NotNull Container in, @NotNull Level level) {
        for (int i = 0; i < 3; i++) {
            if (!inputs.get(i).test(in.getItem(i))) return false;
        }
        return true;
    }
    
    @Override
    public @NotNull ItemStack assemble(@NotNull Container container, @NotNull RegistryAccess ra) {
        return result.copy();
    }
    
    @Override
    public boolean canCraftInDimensions(int w, int h) {
        return true;
    }
    
    @Override
    public @NotNull ItemStack getResultItem(@NotNull RegistryAccess ra) {
        return result;
    }
    
    @Override
    public @NotNull ResourceLocation getId() {
        return new ResourceLocation("furrybohe", "fur_recipe");
    }
    
    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return FurryBoHeRecipeSerializerRegister.FUR_RECIPE_SERIALIZER.get();
    }
    
    @Override
    public @NotNull RecipeType<?> getType() {
        return FurryBoHeRecipesRegister.FUR_RECIPE_TYPE_REGISTER.get();
    }
    
    public List<Ingredient> getInputs() {
        return inputs;
    }
    
    public static class Serializer implements RecipeSerializer<FurRecipe> {
        @Override
        public @NotNull FurRecipe fromJson(@NotNull ResourceLocation rl, @NotNull JsonObject json) {
            JsonArray inputArray = json.getAsJsonArray("inputs");
            List<Ingredient> inputs = new ArrayList<>();
            for (JsonElement e : inputArray) {
                inputs.add(Ingredient.fromJson(e));
            }
            ItemStack result = CraftingHelper.getItemStack(json.getAsJsonObject("result"), true);
            return new FurRecipe(inputs, result);
        }
        
        @Override
        public @Nullable FurRecipe fromNetwork(@NotNull ResourceLocation rl, @NotNull FriendlyByteBuf buf) {
            List<Ingredient> inputs = new ArrayList<>();
            for (int i = 0; i < 3; i++) {
                inputs.add(Ingredient.fromNetwork(buf));
            }
            ItemStack result = buf.readItem();
            return new FurRecipe(inputs, result);
        }
        
        @Override
        public void toNetwork(@NotNull FriendlyByteBuf buf, @NotNull FurRecipe fr) {
            for (Ingredient ing : fr.getInputs()) {
                ing.toNetwork(buf);
            }
            buf.writeItem(fr.getResultItem(RegistryAccess.EMPTY));
        }
    }
}