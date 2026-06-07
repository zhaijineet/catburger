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
import net.neoforged.neoforge.client.event.RenderLivingEvent;
import net.zhaiji.catburger.client.render.CatBurgerRenderData;
import net.zhaiji.catburger.client.render.CatBurgerRenderer;
import net.zhaiji.catburger.compat.CompatManager;
import net.zhaiji.catburger.compat.TLMCompat;
import net.zhaiji.catburger.config.CatBurgerClientConfig;
import net.zhaiji.catburger.init.InitItem;
import org.joml.Quaternionf;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;

import java.util.Optional;

public class ClientCompatHandler {
    public static void handlerRenderLivingEvent$Post(RenderLivingEvent.Post event) {
        LivingEntity entity = event.getEntity();
        if (!CompatManager.isYSMLoad() && !(CompatManager.isTLMLoad() && TLMCompat.canRender(entity))) return;
        Item item = InitItem.CAT_BURGER.get();
        CuriosApi.getCuriosInventory(entity).ifPresent(iCuriosItemHandler -> {
            Optional<SlotResult> slotResult = iCuriosItemHandler.findFirstCurio(item);
            if (slotResult.isPresent() && slotResult.get().slotContext().visible()) {
                // 第一部分：基础数据准备
                PoseStack matrixStack = event.getPoseStack();
                float partialTicks = event.getPartialTick();
                float viewYRot = entity.getViewYRot(partialTicks);
                float headPitch = entity.getViewXRot(partialTicks);
                MultiBufferSource renderTypeBuffer = event.getMultiBufferSource();
                int light = event.getPackedLight();
                Minecraft minecraft = Minecraft.getInstance();
                BakedModel model = CatBurgerRenderer.getModel();

                // 获取或创建渲染数据
                CatBurgerRenderData data = CatBurgerRenderData.RENDER_DATA_MAP.computeIfAbsent(
                        entity,
                        e -> new CatBurgerRenderData(viewYRot, entity)
                );

                // 第二部分：物理系统更新（每tick执行一次）
                int currentTick = entity.tickCount;
                double entityX = entity.getX();
                double entityY = entity.getY();
                double entityZ = entity.getZ();

                if (currentTick != data.lastTick) {
                    // 断点检测与重置
                    if (currentTick - data.lastTick > 5) {
                        data.dragX = entityX;
                        data.dragY = entityY;
                        data.dragZ = entityZ;
                        data.dragYaw = viewYRot;
                        data.springYaw = viewYRot;
                        data.velocity = 0;
                        // 重置蹲姿高度偏移
                        data.dragCrouchOffset = entity.isCrouching() ? -0.5 : 0;
                    }

                    // 位置拖拽系统更新
                    data.prevDragX = data.dragX;
                    data.prevDragY = data.dragY;
                    data.prevDragZ = data.dragZ;
                    data.prevDragCrouchOffset = data.dragCrouchOffset;

                    double targetCrouchOffset = entity.isCrouching() ? -0.5 : 0;

                    if (CatBurgerClientConfig.dragEnabled) {
                        data.dragX += (entityX - data.dragX) * CatBurgerClientConfig.dragStrength;
                        data.dragY += (entityY - data.dragY) * CatBurgerClientConfig.dragStrength;
                        data.dragZ += (entityZ - data.dragZ) * CatBurgerClientConfig.dragStrength;
                        data.dragCrouchOffset += (targetCrouchOffset - data.dragCrouchOffset) * CatBurgerClientConfig.dragStrength;
                    } else {
                        data.dragX = entityX;
                        data.dragY = entityY;
                        data.dragZ = entityZ;
                        data.dragCrouchOffset = targetCrouchOffset;
                    }

                    // 旋转拖拽系统更新
                    data.prevDragYaw = data.dragYaw;

                    if (CatBurgerClientConfig.rotationDragEnabled) {
                        float angleDiff = CatBurgerClientConfig.rotationDragUseWrapDegrees
                                ? Mth.wrapDegrees(viewYRot - data.dragYaw)
                                : viewYRot - data.dragYaw;
                        data.dragYaw += angleDiff * (float) CatBurgerClientConfig.rotationDragSmoothness;
                    } else {
                        data.dragYaw = viewYRot;
                    }

                    // 弹簧物理系统更新
                    data.prevSpringYaw = data.springYaw;

                    if (CatBurgerClientConfig.springEnabled) {
                        float displacement = viewYRot - data.springYaw;
                        float springForce = (float) CatBurgerClientConfig.springStiffness * displacement;
                        data.velocity = data.velocity * (float) CatBurgerClientConfig.springDamping + springForce;
                        data.springYaw += data.velocity;
                    } else {
                        data.springYaw = viewYRot;
                        data.velocity = 0;
                    }

                    data.lastTick = currentTick;
                }

                // 第三部分：计算使用的角度（优先级：弹簧 > 旋转拖拽 > 原始）
                float usedYaw;
                if (CatBurgerClientConfig.springEnabled) {
                    usedYaw = Mth.lerp(partialTicks, data.prevSpringYaw, data.springYaw);
                } else if (CatBurgerClientConfig.rotationDragEnabled) {
                    usedYaw = Mth.lerp(partialTicks, data.prevDragYaw, data.dragYaw);
                } else {
                    usedYaw = viewYRot;
                }

                // 第四部分：位置偏移计算
                double yawRadians = Math.toRadians(-usedYaw);

                // 实体插值位置
                double entityLerpX = Mth.lerp(partialTicks, entity.xo, entityX);
                double entityLerpY = Mth.lerp(partialTicks, entity.yo, entityY);
                double entityLerpZ = Mth.lerp(partialTicks, entity.zo, entityZ);

                // 拖拽偏移量（直接使用世界坐标偏移）
                double dragOffsetX = Mth.lerp(partialTicks, data.prevDragX, data.dragX) - entityLerpX;
                double dragOffsetY = Mth.lerp(partialTicks, data.prevDragY, data.dragY) - entityLerpY;
                double dragOffsetZ = Mth.lerp(partialTicks, data.prevDragZ, data.dragZ) - entityLerpZ;

                // 蹲姿高度偏移插值
                double crouchOffset = Mth.lerp(partialTicks, data.prevDragCrouchOffset, data.dragCrouchOffset);

                // 最终偏移量合成（保持原有的负号模式）
                double xOffset =
                        Math.cos(yawRadians - Mth.HALF_PI) * CatBurgerClientConfig.frontBackOffset
                        - Math.cos(yawRadians) * CatBurgerClientConfig.leftRightOffset
                        + dragOffsetX;

                double yOffset =
                        1.5 // 额外补偿高度
                        + CatBurgerRenderer.getFloatSpeed(entity, partialTicks)
                        + CatBurgerClientConfig.verticalOffset
                        + dragOffsetY
                        + crouchOffset;  // 添加拖拽的蹲姿偏移

                double zOffset =
                        -Math.sin(yawRadians - Mth.HALF_PI) * CatBurgerClientConfig.frontBackOffset
                        + Math.sin(yawRadians) * CatBurgerClientConfig.leftRightOffset
                        + dragOffsetZ;

                // 3.2 模型渲染使用的角度（受朝向开关控制）
                float renderYaw;
                if (
                        CatBurgerClientConfig.springEnabled
                        || CatBurgerClientConfig.rotationDragEnabled && CatBurgerClientConfig.rotationDragAffectOrientation
                ) {
                    // 弹簧系统或朝向影响：使用拖拽旋转
                    renderYaw = usedYaw;
                } else {
                    // 禁用朝向影响：使用原始视角旋转（立即跟随）
                    renderYaw = viewYRot;
                }

                // 第五部分：执行渲染
                matrixStack.pushPose();

                matrixStack.translate(xOffset, yOffset, zOffset);

                float scale = (float) CatBurgerClientConfig.scale;
                matrixStack.scale(scale, scale, scale);
                matrixStack.mulPose(new Quaternionf().rotateY((float) Math.toRadians(180)));
                matrixStack.mulPose(Axis.YP.rotationDegrees(-renderYaw));
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
