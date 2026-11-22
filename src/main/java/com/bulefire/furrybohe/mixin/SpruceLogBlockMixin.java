package com.bulefire.furrybohe.mixin;

import com.bulefire.furrybohe.module.material.resincollector.block.ResinCollectorBlock;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RotatedPillarBlock.class)
public class SpruceLogBlockMixin {
    @Inject(method = "createBlockStateDefinition(Lnet/minecraft/world/level/block/state/StateDefinition$Builder;)V", at = @At("HEAD"))
    private void furrybohe$createBlockStateDefinitionHEAD(StateDefinition.@NotNull Builder<Block, BlockState> builder, CallbackInfo ci) {
        builder.add(ResinCollectorBlock.ResinCollectorBlockEntity.MixinProperties.NATURAL);
    }
    
    @Inject(method = "getStateForPlacement(Lnet/minecraft/world/item/context/BlockPlaceContext;)Lnet/minecraft/world/level/block/state/BlockState;",
            at = @At("RETURN"),
            cancellable=true)
    private void furrybohe$getStateForPlacementRETURN(BlockPlaceContext context, @NotNull CallbackInfoReturnable<BlockState> cir) {
        if ((Block) (Object) this != Blocks.SPRUCE_LOG) return;
        
        BlockState bs = cir.getReturnValue();
        if (bs == null) return;
        cir.setReturnValue(bs.setValue(ResinCollectorBlock.ResinCollectorBlockEntity.MixinProperties.NATURAL, false));
    }
}
