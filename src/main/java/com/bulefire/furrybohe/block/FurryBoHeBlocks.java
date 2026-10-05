package com.bulefire.furrybohe.block;

import com.bulefire.furrybohe.FurryBoHe;
import com.bulefire.furrybohe.register.FurryBoHeBlocksRegister;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

public class FurryBoHeBlocks {
    static {
        if (!FurryBoHeBlocksState.loaded)
            throw new IllegalStateException("你不应该在这里使用这个类!");
    }
    
    // ================================================================
    // 功能方块
    // ================================================================
    
    /** 兽装工作站 */
    public static final Block FURSUIT_WORKSTATION = FurryBoHeBlocksRegister.FURSUIT_WORKSTATION_REGISTER.get();
    
    /** 树脂收集器【参考原版蜂巢 BEEHIVE：木质、附着于树、可右键交互】 */
    public static final Block RESIN_COLLECTOR = FurryBoHeBlocksRegister.RESIN_COLLECTOR_REGISTER.get();
    
    /** 毛布制作台【参考原版工作台 CRAFTING_TABLE / 织布机 LOOM：木质工作台】 */
    public static final Block FUR_CRAFTING_TABLE = FurryBoHeBlocksRegister.FUR_CRAFTING_TABLE.get();
    
    /** 结晶台【参考原版附魔台 ENCHANTING_TABLE：需镐、发光、高爆炸抗性】 */
    public static final Block CRYSTAL_TABLE = FurryBoHeBlocksRegister.CRYSTAL_TABLE_REGISTER.get();
    
    // ================================================================
    // 装饰与作物方块
    // ================================================================
    
    /** 晾衣架【参考原版脚手架 SCAFFOLDING：无碰撞、动态形状、可悬挂】 */
    public static final Block DRYING_RACK = FurryBoHeBlocksRegister.DRYING_RACK_REGISTER.get();
    
    /** 各色兽装部位【参考原版羊毛 WHITE_WOOL：柔软、可染色、装饰性放置】 */
    public static final Block FURSUIT_PART_DISPLAY = FurryBoHeBlocksRegister.FURSUIT_PART_DISPLAY_REGISTER.get();
    
    /** 棉花植株【参考原版小麦 WHEAT：作物、无碰撞、随机刻、瞬间破坏】 */
    public static final Block COTTON_CROP = FurryBoHeBlocksRegister.COTTON_CROP_REGISTER.get();
}
// 别学这个，我只是艺高人胆大（bushi
@Mod.EventBusSubscriber(modid=FurryBoHe.MODID, bus=Mod.EventBusSubscriber.Bus.MOD, value=Dist.CLIENT)
class FurryBoHeBlocksState {
    public static boolean loaded = false;
    @SubscribeEvent
    public static void common(FMLCommonSetupEvent event) {
        loaded = true;
    }
}