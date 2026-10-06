package com.bulefire.furrybohe.datagen;

import com.bulefire.furrybohe.FurryBoHe;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = FurryBoHe.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class FurryBoHeDataGen {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        
        generator.addProvider(event.includeServer(), new FursuitEyes(packOutput));
    }
}
