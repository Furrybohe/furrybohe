package com.bulefire.furrybohe.module.fursuit.dtd.item;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorMaterials;

public class FursuitCurvedLegDTDItem extends AbstractFursuitDTDItem {
    public FursuitCurvedLegDTDItem(Properties properties) {
        super(ArmorMaterials.LEATHER, Type.LEGGINGS, properties);
        registerDTD(EquipmentSlot.LEGS, this);
    }
}
