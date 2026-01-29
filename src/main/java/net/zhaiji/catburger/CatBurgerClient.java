package net.zhaiji.catburger;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.zhaiji.catburger.client.event.ClientEventManager;

@Mod(value = CatBurger.MOD_ID, dist = Dist.CLIENT)
public class CatBurgerClient {
    public CatBurgerClient(IEventBus modEventBus, ModContainer modContainer) {
        ClientEventManager.init(modEventBus, NeoForge.EVENT_BUS);
    }
}
