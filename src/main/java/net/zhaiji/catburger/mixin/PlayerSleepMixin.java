package net.zhaiji.catburger.mixin;

import net.minecraft.world.entity.player.Player;
import net.zhaiji.catburger.item.CatBurgerItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public class PlayerSleepMixin {
    @Inject(method = "stopSleeping", at = @At("HEAD"))
    private void onWakeUp(CallbackInfo ci) {
        Player player = (Player)(Object)this;
        CatBurgerItem.handlePlayerWakeUp(player);
    }
}