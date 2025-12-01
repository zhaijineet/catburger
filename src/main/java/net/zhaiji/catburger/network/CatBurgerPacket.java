package net.zhaiji.catburger.network;


import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.zhaiji.catburger.init.InitItem;
import net.zhaiji.catburger.network.packet.PlayerDeathPayload;

public class CatBurgerPacket {

    public static void registerClient() {

        ClientPlayNetworking.registerGlobalReceiver(
                PlayerDeathPayload.PAYLOAD_TYPE,
                (payload, context) -> {
                    Minecraft client = context.client();
                    client.execute(() -> {
                        client.gameRenderer.displayItemActivation(new ItemStack(InitItem.CAT_BURGER));
                        System.out.println("Kitty you can has cheese burger");
                    });
                }
        );
    }

    public static void registerServer() {
        PayloadTypeRegistry.playS2C().register(
                PlayerDeathPayload.PAYLOAD_TYPE,
                PlayerDeathPayload.CODEC
        );
        PayloadTypeRegistry.playC2S().register(
                PlayerDeathPayload.PAYLOAD_TYPE,
                PlayerDeathPayload.CODEC
        );
    }

    public static void sendToClient(ServerPlayer player) {
        ServerPlayNetworking.send(player, new PlayerDeathPayload());
    }
}