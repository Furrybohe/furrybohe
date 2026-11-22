package com.bulefire.furrybohe.module.fursuit.dtd.item;

import com.bulefire.furrybohe.FurryBoHe;
import com.bulefire.furrybohe.common.registry.FurryBoHeItemsRegister;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public abstract class AbstractFursuitDTDItem extends ArmorItem {
    public static final String SHAPED_KEY = FurryBoHe.MODID + ":shaped";
    public static final String WEAR_TICKS_KEY = FurryBoHe.MODID + ":wear_ticks";
    private static final int FIXED_TICKS = 200;
    private static final int PER_FAILURE_DAMAGE = 50;
    private static final int RESIN_RETURN_MIN = 1;
    private static final int RESIN_RETURN_MAX_EXCLUSIVE = 4;
    private static final UUID DTD_UUID = UUID.fromString("01a1f98a-1fcc-75f8-b584-edcadfdc5f29");
    
    
    private static final Map<EquipmentSlot, List<Item>> DTD_SLOTS = new HashMap<>();
    private static final Map<UUID, Integer> PENDING_RESIN = new HashMap<>();
    
    public AbstractFursuitDTDItem(ArmorMaterial material, Type type, Properties properties) {
        super(material, type, properties);
    }
    
    @Override
    public void onInventoryTick(@NotNull ItemStack stack, @NotNull Level level, Player player, int slotIndex, int selectedIndex) {
        if (level.isClientSide) return;
        if (isShaped(stack)) return;
        
        if (player.getItemBySlot(getEquipmentSlot()) != stack) {
            CompoundTag tag = stack.getTag();
            if (tag != null && tag.getInt(WEAR_TICKS_KEY) > 0) {
                settleOnUnequip(stack, player);
            }
            return;
        }

        CompoundTag tag = stack.getOrCreateTag();
        int ticks = tag.getInt(WEAR_TICKS_KEY) + 1;

        if (ticks >= FIXED_TICKS) {
            tag.putBoolean(SHAPED_KEY, true);
            tag.remove(WEAR_TICKS_KEY);
        } else {
            tag.putInt(WEAR_TICKS_KEY, ticks);
        }
    }
    
    public static void settleOnUnequip(@NotNull ItemStack stack, @NotNull Player player) {
        CompoundTag tag = stack.getOrCreateTag();
        tag.remove(WEAR_TICKS_KEY);
        int newDamage = stack.getDamageValue() + PER_FAILURE_DAMAGE;
        if (newDamage >= stack.getMaxDamage()) {
            stack.shrink(stack.getCount());
        } else {
            stack.setDamageValue(newDamage);
        }

        int resinCount = player.getRandom().nextInt(RESIN_RETURN_MIN, RESIN_RETURN_MAX_EXCLUSIVE);
        PENDING_RESIN.merge(player.getUUID(), resinCount, Integer::sum);
    }
    
    public static void flushPendingResin(@NotNull Player player) {
        Integer count = PENDING_RESIN.remove(player.getUUID());
        if (count == null || count <= 0) return;
        player.getInventory().placeItemBackInInventory(
                new ItemStack(FurryBoHeItemsRegister.RESIN_REGISTER.get(), count)
        );
    }

    public static boolean isShaped(@NotNull ItemStack stack) {
        CompoundTag tag = stack.getTag();
        return tag != null && tag.getBoolean(SHAPED_KEY);
    }
    
    public static void setShaped(@NotNull ItemStack stack, boolean shaped) {
        if (shaped) {
            stack.getOrCreateTag().putBoolean(SHAPED_KEY, true);
            return;
        }
        
        CompoundTag tag = stack.getOrCreateTag();
        tag.remove(SHAPED_KEY);
        if (tag.isEmpty()) {
            stack.setTag(null);
        }
    }
    
    @Override
    public @NotNull String getDescriptionId(@NotNull ItemStack stack) {
        return getDescriptionId() + (isShaped(stack) ? "_shaped" : "_unshaped");
    }
    
    protected static void registerDTD(@NotNull EquipmentSlot slot, Item item) {
        if (!DTD_SLOTS.containsKey(slot)) DTD_SLOTS.put(slot, new ArrayList<>());
        DTD_SLOTS.get(slot).add(item);
    }
    
    public static boolean isWearingDTD(@NotNull Player player) {
        for (Map.Entry<EquipmentSlot, List<Item>> entry : DTD_SLOTS.entrySet()) {
            for (Item item : entry.getValue()) {
                if (player.getItemBySlot(entry.getKey()).is(item)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public static boolean isWearingDTD(@NotNull EquipmentSlot slot, @NotNull ItemStack itemStack) {
        if (!DTD_SLOTS.containsKey(slot)) return false;
        for (Item item : DTD_SLOTS.get(slot))
            if (itemStack.is(item)) return true;
        return false;
    }
    
    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        var modifiers = super.getAttributeModifiers(slot, stack);
        if (slot == null || !isWearingDTD(slot, stack)) return modifiers;
        Multimap<Attribute, AttributeModifier> newModifiers = HashMultimap.create(modifiers);
        newModifiers.put(
                Attributes.MOVEMENT_SPEED,
                new AttributeModifier(
                        DTD_UUID,
                        "Fursuit freeze effect",
                        -1.0D,
                        AttributeModifier.Operation.MULTIPLY_TOTAL
                )
        );
        return newModifiers;
    }
}
