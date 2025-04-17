package net.zhaiji.catburger.item;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.player.PlayerWakeUpEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.zhaiji.catburger.CatBurger;
import net.zhaiji.catburger.config.CatBurgerCommonConfig;
import net.zhaiji.catburger.init.InitItem;
import net.zhaiji.catburger.network.packet.PlayerDeathPacket;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.List;

@EventBusSubscriber(modid = CatBurger.MOD_ID)
public class CatBurgerItem extends Item implements ICurioItem {
    public CatBurgerItem() {
        super(new Properties().stacksTo(1));
    }

    @Override
    public boolean canEquipFromUse(SlotContext slotContext, ItemStack stack) {
        return true;
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        if (slotContext.entity() instanceof Player player && player.tickCount % CatBurgerCommonConfig.curios_cooldown == 0) {
            FoodData foodData = player.getFoodData();
            int foodLevel = foodData.getFoodLevel();
            foodData.setFoodLevel(Math.min(foodLevel + CatBurgerCommonConfig.food_restoration_form_curios, CatBurgerCommonConfig.food_max_restoration));
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        tooltipComponents.add(Component.translatable("item.catburger.cat_burger.tooltip"));
    }

    @SubscribeEvent
    public static void LivingDeathEvent(LivingDeathEvent event) {
        if (!CatBurgerCommonConfig.totem_effect_active) return;
        if (event.getEntity() instanceof Player player && !player.getCooldowns().isOnCooldown(InitItem.CAT_BURGER.get())) {
            CuriosApi.getCuriosInventory(player).ifPresent(iCuriosItemHandler -> {
                if(!iCuriosItemHandler.findCurios(InitItem.CAT_BURGER.get()).isEmpty()){
                    player.setHealth(CatBurgerCommonConfig.health_restoration_form_totem);
                    player.getFoodData().setFoodLevel(CatBurgerCommonConfig.food_restoration_form_totem);
                    player.getFoodData().setSaturation(CatBurgerCommonConfig.saturation_restoration_form_totem);
                    player.getCooldowns().addCooldown(InitItem.CAT_BURGER.get(), CatBurgerCommonConfig.totem_cooldown);
                    player.level().broadcastEntityEvent(player, (byte) 35);
                    PacketDistributor.sendToPlayer((ServerPlayer) player, new PlayerDeathPacket());
                    event.setCanceled(true);
                }
            });
        }
    }

    @SubscribeEvent
    public static void PlayerWakeUpEvent(PlayerWakeUpEvent event) {
        if (!CatBurgerCommonConfig.wake_up_can_reset_cooldown) return;
        Player player = event.getEntity();
        CuriosApi.getCuriosInventory(player).ifPresent(iCuriosItemHandler -> {
            if (!iCuriosItemHandler.findCurios(InitItem.CAT_BURGER.get()).isEmpty()) {
                player.getCooldowns().removeCooldown(InitItem.CAT_BURGER.get());
            }
        });
    }
}
