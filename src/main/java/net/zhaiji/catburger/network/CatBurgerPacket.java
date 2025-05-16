package net.zhaiji.catburger.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.zhaiji.catburger.CatBurger;
import net.zhaiji.catburger.network.packet.PlayerDeathPacket;

public class CatBurgerPacket {
    // 定义网络包ID
    public static final ResourceLocation PLAYER_DEATH_PACKET_ID = new ResourceLocation(CatBurger.MOD_ID, "player_death");

    // 注册网络包
    public static void registry() {
        // 在客户端注册接收器
        ClientPlayNetworking.registerGlobalReceiver(PLAYER_DEATH_PACKET_ID, (client, handler, buf, responseSender) -> {
            PlayerDeathPacket packet = PlayerDeathPacket.decode(buf);
            client.execute(packet::handle);
        });
    }

    // 发送网络包到客户端
    public static void sendToClient(PlayerDeathPacket msg, ServerPlayer serverPlayer) {
        var buf = PacketByteBufs.create();
        msg.encode(buf);
        ServerPlayNetworking.send(serverPlayer, PLAYER_DEATH_PACKET_ID, buf);
    }
}