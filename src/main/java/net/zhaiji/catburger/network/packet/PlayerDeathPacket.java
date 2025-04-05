package net.zhaiji.catburger.network.packet;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.zhaiji.catburger.CatBurger;
import net.zhaiji.catburger.init.InitItem;

public record PlayerDeathPacket() implements CustomPacketPayload {
    public static final Type<PlayerDeathPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(CatBurger.MOD_ID, "player_death_packet"));

    public static final StreamCodec<ByteBuf, PlayerDeathPacket> STREAM_CODEC = StreamCodec.unit(new PlayerDeathPacket());

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(final PlayerDeathPacket packet, final IPayloadContext context) {
        if (FMLEnvironment.dist == Dist.CLIENT) {
            Minecraft minecraft = Minecraft.getInstance();
            context.enqueueWork(() -> {
                minecraft.gameRenderer.displayItemActivation(new ItemStack(InitItem.CAT_BURGER.get()));
            });
        }
    }
}
