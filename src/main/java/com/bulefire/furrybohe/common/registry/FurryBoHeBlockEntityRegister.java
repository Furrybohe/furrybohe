package com.bulefire.furrybohe.common.registry;

import com.bulefire.furrybohe.FurryBoHe;
import com.bulefire.furrybohe.module.material.furcraftingtable.block.FurCraftingTableBlock;
import com.bulefire.furrybohe.module.material.resincollector.block.ResinCollectorBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class FurryBoHeBlockEntityRegister {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES_REGISTER = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, FurryBoHe.MODID);
    
    public static final RegistryObject<BlockEntityType<FurCraftingTableBlock.FurCraftingTableBlockEntity>> FUR_CRAFTING_TABLE_BLOCK_ENTITY_REGISTER =
            BLOCK_ENTITIES_REGISTER.register(
                    "fur_crafting_table_block_entity", () ->
                            BlockEntityType.Builder.of(
                                                   FurCraftingTableBlock.FurCraftingTableBlockEntity::new,
                                                   FurryBoHeBlocksRegister.FUR_CRAFTING_TABLE_REGISTER.get()
                                           )
                                                   .build(null)
            );
    
    public static final RegistryObject<BlockEntityType<?>> RESIN_COLLECTOR_ENTITY_REGISTER =
            BLOCK_ENTITIES_REGISTER.register("resin_collector_entity", () ->
                    BlockEntityType.Builder
                            .of(ResinCollectorBlock.ResinCollectorBlockEntity::new,
                                FurryBoHeBlocksRegister.RESIN_COLLECTOR_REGISTER.get()
                            ).build(null));
}
