package com.bulefire.furrybohe.client.screnn;

import com.bulefire.furrybohe.FurryBoHe;
import com.bulefire.furrybohe.register.FurryBoHeMenuTypeRegister;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.jetbrains.annotations.NotNull;

@Mod.EventBusSubscriber(modid = FurryBoHe.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientScreenRegister {
    @SubscribeEvent
    public static void onClientSetup(@NotNull FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            MenuScreens.register(FurryBoHeMenuTypeRegister.FUR_CRAFTING_TABLE_MENU_TYPE.get(), FurCraftingTableScreen::new);
        });
    }
}
