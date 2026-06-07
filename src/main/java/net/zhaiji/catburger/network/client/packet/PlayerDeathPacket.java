package net.zhaiji.catburger.network.client.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import net.zhaiji.catburger.network.client.ClientPacketHandler;

import java.util.function.Supplier;

public class PlayerDeathPacket {
    public void encode(FriendlyByteBuf buf) {
    }

    public static PlayerDeathPacket decode(FriendlyByteBuf buf) {
        return new PlayerDeathPacket();
    }

    public void handler(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
                ClientPacketHandler.handlerPlayerDeathPacket(this);
            });
        });
        context.setPacketHandled(true);
    }
}
