package net.zhaiji.catburger.client.compat;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.neoforged.fml.loading.LoadingModList;
import net.neoforged.neoforge.client.event.RenderLivingEvent;
import net.zhaiji.catburger.config.CatBurgerClientConfig;
import net.zhaiji.catburger.client.render.CatBurgerRenderer;
import net.zhaiji.catburger.init.InitItem;
import org.joml.Quaternionf;
import top.theillusivec4.curios.api.CuriosApi;

public class YSMCompat {
    private static final String YSM_ID = "yes_steve_model";

    private static float netHeadYaw;
    private static float headPitch;

    public static boolean isLoad(){
        return LoadingModList.get().getModFileById("yes_steve_model") != null;
    }

    // LivingEntityRenderer render
    private static void getHeadRot(float partialTicks, LivingEntity entity) {
        float yaw = Mth.rotLerp(partialTicks, entity.yHeadRotO, entity.yHeadRot);
        float pitch = Mth.lerp(partialTicks, entity.xRotO, entity.getXRot());
        yaw = Mth.wrapDegrees(yaw);
        netHeadYaw = yaw;
        headPitch = pitch;
    }

    public static void RenderLivingEvent(RenderLivingEvent.Post event) {
        Item item = InitItem.CAT_BURGER.get();
        LivingEntity livingEntity = event.getEntity();
        PoseStack matrixStack = event.getPoseStack();
        MultiBufferSource renderTypeBuffer = event.getMultiBufferSource();
        int light = event.getPackedLight();
        CuriosApi.getCuriosInventory(livingEntity).ifPresent(iCuriosItemHandler -> {
            if (!iCuriosItemHandler.findCurios(item).isEmpty()) {
                Minecraft minecraft = Minecraft.getInstance();
                BakedModel model = CatBurgerRenderer.getModel();
                matrixStack.pushPose();
                getHeadRot(event.getPartialTick(), livingEntity);
                // 不知道为什么没有用
                // ICurioRenderer.translateIfSneaking(matrixStack, livingEntity);
                double yawRadians = Math.toRadians(-netHeadYaw);
                double xOffset = 0;
                double yOffset = 0;
                double zOffset = 0;

                xOffset -= Math.cos(yawRadians + Math.PI / 2) * CatBurgerClientConfig.front_back_offset;
                zOffset += Math.sin(yawRadians + Math.PI / 2) * CatBurgerClientConfig.front_back_offset;

                yOffset += CatBurgerRenderer.getFloatSpeed(livingEntity);
                yOffset += CatBurgerClientConfig.vertical_offset;

                if (livingEntity.isCrouching()) {
                    yOffset += 1;
                } else {
                    yOffset += 1.5;
                }

                xOffset += Math.cos(yawRadians) * CatBurgerClientConfig.left_right_offset;
                zOffset -= Math.sin(yawRadians) * CatBurgerClientConfig.left_right_offset;

                matrixStack.translate(xOffset, yOffset, zOffset);

                float scale = (float) CatBurgerClientConfig.scale;
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
        });
    }
}
