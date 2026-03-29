package net.zhaiji.catburger.client.render;

import net.minecraft.world.entity.LivingEntity;

import java.util.Map;
import java.util.WeakHashMap;

public class CatBurgerRenderData {
    public static final Map<LivingEntity, CatBurgerRenderData> RENDER_DATA_MAP = new WeakHashMap<>();

    public int lastTick;

    public double dragX;
    public double dragY;
    public double dragZ;
    public float dragYaw;
    public double prevDragX;
    public double prevDragY;
    public double prevDragZ;
    public float prevDragYaw;

    public float springYaw;
    public float velocity;
    public float prevSpringYaw;

    public double dragCrouchOffset;
    public double prevDragCrouchOffset;

    public CatBurgerRenderData(float yaw, LivingEntity entity) {
        dragX = prevDragX = entity.getX();
        dragY = prevDragY = entity.getY();
        dragZ = prevDragZ = entity.getZ();
        dragYaw = prevDragYaw = springYaw = prevSpringYaw = yaw;
        dragCrouchOffset = prevDragCrouchOffset = entity.isCrouching() ? -0.5 : 0;
    }
}
