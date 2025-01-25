package teddy.minecraftautomation.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.registry.RegistryWrapper;
import teddy.minecraftautomation.blocks.ModBlocks;

import java.util.concurrent.CompletableFuture;

public class ModLootTablesProvider extends FabricBlockLootTableProvider {
    public ModLootTablesProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generate() {
        addDrop(ModBlocks.WOODEN_ITEM_PIPE);
        addDrop(ModBlocks.WOODEN_FLUID_PIPE);
        addDrop(ModBlocks.WOODEN_ITEM_PUMP);
        addDrop(ModBlocks.WOODEN_FLUID_PUMP);
        addDrop(ModBlocks.WOODEN_FLUID_TANK);
        addDrop(ModBlocks.COPPER_ITEM_PIPE);
        addDrop(ModBlocks.COPPER_FLUID_PIPE);
        addDrop(ModBlocks.COPPER_ITEM_PUMP);
        addDrop(ModBlocks.COPPER_FLUID_PUMP);
        addDrop(ModBlocks.COPPER_FLUID_TANK);
        addDrop(ModBlocks.IRON_ITEM_PIPE);
        addDrop(ModBlocks.IRON_FLUID_PIPE);
        addDrop(ModBlocks.IRON_ITEM_PUMP);
        addDrop(ModBlocks.IRON_FLUID_PUMP);
        addDrop(ModBlocks.IRON_FLUID_TANK);
        addDrop(ModBlocks.GOLD_ITEM_PIPE);
        addDrop(ModBlocks.GOLD_FLUID_PIPE);
        addDrop(ModBlocks.GOLD_ITEM_PUMP);
        addDrop(ModBlocks.GOLD_FLUID_PUMP);
        addDrop(ModBlocks.GOLD_FLUID_TANK);
        addDrop(ModBlocks.DIAMOND_ITEM_PIPE);
        addDrop(ModBlocks.DIAMOND_FLUID_PIPE);
        addDrop(ModBlocks.DIAMOND_ITEM_PUMP);
        addDrop(ModBlocks.DIAMOND_FLUID_PUMP);
        addDrop(ModBlocks.DIAMOND_FLUID_TANK);
        addDrop(ModBlocks.NETHERITE_ITEM_PIPE);
        addDrop(ModBlocks.NETHERITE_FLUID_PIPE);
        addDrop(ModBlocks.NETHERITE_ITEM_PUMP);
        addDrop(ModBlocks.NETHERITE_FLUID_PUMP);
        addDrop(ModBlocks.NETHERITE_FLUID_TANK);
    }
}
