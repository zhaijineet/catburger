package net.zhaiji.catburger;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.zhaiji.catburger.client.compat.YSMCompat;
import net.zhaiji.catburger.client.render.CatBurgerRenderer;
import net.zhaiji.catburger.init.InitItem;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;

public class CatBurgerClient {
    public static void onClient(IEventBus modEventBus,IEventBus forgeEventBus) {
        if (YSMCompat.isLoad()) {
            forgeEventBus.addListener(YSMCompat::RenderLivingEvent);
        } else {
            modEventBus.addListener(CatBurgerClient::FMLClientSetupEvent);
        }
    }

    public static void FMLClientSetupEvent(FMLClientSetupEvent event) {
        CuriosRendererRegistry.register(InitItem.CAT_BURGER.get(), CatBurgerRenderer::new);
    }
}
