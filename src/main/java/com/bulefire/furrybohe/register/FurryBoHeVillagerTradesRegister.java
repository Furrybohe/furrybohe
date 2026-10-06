package com.bulefire.furrybohe.register;

import com.bulefire.furrybohe.FurryBoHe;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Mod.EventBusSubscriber(modid=FurryBoHe.MODID, bus=Mod.EventBusSubscriber.Bus.FORGE, value=Dist.CLIENT)
public class FurryBoHeVillagerTradesRegister {
    
    @SubscribeEvent
    public static void registerVillagerTrades(VillagerTradesEvent event) {
        if (event.getType() == FurryBoHeVillagerProfessionRegister.FURSUIT_MAKER_PROFESSION_REGISTER.get())
            registerFursuitMakerTrade(event.getTrades());
        else if (event.getType() == FurryBoHeVillagerProfessionRegister.FUR_TRADER_PROFESSION_REGISTER.get())
            registerFurTraderTrade(event.getTrades());
    }
    
    // ================================================================
    // 装师【fursuit_maker】交易
    // 「未定型DTD」「空白/预设设计图纸」「无拓展/满拓展装师手册」「随机刷新颜色兽装眼睛」
    // 「30~60% / 50~80% / 70~100% 随机」等均由物品状态（NBT/组件）表示，
    // 现无对应状态实现，一律留空并标记 TODO
    // ================================================================
    
    private static void registerFursuitMakerTrade(@NotNull Int2ObjectMap<List<VillagerTrades.ItemListing>> trades) {
        // ---------------- 新手 ----------------
        
        // TODO: 兽爪版型 30~60% 随机（版型属性由物品状态表示，待接入）
        trades.get(1).add((trader, rand) -> new MerchantOffer(
                new ItemStack(Items.EMERALD, 20),
                new ItemStack(Items.PAPER, 1),
                new ItemStack(FurryBoHeItemsRegister.FURSUIT_PAW_CLAW_PATTERN_REGISTER.get(), 1),
                10,
                2,
                0.05F
        ));
        
        // TODO: 随机刷新颜色兽装眼睛（眼睛颜色由物品状态表示，待接入）
        trades.get(1).add((trader, rand) -> new MerchantOffer(
                new ItemStack(Items.EMERALD, 15),
                new ItemStack(FurryBoHeItemsRegister.FURSUIT_EYES_REGISTER.get(), 1),
                10,
                2,
                0.05F
        ));
        
        // TODO: 空白设计图纸（空白与否由设计数据表示，待接入）
        trades.get(1).add((trader, rand) -> new MerchantOffer(
                new ItemStack(Items.EMERALD, 7),
                new ItemStack(FurryBoHeItemsRegister.FURSUIT_BLUEPRINTS_REGISTER.get(), 1),
                10,
                2,
                0.05F
        ));
        
        trades.get(1).add((trader, rand) -> new MerchantOffer(
                new ItemStack(Items.EMERALD, 12),
                new ItemStack(FurryBoHeItemsRegister.NEEDLE_BOX_REGISTER.get(), 1),
                10,
                2,
                0.05F
        ));
        
        // ---------------- 学徒 ----------------
        
        // TODO: 无拓展装师手册（拓展与否由状态表示，待接入）
        trades.get(2).add((trader, rand) -> new MerchantOffer(
                new ItemStack(Items.EMERALD, 13),
                new ItemStack(FurryBoHeItemsRegister.FURSUIT_MANUAL_REGISTER.get(), 1),
                10,
                2,
                0.05F
        ));
        
        // TODO: 尾巴版型 50~80% 随机（版型属性由物品状态表示，待接入）
        trades.get(2).add((trader, rand) -> new MerchantOffer(
                new ItemStack(Items.EMERALD, 25),
                new ItemStack(Items.PAPER, 1),
                new ItemStack(FurryBoHeItemsRegister.FURSUIT_TAIL_PATTERN_REGISTER.get(), 1),
                10,
                2,
                0.05F
        ));
        
        // TODO: 树脂头骨 50~80% 随机（头骨属性由物品状态表示，待接入）
        trades.get(2).add((trader, rand) -> new MerchantOffer(
                new ItemStack(Items.EMERALD, 34),
                new ItemStack(FurryBoHeItemsRegister.FURSUIT_RESIN_SKULL_REGISTER.get(), 1),
                10,
                2,
                0.05F
        ));
        
        // TODO: 树脂兽脚底 50~80% 随机（兽脚底属性由物品状态表示，待接入）
        trades.get(2).add((trader, rand) -> new MerchantOffer(
                new ItemStack(Items.EMERALD, 14),
                new ItemStack(FurryBoHeItemsRegister.FURSUIT_RESIN_FOOT_SOLE_REGISTER.get(), 1),
                10,
                2,
                0.05F
        ));
        
        trades.get(2).add((trader, rand) -> new MerchantOffer(
                new ItemStack(Items.EMERALD, 16),
                new ItemStack(FurryBoHeItemsRegister.CARVING_KNIFE_REGISTER.get(), 1),
                10,
                2,
                0.05F
        ));
        
        trades.get(2).add((trader, rand) -> new MerchantOffer(
                new ItemStack(Items.EMERALD, 7),
                new ItemStack(FurryBoHeItemsRegister.FUR_BRUSH_REGISTER.get(), 1),
                10,
                2,
                0.05F
        ));
        
        // ---------------- 老手 ----------------
        
        // TODO: 身体未定型DTD（定型与否由状态表示，待接入）
        trades.get(3).add((trader, rand) -> new MerchantOffer(
                new ItemStack(Items.EMERALD, 58),
                new ItemStack(FurryBoHeItemsRegister.FURSUIT_BODY_DTD_REGISTER.get(), 1),
                10,
                2,
                0.05F
        ));
        
        // TODO: 腿未定型DTD（定型与否由状态表示，待接入）
        trades.get(3).add((trader, rand) -> new MerchantOffer(
                new ItemStack(Items.EMERALD, 54),
                new ItemStack(FurryBoHeItemsRegister.FURSUIT_STRAIGHT_LEG_DTD_REGISTER.get(), 1),
                10,
                2,
                0.05F
        ));
        
        trades.get(3).add((trader, rand) -> new MerchantOffer(
                new ItemStack(Items.EMERALD, 42),
                new ItemStack(FurryBoHeItemsRegister.FURSUIT_BONE_SKULL_REGISTER.get(), 1),
                10,
                2,
                0.05F
        ));
        
        trades.get(3).add((trader, rand) -> new MerchantOffer(
                new ItemStack(Items.EMERALD, 19),
                new ItemStack(FurryBoHeItemsRegister.FURSUIT_CLEANER_REGISTER.get(), 1),
                10,
                2,
                0.05F
        ));
        
        trades.get(3).add((trader, rand) -> new MerchantOffer(
                new ItemStack(Items.EMERALD, 8),
                new ItemStack(FurryBoHeItemsRegister.CLEANING_BRUSH_REGISTER.get(), 1),
                10,
                2,
                0.05F
        ));
        
        // ---------------- 专家 ----------------
        
        // TODO: 海绵头骨 70~100% 随机（头骨属性由物品状态表示，待接入）
        trades.get(4).add((trader, rand) -> new MerchantOffer(
                new ItemStack(Items.EMERALD, 46),
                new ItemStack(FurryBoHeItemsRegister.FURSUIT_SPONGE_SKULL_REGISTER.get(), 1),
                10,
                2,
                0.05F
        ));
        
        trades.get(4).add((trader, rand) -> new MerchantOffer(
                new ItemStack(Items.EMERALD, 16),
                new ItemStack(Items.SPONGE, 2),
                10,
                2,
                0.05F
        ));
        
        // TODO: 随机刷新预设设计图纸（预设由设计数据表示，待接入）
        trades.get(4).add((trader, rand) -> new MerchantOffer(
                new ItemStack(Items.EMERALD, 18),
                new ItemStack(FurryBoHeItemsRegister.FURSUIT_BLUEPRINTS_REGISTER.get(), 1),
                10,
                2,
                0.05F
        ));
        
        // TODO: 随机刷新1拓展装师手册（拓展由状态表示，待接入）
        trades.get(4).add((trader, rand) -> new MerchantOffer(
                new ItemStack(Items.EMERALD, 17),
                new ItemStack(FurryBoHeItemsRegister.FURSUIT_MANUAL_REGISTER.get(), 1),
                10,
                2,
                0.05F
        ));
        
        trades.get(4).add((trader, rand) -> new MerchantOffer(
                new ItemStack(Items.EMERALD, 9),
                new ItemStack(FurryBoHeItemsRegister.BLANK_CRYSTAL_REGISTER.get(), 1),
                10,
                2,
                0.05F
        ));
        
        trades.get(4).add((trader, rand) -> new MerchantOffer(
                new ItemStack(Items.EMERALD, 12),
                new ItemStack(FurryBoHeItemsRegister.UPGRADE_TEMPLATE_REGISTER.get(), 1),
                10,
                2,
                0.05F
        ));
        
        // ---------------- 大师 ----------------
        
        // TODO: 满拓展装师手册（拓展由状态表示，待接入）
        trades.get(5).add((trader, rand) -> new MerchantOffer(
                new ItemStack(Items.EMERALD, 29),
                new ItemStack(FurryBoHeItemsRegister.FURSUIT_MANUAL_REGISTER.get(), 1),
                10,
                2,
                0.05F
        ));
        
        // TODO: 随机刷新预设设计图纸（预设由设计数据表示，待接入）
        trades.get(5).add((trader, rand) -> new MerchantOffer(
                new ItemStack(Items.EMERALD, 21),
                new ItemStack(FurryBoHeItemsRegister.FURSUIT_BLUEPRINTS_REGISTER.get(), 1),
                10,
                2,
                0.05F
        ));
        
        trades.get(5).add((trader, rand) -> new MerchantOffer(
                new ItemStack(Items.EMERALD, 7),
                new ItemStack(Items.SLIME_BALL, 1),
                10,
                2,
                0.05F
        ));
        
        // TODO: 随机刷新特性结晶（结晶特性与耐久由物品状态表示，待接入）
        trades.get(5).add((trader, rand) -> new MerchantOffer(
                new ItemStack(Items.EMERALD, 19),
                new ItemStack(FurryBoHeItemsRegister.BLANK_CRYSTAL_REGISTER.get(), 1),
                10,
                2,
                0.05F
        ));
        
        trades.get(5).add((trader, rand) -> new MerchantOffer(
                new ItemStack(Items.EMERALD, 64),
                new ItemStack(FurryBoHeItemsRegister.QUALITY_CRAFTSMAN_CERTIFICATE_REGISTER.get(), 1),
                10,
                2,
                0.05F
        ));
    }
    
    // ================================================================
    // 毛布商【fur_trader】交易
    // ================================================================
    
    private static void registerFurTraderTrade(Int2ObjectMap<List<VillagerTrades.ItemListing>> trades) {
        trades.get(1).add((trader, rand) -> new MerchantOffer(
                new ItemStack(Items.LEATHER, 4),
                new ItemStack(Items.EMERALD, 1),
                10,
                2,
                0.05F
        ));
        
        trades.get(1).add((trader, rand) -> new MerchantOffer(
                new ItemStack(Items.EMERALD, 2),
                new ItemStack(FurryBoHeItemsRegister.COTTON_SEEDS_REGISTER.get(), 5),
                10,
                2,
                0.05F
        ));
        
        trades.get(1).add(new VillagerTrades.ItemListing() {
            @Override
            public @NotNull MerchantOffer getOffer(@NotNull Entity trader, @NotNull RandomSource rand) {
                DyeColor[] colors = DyeColor.values();
                DyeColor randomColor = colors[rand.nextInt(colors.length)];
                ItemStack dyeStack = new ItemStack(DyeItem.byColor(randomColor), 2);
                
                return new MerchantOffer(
                        new ItemStack(Items.EMERALD, 1),
                        dyeStack,
                        16,
                        2,
                        0.05f
                );
            }
        });
        
        trades.get(1).add((trader, rand) -> new MerchantOffer(
                new ItemStack(FurryBoHeItemsRegister.RESIN_FRAGMENT_REGISTER.get(), 12),
                new ItemStack(Items.EMERALD, 1),
                10,
                2,
                0.05F
        ));
        
        trades.get(2).add((trader, rand) -> new MerchantOffer(
                new ItemStack(Items.EMERALD, 8),
                new ItemStack(Items.LEATHER, 3),
                10,
                2,
                0.05F
        ));
        
        trades.get(2).add((trader, rand) -> new MerchantOffer(
                new ItemStack(FurryBoHeItemsRegister.COTTON_REGISTER.get(), 13),
                new ItemStack(Items.EMERALD, 1),
                10,
                2,
                0.05F
        ));
        
        trades.get(2).add(new VillagerTrades.ItemListing() {
            @Override
            public @NotNull MerchantOffer getOffer(@NotNull Entity trader, @NotNull RandomSource rand) {
                DyeColor[] colors = DyeColor.values();
                DyeColor randomColor = colors[rand.nextInt(colors.length)];
                ItemStack dyeStack = new ItemStack(DyeItem.byColor(randomColor), 5);
                
                return new MerchantOffer(
                        dyeStack,
                        new ItemStack(Items.EMERALD, 1),
                        16,
                        2,
                        0.05f
                );
            }
        });
        
        trades.get(2).add((trader, rand) -> new MerchantOffer(
                new ItemStack(Items.EMERALD, 7),
                new ItemStack(FurryBoHeItemsRegister.RESIN_REGISTER.get(), 2),
                10,
                2,
                0.05F
        ));
        // TODO: 耐久10~50%白色毛布
        trades.get(3).add(new VillagerTrades.ItemListing() {
            @Override
            public @NotNull MerchantOffer getOffer(@NotNull Entity trader, @NotNull RandomSource rand) {
                return new MerchantOffer(
                        new ItemStack(Items.EMERALD, 41),
                        new ItemStack(FurryBoHeItemsRegister.COLOR_FUR_REGISTER.get(DyeColor.WHITE.getName()).get(), 5),
                        10,
                        2,
                        0.05F
                );
            }
        });
        
        trades.get(3).add((trader, rand) -> new MerchantOffer(
                new ItemStack(Items.WHITE_WOOL, 11),
                new ItemStack(Items.EMERALD, 3),
                10,
                2,
                0.05F
        ));
        
        trades.get(3).add((trader, rand) -> new MerchantOffer(
                new ItemStack(Items.EMERALD, 15),
                new ItemStack(FurryBoHeItemsRegister.SHAPE_RESIN_REGISTER.get(), 2),
                10,
                2,
                0.05F
        ));
        
        trades.get(3).add((trader, rand) -> new MerchantOffer(
                new ItemStack(Items.EMERALD, 19),
                new ItemStack(FurryBoHeItemsRegister.RESIN_COLLECTOR_REGISTER.get(), 1),
                10,
                2,
                0.05F
        ));
        // TODO: 耐久20-60%随机颜色毛布
        trades.get(4).add(new VillagerTrades.ItemListing() {
            @Override
            public @NotNull MerchantOffer getOffer(@NotNull Entity trader, @NotNull RandomSource rand) {
                DyeColor[] colors = DyeColor.values();
                DyeColor randomColor = colors[rand.nextInt(colors.length)];
                ItemStack dyeStack = new ItemStack(DyeItem.byColor(randomColor), 1);
                ItemStack furStack = new ItemStack(FurryBoHeItemsRegister.COLOR_FUR_REGISTER.get(randomColor.getName()).get(), 1);
                
                return new MerchantOffer(
                        new ItemStack(Items.EMERALD, 60),
                        dyeStack,
                        furStack,
                        10,
                        2,
                        0.05F
                );
            }
        });
        // TODO: 满耐久随机颜色毛布
        trades.get(4).add(new VillagerTrades.ItemListing() {
            @Override
            public @NotNull MerchantOffer getOffer(@NotNull Entity trader, @NotNull RandomSource rand) {
                DyeColor[] colors = DyeColor.values();
                DyeColor randomColor = colors[rand.nextInt(colors.length)];
                ItemStack furStack = new ItemStack(FurryBoHeItemsRegister.COLOR_FUR_REGISTER.get(randomColor.getName()).get(), 1);
                
                return new MerchantOffer(
                        new ItemStack(Items.EMERALD, 64),
                        new ItemStack(Items.EMERALD, 52),
                        furStack,
                        10,
                        2,
                        0.05F
                );
            }
        });
    }
}
