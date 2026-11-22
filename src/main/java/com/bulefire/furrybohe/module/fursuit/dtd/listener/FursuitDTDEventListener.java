package com.bulefire.furrybohe.module.fursuit.dtd.listener;

import com.bulefire.furrybohe.FurryBoHe;
import com.bulefire.furrybohe.module.fursuit.dtd.item.AbstractFursuitDTDItem;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = FurryBoHe.MODID, value = Dist.CLIENT)
public class FursuitDTDEventListener {
    @SubscribeEvent
    public static void onRightClickItem(PlayerInteractEvent.@NotNull RightClickItem event) {
        if (AbstractFursuitDTDItem.isWearingDTD(event.getEntity())) {
            event.setCanceled(true);
        }
    }
    
    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.@NotNull RightClickBlock event) {
        if (AbstractFursuitDTDItem.isWearingDTD(event.getEntity())) {
            event.setCanceled(true);
        }
    }
    
    private static final Map<UUID, ItemStack[]> lastArmor = new HashMap<>();
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.@NotNull PlayerTickEvent event) {
        Player player = event.player;
        if (player.level().isClientSide) return;
        Inventory inventory = player.getInventory();

        ItemStack[] currentArmor = new ItemStack[]{
                inventory.armor.get(3),
                inventory.armor.get(2),
                inventory.armor.get(1),
                inventory.armor.get(0)
        };
        ItemStack[] previous = lastArmor.get(player.getUUID());
        if (previous != null) {
            for (int i = 0; i < 4; i++) {
                if (!AbstractFursuitDTDItem.isSameDTD(previous[i], currentArmor[i])) {
                    ItemStack removedItem = previous[i];
                    if (!removedItem.isEmpty()) {
                        if (removedItem.getItem() instanceof AbstractFursuitDTDItem) {
                            if (!previous[i].is(Items.AIR)) {
                                var result = AbstractFursuitDTDItem.getLivingResult(removedItem, player.getRandom());
                                if (result.length == 2) {
                                    if (!inventory.add(result[1]))
                                        player.drop(result[1], false);
                                    if (!inventory.add(result[0]))
                                        player.drop(result[0], false);
                                }
                            }
                        }
                    }
                }
            }
        }
        lastArmor.put(player.getUUID(), currentArmor);
    }
}