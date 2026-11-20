package com.bulefire.furrybohe.module.material.resincollector.block;

import com.bulefire.furrybohe.common.registry.FurryBoHeBlockEntityRegister;
import com.bulefire.furrybohe.common.registry.FurryBoHeItemsRegister;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class ResinCollectorBlock extends HorizontalDirectionalBlock implements EntityBlock {
    
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final IntegerProperty STAGE = IntegerProperty.create("stage", 0, 3);
    public static final IntegerProperty RESIN_AMOUNT = IntegerProperty.create("resin_amount", 0, 1);
    
    public static final int MAX_STAGE = 3;
    private static final int MAX_RESIN = 1;
    
    public ResinCollectorBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
        this.registerDefaultState(this.stateDefinition.any().setValue(STAGE, 0));
        this.registerDefaultState(this.stateDefinition.any().setValue(RESIN_AMOUNT, 0));
    }
    
    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
        builder.add(FACING);
        builder.add(STAGE);
        builder.add(RESIN_AMOUNT);
    }
    
    @Override
    public BlockState getStateForPlacement(@NotNull BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }
    
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new ResinCollectorBlockEntity(pos, state);
    }
    
    @Override
    public boolean isRandomlyTicking(@NotNull BlockState state) {
        return state.getValue(RESIN_AMOUNT) < MAX_RESIN;
    }
    
    @Override
    public void randomTick(@NotNull BlockState state, @NotNull ServerLevel level, @NotNull BlockPos pos, @NotNull RandomSource random) {
        if (!level.isLoaded(pos)) return;
        BlockEntity rbe = level.getBlockEntity(pos);
        if (!(rbe instanceof ResinCollectorBlockEntity be)) return;
        int[] v = ResinCollectorBlockEntity.randomTick(level, pos, state, be);
        BlockState newState = state.setValue(STAGE, v[0]).setValue(RESIN_AMOUNT, v[1]);
        level.setBlockAndUpdate(pos, newState);
    }
    
    @Override
    public @NotNull InteractionResult use(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hit) {
        if (!(level.getBlockEntity(pos) instanceof ResinCollectorBlockEntity be)) return InteractionResult.PASS;
        
        InteractionHand otherHand = hand == InteractionHand.MAIN_HAND
                ? InteractionHand.OFF_HAND
                : InteractionHand.MAIN_HAND;
        
        for (InteractionHand h : new InteractionHand[]{hand, otherHand}) {
            ItemStack stack = player.getItemInHand(h);
            InteractionResult result = InteractionResult.PASS;
            
            if (stack.is(Items.GLASS_BOTTLE))
                result = glassBottle(state, level, pos, player, h, hit, stack, be);
            
            if (stack.is(Items.SLIME_BALL))
                result = slimeBall(state, level, pos, player, h, hit, stack, be);
            
            if (result.consumesAction()) {
                if (h != hand) {
                    player.swing(h, true);
                    return InteractionResult.CONSUME;
                }
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.CONSUME;
        
//        ItemStack held = player.getItemInHand(hand);
//
//        if (held.is(Items.GLASS_BOTTLE)) return glassBottle(state, level, pos, player, hand, hit, held, be);
//        if (held.is(Items.SLIME_BALL)) return slimeBall(state, level, pos, player, hand, hit, held, be);
//
//        return InteractionResult.PASS;
    }
    
    public InteractionResult glassBottle(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hit, @NotNull ItemStack held, @NotNull ResinCollectorBlockEntity be) {
        if (level.isClientSide) return InteractionResult.PASS;
        var result = collect(be, !player.getAbilities().instabuild, held, state, level, pos);
        if (!result.isEmpty() && !player.getInventory().add(result)) player.drop(result, false);
        return result == ItemStack.EMPTY ? InteractionResult.PASS : InteractionResult.SUCCESS;
    }
    
    public InteractionResult slimeBall(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hit, @NotNull ItemStack held, @NotNull ResinCollectorBlockEntity be) {
        if (level.isClientSide) return InteractionResult.PASS;
        if (!(level instanceof ServerLevel serverLevel)) return InteractionResult.PASS;
        ItemStack result = fill(be, !player.getAbilities().instabuild, held, state, serverLevel, pos);
        return result == ItemStack.EMPTY ? InteractionResult.PASS : InteractionResult.SUCCESS;
    }
    
    
    public ItemStack fill(@NotNull ResinCollectorBlockEntity be, boolean consume, ItemStack held, BlockState state, ServerLevel serverLevel, BlockPos pos) {
        boolean flag = false;
        while (!be.isReady() && ResinCollectorBlockEntity.canCollectResin(serverLevel, pos)) {
            randomTick(state, serverLevel, pos, serverLevel.getRandom());
            flag = true;
        }
        if (flag) {
            if (consume) held.shrink(1);
            serverLevel.playSound(null, pos, SoundEvents.POINTED_DRIPSTONE_DRIP_WATER_INTO_CAULDRON, SoundSource.BLOCKS, 1.0F, 1.0F);
            return held;
        }
        return ItemStack.EMPTY;
    }
    
    public ItemStack collect(@NotNull ResinCollectorBlockEntity be, boolean consume, ItemStack held, BlockState state, Level level, BlockPos pos) {
        if (!be.isReady()) return ItemStack.EMPTY;
        
        if (consume) held.shrink(1);
        
        ItemStack result = new ItemStack(FurryBoHeItemsRegister.BOTTLE_OF_RESIN_REGISTER.get(), 1);
        
        int[] v = be.consumeResin();
        BlockState newState = state.setValue(STAGE, v[0]).setValue(RESIN_AMOUNT, v[1]);
        level.setBlockAndUpdate(pos, newState);
        level.playSound(null, pos, SoundEvents.BOTTLE_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
        return result;
    }
    
    @Override
    public void onNeighborChange(BlockState state, LevelReader level, BlockPos pos, BlockPos neighbor) {
        super.onNeighborChange(state, level, pos, neighbor);
    }
    
    @Override
    public boolean hasAnalogOutputSignal(@NotNull BlockState state) {
        return true;
    }
    
    @Override
    public int getAnalogOutputSignal(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (!(blockEntity instanceof ResinCollectorBlockEntity be)) return 0;
        if (be.resinAmount == MAX_RESIN) return 15;
        return switch (be.stage) {
            case 1 -> 5;
            case 2 -> 10;
            case 3 -> 15;
            default -> 0;
        };
    }
    
    public static class ResinCollectorBlockEntity extends BlockEntity {
        
        private static final int TRUNK_SCAN_HEIGHT = 120;
        private static final int LEAF_SCAN_RADIUS  = 5;
        
        private static final Set<ResourceKey<Biome>> ALLOWED_BIOMES = Set.of(
                Biomes.OLD_GROWTH_PINE_TAIGA, Biomes.OLD_GROWTH_SPRUCE_TAIGA, Biomes.SNOWY_TAIGA);
        
        private int resinAmount = 0;
        private int stage = 0;
        
        public ResinCollectorBlockEntity(BlockPos pos, BlockState state) {
            super(FurryBoHeBlockEntityRegister.RESIN_COLLECTOR_ENTITY_REGISTER.get(), pos, state);
        }
        
        @Contract("_, _, _, _ -> new")
        public static int @NotNull [] randomTick(@NotNull Level level, BlockPos pos, BlockState blockState, ResinCollectorBlockEntity be) {
            if (level.isClientSide) return new int[]{be.stage, be.resinAmount};
            
            if (be.resinAmount >= MAX_RESIN) {
                be.stage = 0;
                return new int[]{be.stage, be.resinAmount};
            }
            
            if (!canCollectResin(level, pos)) return new int[]{be.stage, be.resinAmount};
            
            be.stage++;
            
            if (be.stage >= MAX_STAGE) {
                be.stage = 0;
                be.resinAmount++;
                be.setChanged();
            }
            return new int[]{be.stage, be.resinAmount};
        }
        
        
        // region alive trees
        private static @Nullable BlockPos findNaturalLogHost(Level level, BlockPos pos) {
            BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos();
            for (Direction dir : Direction.values()) {
                cursor.setWithOffset(pos, dir);
                if (!level.isLoaded(cursor)) continue;
                if (isInTaigaBiome(level, cursor) && isNaturalLog(level.getBlockState(cursor))) return cursor.immutable();
            }
            return null;
        }
        
        private static boolean isNaturalLog(@NotNull BlockState state) {
            return state.is(BlockTags.OVERWORLD_NATURAL_LOGS) && state.is(BlockTags.SPRUCE_LOGS);
        }
        
        private static boolean isInTaigaBiome(@NotNull Level level, BlockPos pos) {
            Optional<ResourceKey<Biome>> biomeKey = level.getBiome(pos).unwrapKey();
            return biomeKey.filter(ALLOWED_BIOMES::contains).isPresent();
        }
        
        private static boolean isPartOfNaturalTree(Level level, BlockPos logPos) {
            return trunkHeight(level, logPos) >= 2
                    && hasNaturalCanopyConnectedToTrunk(level, logPos);
        }

        private static int trunkHeight(Level level, BlockPos logPos) {
            BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos();
            int count = 1;
            for (int dy = 1; dy <= TRUNK_SCAN_HEIGHT; dy++) {
                cursor.setWithOffset(logPos, 0, dy, 0);
                if (!level.isLoaded(cursor) || !isNaturalLog(level.getBlockState(cursor))) break;
                count++;
            }
            for (int dy = 1; dy <= TRUNK_SCAN_HEIGHT; dy++) {
                cursor.setWithOffset(logPos, 0, -dy, 0);
                if (!level.isLoaded(cursor) || !isNaturalLog(level.getBlockState(cursor))) break;
                count++;
            }
            return count;
        }
        
        private static boolean hasNaturalCanopyConnectedToTrunk(Level level, BlockPos logPos) {
            BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos();
            int topOffset = 0;
            for (int dy = 1; dy <= TRUNK_SCAN_HEIGHT; dy++) {
                cursor.setWithOffset(logPos, 0, dy, 0);
                if (!level.isLoaded(cursor)) break;
                if (!isNaturalLog(level.getBlockState(cursor))) break;
                topOffset = dy;
            }
            if (topOffset < 1) {
                return false;
            }
            
            BlockPos top = logPos.above(topOffset);
            
            Set<BlockPos> visited = new HashSet<>();
            Deque<BlockPos> queue = new ArrayDeque<>();
            
            for (Direction dir : Direction.values()) {
                cursor.setWithOffset(top, dir);
                if (!level.isLoaded(cursor)) continue;
                if (isNaturalLeaves(level.getBlockState(cursor))) {
                    BlockPos p = cursor.immutable();
                    if (visited.add(p)) {
                        queue.add(p);
                    }
                }
            }
            
            final int REQUIRED_LEAVES = 8;
            final int scanRadius = LEAF_SCAN_RADIUS * 2;
            int leafCount = 0;
            
            while (!queue.isEmpty()) {
                BlockPos cur = queue.poll();
                leafCount++;

                if (leafCount > REQUIRED_LEAVES) {
                    return true;
                }
                
                for (Direction dir : Direction.values()) {
                    cursor.setWithOffset(cur, dir);
                    if (!level.isLoaded(cursor)) continue;

                    if (Math.abs(cursor.getX() - top.getX()) > scanRadius) continue;
                    if (Math.abs(cursor.getY() - top.getY()) > scanRadius) continue;
                    if (Math.abs(cursor.getZ() - top.getZ()) > scanRadius) continue;
                    
                    if (visited.contains(cursor)) continue;
                    
                    if (isNaturalLeaves(level.getBlockState(cursor))) {
                        BlockPos p = cursor.immutable();
                        if (visited.add(p)) {
                            queue.add(p);
                        }
                    }
                }
            }
            
            return false;
        }
        
        private static boolean isNaturalLeaves(@NotNull BlockState state) {
            if (!state.is(BlockTags.LEAVES)) return false;
            if (!state.hasProperty(BlockStateProperties.PERSISTENT)) return true;
            return !state.getValue(BlockStateProperties.PERSISTENT);
        }
        
        // endregion
        
        private static boolean canCollectResin(@NotNull Level level, BlockPos pos) {
            // 启发式算法：效果不佳
//            BlockPos host = findNaturalLogHost(level, pos);
//            return host != null && isPartOfNaturalTree(level, host);
            BlockPos host = findNaturalLogHost(level, pos);
            if (host == null) return false;
            if (!isNaturalLog(level.getBlockState(host))) return false;
            BlockState blockState = level.getBlockState(host);
            if (!isNaturalLog(blockState)) return false;
            if (!blockState.hasProperty(MixinProperties.NATURAL)) return true;
            return blockState.getValue(MixinProperties.NATURAL);
        }
        
        public boolean isReady() {
            return resinAmount >= MAX_RESIN;
        }
        
        public int[] consumeResin() {
            if (resinAmount > 0) {
                resinAmount--;
                setChanged();
            }
            return new int[]{stage, resinAmount};
        }
        
        
        
        @Override
        protected void saveAdditional(@NotNull CompoundTag tag) {
            super.saveAdditional(tag);
            tag.putInt("resin_amount", resinAmount);
            tag.putInt("stage", stage);
        }
        
        @Override
        public void load(@NotNull CompoundTag tag) {
            super.load(tag);
            resinAmount = tag.getInt("resin_amount");
            stage = tag.getInt("stage");
        }
        
        public static class MixinProperties {
            public static final BooleanProperty NATURAL = BooleanProperty.create("natural");
        }
    }
}
