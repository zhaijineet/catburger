package net.zhaiji.catburger.compat;

import net.minecraftforge.fml.ModList;

public class CompatManager {
    private static boolean YSMLoad = ModList.get().isLoaded("yes_steve_model");
    private static boolean TLMLoad = ModList.get().isLoaded("touhou_little_maid");

    public static boolean isYSMLoad() {
        return YSMLoad;
    }

    public static boolean isTLMLoad() {
        return TLMLoad;
    }
}
