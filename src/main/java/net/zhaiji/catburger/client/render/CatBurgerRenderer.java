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
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.zhaiji.catburger.CatBurger;
import org.joml.Quaternionf;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.client.ICurioRenderer;

public class CatBurgerRenderer implements ICurioRenderer {
    public double getFloatSpeed(LivingEntity livingEntity) {
        return 0.05 * Math.sin(livingEntity.tickCount * Math.PI / 18);
    }

    @Override
    public <T extends LivingEntity, M extends EntityModel<T>> void render(ItemStack stack,
                                                                          SlotContext slotContext,
                                                                          PoseStack matrixStack,
                                                                          RenderLayerParent<T, M> renderLayerParent,
                                                                          MultiBufferSource renderTypeBuffer,
                                                                          int light, float limbSwing,
                                                                          float limbSwingAmount,
                                                                          float partialTicks,
                                                                          float ageInTicks,
                                                                          float netHeadYaw,
                                                                          float headPitch) {
        LivingEntity livingEntity = slotContext.entity();
        Minecraft client = Minecraft.getInstance();
        BakedModel model = client.getModelManager().getModel(ModelResourceLocation.inventory(ResourceLocation.fromNamespaceAndPath(CatBurger.MOD_ID, "cat_burger")));
        matrixStack.pushPose();
        ICurioRenderer.translateIfSneaking(matrixStack, livingEntity);
        matrixStack.mulPose(new Quaternionf().rotateZ((float) Math.toRadians(180)));
        matrixStack.scale(0.7f, 0.7f, 0.7f);
        matrixStack.mulPose(Axis.YP.rotationDegrees(-netHeadYaw));
        matrixStack.mulPose(Axis.XP.rotationDegrees(-headPitch));
        matrixStack.translate(0, this.getFloatSpeed(slotContext.entity()), 0);
        client.getItemRenderer().render(
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
