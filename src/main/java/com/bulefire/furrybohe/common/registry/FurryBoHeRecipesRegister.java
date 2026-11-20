package com.bulefire.furrybohe.common.registry;

import com.bulefire.furrybohe.FurryBoHe;
import com.bulefire.furrybohe.module.material.furcraftingtable.recipe.FurRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class FurryBoHeRecipesRegister {
    public static final DeferredRegister<RecipeType<?>> RECIPES_REGISTER = DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, FurryBoHe.MODID);
    
    public static final RegistryObject<RecipeType<FurRecipe>> FUR_RECIPE_TYPE_REGISTER =
            RECIPES_REGISTER.register(
                    "fur_recipe", () ->
                            RecipeType.simple(ResourceLocation.fromNamespaceAndPath(FurryBoHe.MODID, "fur_recipe"))
            );
}
