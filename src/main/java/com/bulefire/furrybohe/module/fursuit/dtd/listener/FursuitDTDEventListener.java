package com.bulefire.furrybohe.module.fursuit.dtd.listener;

import com.bulefire.furrybohe.FurryBoHe;
import com.bulefire.furrybohe.module.fursuit.dtd.item.AbstractFursuitDTDItem;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

@Mod.EventBusSubscriber(modid = FurryBoHe.MODID)
public class FursuitDTDEventListener {
    private static boolean isHandsFrozen(@NotNull Player player) {
        return AbstractFursuitDTDItem.isWearingDTD(player);
    }

    @SubscribeEvent
    public static void onRightClickItem(PlayerInteractEvent.@NotNull RightClickItem event) {
        if (isHandsFrozen(event.getEntity())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.@NotNull RightClickBlock event) {
        if (isHandsFrozen(event.getEntity())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onLeftClickBlock(PlayerInteractEvent.@NotNull LeftClickBlock event) {
        if (isHandsFrozen(event.getEntity())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onEntityInteract(PlayerInteractEvent.@NotNull EntityInteract event) {
        if (isHandsFrozen(event.getEntity())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onEntityInteractSpecific(PlayerInteractEvent.@NotNull EntityInteractSpecific event) {
        if (isHandsFrozen(event.getEntity())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onAttackEntity(@NotNull AttackEntityEvent event) {
        if (isHandsFrozen(event.getEntity())) {
            event.setCanceled(true);
        }
    }
    
    @SubscribeEvent
    public static void onPlayerTickEnd(TickEvent.@NotNull PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        if (event.player.level().isClientSide) return;
        AbstractFursuitDTDItem.flushPendingResin(event.player);
    }

    @SubscribeEvent
    public static void onPlayerLoggedOut(PlayerEvent.@NotNull PlayerLoggedOutEvent event) {
        AbstractFursuitDTDItem.flushPendingResin(event.getEntity());
    }
}
