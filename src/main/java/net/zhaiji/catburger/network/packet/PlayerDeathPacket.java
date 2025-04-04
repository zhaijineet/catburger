package net.zhaiji.catburger.network.packet;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import net.zhaiji.catburger.init.InitItem;

import java.util.function.Supplier;

public class PlayerDeathPacket {
    public void encode(FriendlyByteBuf buf) {
    }

    public static PlayerDeathPacket decode(FriendlyByteBuf buf) {
        return new PlayerDeathPacket();
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        Minecraft minecraft = Minecraft.getInstance();
        context.enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
                minecraft.gameRenderer.displayItemActivation(new ItemStack(InitItem.CAT_BURGER.get()));
                System.out.println("Kitty you can has cheese burger");
            });
        });
        context.setPacketHandled(true);
    }
}
