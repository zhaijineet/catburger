package net.zhaiji.catburger.config;

import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

public class CatBurgerCommonConfig {
    public static boolean totemEffectActive;
    public static boolean wakeUpCanResetCooldown;
    public static int totemCooldown;
    public static int curiosCooldown;
    public static int foodRestorationFromCurios;
    public static int foodMaxRestoration;
    public static int healthRestorationFromTotem;
    public static int foodRestorationFromTotem;
    public static int saturationRestorationFromTotem;

    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder()
            .comment("config")
            .push("Config");

    private static final ModConfigSpec.BooleanValue TOTEM_EFFECT_ACTIVE = BUILDER
            .comment("Enable CatBurger totem effect")
            .define(
                    "active",
                    true
            );

    private static final ModConfigSpec.BooleanValue WAKE_UP_CAN_RESET_COOLDOWN = BUILDER
            .comment("Reset totem cooldown when waking up from bed")
            .define(
                    "wakeUpCanResetCooldown",
                    true
            );

    private static final ModConfigSpec.IntValue TOTEM_COOLDOWN_VALUE = BUILDER
            .comment("Totem effect cooldown in ticks")
            .defineInRange(
                    "totemCooldown",
                    36000,
                    0,
                    Integer.MAX_VALUE
            );

    private static final ModConfigSpec.IntValue CURIOS_COOLDOWN_VALUE = BUILDER
            .comment("Curios effect cooldown in ticks")
            .defineInRange(
                    "curiosCooldown",
                    1200,
                    0,
                    Integer.MAX_VALUE
            );

    private static final ModConfigSpec.IntValue FOOD_RESTORATION_VALUE = BUILDER
            .comment("Hunger value restored by Curios effect")
            .defineInRange(
                    "foodRestorationFromCurios",
                    1,
                    0,
                    20
            );

    private static final ModConfigSpec.IntValue FOOD_MAX_RESTORATION = BUILDER
            .comment("Maximum hunger value that can be restored by Curios")
            .defineInRange(
                    "foodMaxRestoration",
                    18,
                    1,
                    20
            );

    private static final ModConfigSpec.IntValue HEALTH_VALUE = BUILDER
            .comment("Health points restored when totem triggers")
            .defineInRange(
                    "healthRestorationFromTotem",
                    20,
                    0,
                    Integer.MAX_VALUE
            );

    private static final ModConfigSpec.IntValue FOOD_VALUE = BUILDER
            .comment("Hunger value restored when totem triggers")
            .defineInRange(
                    "foodRestorationFromTotem",
                    20,
                    0,
                    20
            );

    private static final ModConfigSpec.IntValue SATURATION_VALUE = BUILDER
            .comment("Saturation restored when totem triggers")
            .defineInRange(
                    "saturationRestorationFromTotem",
                    20,
                    0,
                    20
            );

    public static final ModConfigSpec SPEC = BUILDER.build();

    public static void handlerModConfigEvent(ModConfigEvent event) {
        if (event.getConfig().getSpec() == SPEC) {
            totemEffectActive = TOTEM_EFFECT_ACTIVE.get();
            wakeUpCanResetCooldown = WAKE_UP_CAN_RESET_COOLDOWN.get();
            totemCooldown = TOTEM_COOLDOWN_VALUE.get();
            curiosCooldown = CURIOS_COOLDOWN_VALUE.get();
            foodRestorationFromCurios = FOOD_RESTORATION_VALUE.get();
            foodMaxRestoration = FOOD_MAX_RESTORATION.get();
            healthRestorationFromTotem = HEALTH_VALUE.get();
            foodRestorationFromTotem = FOOD_VALUE.get();
            saturationRestorationFromTotem = SATURATION_VALUE.get();
        }
    }
}
