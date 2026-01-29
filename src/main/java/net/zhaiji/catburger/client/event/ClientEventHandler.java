package net.zhaiji.catburger.client.event;

import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.zhaiji.catburger.client.render.CatBurgerRenderer;
import net.zhaiji.catburger.init.InitItem;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;

public class ClientEventHandler {
    public static void handlerFMLClientSetupEvent(FMLClientSetupEvent event) {
        CuriosRendererRegistry.register(InitItem.CAT_BURGER.get(), CatBurgerRenderer::new);
    }
}
