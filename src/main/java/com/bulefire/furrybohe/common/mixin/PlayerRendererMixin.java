package com.bulefire.furrybohe.common.mixin;

import com.bulefire.furrybohe.common.render.FursuitArmGeoRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerRenderer.class)
public class PlayerRendererMixin {
    @Inject(method = "renderHand", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/player/PlayerRenderer;setModelProperties(Lnet/minecraft/client/player/AbstractClientPlayer;)V", shift = At.Shift.AFTER))
    private void renderHand_ModifyVanillaModel(PoseStack matrices, MultiBufferSource vertexConsumers, int light, AbstractClientPlayer player, ModelPart arm, ModelPart sleeve, CallbackInfo ci){
        if (player.isCreative()) {  // TODO 需要修改为需要修改手臂渲染的Condition
            arm.visible = false;
            sleeve.visible = false;
        }
    }

    @Inject(method = "renderHand", at = @At(value = "RETURN"))
    private void renderHand_RenderSelfModel(PoseStack matrices, MultiBufferSource vertexConsumers, int light, AbstractClientPlayer player, ModelPart arm, ModelPart sleeve, CallbackInfo ci){
        if (player.isCreative()) {
            PlayerRenderer realThis = (PlayerRenderer) (Object) this;
            FursuitArmGeoRenderer.rFPM_PartB(realThis, matrices, vertexConsumers, light, player, arm, sleeve);
        }
    }
}
