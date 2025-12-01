package net.zhaiji.catburger.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.zhaiji.catburger.client.compat.YSMCompat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderDispatcher.class)
public class EntityRenderDispatcherMixin {
    @Inject(method = "render(Lnet/minecraft/world/entity/Entity;DDDFFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", at = @At("HEAD"))
    private void onRenderHead(Entity entity, double x, double y, double z, float yaw, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int light, CallbackInfo ci) {
        if (!YSMCompat.isLoad()) {
            return;
        }
        if (entity instanceof Player player) {
            poseStack.pushPose();
            poseStack.translate(x, y, z);
            YSMCompat.renderLivingPost(player, partialTick, poseStack, buffer, light);
            poseStack.popPose();
        }
    }
}