package net.zhaiji.catburger;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.zhaiji.catburger.client.compat.YSMCompat;
import net.zhaiji.catburger.client.render.CatBurgerRenderer;
import net.zhaiji.catburger.init.InitItem;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;

@Mod(value = CatBurger.MOD_ID,dist = Dist.CLIENT)
public class CatBurgerClient {
    public CatBurgerClient(IEventBus modEventBus, ModContainer modContainer) {
        if (YSMCompat.isLoad()) {
            NeoForge.EVENT_BUS.addListener(YSMCompat::RenderLivingEvent);
        } else {
            modEventBus.addListener(this::FMLClientSetupEvent);
        }
    }

    public void FMLClientSetupEvent(FMLClientSetupEvent event) {
        CuriosRendererRegistry.register(InitItem.CAT_BURGER.get(), CatBurgerRenderer::new);
    }
}
