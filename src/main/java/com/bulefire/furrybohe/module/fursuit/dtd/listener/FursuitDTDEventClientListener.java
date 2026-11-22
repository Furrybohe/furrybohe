package com.bulefire.furrybohe.module.fursuit.dtd.listener;

import com.bulefire.furrybohe.FurryBoHe;
import com.bulefire.furrybohe.module.fursuit.dtd.item.AbstractFursuitDTDItem;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = FurryBoHe.MODID, value = Dist.CLIENT)
public class FursuitDTDEventClientListener {
    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.screen != null) return;
        
        if (AbstractFursuitDTDItem.isWearingDTD(mc.player)) {
            mc.options.keyUp.setDown(false);
            mc.options.keyDown.setDown(false);
            mc.options.keyLeft.setDown(false);
            mc.options.keyRight.setDown(false);
            mc.options.keyJump.setDown(false);
            mc.options.keyShift.setDown(false);
            mc.options.keySprint.setDown(false);
            
            mc.options.keyAttack.setDown(false);
            mc.options.keyUse.setDown(false);
        }
    }
}
