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
        // 注册Trinkets渲染器
        TrinketRendererRegistry.registerRenderer(InitItem.CAT_BURGER, new CatBurgerRenderer());

        // 如果YSM模组已加载，注册实体渲染事件
        if (YSMCompat.isLoad()) {
            registerYSMCompatRenderer();
        }
    }

    private void registerYSMCompatRenderer() {
        // 使用Fabric的实体渲染回调
        LivingEntityFeatureRendererRegistrationCallback.EVENT.register(
                (entityType, entityRenderer, registrationHelper, context) -> {
                    // 创建一个自定义的特性渲染器，用于调用YSMCompat的渲染方法
                    registrationHelper.register(new YSMCompatFeatureRenderer<>(entityRenderer));
                }
        );
    }

    // 自定义特性渲染器，用于调用YSMCompat的渲染方法
    private static class YSMCompatFeatureRenderer<T extends LivingEntity, M extends EntityModel<T>>
            extends RenderLayer<T, M> {

        public YSMCompatFeatureRenderer(RenderLayerParent<T, M> parent) {
            super(parent);
        }

        @Override
        public void render(
                PoseStack poseStack,
                MultiBufferSource multiBufferSource,
                int light,
                T livingEntity,
                float limbAngle,
                float limbDistance,
                float tickDelta,
                float animationProgress,
                float headYaw,
                float headPitch) {

            // 调用YSMCompat的渲染方法
            YSMCompat.renderLivingPost(livingEntity, tickDelta, poseStack, multiBufferSource, light);
        }
    }
}