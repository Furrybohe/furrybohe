package com.bulefire.furrybohe.module.villager.fursuitmaker.trade;

import com.bulefire.furrybohe.common.registry.FurryBoHeItemsRegister;
import com.bulefire.furrybohe.module.document.blueprints.item.FursuitBlueprintsItem;
import com.bulefire.furrybohe.module.fursuit.dtd.item.FursuitDTDItem;
import com.bulefire.furrybohe.module.villager.util.RegistryUtil;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class FursuitMakerTrades {
    public static void registerFursuitMakerTrade(@NotNull Int2ObjectMap<List<VillagerTrades.ItemListing>> trades) {
        // TODO: 兽爪版型 30~60% 随机（版型属性由物品状态表示，待接入）
        trades.get(1).add(RegistryUtil.offer(
                new ItemStack(Items.EMERALD, 20),
                new ItemStack(Items.PAPER, 1),
                new ItemStack(FurryBoHeItemsRegister.FURSUIT_PAW_CLAW_PATTERN_REGISTER.get(), 1)
        ));
        
        trades.get(1).add((trader, rand) -> {
            DyeColor[] colors = DyeColor.values();
            DyeColor randomColor = colors[rand.nextInt(colors.length)];
            ItemStack eyesStack = new ItemStack(FurryBoHeItemsRegister.FURSUIT_EYES_REGISTER.get(randomColor.getName()).get(), 1);
            
            return RegistryUtil.createDefaultOffer(new ItemStack(Items.EMERALD, 15), eyesStack);
        });
        
        trades.get(1).add((trader, rand) -> {
            ItemStack bp = new ItemStack(FurryBoHeItemsRegister.FURSUIT_BLUEPRINTS_REGISTER.get(), 1);
            
            bp.getOrCreateTag().putString(FursuitBlueprintsItem.STATUS_KEY, FursuitBlueprintsItem.EMPTY);
            
            return RegistryUtil.createDefaultOffer(new ItemStack(Items.EMERALD, 7), bp);
        });
        
        trades.get(1).add(RegistryUtil.offer(
                new ItemStack(Items.EMERALD, 12),
                new ItemStack(FurryBoHeItemsRegister.NEEDLE_BOX_REGISTER.get(), 1)
        ));
        
        // TODO: 无拓展装师手册（拓展与否由状态表示，待接入）
        trades.get(2).add(RegistryUtil.offer(
                new ItemStack(Items.EMERALD, 13),
                new ItemStack(FurryBoHeItemsRegister.FURSUIT_MANUAL_REGISTER.get(), 1)
        ));
        
        // TODO: 尾巴版型 50~80% 随机（版型属性由物品状态表示，待接入）
        trades.get(2).add(RegistryUtil.offer(
                new ItemStack(Items.EMERALD, 25),
                new ItemStack(Items.PAPER, 1),
                new ItemStack(FurryBoHeItemsRegister.FURSUIT_TAIL_PATTERN_REGISTER.get(), 1)
        ));
        
        // TODO: 树脂头骨 50~80% 随机（头骨属性由物品状态表示，待接入）
        trades.get(2).add(RegistryUtil.offer(
                new ItemStack(Items.EMERALD, 34),
                new ItemStack(FurryBoHeItemsRegister.FURSUIT_RESIN_SKULL_REGISTER.get(), 1)
        ));
        
        // TODO: 树脂兽脚底 50~80% 随机（兽脚底属性由物品状态表示，待接入）
        trades.get(2).add(RegistryUtil.offer(
                new ItemStack(Items.EMERALD, 14),
                new ItemStack(FurryBoHeItemsRegister.FURSUIT_RESIN_FOOT_SOLE_REGISTER.get(), 1)
        ));
        
        trades.get(2).add(RegistryUtil.offer(
                new ItemStack(Items.EMERALD, 16),
                new ItemStack(FurryBoHeItemsRegister.CARVING_KNIFE_REGISTER.get(), 1)
        ));
        
        trades.get(2).add(RegistryUtil.offer(
                new ItemStack(Items.EMERALD, 7),
                new ItemStack(FurryBoHeItemsRegister.FUR_BRUSH_REGISTER.get(), 1)
        ));
        
        trades.get(3).add((trader, rand) -> {
            ItemStack dtd = new ItemStack(FurryBoHeItemsRegister.FURSUIT_BODY_DTD_REGISTER.get(), 1);
            
            FursuitDTDItem.setShaped(dtd, false);
            
            return RegistryUtil.createDefaultOffer(new ItemStack(Items.EMERALD, 58), dtd);
        });
        
        trades.get(3).add((trader, rand) -> {
            ItemStack dtd = new ItemStack(FurryBoHeItemsRegister.FURSUIT_STRAIGHT_LEG_DTD_REGISTER.get(), 1);
            
            FursuitDTDItem.setShaped(dtd, false);
            
            return RegistryUtil.createDefaultOffer(new ItemStack(Items.EMERALD, 54), dtd);
        });
        
        trades.get(3).add(RegistryUtil.offer(
                new ItemStack(Items.EMERALD, 42),
                new ItemStack(FurryBoHeItemsRegister.FURSUIT_BONE_SKULL_REGISTER.get(), 1)
        ));
        
        trades.get(3).add(RegistryUtil.offer(
                new ItemStack(Items.EMERALD, 19),
                new ItemStack(FurryBoHeItemsRegister.FURSUIT_CLEANER_REGISTER.get(), 1)
        ));
        
        trades.get(3).add(RegistryUtil.offer(
                new ItemStack(Items.EMERALD, 8),
                new ItemStack(FurryBoHeItemsRegister.CLEANING_BRUSH_REGISTER.get(), 1)
        ));
        
        // TODO: 海绵头骨 70~100% 随机（头骨属性由物品状态表示，待接入）
        trades.get(4).add(RegistryUtil.offer(
                new ItemStack(Items.EMERALD, 46),
                new ItemStack(FurryBoHeItemsRegister.FURSUIT_SPONGE_SKULL_REGISTER.get(), 1)
        ));
        
        trades.get(4).add(RegistryUtil.offer(
                new ItemStack(Items.EMERALD, 16),
                new ItemStack(Items.SPONGE, 2)
        ));
        
        // TODO: 随机刷新预设设计图纸（预设由设计数据表示，待接入）
        trades.get(4).add(RegistryUtil.offer(
                new ItemStack(Items.EMERALD, 18),
                new ItemStack(FurryBoHeItemsRegister.FURSUIT_BLUEPRINTS_REGISTER.get(), 1)
        ));
        
        // TODO: 随机刷新1拓展装师手册（拓展由状态表示，待接入）
        trades.get(4).add(RegistryUtil.offer(
                new ItemStack(Items.EMERALD, 17),
                new ItemStack(FurryBoHeItemsRegister.FURSUIT_MANUAL_REGISTER.get(), 1)
        ));
        
        trades.get(4).add(RegistryUtil.offer(
                new ItemStack(Items.EMERALD, 9),
                new ItemStack(FurryBoHeItemsRegister.BLANK_CRYSTAL_REGISTER.get(), 1)
        ));
        
        trades.get(4).add(RegistryUtil.offer(
                new ItemStack(Items.EMERALD, 12),
                new ItemStack(FurryBoHeItemsRegister.UPGRADE_TEMPLATE_REGISTER.get(), 1)
        ));
        
        // TODO: 满拓展装师手册（拓展由状态表示，待接入）
        trades.get(5).add(RegistryUtil.offer(
                new ItemStack(Items.EMERALD, 29),
                new ItemStack(FurryBoHeItemsRegister.FURSUIT_MANUAL_REGISTER.get(), 1)
        ));
        
        // TODO: 随机刷新预设设计图纸（预设由设计数据表示，待接入）
        trades.get(5).add(RegistryUtil.offer(
                new ItemStack(Items.EMERALD, 21),
                new ItemStack(FurryBoHeItemsRegister.FURSUIT_BLUEPRINTS_REGISTER.get(), 1)
        ));
        
        trades.get(5).add(RegistryUtil.offer(
                new ItemStack(Items.EMERALD, 7),
                new ItemStack(Items.SLIME_BALL, 1)
        ));
        
        // TODO: 随机刷新特性结晶（结晶特性与耐久由物品状态表示，待接入）
        trades.get(5).add(RegistryUtil.offer(
                new ItemStack(Items.EMERALD, 19),
                new ItemStack(FurryBoHeItemsRegister.BLANK_CRYSTAL_REGISTER.get(), 1)
        ));
        
        trades.get(5).add(RegistryUtil.offer(
                new ItemStack(Items.EMERALD, 64),
                new ItemStack(FurryBoHeItemsRegister.QUALITY_CRAFTSMAN_CERTIFICATE_REGISTER.get(), 1)
        ));
    }
}
