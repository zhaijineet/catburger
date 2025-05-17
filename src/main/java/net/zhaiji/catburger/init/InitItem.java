package net.zhaiji.catburger.init;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.zhaiji.catburger.CatBurger;
import net.zhaiji.catburger.item.CatBurgerItem;

public class InitItem {
    public static final Item CAT_BURGER = new CatBurgerItem();

    public static void register() {
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(CatBurger.MOD_ID, "cat_burger"), CAT_BURGER);
    }
}
