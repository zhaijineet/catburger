package net.zhaiji.catburger.client.compat;

import com.github.tartaricacid.touhoulittlemaid.api.event.MaidDeathEvent;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemCooldowns;
import net.neoforged.bus.api.IEventBus;
import net.zhaiji.catburger.config.CatBurgerCommonConfig;
import net.zhaiji.catburger.init.InitItem;

public class TLMCompat {
    public static boolean canRender(LivingEntity entity) {
        return entity instanceof EntityMaid;
    }

    public static void init(IEventBus modBus, IEventBus gameBus) {
        TLMCompat.modBusListener(modBus);
        TLMCompat.gameBusListener(gameBus);
    }

    public static void modBusListener(IEventBus modBus) {
    }

    public static void gameBusListener(IEventBus gameBus) {
        gameBus.addListener(TLMCompat::handlerMaidDeathEvent);
    }

    public static void handlerMaidDeathEvent(MaidDeathEvent event) {
        if (!CatBurgerCommonConfig.totemEffectActive) return;
        EntityMaid maid = event.getMaid();
        ItemCooldowns cooldowns = maid.getCooldowns();
        if (!cooldowns.isOnCooldown(InitItem.CAT_BURGER.get())) {
            maid.setHealth(CatBurgerCommonConfig.healthRestorationFromTotem);
            cooldowns.addCooldown(InitItem.CAT_BURGER.get(), CatBurgerCommonConfig.totemCooldown);
            maid.level().broadcastEntityEvent(maid, (byte) 35);
            event.setCanceled(true);
        }
    }
}
