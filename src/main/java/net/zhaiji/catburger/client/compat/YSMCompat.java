package net.zhaiji.catburger.client.compat;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketsApi;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.zhaiji.catburger.client.render.CatBurgerRenderer;
import net.zhaiji.catburger.config.CatBurgerConfig;
import net.zhaiji.catburger.init.InitItem;
import org.joml.Quaternionf;

import java.util.List;
import java.util.Optional;

public class YSMCompat {
    private static final String YSM_ID = "yes_steve_model";

    private static float netHeadYaw;
    private static float headPitch;

    public static boolean isLoad(){
        return FabricLoader.getInstance().isModLoaded(YSM_ID);
    }

    // LivingEntityRenderer render
    private static void getHeadRot(float partialTicks, LivingEntity entity) {
        float yaw = Mth.rotLerp(partialTicks, entity.yHeadRotO, entity.yHeadRot);
        float pitch = Mth.lerp(partialTicks, entity.xRotO, entity.getXRot());
        yaw = Mth.wrapDegrees(yaw);
        netHeadYaw = yaw;
        headPitch = pitch;
    }

    // 在Fabric中，我们需要使用EntityRenderEvents或Mixin来替代Forge的RenderLivingEvent
    public static void renderLivingPost(LivingEntity livingEntity, float partialTick, PoseStack matrixStack,
                                        MultiBufferSource renderTypeBuffer, int light) {
        Item item = InitItem.CAT_BURGER;

        // 使用Trinkets API获取饰品组件
        Optional<TrinketComponent> component = TrinketsApi.getTrinketComponent(livingEntity);
        if (component.isPresent()) {
            // 直接使用TrinketComponent的isEquipped方法检查是否装备了猫汉堡物品
            boolean hasCatBurger = component.get().isEquipped(item);


            if (hasCatBurger) {
                Minecraft minecraft = Minecraft.getInstance();
                BakedModel model = CatBurgerRenderer.getModel();
                matrixStack.pushPose();
                getHeadRot(partialTick, livingEntity);
                
                double yawRadians = Math.toRadians(-netHeadYaw);
                double xOffset = 0;
                double yOffset = 0;
                double zOffset = 0;

                xOffset -= Math.cos(yawRadians + Math.PI / 2) * CatBurgerConfig.get().client.front_back_offset;
                zOffset += Math.sin(yawRadians + Math.PI / 2) * CatBurgerConfig.get().client.front_back_offset;

                yOffset += CatBurgerRenderer.getFloatSpeed(livingEntity);
                yOffset += CatBurgerConfig.get().client.vertical_offset;

                if (livingEntity.isCrouching()) {
                    yOffset += 1;
                } else {
                    yOffset += 1.5;
                }

                xOffset += Math.cos(yawRadians) * CatBurgerConfig.get().client.left_right_offset;
                zOffset -= Math.sin(yawRadians) * CatBurgerConfig.get().client.left_right_offset;

                matrixStack.translate(xOffset, yOffset, zOffset);

                float scale = (float) CatBurgerConfig.get().client.scale;
                matrixStack.scale(scale, scale, scale);
                matrixStack.mulPose(new Quaternionf().rotateY((float) Math.toRadians(180)));
                matrixStack.mulPose(Axis.YP.rotationDegrees(-netHeadYaw));
                matrixStack.mulPose(Axis.XP.rotationDegrees(-headPitch));
                minecraft.getItemRenderer().render(
                        new ItemStack(item),
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
    }
}
