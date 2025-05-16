package net.zhaiji.catburger.init;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.zhaiji.catburger.CatBurger;

public class InitCreativeModeTab {
    public static final ResourceKey<CreativeModeTab> CATBURGER_TAB_KEY = ResourceKey.create(
            Registries.CREATIVE_MODE_TAB,
            new ResourceLocation(CatBurger.MOD_ID, "catburger_tab")
    );

    public static final CreativeModeTab CATBURGER_TAB = FabricItemGroup.builder()
            .icon(() -> new ItemStack(InitItem.CAT_BURGER))
            .title(Component.translatable("itemGroup." + CatBurger.MOD_ID + ".catburger_tab"))
            .displayItems((context, entries) -> {
                entries.accept(InitItem.CAT_BURGER);
            })
            .build();

    public static void register() {
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, CATBURGER_TAB_KEY, CATBURGER_TAB);
    }
}