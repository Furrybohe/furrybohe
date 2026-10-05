package com.bulefire.furrybohe.register;

import com.bulefire.furrybohe.FurryBoHe;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.function.Supplier;

public class FurryBoHeItemsRegister {
    public static final DeferredRegister<Item> ITEMS_REGISTER = DeferredRegister.create(ForgeRegistries.ITEMS, FurryBoHe.MODID);
    
    // 冷知识：JVM初始化字段是有顺序的，不要再这个字段前加别的注册字段，除非你想要莫名其妙的NPE
    private static final Collection<RegistryObject<Item>> all_item = new HashSet<>();
    
    // ================================================================
    // 基础材料【不参与兽装部位前缀，保持短名】
    // ================================================================
    
    /** 树脂注册 */
    public static final RegistryObject<Item> RESIN_REGISTER = register("resin");
    /** 定型树脂注册 */
    public static final RegistryObject<Item> SHAPE_RESIN_REGISTER = register("shape_resin");
    /** 树脂碎片注册 */
    public static final RegistryObject<Item> RESIN_FRAGMENT_REGISTER = register("resin_fragment");
    /** 雕刻树脂碎片注册【指甲和牙齿都是这个】 */
    public static final RegistryObject<Item> CARVED_RESIN_FRAGMENT_REGISTER = register("carved_resin_fragment");
    /** 一瓶树脂注册 */
    public static final RegistryObject<Item> BOTTLE_OF_RESIN_REGISTER = register("bottle_of_resin");
    /** 棉花注册 */
    public static final RegistryObject<Item> COTTON_REGISTER = register("cotton");
    /** 棉花种子注册 */
    public static final RegistryObject<Item> COTTON_SEEDS_REGISTER = register("cotton_seeds");
    /** 有色毛布注册【键为染料颜色名】 */
    public static final Map<String, RegistryObject<Item>> COLOR_CLOTH_REGISTER = new HashMap<>(16, 1.0f);
    
    // ================================================================
    // 工具与耗材
    // ================================================================
    
    /** 雕刻刀注册 */
    public static final RegistryObject<Item> CARVING_KNIFE_REGISTER = register("carving_knife");
    /** 针线盒注册 */
    public static final RegistryObject<Item> NEEDLE_BOX_REGISTER = register("needle_box");
    /** 毛刷注册【清洗流程：梳毛】 */
    public static final RegistryObject<Item> FUR_BRUSH_REGISTER = register("fur_brush");
    /** 刷子注册【清洗流程：刷洗】 */
    public static final RegistryObject<Item> CLEANING_BRUSH_REGISTER = register("cleaning_brush");
    /** 兽装清洁剂注册 */
    public static final RegistryObject<Item> FURSUIT_CLEANER_REGISTER = register("fursuit_cleaner");
    /** 兽装风扇注册【右键使用消退中暑等级】 */
    public static final RegistryObject<Item> FURSUIT_FAN_REGISTER = register("fursuit_fan");
    
    // ================================================================
    // 特性结晶
    // ================================================================
    
    /** 空白结晶注册 */
    public static final RegistryObject<Item> BLANK_CRYSTAL_REGISTER = register("blank_crystal");
    /** 各类特性结晶注册【键为特性名；耐久耗尽即损坏，损坏态由耐久状态表示，不再单独注册】
     * <p>结晶耐久 2048，镶嵌并体现时每 5tick-0.1（-0.4/s）；耗尽后等级-1（最低至 1） */
    public static final Map<String, RegistryObject<Item>> SPECIAL_CRYSTAL_REGISTER = new HashMap<>();
    /** 【负面消除】结晶注册【不可镶嵌，一手拿结晶一手拿兽装 shift+右键 消除 1 个负面特性】 */
    public static final RegistryObject<Item> NEGATIVE_REMOVAL_CRYSTAL_REGISTER = register("negative_removal_crystal");
    
    /** 仅可镶在各个部位的特性结晶 */
    public static final String[] PART_SOCKET_CRYSTAL_NAMES = {
            "light_load",           // 轻装上阵
            "armor_compatibility",  // 盔甲兼容
            "built_in_fan",         // 内置风扇（仅兽头）
            "fireproof_coating",    // 耐火涂层
            "wide_vision",          // 宽阔视野（仅兽头）
            "dim_vision",           // 微光视野（仅兽头）
            "heart_container",      // 心之容器（仅兽装身/兽装身+爪）
            "sharp_claw",           // 利爪（仅兽爪/兽装身+爪）
            "long_claw",            // 长爪（仅兽爪/兽装身+爪）
            "swift",                // 迅捷（仅兽装腿/兽装腿+尾）
            "power_jump",           // 强力跳跃（仅兽装腿/兽装腿+尾）
            "breakthrough",         // 突围（仅兽装腿/兽装腿+尾）
            "extra_storage",        // 额外存储（仅尾巴/兽装腿+尾）
            "lightfoot",            // 轻功（仅兽脚）
            "multi_jump",           // 多段跳（仅兽脚）
            "paw_pad_buffer",       // 肉垫缓冲（仅兽脚）
            "spring_lock"           // 弹簧锁（特殊特性）
    };
    
    /** 仅可镶嵌在整体的特性结晶 */
    public static final String[] WHOLE_SOCKET_CRYSTAL_NAMES = {
            "dirt_resistance",      // 耐脏技术
            "waterproof_fabric",    // 防水布料
            "soft_fur_buffer",      // 软毛缓冲
            "revive",               // 复生
            "moonlight_power",      // 月光之力
            "instant_amnesia"       // 瞬间失忆（特殊特性）
    };
    
    /** 部位与整体均可镶嵌的特性结晶 */
    public static final String[] UNIVERSAL_SOCKET_CRYSTAL_NAMES = {
            "infinite_potential",   // 潜能无限
            "crystal_stability"     // 结晶稳定
    };
    
    // ================================================================
    // DTD【未定型→定型由状态表示，仅注册定型态】
    // ================================================================
    
    /** 身体DTD注册【定型与否由状态表示】 */
    public static final RegistryObject<Item> FURSUIT_BODY_DTD_REGISTER = register("fursuit_body_dtd");
    /** 直腿DTD注册【定型与否由状态表示】 */
    public static final RegistryObject<Item> FURSUIT_STRAIGHT_LEG_DTD_REGISTER = register("fursuit_straight_leg_dtd");
    /** 曲腿DTD注册【直腿DTD+树脂5，独立合成产物】 */
    public static final RegistryObject<Item> FURSUIT_CURVED_LEG_DTD_REGISTER = register("fursuit_curved_leg_dtd");
    
    // ================================================================
    // 图纸、手册与文书【空白/已设计/预设由设计数据表示，拓展由状态表示】
    // ================================================================
    
    /** 兽装设计图纸注册【右键打开可预览全装样式；空白、已设计、预设均由设计数据表示】 */
    public static final RegistryObject<Item> FURSUIT_BLUEPRINTS_REGISTER = register("fursuit_blueprints");
    /** 装师手册注册【特性/设计储存/委托拓展由状态表示】 */
    public static final RegistryObject<Item> FURSUIT_MANUAL_REGISTER = register("fursuit_manual");
    /** 委托单注册【设计图纸+空白设计图纸+纸，不消耗原设计图纸】 */
    public static final RegistryObject<Item> COMMISSION_FORM_REGISTER = register("commission_form");
    /** “品质升级”模板注册 */
    public static final RegistryObject<Item> UPGRADE_TEMPLATE_REGISTER = register("upgrade_template");
    /** “优质装师”认证注册 */
    public static final RegistryObject<Item> QUALITY_CRAFTSMAN_CERTIFICATE_REGISTER = register("quality_craftsman_certificate");
    
    // ================================================================
    // 兽装部件——头【未剃毛→成品由状态表示】
    // ================================================================
    
    /** 海绵头骨注册 */
    public static final RegistryObject<Item> FURSUIT_SPONGE_SKULL_REGISTER = register("fursuit_sponge_skull");
    /** 树脂头骨注册 */
    public static final RegistryObject<Item> FURSUIT_RESIN_SKULL_REGISTER = register("fursuit_resin_skull");
    /** 骨头头骨注册【彩蛋，仅装饰】 */
    public static final RegistryObject<Item> FURSUIT_BONE_SKULL_REGISTER = register("fursuit_bone_skull");
    /** 兽装眼睛注册 */
    public static final RegistryObject<Item> FURSUIT_EYES_REGISTER = register("fursuit_eyes");
    /** 舌头注册 */
    public static final RegistryObject<Item> FURSUIT_TONGUE_REGISTER = register("fursuit_tongue");
    /** 兽装头注册【可穿戴；剃毛与否由状态表示】 */
    public static final RegistryObject<Item> FURSUIT_HEAD_REGISTER = register("fursuit_head");
    
    // ================================================================
    // 兽装部件——手爪
    // ================================================================
    
    /** 兽爪版型注册 */
    public static final RegistryObject<Item> FURSUIT_CLAW_PATTERN_REGISTER = register("fursuit_claw_pattern");
    /** 肉垫（手指）注册 */
    public static final RegistryObject<Item> FURSUIT_FINGER_PAD_REGISTER = register("fursuit_finger_pad");
    /** 肉垫（手掌）注册 */
    public static final RegistryObject<Item> FURSUIT_PALM_PAD_REGISTER = register("fursuit_palm_pad");
    /** 兽爪注册【单只】 */
    public static final RegistryObject<Item> FURSUIT_CLAW_REGISTER = register("fursuit_claw");
    /** 一对兽爪注册【可穿戴，2兽爪合成】 */
    public static final RegistryObject<Item> FURSUIT_CLAW_PAIR_REGISTER = register("fursuit_claw_pair");
    
    // ================================================================
    // 兽装部件——尾巴
    // ================================================================
    
    /** 尾巴版型注册 */
    public static final RegistryObject<Item> FURSUIT_TAIL_PATTERN_REGISTER = register("fursuit_tail_pattern");
    /** 兽装尾注册【可穿戴】 */
    public static final RegistryObject<Item> FURSUIT_TAIL_REGISTER = register("fursuit_tail");
    
    // ================================================================
    // 兽装部件——脚爪
    // ================================================================
    
    /** 海绵兽脚底注册 */
    public static final RegistryObject<Item> FURSUIT_SPONGE_FOOT_SOLE_REGISTER = register("fursuit_sponge_foot_sole");
    /** 树脂兽脚底注册 */
    public static final RegistryObject<Item> FURSUIT_RESIN_FOOT_SOLE_REGISTER = register("fursuit_resin_foot_sole");
    /** 兽脚注册【单只】 */
    public static final RegistryObject<Item> FURSUIT_FOOT_REGISTER = register("fursuit_foot");
    /** 一对兽脚注册【可穿戴，不同花纹设计图不可组合，不同兽脚底可组合但掉品质】 */
    public static final RegistryObject<Item> FURSUIT_FOOT_PAIR_REGISTER = register("fursuit_foot_pair");
    
    // ================================================================
    // 兽装部件——身体与腿
    // ================================================================
    
    /** 兽装身体注册【可穿戴】 */
    public static final RegistryObject<Item> FURSUIT_BODY_REGISTER = register("fursuit_body");
    /** 兽装直腿注册【可穿戴】 */
    public static final RegistryObject<Item> FURSUIT_STRAIGHT_LEG_REGISTER = register("fursuit_straight_leg");
    /** 兽装曲腿注册【可穿戴】 */
    public static final RegistryObject<Item> FURSUIT_CURVED_LEG_REGISTER = register("fursuit_curved_leg");
    
    // ================================================================
    // 可穿戴二级合成组件
    // ================================================================
    
    /** 身体（含爪子）注册【其他均已穿戴时显示身体，否则仅显示爪子】 */
    public static final RegistryObject<Item> FURSUIT_BODY_WITH_CLAWS_REGISTER = register("fursuit_body_with_claws");
    /** 腿（含尾）注册【其他均已穿戴时显示腿，否则仅显示尾巴】 */
    public static final RegistryObject<Item> FURSUIT_LEG_WITH_TAIL_REGISTER = register("fursuit_leg_with_tail");
    /** 伪全身体注册【其他均已穿戴时显示身体，否则仅显示爪子；用于触发伪全模型】 */
    public static final RegistryObject<Item> FURSUIT_PSEUDO_FULL_BODY_REGISTER = register("fursuit_pseudo_full_body");
    
    // ================================================================
    // 方块物品【id 与方块一致】
    // ================================================================
    
    /** 兽装工作站方块物品注册 */
    public static final RegistryObject<Item> FURSUIT_WORKSTATION_REGISTER = register(FurryBoHeBlocksRegister.FURSUIT_WORKSTATION_REGISTER);
    /** 树脂收集器方块物品注册 */
    public static final RegistryObject<Item> RESIN_COLLECTOR_REGISTER = register(FurryBoHeBlocksRegister.RESIN_COLLECTOR_REGISTER);
    /** 毛布制作台方块物品注册 */
    public static final RegistryObject<Item> CLOTH_CRAFTING_TABLE_REGISTER = register(FurryBoHeBlocksRegister.CLOTH_CRAFTING_TABLE_REGISTER);
    /** 结晶台方块物品注册 */
    public static final RegistryObject<Item> CRYSTAL_TABLE_REGISTER = register(FurryBoHeBlocksRegister.CRYSTAL_TABLE_REGISTER);
    /** 晾衣架方块物品注册 */
    public static final RegistryObject<Item> DRYING_RACK_REGISTER = register(FurryBoHeBlocksRegister.DRYING_RACK_REGISTER);
    /** 各色兽装部位方块物品注册 */
    public static final RegistryObject<Item> FURSUIT_PART_DISPLAY_REGISTER = register(FurryBoHeBlocksRegister.FURSUIT_PART_DISPLAY_REGISTER);
    /** 棉花植株方块物品注册 */
    public static final RegistryObject<Item> COTTON_CROP_REGISTER = register(FurryBoHeBlocksRegister.COTTON_CROP_REGISTER);
    
    // ================================================================
    // 联动内容（ToNeko）
    // ================================================================
    
    /** 猫猫收集器注册【有猫猫在附近时收集猫猫能量，集满获得成就“喵？”】 */
    public static final RegistryObject<Item> CAT_ENERGY_COLLECTOR_REGISTER = register("cat_energy_collector");
    
    static  {
        for (DyeColor dyeColor : DyeColor.values())
            COLOR_CLOTH_REGISTER.put(dyeColor.getName(), register(dyeColor.getName() + "_cloth"));
        
        for (String name : PART_SOCKET_CRYSTAL_NAMES)
            SPECIAL_CRYSTAL_REGISTER.put(name, register(name + "_crystal"));
        for (String name : WHOLE_SOCKET_CRYSTAL_NAMES)
            SPECIAL_CRYSTAL_REGISTER.put(name, register(name + "_crystal"));
        for (String name : UNIVERSAL_SOCKET_CRYSTAL_NAMES)
            SPECIAL_CRYSTAL_REGISTER.put(name, register(name + "_crystal"));
    }
    
    public static @NotNull RegistryObject<Item> register(String id) {
        return register(id, () -> new Item(new Item.Properties()));
    }
    
    public static @NotNull RegistryObject<Item> register(String id, Supplier<? extends Item> item) {
        RegistryObject<Item> i = ITEMS_REGISTER.register(id, item);
        all_item.add(i);
        return i;
    }
    
    /** 由方块注册对应的方块物品【id 取方块路径名，延迟取值以避免方块尚未绑定】
     * <p>不可用 {@code block.getRegisteredName()}：其结果为「命名空间:路径」，会被再次拼上命名空间；
     * 也不可在静态初始化时调用 {@code block.get()}，此时方块尚未注册会抛 NPE */
    public static @NotNull RegistryObject<Item> register(@NotNull RegistryObject<? extends Block> block) {
        return ITEMS_REGISTER.register(block.getId().getPath(), () -> new BlockItem(block.get(), new Item.Properties()));
    }
    
    public static void registerAllItems(CreativeModeTab.ItemDisplayParameters parameters, CreativeModeTab.Output output) {
        all_item.forEach(item -> output.accept(item.get()));
    }
}
