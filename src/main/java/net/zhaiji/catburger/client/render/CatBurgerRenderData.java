package net.zhaiji.catburger.client.render;

import net.minecraft.world.entity.LivingEntity;

import java.util.Map;
import java.util.WeakHashMap;

public class CatBurgerRenderData {
    // 渲染数据缓存
    public static final Map<LivingEntity, CatBurgerRenderData> RENDER_DATA_MAP = new WeakHashMap<>();

    public int lastTick;         // 上次更新时的tick计数

    // 拖拽位置字段
    public double dragX;         // 当前拖拽位置X
    public double dragY;         // 当前拖拽位置Y
    public double dragZ;         // 当前拖拽位置Z
    public float dragYaw;        // 当前拖拽旋转
    public double prevDragX;     // 上一tick拖拽位置X
    public double prevDragY;     // 上一tick拖拽位置Y
    public double prevDragZ;     // 上一tick拖拽位置Z
    public float prevDragYaw;    // 上一tick的拖拽旋转

    // 弹簧旋转字段
    public float springYaw;      // 当前tick的弹簧位置
    public float velocity;       // 当前tick的弹簧速度
    public float prevSpringYaw;  // 上一个tick的弹簧位置

    // 蹲姿高度拖拽字段
    public double dragCrouchOffset;      // 当前拖拽的蹲姿偏移
    public double prevDragCrouchOffset;  // 上一tick的蹲姿偏移

    public CatBurgerRenderData(float yaw, LivingEntity entity) {
        lastTick = entity.tickCount;
        // 初始化拖拽位置系统
        dragX = prevDragX = entity.getX();
        dragY = prevDragY = entity.getY();
        dragZ = prevDragZ = entity.getZ();
        // 初始化旋转拖拽系统和弹簧系统
        dragYaw = prevDragYaw = springYaw = prevSpringYaw = yaw;
        // 初始化蹲姿高度拖拽系统（根据当前状态初始化）
        dragCrouchOffset = prevDragCrouchOffset = entity.isCrouching() ? -0.5 : 0;
    }
}
