package net.zhaiji.catburger.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.zhaiji.catburger.config.CatBurgerClientConfig;
import org.joml.Quaternionf;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.client.ICurioRenderer;

public class CatBurgerRenderer implements ICurioRenderer {
    private static final Minecraft MC = Minecraft.getInstance();
    private static final ItemStackRenderState ITEM_RENDER_STATE = new ItemStackRenderState();

    private static double getFloatSpeed(LivingEntity livingEntity, float partialTicks) {
        return CatBurgerClientConfig.floatDistance / 2.0 * Math.sin((livingEntity.tickCount + partialTicks) * Mth.HALF_PI / CatBurgerClientConfig.time);
    }

    @Override
    public <S extends LivingEntityRenderState, M extends EntityModel<? super S>> void render(
            ItemStack stack,
            SlotContext slotContext,
            PoseStack poseStack,
            SubmitNodeCollector submitNodeCollector,
            int packedLight,
            S renderState,
            RenderLayerParent<S, M> renderLayerParent,
            EntityRendererProvider.Context context,
            float yRotation,
            float xRotation
    ) {
        LivingEntity entity = slotContext.entity();
        if (entity == null) {
            return;
        }

        float partialTicks = Mth.clamp(renderState.ageInTicks - entity.tickCount, 0.0F, 1.0F);
        float viewYRot = entity.getViewYRot(partialTicks);
        CatBurgerRenderData data = CatBurgerRenderData.RENDER_DATA_MAP.computeIfAbsent(entity, e -> new CatBurgerRenderData(viewYRot, entity));

        int currentTick = entity.tickCount;
        double entityX = entity.getX();
        double entityY = entity.getY();
        double entityZ = entity.getZ();

        if (currentTick != data.lastTick) {
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
                data.dragX = data.dragX + (entityX - data.dragX) * CatBurgerClientConfig.dragStrength;
                data.dragY = data.dragY + (entityY - data.dragY) * CatBurgerClientConfig.dragStrength;
                data.dragZ = data.dragZ + (entityZ - data.dragZ) * CatBurgerClientConfig.dragStrength;
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
                data.dragYaw = data.dragYaw + angleDiff * (float) CatBurgerClientConfig.rotationDragSmoothness;
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

        float usedYaw;
        if (CatBurgerClientConfig.springEnabled) {
            usedYaw = Mth.lerp(partialTicks, data.prevSpringYaw, data.springYaw);
        } else if (CatBurgerClientConfig.rotationDragEnabled) {
            usedYaw = Mth.lerp(partialTicks, data.prevDragYaw, data.dragYaw);
        } else {
            usedYaw = viewYRot;
        }

        float bodyRot = renderState.bodyRot;
        float offsetHeadYaw = usedYaw - bodyRot;
        double yawRadians = Math.toRadians(offsetHeadYaw);

        double entityLerpX = Mth.lerp(partialTicks, entity.xo, entityX);
        double entityLerpY = Mth.lerp(partialTicks, entity.yo, entityY);
        double entityLerpZ = Mth.lerp(partialTicks, entity.zo, entityZ);

        double dragOffsetX = Mth.lerp(partialTicks, data.prevDragX, data.dragX) - entityLerpX;
        double dragOffsetY = Mth.lerp(partialTicks, data.prevDragY, data.dragY) - entityLerpY;
        double dragOffsetZ = Mth.lerp(partialTicks, data.prevDragZ, data.dragZ) - entityLerpZ;

        double crouchOffset = Mth.lerp(partialTicks, data.prevDragCrouchOffset, data.dragCrouchOffset);

        double bodyRotation = Math.toRadians(bodyRot);
        double cosBody = Math.cos(bodyRotation);
        double sinBody = Math.sin(bodyRotation);
        double localDragOffsetX = -dragOffsetX * cosBody - dragOffsetZ * sinBody;
        double localDragOffsetZ = dragOffsetX * sinBody - dragOffsetZ * cosBody;

        double xOffset =
                Math.cos(yawRadians - Mth.HALF_PI) * CatBurgerClientConfig.frontBackOffset
                        + Math.cos(yawRadians) * CatBurgerClientConfig.leftRightOffset
                        + localDragOffsetX;

        double yOffset =
                getFloatSpeed(entity, partialTicks)
                        + CatBurgerClientConfig.verticalOffset
                        + dragOffsetY
                        + crouchOffset;

        double zOffset =
                Math.sin(yawRadians - Mth.HALF_PI) * CatBurgerClientConfig.frontBackOffset
                        + Math.sin(yawRadians) * CatBurgerClientConfig.leftRightOffset
                        + localDragOffsetZ;

        float renderHeadYaw;
        if (CatBurgerClientConfig.springEnabled
                || (CatBurgerClientConfig.rotationDragEnabled && CatBurgerClientConfig.rotationDragAffectOrientation)) {
            renderHeadYaw = offsetHeadYaw;
        } else {
            renderHeadYaw = viewYRot - bodyRot;
        }

        poseStack.pushPose();
        poseStack.mulPose(new Quaternionf().rotateZ((float) Math.toRadians(180)));
        poseStack.translate(xOffset, yOffset, zOffset);

        float scale = (float) CatBurgerClientConfig.scale;
        poseStack.scale(scale, scale, scale);
        poseStack.mulPose(Axis.YP.rotationDegrees(-renderHeadYaw));
        poseStack.mulPose(Axis.XP.rotationDegrees(-xRotation));

        if (MC.level != null) {
            MC.getItemModelResolver().updateForTopItem(ITEM_RENDER_STATE, stack, ItemDisplayContext.HEAD, MC.level, null, 0);
            ITEM_RENDER_STATE.submit(poseStack, submitNodeCollector, packedLight, OverlayTexture.NO_OVERLAY, renderState.outlineColor);
        }

        poseStack.popPose();
    }
}
