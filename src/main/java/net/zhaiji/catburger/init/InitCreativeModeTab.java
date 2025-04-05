package net.zhaiji.catburger.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.zhaiji.catburger.CatBurger;

import java.util.function.Supplier;

public class InitCreativeModeTab {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, CatBurger.MOD_ID);

    public static final Supplier<CreativeModeTab> CATBURGER_TAB = CREATIVE_MODE_TAB.register(
            "catburger_tab",
            () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(InitItem.CAT_BURGER.get()))
                    .title(Component.translatable("creativetab.catburger.catburger_tab"))
                    .displayItems(((itemDisplayParameters, output) -> {
                        output.accept(InitItem.CAT_BURGER.get());
                    }))
                    .build()
    );

}