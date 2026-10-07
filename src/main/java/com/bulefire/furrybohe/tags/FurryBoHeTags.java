package com.bulefire.furrybohe.tags;

import com.bulefire.furrybohe.FurryBoHe;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class FurryBoHeTags {
    public static final TagKey<Item> COLOR_FUR =
            TagKey.create(Registries.ITEM,
                          ResourceLocation.fromNamespaceAndPath(FurryBoHe.MODID, "color_fur"));
}
