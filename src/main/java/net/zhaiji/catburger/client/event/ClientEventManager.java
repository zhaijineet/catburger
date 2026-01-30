package net.zhaiji.catburger.client.event;

import net.minecraftforge.eventbus.api.IEventBus;
import net.zhaiji.catburger.client.compat.ClientCompatHandler;
import net.zhaiji.catburger.compat.CompatManager;
import net.zhaiji.catburger.config.CatBurgerClientConfig;

public class ClientEventManager {
    public static void init(IEventBus modBus, IEventBus gameBus) {
        ClientEventManager.modBusListener(modBus);
        ClientEventManager.gameBusListener(gameBus);
    }

    public static void modBusListener(IEventBus modBus) {
        if (!CompatManager.isYSMLoad()) {
            modBus.addListener(ClientEventHandler::handlerFMLClientSetupEvent);
        }
        modBus.addListener(CatBurgerClientConfig::handlerModConfigEvent);
    }

    public static void gameBusListener(IEventBus gameBus) {
        if (CompatManager.isYSMLoad() || CompatManager.isTLMLoad()) {
            gameBus.addListener(ClientCompatHandler::handlerRenderLivingEvent$Post);
        }
    }
}
