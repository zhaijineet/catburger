package net.zhaiji.catburger.event;

import net.minecraftforge.eventbus.api.IEventBus;
import net.zhaiji.catburger.client.compat.TLMCompat;
import net.zhaiji.catburger.client.compat.CompatManager;
import net.zhaiji.catburger.config.CatBurgerCommonConfig;

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
    }

    public static void gameBusListener(IEventBus gameBus) {
        gameBus.addListener(CommonEventHandler::handlerLivingDeathEvent);
        gameBus.addListener(CommonEventHandler::handlerPlayerWakeUpEvent);
    }
}
