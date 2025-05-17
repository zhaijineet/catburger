package net.zhaiji.catburger;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.world.entity.player.Player;
import net.zhaiji.catburger.config.CatBurgerConfig;
import net.zhaiji.catburger.init.InitCreativeModeTab;
import net.zhaiji.catburger.init.InitItem;
import net.zhaiji.catburger.item.CatBurgerItem;
import net.zhaiji.catburger.network.CatBurgerPacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CatBurger implements ModInitializer {
    public static final String MOD_ID = "catburger";
    private static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        AutoConfig.register(CatBurgerConfig.class, JanksonConfigSerializer::new);

        InitItem.register();

        InitCreativeModeTab.register();

        CatBurgerPacket.registry();

        ServerLifecycleEvents.SERVER_STARTED.register(server -> LOGGER.info("CatBurger模组已加载！"));

        ServerLivingEntityEvents.ALLOW_DEATH.register((livingEntity, source, damageAmount) -> {
            if (!(livingEntity instanceof Player player)) {
                return true;
            }

            boolean revived = CatBurgerItem.handlePlayerDeath(player, source);

            return !revived;
        });
    }
}