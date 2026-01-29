package net.zhaiji.catburger.network.client;

import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import net.zhaiji.catburger.init.InitItem;
import net.zhaiji.catburger.network.client.packet.PlayerDeathPacket;

public class ClientPacketHandler {
    public static void handlerPlayerDeathPacket(PlayerDeathPacket packet) {
        Minecraft.getInstance().gameRenderer.displayItemActivation(new ItemStack(InitItem.CAT_BURGER.get()));
    }
}
