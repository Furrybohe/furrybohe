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
    private static final UUID DTD_UUID = UUID.fromString("01a1f98a-1fcc-75f8-b584-edcadfdc5f29");
    
    
    private static final Map<EquipmentSlot, List<Item>> DTD_SLOTS = new HashMap<>();
    
    public AbstractFursuitDTDItem(ArmorMaterial material, Type type, Properties properties) {
        super(material, type, properties);
    }
    
    @Override
    public void onInventoryTick(@NotNull ItemStack stack, @NotNull Level level, Player player, int slotIndex, int selectedIndex) {
        if (level.isClientSide) return;
        if (isShaped(stack)) return;
        if (player.getItemBySlot(getEquipmentSlot()) != stack) return;
        
        CompoundTag tag = stack.getOrCreateTag();
        int ticks = tag.getInt(WEAR_TICKS_KEY) + 1;
        
        if (ticks >= FIXED_TICKS) {
            tag.putBoolean(SHAPED_KEY, true);
            tag.remove(WEAR_TICKS_KEY);
        } else {
            tag.putInt(WEAR_TICKS_KEY, ticks);
        }
    }
    
    public static boolean isShaped(@NotNull ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();
        return tag.getBoolean(SHAPED_KEY);
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
    
    @Contract("_, _ -> new")
    public static @NotNull ItemStack @NotNull [] getLivingResult(@NotNull ItemStack item, RandomSource random) {
        CompoundTag tag = item.getOrCreateTag();
        if (tag.contains(SHAPED_KEY) && tag.getBoolean(SHAPED_KEY)) return new ItemStack[]{item};
        int ticks = tag.getInt(WEAR_TICKS_KEY);
        if (ticks >= FIXED_TICKS) return new ItemStack[]{item};
        
        item.getOrCreateTag().putInt(WEAR_TICKS_KEY, 0);
        item.setDamageValue(50);
        int resinCount = random.nextInt(1,4);
        return new ItemStack[]{item, new ItemStack(FurryBoHeItemsRegister.RESIN_REGISTER.get(), resinCount)};
    }
    
    public static boolean isSameDTD(@NotNull ItemStack stack1, @NotNull ItemStack stack2) {
        return stack1.getItem() instanceof AbstractFursuitDTDItem &&
                stack2.getItem() instanceof AbstractFursuitDTDItem &&
                stack1.getDamageValue() == stack2.getDamageValue();
    }
    
    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        var modifiers = super.getAttributeModifiers(slot, stack);
        if (!isWearingDTD(slot, stack)) return modifiers;
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
