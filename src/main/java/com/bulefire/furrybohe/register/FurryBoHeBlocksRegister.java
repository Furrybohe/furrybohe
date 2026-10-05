package com.bulefire.furrybohe.register;

import com.bulefire.furrybohe.FurryBoHe;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class FurryBoHeBlocksRegister {
    public static final DeferredRegister<Block> BLOCKS_REGISTER = DeferredRegister.create(ForgeRegistries.BLOCKS, FurryBoHe.MODID);
    
    // ================================================================
    // 功能方块
    // ================================================================
    
    /** 兽装工作站注册 */
    public static final RegistryObject<Block> FURSUIT_WORKSTATION_REGISTER = register(
            "fursuit_workstation",
            () -> new Block(BlockBehaviour.Properties.of().strength(3.0F, 3.0F))
    );
    
    /** 树脂收集器注册【参考原版蜂巢 BEEHIVE：木质、附着于树、可右键交互】 */
    public static final RegistryObject<Block> RESIN_COLLECTOR_REGISTER = register(
            "resin_collector",
            () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).instrument(NoteBlockInstrument.BASS).strength(0.6F).sound(SoundType.WOOD).ignitedByLava())
    );
    
    /** 毛布制作台注册【参考原版工作台 CRAFTING_TABLE / 织布机 LOOM：木质工作台】 */
    public static final RegistryObject<Block> CLOTH_CRAFTING_TABLE_REGISTER = register(
            "cloth_crafting_table",
            () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).instrument(NoteBlockInstrument.BASS).strength(2.5F).sound(SoundType.WOOD).ignitedByLava())
    );
    
    /** 结晶台注册【参考原版附魔台 ENCHANTING_TABLE：需镐、发光、高爆炸抗性】 */
    public static final RegistryObject<Block> CRYSTAL_TABLE_REGISTER = register(
            "crystal_table",
            () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_RED).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().lightLevel(state -> 7).strength(5.0F, 1200.0F))
    );
    
    // ================================================================
    // 装饰与作物方块
    // ================================================================
    
    /** 晾衣架注册【参考原版脚手架 SCAFFOLDING：无碰撞、动态形状、可悬挂】 */
    public static final RegistryObject<Block> DRYING_RACK_REGISTER = register(
            "drying_rack",
            () -> new Block(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.SAND)
                    .noCollission()
                    .sound(SoundType.SCAFFOLDING)
                    .dynamicShape()
                    .isValidSpawn((state, getter, pos, entity) -> false)
                    .pushReaction(PushReaction.DESTROY)
                    .isRedstoneConductor((state, level, pos) -> false))
    );
    
    /** 各色兽装部位注册【参考原版羊毛 WHITE_WOOL：柔软、可染色、装饰性放置】 */
    public static final RegistryObject<Block> FURSUIT_PART_DISPLAY_REGISTER = register(
            "fursuit_part_display",
            () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.WOOL).instrument(NoteBlockInstrument.GUITAR).strength(0.8F).sound(SoundType.WOOL).ignitedByLava())
    );
    
    /** 棉花植株注册【参考原版小麦 WHEAT：作物、无碰撞、随机刻、瞬间破坏】 */
    public static final RegistryObject<Block> COTTON_CROP_REGISTER = register(
            "cotton_crop",
            () -> new Block(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.PLANT)
                    .noCollission()
                    .randomTicks()
                    .instabreak()
                    .sound(SoundType.CROP)
                    .pushReaction(PushReaction.DESTROY))
    );
    
    public static @NotNull RegistryObject<Block> register(String id, Supplier<Block> block) {
        return BLOCKS_REGISTER.register(id, block);
    }
}
