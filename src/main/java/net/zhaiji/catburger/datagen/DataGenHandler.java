package net.zhaiji.catburger.datagen;

import net.neoforged.neoforge.data.event.GatherDataEvent;

public class DataGenHandler {
    public static void handlerGatherDataEvent$Client(GatherDataEvent.Client event) {
        event.createProvider(RecipeProvider.Runner::new);
        event.createProvider(ModelProvider::new);
    }
}
