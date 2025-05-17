package net.zhaiji.catburger.network.packet;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.zhaiji.catburger.init.InitItem;

public class PlayerDeathPacket {
    public void encode(FriendlyByteBuf buf) {
    }

    public static PlayerDeathPacket decode(FriendlyByteBuf buf) {
        return new PlayerDeathPacket();
    }

    @Environment(EnvType.CLIENT)
    public void handle() {
        Minecraft minecraft = Minecraft.getInstance();
        minecraft.gameRenderer.displayItemActivation(new ItemStack(InitItem.CAT_BURGER));
        System.out.println("Kitty you can has cheese burger");
    }
}