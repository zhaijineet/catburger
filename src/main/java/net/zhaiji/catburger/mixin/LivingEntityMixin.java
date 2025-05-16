package net.zhaiji.catburger.mixin;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.zhaiji.catburger.item.CatBurgerItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @Inject(method = "die", at = @At("HEAD"), cancellable = true)
    private void onDie(DamageSource damageSource, CallbackInfo ci) {
        if ((Object)this instanceof Player player) {
            if (CatBurgerItem.handlePlayerDeath(player, damageSource)) {
                ci.cancel(); // 取消死亡
            }
        }
    }
}