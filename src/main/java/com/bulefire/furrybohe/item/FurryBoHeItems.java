package com.bulefire.furrybohe.item;

import com.bulefire.furrybohe.FurryBoHe;
import com.bulefire.furrybohe.register.FurryBoHeItemsRegister;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import java.util.HashMap;
import java.util.Map;

public class FurryBoHeItems {
    static {
        if (!FurryBoHeItemsState.loaded)
            throw new IllegalStateException("你不应该在这里使用这个类!");
    }
    
    // ================================================================
    // 基础材料【不参与兽装部位前缀，保持短名】
    // ================================================================
    
    /** 树脂 */
    public static final Item RESIN = FurryBoHeItemsRegister.RESIN_REGISTER.get();
    /** 定型树脂 */
    public static final Item SHAPE_RESIN = FurryBoHeItemsRegister.SHAPE_RESIN_REGISTER.get();
    /** 树脂碎片 */
    public static final Item RESIN_FRAGMENT = FurryBoHeItemsRegister.RESIN_FRAGMENT_REGISTER.get();
    /** 雕刻树脂碎片【指甲和牙齿都是这个】 */
    public static final Item CARVED_RESIN_FRAGMENT = FurryBoHeItemsRegister.CARVED_RESIN_FRAGMENT_REGISTER.get();
    /** 一瓶树脂 */
    public static final Item BOTTLE_OF_RESIN = FurryBoHeItemsRegister.BOTTLE_OF_RESIN_REGISTER.get();
    /** 棉花 */
    public static final Item COTTON = FurryBoHeItemsRegister.COTTON_REGISTER.get();
    /** 棉花种子 */
    public static final Item COTTON_SEEDS = FurryBoHeItemsRegister.COTTON_SEEDS_REGISTER.get();
    /** 有色毛布【键为染料颜色名】 */
    public static final Map<String, Item> COLOR_FUR = new HashMap<>(16, 1.0f);
    
    // ================================================================
    // 工具与耗材
    // ================================================================
    
    /** 雕刻刀 */
    public static final Item CARVING_KNIFE = FurryBoHeItemsRegister.CARVING_KNIFE_REGISTER.get();
    /** 针线盒 */
    public static final Item NEEDLE_BOX = FurryBoHeItemsRegister.NEEDLE_BOX_REGISTER.get();
    /** 毛刷【清洗流程：梳毛】 */
    public static final Item FUR_BRUSH = FurryBoHeItemsRegister.FUR_BRUSH_REGISTER.get();
    /** 刷子【清洗流程：刷洗】 */
    public static final Item CLEANING_BRUSH = FurryBoHeItemsRegister.CLEANING_BRUSH_REGISTER.get();
    /** 兽装清洁剂 */
    public static final Item FURSUIT_CLEANER = FurryBoHeItemsRegister.FURSUIT_CLEANER_REGISTER.get();
    /** 兽装风扇【右键使用消退中暑等级】 */
    public static final Item FURSUIT_FAN = FurryBoHeItemsRegister.FURSUIT_FAN_REGISTER.get();
    
    // ================================================================
    // 特性结晶
    // ================================================================
    
    /** 空白结晶 */
    public static final Item BLANK_CRYSTAL = FurryBoHeItemsRegister.BLANK_CRYSTAL_REGISTER.get();
    /** 各类特性结晶【键为特性名；耐久耗尽即损坏，损坏态由耐久状态表示，不再单独注册】
     * <p>结晶耐久 2048，镶嵌并体现时每 5tick-0.1（-0.4/s）；耗尽后等级-1（最低至 1） */
    public static final Map<String, Item> SPECIAL_CRYSTAL = new HashMap<>();
    /** 【负面消除】结晶【不可镶嵌，一手拿结晶一手拿兽装 shift+右键 消除 1 个负面特性】 */
    public static final Item NEGATIVE_REMOVAL_CRYSTAL = FurryBoHeItemsRegister.NEGATIVE_REMOVAL_CRYSTAL_REGISTER.get();
    
    // ================================================================
    // DTD【未定型→定型由状态表示，仅注册定型态】
    // ================================================================
    
    /** 身体DTD【定型与否由状态表示】 */
    public static final Item FURSUIT_BODY_DTD = FurryBoHeItemsRegister.FURSUIT_BODY_DTD_REGISTER.get();
    /** 直腿DTD【定型与否由状态表示】 */
    public static final Item FURSUIT_STRAIGHT_LEG_DTD = FurryBoHeItemsRegister.FURSUIT_STRAIGHT_LEG_DTD_REGISTER.get();
    /** 曲腿DTD【直腿DTD+树脂5，独立合成产物】 */
    public static final Item FURSUIT_CURVED_LEG_DTD = FurryBoHeItemsRegister.FURSUIT_CURVED_LEG_DTD_REGISTER.get();
    
    // ================================================================
    // 图纸、手册与文书【空白/已设计/预设由设计数据表示，拓展由状态表示】
    // ================================================================
    
    /** 兽装设计图纸【右键打开可预览全装样式；空白、已设计、预设均由设计数据表示】 */
    public static final Item FURSUIT_BLUEPRINTS = FurryBoHeItemsRegister.FURSUIT_BLUEPRINTS_REGISTER.get();
    /** 装师手册【特性/设计储存/委托拓展由状态表示】 */
    public static final Item FURSUIT_MANUAL = FurryBoHeItemsRegister.FURSUIT_MANUAL_REGISTER.get();
    /** 委托单【设计图纸+空白设计图纸+纸，不消耗原设计图纸】 */
    public static final Item COMMISSION_FORM = FurryBoHeItemsRegister.COMMISSION_FORM_REGISTER.get();
    /** “品质升级”模板 */
    public static final Item UPGRADE_TEMPLATE = FurryBoHeItemsRegister.UPGRADE_TEMPLATE_REGISTER.get();
    /** “优质装师”认证 */
    public static final Item QUALITY_CRAFTSMAN_CERTIFICATE = FurryBoHeItemsRegister.QUALITY_CRAFTSMAN_CERTIFICATE_REGISTER.get();
    
    // ================================================================
    // 兽装部件——头【未剃毛→成品由状态表示】
    // ================================================================
    
    /** 海绵头骨 */
    public static final Item FURSUIT_SPONGE_SKULL = FurryBoHeItemsRegister.FURSUIT_SPONGE_SKULL_REGISTER.get();
    /** 树脂头骨 */
    public static final Item FURSUIT_RESIN_SKULL = FurryBoHeItemsRegister.FURSUIT_RESIN_SKULL_REGISTER.get();
    /** 骨头头骨【彩蛋，仅装饰】 */
    public static final Item FURSUIT_BONE_SKULL = FurryBoHeItemsRegister.FURSUIT_BONE_SKULL_REGISTER.get();
    /** 兽装眼睛 */
    public static final Item FURSUIT_EYES = FurryBoHeItemsRegister.FURSUIT_EYES_REGISTER.get();
    /** 舌头 */
    public static final Item FURSUIT_TONGUE = FurryBoHeItemsRegister.FURSUIT_TONGUE_REGISTER.get();
    /** 兽装头【可穿戴；剃毛与否由状态表示】 */
    public static final Item FURSUIT_HEAD = FurryBoHeItemsRegister.FURSUIT_HEAD_REGISTER.get();
    
    // ================================================================
    // 兽装部件——手爪
    // ================================================================
    
    /** 兽爪版型 */
    public static final Item FURSUIT_CLAW_PATTERN = FurryBoHeItemsRegister.FURSUIT_CLAW_PATTERN_REGISTER.get();
    /** 肉垫（手指） */
    public static final Item FURSUIT_FINGER_PAD = FurryBoHeItemsRegister.FURSUIT_FINGER_PAD_REGISTER.get();
    /** 肉垫（手掌） */
    public static final Item FURSUIT_PALM_PAD = FurryBoHeItemsRegister.FURSUIT_PALM_PAD_REGISTER.get();
    /** 兽爪【单只】 */
    public static final Item FURSUIT_CLAW = FurryBoHeItemsRegister.FURSUIT_CLAW_REGISTER.get();
    /** 一对兽爪【可穿戴，2兽爪合成】 */
    public static final Item FURSUIT_CLAW_PAIR = FurryBoHeItemsRegister.FURSUIT_CLAW_PAIR_REGISTER.get();
    
    // ================================================================
    // 兽装部件——尾巴
    // ================================================================
    
    /** 尾巴版型 */
    public static final Item FURSUIT_TAIL_PATTERN = FurryBoHeItemsRegister.FURSUIT_TAIL_PATTERN_REGISTER.get();
    /** 兽装尾【可穿戴】 */
    public static final Item FURSUIT_TAIL = FurryBoHeItemsRegister.FURSUIT_TAIL_REGISTER.get();
    
    // ================================================================
    // 兽装部件——脚爪
    // ================================================================
    
    /** 海绵兽脚底 */
    public static final Item FURSUIT_SPONGE_FOOT_SOLE = FurryBoHeItemsRegister.FURSUIT_SPONGE_FOOT_SOLE_REGISTER.get();
    /** 树脂兽脚底 */
    public static final Item FURSUIT_RESIN_FOOT_SOLE = FurryBoHeItemsRegister.FURSUIT_RESIN_FOOT_SOLE_REGISTER.get();
    /** 兽脚【单只】 */
    public static final Item FURSUIT_FOOT = FurryBoHeItemsRegister.FURSUIT_FOOT_REGISTER.get();
    /** 一对兽脚【可穿戴，不同花纹设计图不可组合，不同兽脚底可组合但掉品质】 */
    public static final Item FURSUIT_FOOT_PAIR = FurryBoHeItemsRegister.FURSUIT_FOOT_PAIR_REGISTER.get();
    
    // ================================================================
    // 兽装部件——身体与腿
    // ================================================================
    
    /** 兽装身体【可穿戴】 */
    public static final Item FURSUIT_BODY = FurryBoHeItemsRegister.FURSUIT_BODY_REGISTER.get();
    /** 兽装直腿【可穿戴】 */
    public static final Item FURSUIT_STRAIGHT_LEG = FurryBoHeItemsRegister.FURSUIT_STRAIGHT_LEG_REGISTER.get();
    /** 兽装曲腿【可穿戴】 */
    public static final Item FURSUIT_CURVED_LEG = FurryBoHeItemsRegister.FURSUIT_CURVED_LEG_REGISTER.get();
    
    // ================================================================
    // 可穿戴二级合成组件
    // ================================================================
    
    /** 身体（含爪子）【其他均已穿戴时显示身体，否则仅显示爪子】 */
    public static final Item FURSUIT_BODY_WITH_CLAWS = FurryBoHeItemsRegister.FURSUIT_BODY_WITH_CLAWS_REGISTER.get();
    /** 腿（含尾）【其他均已穿戴时显示腿，否则仅显示尾巴】 */
    public static final Item FURSUIT_LEG_WITH_TAIL = FurryBoHeItemsRegister.FURSUIT_LEG_WITH_TAIL_REGISTER.get();
    /** 伪全身体【其他均已穿戴时显示身体，否则仅显示爪子；用于触发伪全模型】 */
    public static final Item FURSUIT_PSEUDO_FULL_BODY = FurryBoHeItemsRegister.FURSUIT_PSEUDO_FULL_BODY_REGISTER.get();
    
    // ================================================================
    // 方块物品【id 与方块一致】
    // ================================================================
    
    /** 兽装工作站方块物品 */
    public static final Item FURSUIT_WORKSTATION = FurryBoHeItemsRegister.FURSUIT_WORKSTATION_REGISTER.get();
    /** 树脂收集器方块物品 */
    public static final Item RESIN_COLLECTOR = FurryBoHeItemsRegister.RESIN_COLLECTOR_REGISTER.get();
    /** 毛布制作台方块物品 */
    public static final Item FUR_CRAFTING_TABLE = FurryBoHeItemsRegister.FUR_CRAFTING_TABLE_REGISTER.get();
    /** 结晶台方块物品 */
    public static final Item CRYSTAL_TABLE = FurryBoHeItemsRegister.CRYSTAL_TABLE_REGISTER.get();
    /** 晾衣架方块物品 */
    public static final Item DRYING_RACK = FurryBoHeItemsRegister.DRYING_RACK_REGISTER.get();
    /** 各色兽装部位方块物品 */
    public static final Item FURSUIT_PART_DISPLAY = FurryBoHeItemsRegister.FURSUIT_PART_DISPLAY_REGISTER.get();
    /** 棉花植株方块物品 */
    public static final Item COTTON_CROP = FurryBoHeItemsRegister.COTTON_CROP_REGISTER.get();
    
    // ================================================================
    // 联动内容（ToNeko）
    // ================================================================
    
    /** 猫猫收集器【有猫猫在附近时收集猫猫能量，集满获得成就“喵？”】 */
    public static final Item CAT_ENERGY_COLLECTOR = FurryBoHeItemsRegister.CAT_ENERGY_COLLECTOR_REGISTER.get();
    
    static {
        for (var entry : FurryBoHeItemsRegister.COLOR_FUR_REGISTER.entrySet())
            COLOR_FUR.put(entry.getKey(), entry.getValue().get());
        
        for (String name : FurryBoHeItemsRegister.PART_SOCKET_CRYSTAL_NAMES)
            SPECIAL_CRYSTAL.put(name, FurryBoHeItemsRegister.SPECIAL_CRYSTAL_REGISTER.get(name).get());
        for (String name : FurryBoHeItemsRegister.WHOLE_SOCKET_CRYSTAL_NAMES)
            SPECIAL_CRYSTAL.put(name, FurryBoHeItemsRegister.SPECIAL_CRYSTAL_REGISTER.get(name).get());
        for (String name : FurryBoHeItemsRegister.UNIVERSAL_SOCKET_CRYSTAL_NAMES)
            SPECIAL_CRYSTAL.put(name, FurryBoHeItemsRegister.SPECIAL_CRYSTAL_REGISTER.get(name).get());
    }
}

// 别学这个，我只是艺高人胆大（bushi
@Mod.EventBusSubscriber(modid=FurryBoHe.MODID, bus=Mod.EventBusSubscriber.Bus.MOD, value=Dist.CLIENT)
class FurryBoHeItemsState {
    public static boolean loaded = false;
    @SubscribeEvent
    public static void common(FMLCommonSetupEvent event) {
        loaded = true;
    }
}
