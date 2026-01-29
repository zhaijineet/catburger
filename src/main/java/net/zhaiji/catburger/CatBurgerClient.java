package net.zhaiji.catburger;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.zhaiji.catburger.client.event.ClientEventManager;

public class CatBurgerClient {
    public static void init(IEventBus modBus,IEventBus gameBus) {
        ClientEventManager.init(modBus, gameBus);
    }
}
