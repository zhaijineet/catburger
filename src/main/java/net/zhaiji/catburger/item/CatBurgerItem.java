package net.zhaiji.catburger.item;

import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketItem;
import dev.emi.trinkets.api.TrinketsApi;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.zhaiji.catburger.config.CatBurgerConfig;
import net.zhaiji.catburger.init.InitItem;
import net.zhaiji.catburger.network.CatBurgerPacket;
import net.zhaiji.catburger.network.packet.PlayerDeathPacket;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CatBurgerItem extends TrinketItem {
    public CatBurgerItem() {
        super(new Item.Properties().stacksTo(1));

        registerEventHandlers();
    }

    private void registerEventHandlers() {

        ServerPlayerEvents.AFTER_RESPAWN.register((oldPlayer, newPlayer, alive) -> {
            if (alive) {
                handlePlayerWakeUp(newPlayer);
            }
        });

        ServerTickEvents.END_SERVER_TICK.register(server -> {
            for (ServerPlayer player : server.getPlayerList().getPlayers()) {
                checkAndRestoreFood(player);
            }
        });
    }

    private void checkAndRestoreFood(Player player) {
        if (player.tickCount % CatBurgerConfig.get().trinket_cooldown != 0) return;

        boolean hasCatBurger = TrinketsApi.getTrinketComponent(player)
                .map(component -> component.isEquipped(InitItem.CAT_BURGER))
                .orElse(false);

        if (hasCatBurger) {
            FoodData foodData = player.getFoodData();
            int foodLevel = foodData.getFoodLevel();
            if (foodLevel < CatBurgerConfig.get().food_max_restoration) {
                foodData.setFoodLevel(Math.min(foodLevel + CatBurgerConfig.get().food_restoration_form_trinket,
                        CatBurgerConfig.get().food_max_restoration));
            }
        }
    }

    @Override
    public void tick(ItemStack stack, SlotReference slot, LivingEntity entity) {
    }

    @Override
    public boolean canEquip(ItemStack stack, SlotReference slot, LivingEntity entity) {
        return true;
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> list, TooltipFlag tooltipFlag) {
        super.appendHoverText(itemStack, level, list, tooltipFlag);
        list.add(Component.translatable("item.catburger.cat_burger.tooltip"));
    }

    public static boolean handlePlayerDeath(Player player, DamageSource source) {
        if (!CatBurgerConfig.get().totem_effect_active) return false;

        if (!player.getCooldowns().isOnCooldown(InitItem.CAT_BURGER)) {
            return TrinketsApi.getTrinketComponent(player)
                    .map(component -> {
                        if (component.isEquipped(InitItem.CAT_BURGER)) {
                            player.setHealth(CatBurgerConfig.get().health_restoration_form_totem);
                            player.getFoodData().setFoodLevel(CatBurgerConfig.get().food_restoration_form_totem);
                            player.getFoodData().setSaturation(CatBurgerConfig.get().saturation_restoration_form_totem);
                            player.getCooldowns().addCooldown(InitItem.CAT_BURGER, CatBurgerConfig.get().totem_cooldown);
                            player.level().broadcastEntityEvent(player, (byte) 35);
                            if (player instanceof ServerPlayer serverPlayer) {
                                CatBurgerPacket.sendToClient(new PlayerDeathPacket(), serverPlayer);
                            }
                            return true;
                        }
                        return false;
                    }).orElse(false);
        }
        return false;
    }

    public static void handlePlayerWakeUp (Player player) {
        if (!CatBurgerConfig.get().wake_up_can_reset_cooldown) return;

        TrinketsApi.getTrinketComponent(player).ifPresent(component -> {
            if (component.isEquipped(InitItem.CAT_BURGER)) {
                player.getCooldowns().removeCooldown(InitItem.CAT_BURGER);
            }
        });
    }

}