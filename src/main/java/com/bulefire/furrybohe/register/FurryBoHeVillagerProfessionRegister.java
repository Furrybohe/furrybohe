package com.bulefire.furrybohe.register;

import com.bulefire.furrybohe.FurryBoHe;
import com.google.common.collect.ImmutableSet;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class FurryBoHeVillagerProfessionRegister {
    public static final DeferredRegister<VillagerProfession> PROFESSIONS_REGISTER = DeferredRegister.create(ForgeRegistries.VILLAGER_PROFESSIONS, FurryBoHe.MODID);

    public static final RegistryObject<VillagerProfession> FURSUIT_MAKER_PROFESSION_REGISTER =
            PROFESSIONS_REGISTER.register("fursuit_maker_profession", () -> new VillagerProfession(
                    "fursuit_maker",
                    holder -> holder.get() == FurryBoHePoiTypesRegister.FURSUIT_MAKER_POI_REGISTER.get(),
                    holder -> holder.get() == FurryBoHePoiTypesRegister.FURSUIT_MAKER_POI_REGISTER.get(),
                    ImmutableSet.of(),
                    ImmutableSet.of(),
                    null
            ));
    
    public static final RegistryObject<VillagerProfession> FUR_TRADER_PROFESSION_REGISTER =
            PROFESSIONS_REGISTER.register("fur_trader_profession", () -> new VillagerProfession(
                    "fur_trader",
                    holder -> holder.get() == FurryBoHePoiTypesRegister.FUR_TRADER_POI_REGISTER.get(),
                    holder -> holder.get() == FurryBoHePoiTypesRegister.FUR_TRADER_POI_REGISTER.get(),
                    ImmutableSet.of(),
                    ImmutableSet.of(),
                    null
            ));
}
