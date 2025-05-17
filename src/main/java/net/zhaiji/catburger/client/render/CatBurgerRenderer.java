package net.zhaiji.catburger.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.client.TrinketRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.zhaiji.catburger.CatBurger;
import net.zhaiji.catburger.config.CatBurgerConfig;
import org.joml.Quaternionf;

public class CatBurgerRenderer implements TrinketRenderer {
    public static BakedModel getModel() {
        return Minecraft.getInstance().getModelManager().getModel(new ModelResourceLocation(CatBurger.MOD_ID, "cat_burger", "inventory"));
    }

    public static double getFloatSpeed(LivingEntity livingEntity) {
        return CatBurgerConfig.get().client.float_distance / 2 * Math.sin(livingEntity.tickCount * Math.PI / CatBurgerConfig.get().client.time * 2);
    }

    @Override
    public void render(
            ItemStack stack,
            SlotReference slotReference,
            EntityModel<? extends LivingEntity> contextModel,
            PoseStack matrices,
            MultiBufferSource vertexConsumers,
            int light,
            LivingEntity entity,
            float limbAngle,
            float limbDistance,
            float tickDelta,
            float animationProgress,
            float headYaw,
            float headPitch
    ) {
        Minecraft minecraft = Minecraft.getInstance();
        BakedModel model = getModel();
        matrices.pushPose();

        double yawRadians = Math.toRadians(headYaw);
        double xOffset = 0;
        double yOffset = 0;
        double zOffset = 0;

        xOffset += Math.cos(yawRadians + Math.PI / 2) * CatBurgerConfig.get().client.front_back_offset;
        zOffset -= Math.sin(yawRadians + Math.PI / 2) * CatBurgerConfig.get().client.front_back_offset;

        yOffset += CatBurgerRenderer.getFloatSpeed(entity);
        yOffset -= CatBurgerConfig.get().client.vertical_offset;

        if (entity.isCrouching() && !entity.isSwimming() && !entity.isPassenger()) {
            matrices.translate(0.0F, 0.2F, 0.0F);
        }

        xOffset += Math.cos(yawRadians) * CatBurgerConfig.get().client.left_right_offset;
        zOffset -= Math.sin(yawRadians) * CatBurgerConfig.get().client.left_right_offset;

        matrices.translate(xOffset, yOffset, zOffset);

        float scale = (float) CatBurgerConfig.get().client.scale;
        matrices.scale(scale, scale, scale);
        matrices.mulPose(new Quaternionf().rotateZ((float) Math.toRadians(180)));
        matrices.mulPose(Axis.YP.rotationDegrees(-headYaw));
        matrices.mulPose(Axis.XP.rotationDegrees(-headPitch));
        minecraft.getItemRenderer().render(
                stack,
                ItemDisplayContext.HEAD,
                false,
                matrices,
                vertexConsumers,
                light,
                OverlayTexture.NO_OVERLAY,
                model
        );
        matrices.popPose();
    }
}