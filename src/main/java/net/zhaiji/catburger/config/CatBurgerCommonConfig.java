package net.zhaiji.catburger.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.zhaiji.catburger.CatBurger;

@Mod.EventBusSubscriber(modid = CatBurger.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CatBurgerCommonConfig {
    public static boolean totem_effect_active;
    public static boolean wake_up_can_reset_cooldown;
    public static int totem_cooldown;
    public static int curios_cooldown;
    public static int food_restoration_form_curios;
    public static int food_max_restoration;
    public static int health_restoration_form_totem;
    public static int food_restoration_form_totem;
    public static int saturation_restoration_form_totem;

    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder()
            .comment("config")
            .push("Config");

    private static final ForgeConfigSpec.BooleanValue TOTEM_EFFECT_ACTIVE = BUILDER
            .comment("totem effect active?")
            .define(
                    "active",
                    true
            );

    private static final ForgeConfigSpec.BooleanValue WAKE_UP_CAN_RESET_COOLDOWN = BUILDER
            .comment("wake up can reset totem effect cooldown?")
            .define(
                    "can_reset",
                    true
            );

    private static final ForgeConfigSpec.IntValue TOTEM_COOLDOWN_VALUE = BUILDER
            .comment("totem effect cooldown(tick)")
            .defineInRange(
                    "totem_cooldown",
                    36000,
                    0,
                    Integer.MAX_VALUE
            );

    private static final ForgeConfigSpec.IntValue CURIOS_COOLDOWN_VALUE = BUILDER
            .comment("totem effect cooldown(tick)")
            .defineInRange(
                    "curios_cooldown",
                    1200,
                    0,
                    Integer.MAX_VALUE
            );

    private static final ForgeConfigSpec.IntValue FOOD_RESTORATION_VALUE = BUILDER
            .comment("food restoration form curios effect")
            .defineInRange(
                    "foodRestoration",
                    1,
                    0,
                    20
            );

    private static final ForgeConfigSpec.IntValue FOOD_MAX_RESTORATION = BUILDER
            .comment("food max restoration")
            .defineInRange(
                    "foodMaxRestoration",
                    18,
                    1,
                    20
            );

    private static final ForgeConfigSpec.IntValue HEALTH_VALUE = BUILDER
            .comment("health restoration form totem effect")
            .defineInRange(
                    "health",
                    20,
                    0,
                    Integer.MAX_VALUE
            );

    private static final ForgeConfigSpec.IntValue FOOD_VALUE = BUILDER
            .comment("food restoration form totem effect")
            .defineInRange(
                    "foodLevel",
                    20,
                    0,
                    20
            );

    private static final ForgeConfigSpec.IntValue SATURATION_VALUE = BUILDER
            .comment("saturation restoration form totem effect")
            .defineInRange(
                    "saturation",
                    20,
                    0,
                    20
            );

    public static final ForgeConfigSpec SPEC = BUILDER.build();

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        if (event.getConfig().getSpec() == SPEC) {
            totem_effect_active = TOTEM_EFFECT_ACTIVE.get();
            wake_up_can_reset_cooldown = WAKE_UP_CAN_RESET_COOLDOWN.get();

            totem_cooldown = TOTEM_COOLDOWN_VALUE.get();
            curios_cooldown = CURIOS_COOLDOWN_VALUE.get();

            food_restoration_form_curios = FOOD_RESTORATION_VALUE.get();
            food_max_restoration = FOOD_MAX_RESTORATION.get();

            health_restoration_form_totem = HEALTH_VALUE.get();
            food_restoration_form_totem = FOOD_VALUE.get();
            saturation_restoration_form_totem = SATURATION_VALUE.get();
        }
    }
}
