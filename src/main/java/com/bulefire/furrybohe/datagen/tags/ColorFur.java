package com.bulefire.furrybohe.datagen.tags;

import com.bulefire.furrybohe.register.FurryBoHeItemsRegister;
import com.bulefire.furrybohe.tags.FurryBoHeTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class ColorFur extends ItemTagsProvider {
    public ColorFur(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider,
                               CompletableFuture<TagLookup<Block>> blockTags, String modId,
                               ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, blockTags, modId, existingFileHelper);
    }
    
    @Override
    protected void addTags(HolderLookup.@NotNull Provider provider) {
        var tag = this.tag(FurryBoHeTags.COLOR_FUR);
        FurryBoHeItemsRegister.COLOR_FUR_REGISTER.values().stream()
                .map(RegistryObject::get)
                .forEach(tag::add);
    }
}
