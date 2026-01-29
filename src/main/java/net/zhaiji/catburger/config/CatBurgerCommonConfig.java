package net.zhaiji.catburger.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.event.config.ModConfigEvent;

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

    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder()
            .comment(
                    "配置",
                    "config"
            )
            .push("Config");

    private static final ForgeConfigSpec.BooleanValue TOTEM_EFFECT_ACTIVE = BUILDER
            .comment(
                    "是否启用猫猫汉堡图腾效果",
                    "Enable CatBurger totem effect"
            )
            .define(
                    "active",
                    true
            );

    private static final ForgeConfigSpec.BooleanValue WAKE_UP_CAN_RESET_COOLDOWN = BUILDER
            .comment(
                    "从床上醒来后是否重置图腾冷却时间",
                    "Reset totem cooldown when waking up from bed"
            )
            .define(
                    "wakeUpCanResetCooldown",
                    true
            );

    private static final ForgeConfigSpec.IntValue TOTEM_COOLDOWN_VALUE = BUILDER
            .comment(
                    "图腾效果冷却时间",
                    "Totem effect cooldown in ticks"
            )
            .defineInRange(
                    "totemCooldown",
                    36000,
                    0,
                    Integer.MAX_VALUE
            );

    private static final ForgeConfigSpec.IntValue CURIOS_COOLDOWN_VALUE = BUILDER
            .comment(
                    "饰品效果冷却时间",
                    "Curios effect cooldown in ticks"
            )
            .defineInRange(
                    "curiosCooldown",
                    1200,
                    0,
                    Integer.MAX_VALUE
            );

    private static final ForgeConfigSpec.IntValue FOOD_RESTORATION_VALUE = BUILDER
            .comment(
                    "饰品效果恢复的饥饿值",
                    "Hunger value restored by Curios effect"
            )
            .defineInRange(
                    "foodRestorationFromCurios",
                    1,
                    0,
                    20
            );

    private static final ForgeConfigSpec.IntValue FOOD_MAX_RESTORATION = BUILDER
            .comment(
                    "饰品效果可恢复的最大饥饿值上限",
                    "Maximum hunger value that can be restored by Curios"
            )
            .defineInRange(
                    "foodMaxRestoration",
                    18,
                    1,
                    20
            );

    private static final ForgeConfigSpec.IntValue HEALTH_VALUE = BUILDER
            .comment(
                    "图腾触发时恢复的生命值",
                    "Health points restored when totem triggers"
            )
            .defineInRange(
                    "healthRestorationFromTotem",
                    20,
                    0,
                    Integer.MAX_VALUE
            );

    private static final ForgeConfigSpec.IntValue FOOD_VALUE = BUILDER
            .comment(
                    "图腾触发时恢复的饥饿值",
                    "Hunger value restored when totem triggers"
            )
            .defineInRange(
                    "foodRestorationFromTotem",
                    20,
                    0,
                    20
            );

    private static final ForgeConfigSpec.IntValue SATURATION_VALUE = BUILDER
            .comment(
                    "图腾触发时恢复的饱和度",
                    "Saturation restored when totem triggers"
            )
            .defineInRange(
                    "saturationRestorationFromTotem",
                    20,
                    0,
                    20
            );

    public static final ForgeConfigSpec SPEC = BUILDER.build();

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

