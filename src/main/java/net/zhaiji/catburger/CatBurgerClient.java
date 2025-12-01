package net.zhaiji.catburger;

import dev.emi.trinkets.api.client.TrinketRendererRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.zhaiji.catburger.client.compat.YSMCompat;
import net.zhaiji.catburger.client.render.CatBurgerRenderer;
import net.zhaiji.catburger.init.InitItem;
import net.zhaiji.catburger.network.CatBurgerPacket;

public class CatBurgerClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        CatBurgerPacket.registerClient();

        if (!YSMCompat.isLoad()) {
            TrinketRendererRegistry.registerRenderer(InitItem.CAT_BURGER, new CatBurgerRenderer());
        }
    }
}