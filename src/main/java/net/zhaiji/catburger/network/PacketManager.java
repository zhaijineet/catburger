package net.zhaiji.catburger.network;

import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.zhaiji.catburger.network.client.ClientPacketHandler;
import net.zhaiji.catburger.network.client.packet.PlayerDeathPacket;

public class PacketManager {
    public static final String VERSION = "1.0";

    public static void handlerRegisterPayloadHandlersEvent(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar(VERSION);
        registrar.playToClient(
                PlayerDeathPacket.TYPE,
                PlayerDeathPacket.STREAM_CODEC,
                ClientPacketHandler::handlerPlayerDeathPacket
        );
    }
}
