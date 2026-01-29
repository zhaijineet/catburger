package net.zhaiji.catburger;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.zhaiji.catburger.config.CatBurgerClientConfig;
import net.zhaiji.catburger.config.CatBurgerCommonConfig;
import net.zhaiji.catburger.event.CommonEventManager;
import net.zhaiji.catburger.init.InitCreativeModeTab;
import net.zhaiji.catburger.init.InitItem;
import net.zhaiji.catburger.network.PacketManager;

@Mod(CatBurger.MOD_ID)
public class CatBurger {
    public static final String MOD_ID = "catburger";

    public CatBurger() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        IEventBus gameBus = MinecraftForge.EVENT_BUS;

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, CatBurgerCommonConfig.SPEC);
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, CatBurgerClientConfig.SPEC);

        InitItem.ITEMS.register(modBus);
        InitCreativeModeTab.CREATIVE_MODE_TAB.register(modBus);

        PacketManager.registry();
        CommonEventManager.init(modBus, gameBus);
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            CatBurgerClient.init(modBus, gameBus);
        });
    }
}
