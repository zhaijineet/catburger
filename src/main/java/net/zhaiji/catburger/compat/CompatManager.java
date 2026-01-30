package net.zhaiji.catburger.compat;

import net.neoforged.fml.ModList;

public class CompatManager {
    public static boolean YSMLoad = false;
    public static boolean TLMLoad = false;

    public static boolean isYSMLoad() {
        YSMLoad = ModList.get().isLoaded("yes_steve_model");
        return YSMLoad;
    }

    public static boolean isTLMLoad() {
        TLMLoad = ModList.get().isLoaded("touhou_little_maid");
        return TLMLoad;
    }
}
