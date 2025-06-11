package net.zhaiji.catburger.network.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.zhaiji.catburger.CatBurger;


public record PlayerDeathPayload() implements CustomPacketPayload {
    public static final ResourceLocation TYPE = ResourceLocation.fromNamespaceAndPath(CatBurger.MOD_ID, "player_death");
    public static final CustomPacketPayload.Type<PlayerDeathPayload> PAYLOAD_TYPE = new CustomPacketPayload.Type<>(TYPE);
    public static final StreamCodec<FriendlyByteBuf, PlayerDeathPayload> CODEC =
            StreamCodec.unit(new PlayerDeathPayload());


    @Override
    public Type<? extends CustomPacketPayload> type() {
        return PAYLOAD_TYPE;
    }
}
