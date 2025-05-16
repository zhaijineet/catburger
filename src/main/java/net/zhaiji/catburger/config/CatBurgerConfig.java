package net.zhaiji.catburger.config;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;
import net.zhaiji.catburger.CatBurger;

@Config(name = CatBurger.MOD_ID)
public class CatBurgerConfig implements ConfigData {
    public static CatBurgerConfig get() {
        return AutoConfig.getConfigHolder(CatBurgerConfig.class).getConfig();
    }

    // 原 CatBurgerCommonConfig 的配置项
    @Comment("totem effect active?")
    @ConfigEntry.Gui.Tooltip
    public boolean totem_effect_active = true;

    @Comment("wake up can reset totem effect cooldown?")
    @ConfigEntry.Gui.Tooltip
    public boolean wake_up_can_reset_cooldown = true;

    @Comment("totem effect cooldown(tick)")
    @ConfigEntry.Gui.Tooltip
    @ConfigEntry.BoundedDiscrete(min = 0, max = Integer.MAX_VALUE)
    public int totem_cooldown = 36000;

    @Comment("trinket effect cooldown(tick)")
    @ConfigEntry.Gui.Tooltip
    @ConfigEntry.BoundedDiscrete(min = 0, max = Integer.MAX_VALUE)
    public int trinket_cooldown = 1200;

    @Comment("food restoration form trinket effect")
    @ConfigEntry.Gui.Tooltip
    @ConfigEntry.BoundedDiscrete(min = 0, max = 20)
    public int food_restoration_form_trinket = 1;

    @Comment("food max restoration")
    @ConfigEntry.Gui.Tooltip
    @ConfigEntry.BoundedDiscrete(min = 1, max = 20)
    public int food_max_restoration = 18;

    @Comment("health restoration form totem effect")
    @ConfigEntry.Gui.Tooltip
    @ConfigEntry.BoundedDiscrete(min = 0, max = Integer.MAX_VALUE)
    public int health_restoration_form_totem = 20;

    @Comment("food restoration form totem effect")
    @ConfigEntry.Gui.Tooltip
    @ConfigEntry.BoundedDiscrete(min = 0, max = 20)
    public int food_restoration_form_totem = 20;

    @Comment("saturation restoration form totem effect")
    @ConfigEntry.Gui.Tooltip
    @ConfigEntry.BoundedDiscrete(min = 0, max = 20)
    public int saturation_restoration_form_totem = 20;

    // 原 CatBurgerClientConfig 的配置项
    @ConfigEntry.Gui.CollapsibleObject
    @Comment("客户端配置")
    public ClientConfig client = new ClientConfig();

    public static class ClientConfig {
        @Comment("scale")
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 100)
        public double scale = 0.7;

        @Comment("float distance (blocks)")
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 0, max = 1000)
        public double float_distance = 0.1;

        @Comment("time to complete a float cycle (tick)")
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 0, max = 1000)
        public double time = 40;

        @Comment("front & back offset")
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = -1000, max = 1000)
        public double front_back_offset = 0;

        @Comment("vertical offset")
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = -1000, max = 1000)
        public double vertical_offset = 0;

        @Comment("left & right offset")
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = -1000, max = 1000)
        public double left_right_offset = -0.8;
    }
}