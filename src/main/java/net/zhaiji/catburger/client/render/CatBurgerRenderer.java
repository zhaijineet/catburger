package net.zhaiji.catburger.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.zhaiji.catburger.CatBurger;
import net.zhaiji.catburger.config.CatBurgerClientConfig;
import org.joml.Quaternionf;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.client.ICurioRenderer;

public class CatBurgerRenderer implements ICurioRenderer {
    public static BakedModel getModel() {
        return Minecraft.getInstance().getModelManager().getModel(new ModelResourceLocation(CatBurger.MOD_ID, "cat_burger", "inventory"));
    }

    public static double getFloatSpeed(LivingEntity livingEntity) {
        return CatBurgerClientConfig.float_distance / 2 * Math.sin(livingEntity.tickCount * Math.PI / CatBurgerClientConfig.time * 2);
    }

    @Override
    public <T extends LivingEntity, M extends EntityModel<T>> void render(
            ItemStack stack,
            SlotContext slotContext,
            PoseStack matrixStack,
            RenderLayerParent<T, M> renderLayerParent,
            MultiBufferSource renderTypeBuffer,
            int light, float limbSwing,
            float limbSwingAmount,
            float partialTicks,
            float ageInTicks,
            float netHeadYaw,
            float headPitch
    ) {
        LivingEntity livingEntity = slotContext.entity();
        Minecraft minecraft = Minecraft.getInstance();
        BakedModel model = getModel();
        matrixStack.pushPose();

        double yawRadians = Math.toRadians(netHeadYaw);
        double xOffset = 0;
        double yOffset = 0;
        double zOffset = 0;

        xOffset += Math.cos(yawRadians + Math.PI / 2) * CatBurgerClientConfig.front_back_offset;
        zOffset -= Math.sin(yawRadians + Math.PI / 2) * CatBurgerClientConfig.front_back_offset;

        yOffset += CatBurgerRenderer.getFloatSpeed(livingEntity);
        yOffset -= CatBurgerClientConfig.vertical_offset;
        ICurioRenderer.translateIfSneaking(matrixStack, livingEntity);

        xOffset += Math.cos(yawRadians) * CatBurgerClientConfig.left_right_offset;
        zOffset -= Math.sin(yawRadians) * CatBurgerClientConfig.left_right_offset;

        matrixStack.translate(xOffset, yOffset, zOffset);

        float scale = (float) CatBurgerClientConfig.scale;
        matrixStack.scale(scale, scale, scale);
        matrixStack.mulPose(new Quaternionf().rotateZ((float) Math.toRadians(180)));
        matrixStack.mulPose(Axis.YP.rotationDegrees(-netHeadYaw));
        matrixStack.mulPose(Axis.XP.rotationDegrees(-headPitch));
        minecraft.getItemRenderer().render(
                stack,
                ItemDisplayContext.HEAD,
                false,
                matrixStack,
                renderTypeBuffer,
                light,
                OverlayTexture.NO_OVERLAY,
                model
        );
        matrixStack.popPose();
    }
}
