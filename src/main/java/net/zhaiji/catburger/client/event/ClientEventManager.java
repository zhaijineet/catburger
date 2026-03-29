package net.zhaiji.catburger.client.event;

import net.neoforged.bus.api.IEventBus;
import net.zhaiji.catburger.config.CatBurgerClientConfig;

public class ClientEventManager {
    public static void init(IEventBus modBus, IEventBus gameBus) {
        ClientEventManager.modBusListener(modBus);
//        ClientEventManager.gameBusListener(gameBus);
    }

    public static void modBusListener(IEventBus modBus) {
        modBus.addListener(ClientEventHandler::handlerFMLClientSetupEvent);
        modBus.addListener(CatBurgerClientConfig::handlerModConfigEvent);
    }

//    public static void gameBusListener(IEventBus gameBus) {
//        if (CompatManager.isYSMLoad() || CompatManager.isTLMLoad()) {
//            gameBus.addListener(ClientCompatHandler::handlerRenderLivingEvent$Post);
//        }
//    }
}
