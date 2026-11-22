package com.bulefire.furrybohe.common.mixin;

import com.bulefire.furrybohe.module.fursuit.dtd.item.AbstractFursuitDTDItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.server.ServerLifecycleHooks;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public class ItemStackSplitMixin {
    @Inject(method = "split(I)Lnet/minecraft/world/item/ItemStack;", at = @At("RETURN"))
    private void furrybohe$splitRETURN(int amount, @NotNull CallbackInfoReturnable<ItemStack> cir) {
        ItemStack result = cir.getReturnValue();
        if (result == null || result.isEmpty()) return;
        if (!(result.getItem() instanceof AbstractFursuitDTDItem dtd)) return;
        if (AbstractFursuitDTDItem.isShaped(result)) return;

        CompoundTag tag = result.getTag();
        if (tag == null || tag.getInt(AbstractFursuitDTDItem.WEAR_TICKS_KEY) <= 0) return;

        ItemStack source = (ItemStack) (Object) this;
        if (!source.isEmpty()) return;

        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        if (server == null || !server.isSameThread()) return;
        
        EquipmentSlot slot = dtd.getEquipmentSlot();
        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            if (player.getItemBySlot(slot) == source) {
                AbstractFursuitDTDItem.settleOnUnequip(result, player);
                return;
            }
        }
    }
}
