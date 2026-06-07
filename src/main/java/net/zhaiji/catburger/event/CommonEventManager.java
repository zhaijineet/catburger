package net.zhaiji.catburger.event;

import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.zhaiji.catburger.compat.CompatManager;
import net.zhaiji.catburger.compat.TLMCompat;
import net.zhaiji.catburger.config.CatBurgerCommonConfig;
import net.zhaiji.catburger.datagen.DataGenHandler;

public class CommonEventManager {
    public static void init(IEventBus modBus, IEventBus gameBus) {
        CommonEventManager.modBusListener(modBus);
        CommonEventManager.gameBusListener(gameBus);
        if (CompatManager.isTLMLoad()) {
            TLMCompat.init(modBus, gameBus);
        }
    }

    public static void modBusListener(IEventBus modBus) {
        modBus.addListener(CatBurgerCommonConfig::handlerModConfigEvent);
        modBus.addListener(DataGenHandler::handlerGatherDataEvent);
    }

    public static void gameBusListener(IEventBus gameBus) {
        gameBus.addListener(EventPriority.HIGHEST, CommonEventHandler::handlerLivingDeathEvent);
        gameBus.addListener(CommonEventHandler::handlerPlayerWakeUpEvent);
    }
}
