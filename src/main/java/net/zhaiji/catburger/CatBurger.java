package net.zhaiji.catburger;

import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.zhaiji.catburger.config.CatBurgerClientConfig;
import net.zhaiji.catburger.config.CatBurgerCommonConfig;
import net.zhaiji.catburger.init.InitCreativeModeTab;
import net.zhaiji.catburger.init.InitItem;
import org.slf4j.Logger;

@Mod(CatBurger.MOD_ID)
public class CatBurger {
    public static final String MOD_ID = "catburger";

    private static final Logger LOGGER = LogUtils.getLogger();

    public CatBurger(IEventBus modEventBus, ModContainer modContainer) {
        modContainer.registerConfig(ModConfig.Type.COMMON, CatBurgerCommonConfig.SPEC);
        modContainer.registerConfig(ModConfig.Type.CLIENT, CatBurgerClientConfig.SPEC);

        InitItem.ITEMS.register(modEventBus);
        InitCreativeModeTab.CREATIVE_MODE_TAB.register(modEventBus);
    }
}
