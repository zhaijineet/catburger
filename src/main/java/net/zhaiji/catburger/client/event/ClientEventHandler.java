package net.zhaiji.catburger.client.event;

import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.zhaiji.catburger.client.render.CatBurgerRenderer;
import net.zhaiji.catburger.init.InitItem;
import top.theillusivec4.curios.api.client.ICurioRenderer;

public class ClientEventHandler {
    public static void handlerFMLClientSetupEvent(FMLClientSetupEvent event) {
        ICurioRenderer.register(InitItem.CAT_BURGER.get(), CatBurgerRenderer::new);
    }
}
