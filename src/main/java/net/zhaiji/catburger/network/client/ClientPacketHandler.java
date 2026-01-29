package net.zhaiji.catburger.network.client;

import net.minecraft.client.Minecraft;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.zhaiji.catburger.init.InitItem;
import net.zhaiji.catburger.network.client.packet.PlayerDeathPacket;

public class ClientPacketHandler {
    public static void handlerPlayerDeathPacket(PlayerDeathPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            Minecraft.getInstance().gameRenderer.displayItemActivation((InitItem.CAT_BURGER.get().getDefaultInstance()));
        });
    }
}
