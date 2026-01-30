package net.zhaiji.catburger.event;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.player.PlayerWakeUpEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.zhaiji.catburger.config.CatBurgerCommonConfig;
import net.zhaiji.catburger.init.InitItem;
import net.zhaiji.catburger.network.client.packet.PlayerDeathPacket;
import top.theillusivec4.curios.api.CuriosApi;

public class CommonEventHandler {
    public static void handlerLivingDeathEvent(LivingDeathEvent event) {
        if (!CatBurgerCommonConfig.totemEffectActive) return;
        Item item = InitItem.CAT_BURGER.get();
        if (event.getEntity() instanceof Player player && !player.getCooldowns().isOnCooldown(item)) {
            CuriosApi.getCuriosInventory(player).ifPresent(iCuriosItemHandler -> {
                if (iCuriosItemHandler.findFirstCurio(item).isPresent()) {
                    FoodData foodData = player.getFoodData();
                    player.setHealth(CatBurgerCommonConfig.healthRestorationFromTotem);
                    foodData.setFoodLevel(CatBurgerCommonConfig.foodRestorationFromTotem);
                    foodData.setSaturation(CatBurgerCommonConfig.saturationRestorationFromTotem);
                    player.getCooldowns().addCooldown(InitItem.CAT_BURGER.get(), CatBurgerCommonConfig.totemCooldown);
                    player.level().broadcastEntityEvent(player, (byte) 35);
                    PacketDistributor.sendToPlayer((ServerPlayer) player, new PlayerDeathPacket());
                    event.setCanceled(true);
                }
            });
        }
    }

    public static void handlerPlayerWakeUpEvent(PlayerWakeUpEvent event) {
        if (!CatBurgerCommonConfig.wakeUpCanResetCooldown) return;
        Player player = event.getEntity();
        Item item = InitItem.CAT_BURGER.get();
        CuriosApi.getCuriosInventory(player).ifPresent(iCuriosItemHandler -> {
            if (iCuriosItemHandler.findFirstCurio(item).isPresent()) {
                player.getCooldowns().removeCooldown(item);
            }
        });
    }
}
