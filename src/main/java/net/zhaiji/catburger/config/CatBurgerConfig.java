package net.zhaiji.catburger.config;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import net.zhaiji.catburger.CatBurger;

@Config(name = CatBurger.MOD_ID)
public class CatBurgerConfig implements ConfigData {
    public static CatBurgerConfig get() {
        return AutoConfig.getConfigHolder(CatBurgerConfig.class).getConfig();
    }

 
    public boolean totem_effect_active = true;

 
    public boolean wake_up_can_reset_cooldown = true;

 
    @ConfigEntry.BoundedDiscrete(min = 1, max = Integer.MAX_VALUE)
    public int totem_cooldown = 36000;

 
    @ConfigEntry.BoundedDiscrete(min = 1, max = Integer.MAX_VALUE)
    public int trinket_cooldown = 1200;

 
    @ConfigEntry.BoundedDiscrete(min = 0, max = 20)
    public int food_restoration_form_trinket = 1;

 
    @ConfigEntry.BoundedDiscrete(min = 1, max = 20)
    public int food_max_restoration = 18;

 
    @ConfigEntry.BoundedDiscrete(min = 1, max = Integer.MAX_VALUE)
    public int health_restoration_form_totem = 20;

 
    @ConfigEntry.BoundedDiscrete(min = 0, max = 20)
    public int food_restoration_form_totem = 20;

 
    @ConfigEntry.BoundedDiscrete(min = 0, max = 20)
    public int saturation_restoration_form_totem = 20;

    @ConfigEntry.Gui.CollapsibleObject
 
    public ClientConfig client = new ClientConfig();

    public static class ClientConfig {
 
        @ConfigEntry.BoundedDiscrete(min = 1, max = 100)
        public double scale = 0.7;

 
        @ConfigEntry.BoundedDiscrete(min = 0, max = 1000)
        public double float_distance = 0.1;

 
        @ConfigEntry.BoundedDiscrete(min = 0, max = 1000)
        public double time = 40;

 
        @ConfigEntry.BoundedDiscrete(min = -1000, max = 1000)
        public double front_back_offset = 0;

 
        @ConfigEntry.BoundedDiscrete(min = -1000, max = 1000)
        public double vertical_offset = 0;

 
        @ConfigEntry.BoundedDiscrete(min = -1000, max = 1000)
        public double left_right_offset = -0.8;
    }
}