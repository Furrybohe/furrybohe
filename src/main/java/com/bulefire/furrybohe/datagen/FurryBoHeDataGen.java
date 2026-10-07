package com.bulefire.furrybohe.datagen;

import com.bulefire.furrybohe.FurryBoHe;
import com.bulefire.furrybohe.datagen.recipes.Fur;
import com.bulefire.furrybohe.datagen.recipes.FursuitEyes;
import com.bulefire.furrybohe.datagen.tags.ColorFur;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

@Mod.EventBusSubscriber(modid = FurryBoHe.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class FurryBoHeDataGen {
    @SubscribeEvent
    public static void gatherData(@NotNull GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        
        generator.addProvider(event.includeServer(), new FurryBoHeRecipes(packOutput));
        
        // empty for contentsGetter
        BlockTagsProvider blockTagsProvider = new BlockTagsProvider(packOutput, lookupProvider, FurryBoHe.MODID, existingFileHelper) {
            @Override
            protected void addTags(HolderLookup.@NotNull Provider provider) {}
        };
        generator.addProvider(event.includeServer(), blockTagsProvider);
        generator.addProvider(event.includeServer(), new ColorFur(
                packOutput,
                lookupProvider,
                blockTagsProvider.contentsGetter(),
                FurryBoHe.MODID,
                existingFileHelper
        ));
    }
    
    /**
     * 唯一的配方 provider。
     * <p>{@code RecipeProvider#getName()} 是 final 的，任何子类都返回 "Recipes"，
     * 因此不能注册两个 RecipeProvider 子类（DataGenerator 会以 Duplicate provider 报错）；
     * 多个配方集合只能合并进同一个 provider，各自在自己的类里提供静态 build 方法。
     */
    public static class FurryBoHeRecipes extends RecipeProvider {
        public FurryBoHeRecipes(PackOutput output) {
            super(output);
        }
        
        @Override
        protected void buildRecipes(@NotNull Consumer<FinishedRecipe> consumer) {
            FursuitEyes.build(consumer, RecipeProvider::has);
            Fur.build(consumer);
        }
    }
}
