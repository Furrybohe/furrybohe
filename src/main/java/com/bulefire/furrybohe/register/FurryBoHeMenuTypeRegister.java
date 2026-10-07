package com.bulefire.furrybohe.register;

import com.bulefire.furrybohe.FurryBoHe;
import com.bulefire.furrybohe.menu.FurCraftingTableMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class FurryBoHeMenuTypeRegister {
    public static final DeferredRegister<MenuType<?>> MENU_TYPES_REGISTER = DeferredRegister.create(ForgeRegistries.MENU_TYPES, FurryBoHe.MODID);

    public static final RegistryObject<MenuType<FurCraftingTableMenu>> FUR_CRAFTING_TABLE_MENU_TYPE =
            MENU_TYPES_REGISTER.register("fur_crafting_table_menu_type", () ->
                    IForgeMenuType.create(FurCraftingTableMenu::new));
}
