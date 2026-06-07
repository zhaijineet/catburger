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
import net.minecraft.util.Mth;
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

    public static double getFloatSpeed(LivingEntity livingEntity, float partialTicks) {
        return CatBurgerClientConfig.floatDistance / 2 * Math.sin((livingEntity.tickCount + partialTicks) * Mth.HALF_PI / CatBurgerClientConfig.time);
    }

    @Override
    public <T extends LivingEntity, M extends EntityModel<T>> void render(
            ItemStack stack,
            SlotContext slotContext,
            PoseStack matrixStack,
            RenderLayerParent<T, M> renderLayerParent,
            MultiBufferSource renderTypeBuffer,
            int light,
            float limbSwing,
            float limbSwingAmount,
            float partialTicks,
            float ageInTicks,
            float netHeadYaw,
            float headPitch
    ) {
        // 第一部分：基础数据准备
        LivingEntity entity = slotContext.entity();
        Minecraft minecraft = Minecraft.getInstance();
        BakedModel model = getModel();
        float viewYRot = entity.getViewYRot(partialTicks);
        CatBurgerRenderData data = CatBurgerRenderData.RENDER_DATA_MAP.computeIfAbsent(entity, e -> new CatBurgerRenderData(viewYRot, entity));

        // 第二部分：物理系统更新（每tick执行一次）
        int currentTick = entity.tickCount;
        double entityX = entity.getX();
        double entityY = entity.getY();
        double entityZ = entity.getZ();
        if (currentTick != data.lastTick) {
            // 2.1 断点检测与重置
            if (currentTick - data.lastTick > 5) {
                // 重置拖拽位置
                data.dragX = entityX;
                data.dragY = entityY;
                data.dragZ = entityZ;
                // 重置拖拽旋转
                data.dragYaw = viewYRot;
                // 重置为当前绝对头部旋转
                data.springYaw = viewYRot;
                data.velocity = 0;
                // 重置蹲姿高度偏移
                data.dragCrouchOffset = entity.isCrouching() ? -0.5 : 0;
            }

            // 2.2 位置拖拽系统更新
            data.prevDragX = data.dragX;
            data.prevDragY = data.dragY;
            data.prevDragZ = data.dragZ;
            data.prevDragCrouchOffset = data.dragCrouchOffset;

            double targetCrouchOffset = entity.isCrouching() ? -0.5 : 0;
            // 线性插值实现平滑拖拽
            if (CatBurgerClientConfig.dragEnabled) {
                data.dragX = data.dragX + (entityX - data.dragX) * CatBurgerClientConfig.dragStrength;
                data.dragY = data.dragY + (entityY - data.dragY) * CatBurgerClientConfig.dragStrength;
                data.dragZ = data.dragZ + (entityZ - data.dragZ) * CatBurgerClientConfig.dragStrength;
                data.dragCrouchOffset += (targetCrouchOffset - data.dragCrouchOffset) * CatBurgerClientConfig.dragStrength;
            } else {
                // 禁用时直接使用实体位置
                data.dragX = entityX;
                data.dragY = entityY;
                data.dragZ = entityZ;
                data.dragCrouchOffset = targetCrouchOffset;
            }

            // 2.3 旋转拖拽系统更新
            data.prevDragYaw = data.dragYaw;

            // 旋转拖拽系统更新
            if (CatBurgerClientConfig.rotationDragEnabled) {
                // 根据配置选择角度差值计算方式
                float angleDiff =
                        CatBurgerClientConfig.rotationDragUseWrapDegrees
                                // 使用 wrapDegrees 处理角度环绕（最短路径）
                                ? Mth.wrapDegrees(viewYRot - data.dragYaw)
                                // 直接差值（可能产生整圈旋转效果）
                                : viewYRot - data.dragYaw;
                data.dragYaw = data.dragYaw + angleDiff * (float) CatBurgerClientConfig.rotationDragSmoothness;
            } else {
                // 禁用时直接使用目标值
                data.dragYaw = viewYRot;
            }

            // 2.4 弹簧物理系统更新
            data.prevSpringYaw = data.springYaw;

            // 弹簧-阻尼系统物理更新
            if (CatBurgerClientConfig.springEnabled) {
                float displacement = viewYRot - data.springYaw;
                float springForce = (float) CatBurgerClientConfig.springStiffness * displacement;
                data.velocity = data.velocity * (float) CatBurgerClientConfig.springDamping + springForce;
                data.springYaw += data.velocity;
            } else {
                // 禁用时直接使用目标值
                data.springYaw = viewYRot;
                data.velocity = 0;
            }

            data.lastTick = currentTick;
        }

        // 第三部分：旋转角度计算
        // 3.1 位置偏移使用的角度（优先级：弹簧系统 > 旋转拖拽 > 原始值）
        float usedYaw;
        if (CatBurgerClientConfig.springEnabled) {
            usedYaw = Mth.lerp(partialTicks, data.prevSpringYaw, data.springYaw);
        } else if (CatBurgerClientConfig.rotationDragEnabled) {
            usedYaw = Mth.lerp(partialTicks, data.prevDragYaw, data.dragYaw);
        } else {
            usedYaw = viewYRot;
        }

        // 转换为相对旋转（相对于身体）- 用于位置偏移计算
        float bodyRot = Mth.lerp(partialTicks, entity.yBodyRotO, entity.yBodyRot);
        float offsetHeadYaw = usedYaw - bodyRot;
        double yawRadians = Math.toRadians(offsetHeadYaw);

        // 第四部分：位置偏移计算
        // 4.1 实体插值位置
        double entityLerpX = Mth.lerp(partialTicks, entity.xo, entityX);
        double entityLerpY = Mth.lerp(partialTicks, entity.yo, entityY);
        double entityLerpZ = Mth.lerp(partialTicks, entity.zo, entityZ);

        // 4.2 拖拽偏移量
        double dragOffsetX = Mth.lerp(partialTicks, data.prevDragX, data.dragX) - entityLerpX;
        double dragOffsetY = Mth.lerp(partialTicks, data.prevDragY, data.dragY) - entityLerpY;
        double dragOffsetZ = Mth.lerp(partialTicks, data.prevDragZ, data.dragZ) - entityLerpZ;

        // 4.2.1 蹲姿高度偏移插值
        double crouchOffset = Mth.lerp(partialTicks, data.prevDragCrouchOffset, data.dragCrouchOffset);

        // 4.3 世界坐标转局部坐标
        double bodyRotation = Math.toRadians(bodyRot);
        double cosBody = Math.cos(bodyRotation);
        double sinBody = Math.sin(bodyRotation);
        double localDragOffsetX = -dragOffsetX * cosBody - dragOffsetZ * sinBody;
        double localDragOffsetZ = dragOffsetX * sinBody - dragOffsetZ * cosBody;

        // 4.4 最终偏移量合成
        double xOffset =
                Math.cos(yawRadians - Mth.HALF_PI) * CatBurgerClientConfig.frontBackOffset
                + Math.cos(yawRadians) * CatBurgerClientConfig.leftRightOffset
                + localDragOffsetX;  // 添加局部坐标拖拽X偏移

        double yOffset =
                CatBurgerRenderer.getFloatSpeed(entity, partialTicks)
                + CatBurgerClientConfig.verticalOffset
                + dragOffsetY  // 添加拖拽Y偏移
                + crouchOffset;  // 添加拖拽的蹲姿偏移

        double zOffset =
                Math.sin(yawRadians - Mth.HALF_PI) * CatBurgerClientConfig.frontBackOffset
                + Math.sin(yawRadians) * CatBurgerClientConfig.leftRightOffset
                + localDragOffsetZ;  // 添加局部坐标拖拽Z偏移

        // 3.2 模型渲染使用的角度（受朝向开关控制）
        float renderHeadYaw;
        if (
                CatBurgerClientConfig.springEnabled
                || CatBurgerClientConfig.rotationDragEnabled && CatBurgerClientConfig.rotationDragAffectOrientation
        ) {
            // 弹簧系统或朝向影响：使用拖拽旋转
            renderHeadYaw = offsetHeadYaw;
        } else {
            // 禁用朝向影响：使用原始视角旋转（立即跟随）
            renderHeadYaw = viewYRot - bodyRot;
        }

        // 第五部分：执行渲染
        matrixStack.pushPose();
        matrixStack.mulPose(new Quaternionf().rotateZ((float) Math.toRadians(180)));

        matrixStack.translate(xOffset, yOffset, zOffset);

        float scale = (float) CatBurgerClientConfig.scale;
        matrixStack.scale(scale, scale, scale);
        matrixStack.mulPose(Axis.YP.rotationDegrees(-renderHeadYaw));
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
