package com.bulefire.furrybohe.datagen;

import com.bulefire.furrybohe.FurryBoHe;
import com.bulefire.furrybohe.datagen.recipes.FursuitEyes;
import com.bulefire.furrybohe.datagen.tags.ColorFur;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(modid = FurryBoHe.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class FurryBoHeDataGen {
    @SubscribeEvent
    public static void gatherData(@NotNull GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        
        generator.addProvider(event.includeServer(), new FursuitEyes(packOutput));
        
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
}
