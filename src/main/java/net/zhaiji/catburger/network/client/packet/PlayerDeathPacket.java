package net.zhaiji.catburger.network.client.packet;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.zhaiji.catburger.CatBurger;

public record PlayerDeathPacket() implements CustomPacketPayload {
    public static final Type<PlayerDeathPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(CatBurger.MOD_ID, "player_death_packet"));

    public static final StreamCodec<ByteBuf, PlayerDeathPacket> STREAM_CODEC = StreamCodec.unit(new PlayerDeathPacket());

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
