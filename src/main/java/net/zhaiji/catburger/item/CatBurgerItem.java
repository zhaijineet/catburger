package net.zhaiji.catburger.item;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.zhaiji.catburger.CatBurger;
import net.zhaiji.catburger.Config;
import net.zhaiji.catburger.init.InitItem;
import net.zhaiji.catburger.network.CatBurgerPacket;
import net.zhaiji.catburger.network.packet.PlayerDeathPacket;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.List;

@Mod.EventBusSubscriber(modid = CatBurger.MOD_ID)
public class CatBurgerItem extends Item implements ICurioItem {
    public CatBurgerItem() {
        super(new Item.Properties().stacksTo(1));
    }

    @Override
    public boolean canEquipFromUse(SlotContext slotContext, ItemStack stack) {
        return true;
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        if (slotContext.entity() instanceof Player player && player.tickCount % Config.curios_cooldown == 0) {
            FoodData foodData = player.getFoodData();
            int foodLevel = foodData.getFoodLevel();
            foodData.setFoodLevel(Math.min(foodLevel + Config.food_restoration_form_curios, 20));
        }
    }

    @SubscribeEvent
    public static void LivingDeathEvent(LivingDeathEvent event) {
        if (event.getEntity() instanceof Player player && !player.getCooldowns().isOnCooldown(InitItem.CAT_BURGER.get())) {
            CuriosApi.getCuriosInventory(player).ifPresent(iCuriosItemHandler -> {
                if(!iCuriosItemHandler.findCurios(InitItem.CAT_BURGER.get()).isEmpty()){
                    player.setHealth(Config.health_restoration_form_totem);
                    player.getFoodData().setFoodLevel(Config.food_restoration_form_totem);
                    player.getFoodData().setSaturation(Config.saturation_restoration_form_totem);
                    player.getCooldowns().addCooldown(InitItem.CAT_BURGER.get(), Config.totem_cooldown);
                    player.level().broadcastEntityEvent(player, (byte) 35);
                    CatBurgerPacket.sendToClient(new PlayerDeathPacket(), (ServerPlayer) player);
                    event.setCanceled(true);
                }
            });
        }
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> list, TooltipFlag tooltipFlag) {
        super.appendHoverText(itemStack, level, list, tooltipFlag);
        list.add(Component.translatable("item.catburger.cat_burger.tooltip"));
    }
}
