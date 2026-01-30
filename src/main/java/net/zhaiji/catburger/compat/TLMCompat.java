package net.zhaiji.catburger.compat;

import com.github.tartaricacid.touhoulittlemaid.api.event.MaidDeathEvent;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemCooldowns;
import net.neoforged.bus.api.IEventBus;
import net.zhaiji.catburger.config.CatBurgerCommonConfig;
import net.zhaiji.catburger.init.InitItem;
import top.theillusivec4.curios.api.CuriosApi;

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
        Item item = InitItem.CAT_BURGER.get();
        ItemCooldowns cooldowns = maid.getCooldowns();
        CuriosApi.getCuriosInventory(maid).ifPresent(iCuriosItemHandler -> {
            if (iCuriosItemHandler.findFirstCurio(item).isPresent() && !cooldowns.isOnCooldown(item)) {
                maid.setHealth(CatBurgerCommonConfig.healthRestorationFromTotem);
                cooldowns.addCooldown(item, CatBurgerCommonConfig.totemCooldown);
                maid.level().broadcastEntityEvent(maid, (byte) 35);
                event.setCanceled(true);
            }
        });
    }
}
