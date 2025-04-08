package net.zhaiji.catburger.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.zhaiji.catburger.CatBurger;

@Mod.EventBusSubscriber(modid = CatBurger.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CatBurgerClientConfig {
    public static double scale;
    public static double float_distance;
    public static double time;
    public static double front_back_offset;
    public static double vertical_offset;
    public static double left_right_offset;

    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder()
            .comment("CatBurger Client Config")
            .push("Config");

    private static final ForgeConfigSpec.DoubleValue SCALE = BUILDER
            .comment("scale")
            .defineInRange(
                    "scale",
                    0.7,
                    0.1,
                    10d
            );

    private static final ForgeConfigSpec.DoubleValue FLOAT_DISTANCE = BUILDER
            .comment("float distance (blocks)")
            .defineInRange(
                    "float_distance",
                    0.1,
                    0,
                    100d
            );

    private static final ForgeConfigSpec.DoubleValue FLOAT_CYCLE_DURATION = BUILDER
            .comment("time to complete a float cycle (tick) ")
            .defineInRange(
                    "time",
                    40,
                    0,
                    100d
            );

    private static final ForgeConfigSpec.DoubleValue FRONT_BACK_OFFSET = BUILDER
            .comment("front & back offset")
            .defineInRange(
                    "front_back_offset",
                    0,
                    -100d,
                    100d
            );

    private static final ForgeConfigSpec.DoubleValue VERTICAL_OFFSET = BUILDER
            .comment("vertical offset")
            .defineInRange(
                    "vertical_offset",
                    0,
                    -100d,
                    100d
            );

    private static final ForgeConfigSpec.DoubleValue LEFT_RIGHT_OFFSET = BUILDER
            .comment("left & right offset")
            .defineInRange(
                    "left_right_offset",
                    -0.8,
                    -100d,
                    100d
            );

    public static final ForgeConfigSpec SPEC = BUILDER.build();

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        if (event.getConfig().getSpec() == SPEC) {
            scale = SCALE.get();
            float_distance = FLOAT_DISTANCE.get();
            time = FLOAT_CYCLE_DURATION.get();
            front_back_offset = FRONT_BACK_OFFSET.get();
            vertical_offset = VERTICAL_OFFSET.get();
            left_right_offset = LEFT_RIGHT_OFFSET.get();
        }
    }
}
