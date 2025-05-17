package net.zhaiji.catburger;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.emi.trinkets.api.client.TrinketRendererRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityFeatureRendererRegistrationCallback;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.LivingEntity;
import net.zhaiji.catburger.client.compat.YSMCompat;
import net.zhaiji.catburger.client.render.CatBurgerRenderer;
import net.zhaiji.catburger.init.InitItem;

public class CatBurgerClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        if (!YSMCompat.isLoad()) {
            TrinketRendererRegistry.registerRenderer(InitItem.CAT_BURGER, new CatBurgerRenderer());
//            registerYSMCompatRenderer();
        }
    }

//    private void registerYSMCompatRenderer() {
//        LivingEntityFeatureRendererRegistrationCallback.EVENT.register(
//                (entityType, entityRenderer, registrationHelper, context) -> {
//                    registrationHelper.register(new YSMCompatFeatureRenderer<>(entityRenderer));
//                }
//        );
//    }
//
//    private static class YSMCompatFeatureRenderer<T extends LivingEntity, M extends EntityModel<T>>
//            extends RenderLayer<T, M> {
//
//        public YSMCompatFeatureRenderer(RenderLayerParent<T, M> parent) {
//            super(parent);
//        }
//
//        @Override
//        public void render(
//                PoseStack poseStack,
//                MultiBufferSource multiBufferSource,
//                int light,
//                T livingEntity,
//                float limbAngle,
//                float limbDistance,
//                float tickDelta,
//                float animationProgress,
//                float headYaw,
//                float headPitch) {
//
//            YSMCompat.renderLivingPost(livingEntity, tickDelta, poseStack, multiBufferSource, light);
//        }
//    }
}