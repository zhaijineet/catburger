package net.zhaiji.catburger.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.LivingEntity;
import net.zhaiji.catburger.client.compat.YSMCompat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntityRenderer.class)
public class LivingEntityRendererMixin<T extends LivingEntity, M extends EntityModel<T>> {
    
    @Inject(method = "render*", at = @At("RETURN"))
    private void onRenderPost(T livingEntity, float f, float g, PoseStack poseStack,
                              MultiBufferSource multiBufferSource, int i, CallbackInfo ci) {
        if (YSMCompat.isLoad()) {
            YSMCompat.renderLivingPost(livingEntity, g, poseStack, multiBufferSource, i);
        }
    }
}