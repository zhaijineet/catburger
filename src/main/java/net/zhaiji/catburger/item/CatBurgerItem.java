package net.zhaiji.catburger.item;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.zhaiji.catburger.CatBurger;
import net.zhaiji.catburger.config.CatBurgerCommonConfig;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.function.Consumer;

public class CatBurgerItem extends Item implements ICurioItem {
    public CatBurgerItem() {
        super(new Properties().stacksTo(1).setId(ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(CatBurger.MOD_ID, "cat_burger"))));
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
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, display, builder, tooltipFlag);
        builder.accept(Component.translatable("item.catburger.cat_burger.tooltip"));
    }
}
