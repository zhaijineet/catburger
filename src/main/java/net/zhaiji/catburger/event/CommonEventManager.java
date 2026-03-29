package net.zhaiji.catburger.event;

import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.zhaiji.catburger.config.CatBurgerCommonConfig;
import net.zhaiji.catburger.network.PacketManager;

public class CommonEventManager {
    public static void init(IEventBus modBus, IEventBus gameBus) {
        CommonEventManager.modBusListener(modBus);
        CommonEventManager.gameBusListener(gameBus);
//        if (CompatManager.isTLMLoad()) {
//            TLMCompat.init(modBus, gameBus);
//        }
    }

    public static void modBusListener(IEventBus modBus) {
        modBus.addListener(CatBurgerCommonConfig::handlerModConfigEvent);
        modBus.addListener(PacketManager::handlerRegisterPayloadHandlersEvent);
    }

    public static void gameBusListener(IEventBus gameBus) {
        gameBus.addListener(EventPriority.HIGHEST, CommonEventHandler::handlerLivingDeathEvent);
        gameBus.addListener(CommonEventHandler::handlerPlayerWakeUpEvent);
    }
}
