package com.bulefire.furrybohe.register;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class FurryBoHeCreativeTabsRegister {
    public static final DeferredRegister<CreativeModeTab> TABS_REGISTER = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, "your_mod_id");
    
    public static final Supplier<CreativeModeTab> FURRY_BO_HE_TAB =
            TABS_REGISTER.register("furry_bo_he_tab",
                    () -> CreativeModeTab.builder()
                            .icon(() -> new ItemStack(FurryBoHeItemsRegister.FURSUIT_HEAD_REGISTER.get()))
                            .title(Component.translatable("tab.furrybohe.main_tab"))
                            .displayItems(FurryBoHeCreativeTabsRegister::registerAll)
                            .build());
    
    private static void registerAll(CreativeModeTab.ItemDisplayParameters parameters, CreativeModeTab.Output output) {
        FurryBoHeItemsRegister.registerAllItems(parameters, output);
    }
}
