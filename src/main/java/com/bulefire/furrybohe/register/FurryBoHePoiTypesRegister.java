package com.bulefire.furrybohe.register;

import com.bulefire.furrybohe.FurryBoHe;
import com.bulefire.furrybohe.block.FurryBoHeBlocks;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Set;

public class FurryBoHePoiTypesRegister {
    public static final DeferredRegister<PoiType> POI_TYPES_REGISTER = DeferredRegister.create(ForgeRegistries.POI_TYPES, FurryBoHe.MODID);
    
    /** 装师 poi 注册 */
    public static final RegistryObject<PoiType> FURSUIT_MAKER_POI_REGISTER =
            POI_TYPES_REGISTER.register(
                    "fursuit_maker_poi", () ->
                            new PoiType(
                                    Set.of(FurryBoHeBlocksRegister.FURSUIT_WORKSTATION_REGISTER.get().defaultBlockState()),
                                    1,
                                    5
                            )
                    );
    
    /** 毛布商 poi 注册 */
    public static final RegistryObject<PoiType> FUR_TRADER_POI_REGISTER =
            POI_TYPES_REGISTER.register(
                    "fur_trader_poi", () ->
                            new PoiType(
                                    Set.of(FurryBoHeBlocksRegister.FUR_CRAFTING_TABLE_REGISTER.get().defaultBlockState()),
                                    1,
                                    5
                            )
                    );
}
