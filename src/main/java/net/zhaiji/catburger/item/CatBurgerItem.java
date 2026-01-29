package net.zhaiji.catburger.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.zhaiji.catburger.config.CatBurgerCommonConfig;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.List;

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
        if (slotContext.entity() instanceof Player player && player.tickCount % CatBurgerCommonConfig.curiosCooldown == 0) {
            FoodData foodData = player.getFoodData();
            int foodLevel = foodData.getFoodLevel();
            if (foodLevel < CatBurgerCommonConfig.foodMaxRestoration) {
                foodData.setFoodLevel(Math.min(foodLevel + CatBurgerCommonConfig.foodRestorationFromCurios, CatBurgerCommonConfig.foodMaxRestoration));
            }
        }
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> list, TooltipFlag tooltipFlag) {
        super.appendHoverText(itemStack, level, list, tooltipFlag);
        list.add(Component.translatable("item.catburger.cat_burger.tooltip"));
    }
}
