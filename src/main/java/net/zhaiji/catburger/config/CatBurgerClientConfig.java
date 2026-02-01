package net.zhaiji.catburger.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.event.config.ModConfigEvent;

public class CatBurgerClientConfig {
    public static double scale;
    public static double floatDistance;
    public static double time;
    public static double frontBackOffset;
    public static double verticalOffset;
    public static double leftRightOffset;
    public static boolean dragEnabled;
    public static double dragStrength;
    public static boolean rotationDragEnabled;
    public static double rotationDragSmoothness;
    public static boolean rotationDragUseWrapDegrees;
    public static boolean rotationDragAffectOrientation;
    public static boolean springEnabled;
    public static double springStiffness;
    public static double springDamping;

    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder()
            .comment("client config")
            .push("Config");

    private static final ForgeConfigSpec.DoubleValue SCALE = BUILDER
            .comment("Display scale")
            .defineInRange(
                    "scale",
                    0.7,
                    0.1,
                    10d
            );

    private static final ForgeConfigSpec.DoubleValue FLOAT_DISTANCE = BUILDER
            .comment("Float distance (blocks)")
            .defineInRange(
                    "floatDistance",
                    0.1,
                    0,
                    100d
            );

    private static final ForgeConfigSpec.DoubleValue FLOAT_CYCLE_DURATION = BUILDER
            .comment("Time to complete a float cycle (tick)")
            .defineInRange(
                    "time",
                    15,
                    1,
                    100d
            );

    private static final ForgeConfigSpec.DoubleValue FRONT_BACK_OFFSET = BUILDER
            .comment("Front & back offset")
            .defineInRange(
                    "frontBackOffset",
                    -0.4,
                    -100d,
                    100d
            );

    private static final ForgeConfigSpec.DoubleValue VERTICAL_OFFSET = BUILDER
            .comment("Vertical offset")
            .defineInRange(
                    "verticalOffset",
                    0,
                    -100d,
                    100d
            );

    private static final ForgeConfigSpec.DoubleValue LEFT_RIGHT_OFFSET = BUILDER
            .comment("Left & right offset")
            .defineInRange(
                    "leftRightOffset",
                    0.8,
                    -100d,
                    100d
            );

    private static final ForgeConfigSpec.BooleanValue DRAG_ENABLED = BUILDER
            .comment("Enable drag system for position lag effect")
            .define(
                    "dragEnabled",
                    true
            );

    private static final ForgeConfigSpec.DoubleValue DRAG_STRENGTH = BUILDER
            .comment("Drag strength factor")
            .defineInRange(
                    "dragStrength",
                    0.2,
                    0.01,
                    1.0
            );

    private static final ForgeConfigSpec.BooleanValue ROTATION_DRAG_ENABLED = BUILDER
            .comment("Enable rotation drag system for head rotation lag effect")
            .define(
                    "rotationDragEnabled",
                    true
            );

    private static final ForgeConfigSpec.DoubleValue ROTATION_DRAG_SMOOTHNESS = BUILDER
            .comment("Rotation drag smoothness factor")
            .defineInRange(
                    "rotationDragSmoothness",
                    0.2,
                    0.01,
                    1.0
            );

    private static final ForgeConfigSpec.BooleanValue ROTATION_DRAG_USE_WRAP_DEGREES = BUILDER
            .comment("Use wrapDegrees for angle wrap-around handling (shortest path rotation)")
            .define(
                    "rotationDragUseWrapDegrees",
                    false
            );

    private static final ForgeConfigSpec.BooleanValue ROTATION_DRAG_AFFECT_ORIENTATION = BUILDER
            .comment("Allow rotation drag to affect burger model orientation (Y-axis rotation)")
            .define(
                    "rotationDragAffectOrientation",
                    false
            );

    private static final ForgeConfigSpec.BooleanValue SPRING_ENABLED = BUILDER
            .comment("Enable spring physics system for head rotation smoothing")
            .define(
                    "springEnabled",
                    false
            );

    private static final ForgeConfigSpec.DoubleValue SPRING_STIFFNESS = BUILDER
            .comment("Spring stiffness coefficient")
            .defineInRange(
                    "springStiffness",
                    0.9,
                    0.1,
                    2.0
            );

    private static final ForgeConfigSpec.DoubleValue SPRING_DAMPING = BUILDER
            .comment("Spring damping coefficient")
            .defineInRange(
                    "springDamping",
                    0.7,
                    0.1,
                    0.99
            );

    public static final ForgeConfigSpec SPEC = BUILDER.build();

    public static void handlerModConfigEvent(ModConfigEvent event) {
        if (event.getConfig().getSpec() == SPEC) {
            scale = SCALE.get();
            floatDistance = FLOAT_DISTANCE.get();
            time = FLOAT_CYCLE_DURATION.get();
            frontBackOffset = FRONT_BACK_OFFSET.get();
            verticalOffset = VERTICAL_OFFSET.get();
            leftRightOffset = LEFT_RIGHT_OFFSET.get();
            dragEnabled = DRAG_ENABLED.get();
            dragStrength = DRAG_STRENGTH.get();
            rotationDragEnabled = ROTATION_DRAG_ENABLED.get();
            rotationDragSmoothness = ROTATION_DRAG_SMOOTHNESS.get();
            rotationDragUseWrapDegrees = ROTATION_DRAG_USE_WRAP_DEGREES.get();
            rotationDragAffectOrientation = ROTATION_DRAG_AFFECT_ORIENTATION.get();
            springEnabled = SPRING_ENABLED.get();
            springStiffness = SPRING_STIFFNESS.get();
            springDamping = SPRING_DAMPING.get();
        }
    }
}

