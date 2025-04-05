package net.zhaiji.catburger.network;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import net.zhaiji.catburger.CatBurger;
import net.zhaiji.catburger.network.packet.PlayerDeathPacket;

public class CatBurgerPacket {
    public static final String VERSION = "1.0";

    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(CatBurger.MOD_ID, "main"),
            () -> VERSION,
            VERSION::equals,
            VERSION::equals
    );

    public static void registry() {
        int id = 0;
        INSTANCE.messageBuilder(PlayerDeathPacket.class, id++, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(PlayerDeathPacket::encode)
                .decoder(PlayerDeathPacket::decode)
                .consumerMainThread(PlayerDeathPacket::handle)
                .add();
    }

    public static <MSG> void sendToClient(MSG msg, ServerPlayer serverPlayer) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> serverPlayer), msg);
    }
}
