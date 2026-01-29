package net.zhaiji.catburger.config;

import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

public class CatBurgerClientConfig {
    public static double scale;
    public static double floatDistance;
    public static double time;
    public static double frontBackOffset;
    public static double verticalOffset;
    public static double leftRightOffset;

    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder()
            .comment(
                    "客户端配置",
                    "client config"
            )
            .push("Config");

    private static final ModConfigSpec.DoubleValue SCALE = BUILDER
            .comment(
                    "模型显示缩放比例",
                    "Display scale"
            )
            .defineInRange(
                    "scale",
                    0.7,
                    0.1,
                    10d
            );

    private static final ModConfigSpec.DoubleValue FLOAT_DISTANCE = BUILDER
            .comment(
                    "浮动高度范围(block)",
                    "Float distance (blocks)"
            )
            .defineInRange(
                    "floatDistance",
                    0.1,
                    0,
                    100d
            );

    private static final ModConfigSpec.DoubleValue FLOAT_CYCLE_DURATION = BUILDER
            .comment(
                    "完成一次上下浮动循环所需时间(tick)",
                    "Time to complete a float cycle (tick)"
            )
            .defineInRange(
                    "time",
                    40,
                    0,
                    100d
            );

    private static final ModConfigSpec.DoubleValue FRONT_BACK_OFFSET = BUILDER
            .comment(
                    "前后位置偏移",
                    "Front & back offset"
            )
            .defineInRange(
                    "frontBackOffset",
                    0,
                    -100d,
                    100d
            );

    private static final ModConfigSpec.DoubleValue VERTICAL_OFFSET = BUILDER
            .comment(
                    "垂直高度偏移",
                    "Vertical offset"
            )
            .defineInRange(
                    "verticalOffset",
                    0,
                    -100d,
                    100d
            );

    private static final ModConfigSpec.DoubleValue LEFT_RIGHT_OFFSET = BUILDER
            .comment(
                    "左右位置偏移",
                    "Left & right offset"
            )
            .defineInRange(
                    "leftRightOffset",
                    -0.8,
                    -100d,
                    100d
            );

    public static final ModConfigSpec SPEC = BUILDER.build();

    public static void handlerModConfigEvent(ModConfigEvent event) {
        if (event.getConfig().getSpec() == SPEC) {
            scale = SCALE.get();
            floatDistance = FLOAT_DISTANCE.get();
            time = FLOAT_CYCLE_DURATION.get();
            frontBackOffset = FRONT_BACK_OFFSET.get();
            verticalOffset = VERTICAL_OFFSET.get();
            leftRightOffset = LEFT_RIGHT_OFFSET.get();
        }
    }
}
