package net.zhaiji.catburger.config;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.zhaiji.catburger.CatBurger;

@EventBusSubscriber(modid = CatBurger.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class CatBurgerCommonConfig {
    public static boolean totem_effect_active;
    public static boolean wake_up_can_reset_cooldown;
    public static int totem_cooldown;
    public static int curios_cooldown;
    public static int food_restoration_form_curios;
    public static int health_restoration_form_totem;
    public static int food_restoration_form_totem;
    public static int saturation_restoration_form_totem;

    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder()
            .comment("config")
            .push("Config");

    private static final ModConfigSpec.BooleanValue TOTEM_EFFECT_ACTIVE = BUILDER
            .comment("totem effect active?")
            .define(
                    "active",
                    true
            );

    private static final ModConfigSpec.BooleanValue WAKE_UP_CAN_RESET_COOLDOWN = BUILDER
            .comment("wake up can reset totem effect cooldown?")
            .define(
                    "can_reset",
                    true
            );

    private static final ModConfigSpec.IntValue TOTEM_COOLDOWN_VALUE = BUILDER
            .comment("totem effect cooldown(tick)")
            .defineInRange(
                    "totem_cooldown",
                    36000,
                    0,
                    Integer.MAX_VALUE
            );

    private static final ModConfigSpec.IntValue CURIOS_COOLDOWN_VALUE = BUILDER
            .comment("totem effect cooldown(tick)")
            .defineInRange(
                    "curios_cooldown",
                    600,
                    0,
                    Integer.MAX_VALUE
            );

    private static final ModConfigSpec.IntValue FOOD_RESTORATION_VALUE = BUILDER
            .comment("food restoration form curios effect")
            .defineInRange(
                    "foodRestoration",
                    1,
                    0,
                    20
            );

    private static final ModConfigSpec.IntValue HEALTH_VALUE = BUILDER
            .comment("health restoration form totem effect")
            .defineInRange(
                    "health",
                    20,
                    0,
                    Integer.MAX_VALUE
            );

    private static final ModConfigSpec.IntValue FOOD_VALUE = BUILDER
            .comment("food restoration form totem effect")
            .defineInRange(
                    "foodLevel",
                    20,
                    0,
                    20
            );

    private static final ModConfigSpec.IntValue SATURATION_VALUE = BUILDER
            .comment("saturation restoration form totem effect")
            .defineInRange(
                    "saturation",
                    20,
                    0,
                    20
            );

    public static final ModConfigSpec SPEC = BUILDER.build();

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        if (event.getConfig().getSpec() == SPEC) {
            totem_effect_active = TOTEM_EFFECT_ACTIVE.get();
            wake_up_can_reset_cooldown = WAKE_UP_CAN_RESET_COOLDOWN.get();

            totem_cooldown = TOTEM_COOLDOWN_VALUE.get();
            curios_cooldown = CURIOS_COOLDOWN_VALUE.get();

            food_restoration_form_curios = FOOD_RESTORATION_VALUE.get();

            health_restoration_form_totem = HEALTH_VALUE.get();
            food_restoration_form_totem = FOOD_VALUE.get();
            saturation_restoration_form_totem = SATURATION_VALUE.get();
        }
    }
}
