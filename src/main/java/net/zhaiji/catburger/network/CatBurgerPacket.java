package net.zhaiji.catburger.network;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.zhaiji.catburger.CatBurger;
import net.zhaiji.catburger.network.packet.PlayerDeathPacket;

@EventBusSubscriber(modid = CatBurger.MOD_ID,bus = EventBusSubscriber.Bus.MOD)
public class CatBurgerPacket {
    public static final String VERSION = "1.0";

    @SubscribeEvent
    public static void register(final RegisterPayloadHandlersEvent event){
        final PayloadRegistrar registrar = event.registrar(VERSION);
        registrar.playToClient(
                PlayerDeathPacket.TYPE,
                PlayerDeathPacket.STREAM_CODEC,
                PlayerDeathPacket::handle
        );
    }
}
