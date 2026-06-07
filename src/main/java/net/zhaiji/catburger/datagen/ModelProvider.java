package net.zhaiji.catburger.datagen;

import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.ModelLocationUtils;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.zhaiji.catburger.CatBurger;
import net.zhaiji.catburger.init.InitItem;

import java.util.stream.Stream;

public class ModelProvider extends net.minecraft.client.data.models.ModelProvider {
    public ModelProvider(PackOutput output) {
        super(output, CatBurger.MOD_ID);
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        itemModels.itemModelOutput.accept(
            InitItem.CAT_BURGER.get(),
            ItemModelUtils.plainModel(ModelLocationUtils.getModelLocation(InitItem.CAT_BURGER.get()))
        );
    }

    @Override
    protected Stream<? extends net.minecraft.core.Holder<Item>> getKnownItems() {
        return Stream.of(InitItem.CAT_BURGER.get()).map(Item::builtInRegistryHolder);
    }
}
