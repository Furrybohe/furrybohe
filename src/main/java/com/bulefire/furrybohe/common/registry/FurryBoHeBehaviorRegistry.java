package com.bulefire.furrybohe.common.registry;

import com.bulefire.furrybohe.FurryBoHe;
import com.bulefire.furrybohe.module.material.resincollector.behavior.ResinCollectorDispenserItemBehavior;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import org.jetbrains.annotations.NotNull;

@Mod.EventBusSubscriber(modid = FurryBoHe.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class FurryBoHeBehaviorRegistry {
    
    @SubscribeEvent
    public static void registerDispenserBlock(@NotNull FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            DispenserBlock.registerBehavior(
                    Items.GLASS_BOTTLE,
                    new ResinCollectorDispenserItemBehavior()
            );
            DispenserBlock.registerBehavior(
                    Items.SLIME_BALL,
                    new ResinCollectorDispenserItemBehavior()
            );
        });
    }
}
