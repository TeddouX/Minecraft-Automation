package teddy.minecraftautomation.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.core.HolderLookup;
import teddy.minecraftautomation.blocks.ModBlocks;

import java.util.concurrent.CompletableFuture;

public class ModLootTablesProvider extends FabricBlockLootTableProvider {
    public ModLootTablesProvider(FabricDataOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generate() {
        dropSelf(ModBlocks.WOODEN_ITEM_PIPE);
        dropSelf(ModBlocks.WOODEN_FLUID_PIPE);
        dropSelf(ModBlocks.WOODEN_ITEM_PUMP);
        dropSelf(ModBlocks.WOODEN_FLUID_PUMP);
        dropSelf(ModBlocks.WOODEN_FLUID_TANK);
        dropSelf(ModBlocks.COPPER_ITEM_PIPE);
        dropSelf(ModBlocks.COPPER_FLUID_PIPE);
        dropSelf(ModBlocks.COPPER_ITEM_PUMP);
        dropSelf(ModBlocks.COPPER_FLUID_PUMP);
        dropSelf(ModBlocks.COPPER_FLUID_TANK);
        dropSelf(ModBlocks.IRON_ITEM_PIPE);
        dropSelf(ModBlocks.IRON_FLUID_PIPE);
        dropSelf(ModBlocks.IRON_ITEM_PUMP);
        dropSelf(ModBlocks.IRON_FLUID_PUMP);
        dropSelf(ModBlocks.IRON_FLUID_TANK);
        dropSelf(ModBlocks.GOLD_ITEM_PIPE);
        dropSelf(ModBlocks.GOLD_FLUID_PIPE);
        dropSelf(ModBlocks.GOLD_ITEM_PUMP);
        dropSelf(ModBlocks.GOLD_FLUID_PUMP);
        dropSelf(ModBlocks.GOLD_FLUID_TANK);
        dropSelf(ModBlocks.DIAMOND_ITEM_PIPE);
        dropSelf(ModBlocks.DIAMOND_FLUID_PIPE);
        dropSelf(ModBlocks.DIAMOND_ITEM_PUMP);
        dropSelf(ModBlocks.DIAMOND_FLUID_PUMP);
        dropSelf(ModBlocks.DIAMOND_FLUID_TANK);
        dropSelf(ModBlocks.NETHERITE_ITEM_PIPE);
        dropSelf(ModBlocks.NETHERITE_FLUID_PIPE);
        dropSelf(ModBlocks.NETHERITE_ITEM_PUMP);
        dropSelf(ModBlocks.NETHERITE_FLUID_PUMP);
        dropSelf(ModBlocks.NETHERITE_FLUID_TANK);
    }
}
