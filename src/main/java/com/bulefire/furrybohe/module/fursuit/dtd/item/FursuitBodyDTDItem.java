package com.bulefire.furrybohe.module.fursuit.dtd.item;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorMaterials;

public class FursuitBodyDTDItem extends AbstractFursuitDTDItem {
    public FursuitBodyDTDItem(Properties properties) {
        super(ArmorMaterials.LEATHER, Type.CHESTPLATE, properties);
        registerDTD(EquipmentSlot.CHEST, this);
    }
}
