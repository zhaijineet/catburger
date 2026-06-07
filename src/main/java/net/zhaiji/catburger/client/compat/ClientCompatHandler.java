package net.zhaiji.catburger.client.compat;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.event.RenderLivingEvent;
import net.zhaiji.catburger.client.render.CatBurgerRenderData;
import net.zhaiji.catburger.client.render.CatBurgerRenderer;
import net.zhaiji.catburger.compat.CompatManager;
import net.zhaiji.catburger.config.CatBurgerClientConfig;
import net.zhaiji.catburger.init.InitItem;
import org.joml.Quaternionf;
import top.theillusivec4.curios.api.SlotResult;
import top.theillusivec4.curios.client.CuriosClientMod;

import java.util.List;
import java.util.Optional;

public class ClientCompatHandler {
    public static void handlerRenderLivingEvent$Post(RenderLivingEvent.Post event) {
        LivingEntityRenderState renderState = event.getRenderState();

        // 从 Curios 预存的 CUSTOM_RENDER ContextKey 中获取槽位数据
        List<SlotResult> slots = renderState.getRenderData(CuriosClientMod.CUSTOM_RENDER);
        if (slots == null) return;

        Item item = InitItem.CAT_BURGER.get();
        Optional<SlotResult> foundSlot = slots.stream()
            .filter(slot -> slot.stack().is(item) && slot.slotContext().visible())
            .findFirst();

        if (foundSlot.isEmpty()) return;

        SlotResult slotResult = foundSlot.get();
        LivingEntity entity = slotResult.slotContext().entity();

        // TODO: TLM 更新后恢复 TLMCompat.canRender(entity) 判断
        if (CompatManager.isYSMLoad()) {
            float partialTicks = event.getPartialTick();
            ItemStack stack = slotResult.stack();
            PoseStack poseStack = event.getPoseStack();

            // 第一部分：基础数据准备
            float viewYRot = entity.getViewYRot(partialTicks);
            float headPitch = entity.getViewXRot(partialTicks);
            Minecraft minecraft = Minecraft.getInstance();
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
                    data.dragCrouchOffset = entity.isCrouching() ? -0.5 : 0;
                }

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

                data.prevDragYaw = data.dragYaw;

                if (CatBurgerClientConfig.rotationDragEnabled) {
                    float angleDiff = CatBurgerClientConfig.rotationDragUseWrapDegrees
                                      ? Mth.wrapDegrees(viewYRot - data.dragYaw)
                                      : viewYRot - data.dragYaw;
                    data.dragYaw += angleDiff * (float) CatBurgerClientConfig.rotationDragSmoothness;
                } else {
                    data.dragYaw = viewYRot;
                }

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

            // 第四部分：位置偏移计算（compat 路径使用 -usedYaw）
            double yawRadians = Math.toRadians(-usedYaw);

            double entityLerpX = Mth.lerp(partialTicks, entity.xo, entityX);
            double entityLerpY = Mth.lerp(partialTicks, entity.yo, entityY);
            double entityLerpZ = Mth.lerp(partialTicks, entity.zo, entityZ);

            double dragOffsetX = Mth.lerp(partialTicks, data.prevDragX, data.dragX) - entityLerpX;
            double dragOffsetY = Mth.lerp(partialTicks, data.prevDragY, data.dragY) - entityLerpY;
            double dragOffsetZ = Mth.lerp(partialTicks, data.prevDragZ, data.dragZ) - entityLerpZ;

            double crouchOffset = Mth.lerp(partialTicks, data.prevDragCrouchOffset, data.dragCrouchOffset);

            double xOffset =
                Math.cos(yawRadians - Mth.HALF_PI) * CatBurgerClientConfig.frontBackOffset
                - Math.cos(yawRadians) * CatBurgerClientConfig.leftRightOffset
                + dragOffsetX;

            double yOffset =
                1.5
                + CatBurgerRenderer.getFloatSpeed(entity.tickCount, partialTicks)
                + CatBurgerClientConfig.verticalOffset
                + dragOffsetY
                + crouchOffset;

            double zOffset =
                -Math.sin(yawRadians - Mth.HALF_PI) * CatBurgerClientConfig.frontBackOffset
                + Math.sin(yawRadians) * CatBurgerClientConfig.leftRightOffset
                + dragOffsetZ;

            float renderYaw;
            if (
                CatBurgerClientConfig.springEnabled
                || CatBurgerClientConfig.rotationDragEnabled && CatBurgerClientConfig.rotationDragAffectOrientation
            ) {
                renderYaw = usedYaw;
            } else {
                renderYaw = viewYRot;
            }

            // 第五部分：执行渲染
            poseStack.pushPose();

            poseStack.translate(xOffset, yOffset, zOffset);

            float scale = (float) CatBurgerClientConfig.scale;
            poseStack.scale(scale, scale, scale);
            poseStack.mulPose(new Quaternionf().rotateY((float) Math.toRadians(180)));
            poseStack.mulPose(Axis.YP.rotationDegrees(-renderYaw));
            poseStack.mulPose(Axis.XP.rotationDegrees(-headPitch));

            // 使用新 API 渲染 item
            ItemStackRenderState itemRenderState = new ItemStackRenderState();
            minecraft.getItemModelResolver().updateForLiving(
                itemRenderState,
                stack,
                ItemDisplayContext.HEAD,
                entity
            );

            if (!itemRenderState.isEmpty()) {
                itemRenderState.submit(
                    poseStack,
                    event.getSubmitNodeCollector(),
                    renderState.lightCoords,
                    OverlayTexture.NO_OVERLAY,
                    renderState.outlineColor
                );
            }

            poseStack.popPose();
        }
    }
}
