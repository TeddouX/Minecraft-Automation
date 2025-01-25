package teddy.minecraftautomation.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.tags.BlockTags;
import teddy.minecraftautomation.blocks.ModBlocks;
import teddy.minecraftautomation.datagen.utils.ModBlockTags;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagsProvider extends FabricTagProvider.BlockTagProvider {
    public ModBlockTagsProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        getOrCreateTagBuilder(ModBlockTags.ITEM_PIPES)
                .add(ModBlocks.WOODEN_ITEM_PIPE)
                .add(ModBlocks.COPPER_ITEM_PIPE)
                .add(ModBlocks.IRON_ITEM_PIPE)
                .add(ModBlocks.GOLD_ITEM_PIPE)
                .add(ModBlocks.DIAMOND_ITEM_PIPE)
                .add(ModBlocks.NETHERITE_ITEM_PIPE);

        getOrCreateTagBuilder(ModBlockTags.FLUID_PIPES)
                .add(ModBlocks.WOODEN_FLUID_PIPE)
                .add(ModBlocks.COPPER_FLUID_PIPE)
                .add(ModBlocks.IRON_FLUID_PIPE)
                .add(ModBlocks.GOLD_FLUID_PIPE)
                .add(ModBlocks.DIAMOND_FLUID_PIPE)
                .add(ModBlocks.NETHERITE_FLUID_PIPE);

        getOrCreateTagBuilder(ModBlockTags.ITEM_PUMPS)
                .add(ModBlocks.WOODEN_ITEM_PUMP)
                .add(ModBlocks.COPPER_ITEM_PUMP)
                .add(ModBlocks.IRON_ITEM_PUMP)
                .add(ModBlocks.GOLD_ITEM_PUMP)
                .add(ModBlocks.DIAMOND_ITEM_PUMP)
                .add(ModBlocks.NETHERITE_ITEM_PUMP);

        getOrCreateTagBuilder(ModBlockTags.FLUID_PUMPS)
                .add(ModBlocks.WOODEN_FLUID_PUMP)
                .add(ModBlocks.COPPER_FLUID_PUMP)
                .add(ModBlocks.IRON_FLUID_PUMP)
                .add(ModBlocks.GOLD_FLUID_PUMP)
                .add(ModBlocks.DIAMOND_FLUID_PUMP)
                .add(ModBlocks.NETHERITE_FLUID_PUMP);

        getOrCreateTagBuilder(ModBlockTags.FLUID_TANKS)
                .add(ModBlocks.WOODEN_FLUID_TANK)
                .add(ModBlocks.COPPER_FLUID_TANK)
                .add(ModBlocks.IRON_FLUID_TANK)
                .add(ModBlocks.GOLD_FLUID_TANK)
                .add(ModBlocks.DIAMOND_FLUID_TANK)
                .add(ModBlocks.NETHERITE_FLUID_TANK);

        getOrCreateTagBuilder(BlockTags.MINEABLE_WITH_PICKAXE)
                .addTag(ModBlockTags.ITEM_PIPES)
                .addTag(ModBlockTags.FLUID_PIPES)
                .addTag(ModBlockTags.ITEM_PUMPS)
                .addTag(ModBlockTags.FLUID_PUMPS)
                .addTag(ModBlockTags.FLUID_TANKS);
    }
}

