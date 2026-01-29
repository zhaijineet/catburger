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
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.client.event.RenderLivingEvent;
import net.zhaiji.catburger.client.render.CatBurgerRenderer;
import net.zhaiji.catburger.config.CatBurgerClientConfig;
import net.zhaiji.catburger.init.InitItem;
import org.joml.Quaternionf;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;

import java.util.Optional;

public class CompatManager {
    public static boolean YSMLoad = false;
    public static boolean TLMLoad = false;

    public static boolean isYSMLoad() {
        YSMLoad = ModList.get().isLoaded("yes_steve_model");
        return YSMLoad;
    }

    public static boolean isTLMLoad() {
        TLMLoad = ModList.get().isLoaded("touhou_little_maid");
        return TLMLoad;
    }

    public static void handlerRenderLivingEvent$Post(RenderLivingEvent.Post event) {
        LivingEntity entity = event.getEntity();
        if (!YSMLoad && !(TLMLoad && TLMCompat.canRender(entity))) return;
        Item item = InitItem.CAT_BURGER.get();
        CuriosApi.getCuriosInventory(entity).ifPresent(iCuriosItemHandler -> {
            Optional<SlotResult> slotResult = iCuriosItemHandler.findFirstCurio(item);
            if (slotResult.isPresent() && slotResult.get().slotContext().visible()) {
                PoseStack matrixStack = event.getPoseStack();
                float partialTicks = event.getPartialTick();
                float netHeadYaw = Mth.rotLerp(partialTicks, entity.yHeadRotO, entity.yHeadRot);
                float headPitch = Mth.lerp(partialTicks, entity.xRotO, entity.getXRot());
                netHeadYaw = Mth.wrapDegrees(netHeadYaw);
                MultiBufferSource renderTypeBuffer = event.getMultiBufferSource();
                int light = event.getPackedLight();
                Minecraft minecraft = Minecraft.getInstance();
                BakedModel model = CatBurgerRenderer.getModel();
                matrixStack.pushPose();
                double yawRadians = Math.toRadians(-netHeadYaw);

                double xOffset = 0;
                double yOffset = 0;
                double zOffset = 0;

                xOffset -= Math.cos(yawRadians + Math.PI / 2) * CatBurgerClientConfig.frontBackOffset;
                zOffset += Math.sin(yawRadians + Math.PI / 2) * CatBurgerClientConfig.frontBackOffset;

                yOffset += CatBurgerRenderer.getFloatSpeed(entity, partialTicks);
                yOffset += CatBurgerClientConfig.verticalOffset;

                if (entity.isCrouching()) {
                    yOffset += 1;
                } else {
                    yOffset += 1.5;
                }

                xOffset += Math.cos(yawRadians) * CatBurgerClientConfig.leftRightOffset;
                zOffset -= Math.sin(yawRadians) * CatBurgerClientConfig.leftRightOffset;

                matrixStack.translate(xOffset, yOffset, zOffset);

                float scale = (float) CatBurgerClientConfig.scale;
                matrixStack.scale(scale, scale, scale);
                matrixStack.mulPose(new Quaternionf().rotateY((float) Math.toRadians(180)));
                matrixStack.mulPose(Axis.YP.rotationDegrees(-netHeadYaw));
                matrixStack.mulPose(Axis.XP.rotationDegrees(-headPitch));
                minecraft.getItemRenderer().render(
                        item.getDefaultInstance(),
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
