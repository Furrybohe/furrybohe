package com.bulefire.furrybohe;

import com.bulefire.furrybohe.register.*;
import com.mojang.logging.LogUtils;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(FurryBoHe.MODID)
public class FurryBoHe {
    
    // Define mod id in a common place for everything to reference
    public static final String MODID = "furrybohe";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();
    
    public FurryBoHe() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);
        
        FurryBoHeItemsRegister.ITEMS_REGISTER.register(modEventBus);
        FurryBoHeBlocksRegister.BLOCKS_REGISTER.register(modEventBus);
        FurryBoHeCreativeTabsRegister.TABS_REGISTER.register(modEventBus);
        FurryBoHePoiTypesRegister.POI_TYPES_REGISTER.register(modEventBus);
        FurryBoHeVillagerProfessionRegister.PROFESSIONS_REGISTER.register(modEventBus);
        FurryBoHeMenuTypeRegister.MENU_TYPES_REGISTER.register(modEventBus);
        FurryBoHeBlockEntityRegister.BLOCK_ENTITIES_REGISTER.register(modEventBus);
        FurryBoHeRecipesRegister.RECIPES_REGISTER.register(modEventBus);
        FurryBoHeRecipeSerializerRegister.RECIPE_SERIALIZERS_REGISTER.register(modEventBus);
        
        // Register our mod's ForgeConfigSpec so that Forge can create and load the config file for us
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }
    
    private void commonSetup(final FMLCommonSetupEvent event) {
    
    }
    
    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Do something when the server starts
    }
}
