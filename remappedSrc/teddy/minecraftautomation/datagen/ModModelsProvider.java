package teddy.minecraftautomation.datagen;

import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.data.BlockStateModelGenerator;
import net.minecraft.client.data.BlockStateSupplier;
import net.minecraft.client.data.BlockStateVariant;
import net.minecraft.client.data.BlockStateVariantMap;
import net.minecraft.client.data.ItemModelGenerator;
import net.minecraft.client.data.Models;
import net.minecraft.client.data.MultipartBlockStateSupplier;
import net.minecraft.client.data.TextureKey;
import net.minecraft.client.data.VariantSettings;
import net.minecraft.client.data.VariantsBlockStateSupplier;
import net.minecraft.client.data.When;
import net.minecraft.client.data.models.blockstates.*;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import teddy.minecraftautomation.blocks.AbstractPumpBlock;
import teddy.minecraftautomation.blocks.ModBlocks;
import teddy.minecraftautomation.datagen.utils.ParentModel;
import teddy.minecraftautomation.items.ModItems;

public class ModModelsProvider extends FabricModelProvider {
    public ModModelsProvider(FabricDataOutput output) {
        super(output);
    }

    BlockStateSupplier createPipeBlockState(Block block, Identifier middle, Identifier half) {
        return MultipartBlockStateSupplier.create(block)
                .with(BlockStateVariant.create().put(VariantSettings.MODEL, middle))
                .with(
                        When.create().set(Properties.NORTH, true),
                        BlockStateVariant.create().put(VariantSettings.MODEL, half)
                                .put(VariantSettings.UVLOCK, false)
                ).with(
                        When.create().set(Properties.EAST, true),
                        BlockStateVariant.create().put(VariantSettings.MODEL, half)
                                .put(VariantSettings.UVLOCK, false)
                                .put(VariantSettings.Y, VariantSettings.Rotation.R90)
                ).with(
                        When.create().set(Properties.SOUTH, true),
                        BlockStateVariant.create().put(VariantSettings.MODEL, half)
                                .put(VariantSettings.UVLOCK, false)
                                .put(VariantSettings.Y, VariantSettings.Rotation.R180)
                ).with(
                        When.create().set(Properties.WEST, true),
                        BlockStateVariant.create().put(VariantSettings.MODEL, half)
                                .put(VariantSettings.UVLOCK, false)
                                .put(VariantSettings.Y, VariantSettings.Rotation.R270)
                ).with(
                        When.create().set(Properties.UP, true),
                        BlockStateVariant.create().put(VariantSettings.MODEL, half)
                                .put(VariantSettings.UVLOCK, false)
                                .put(VariantSettings.X, VariantSettings.Rotation.R270)
                ).with(
                        When.create().set(Properties.DOWN, true),
                        BlockStateVariant.create().put(VariantSettings.MODEL, half)
                                .put(VariantSettings.UVLOCK, false)
                                .put(VariantSettings.X, VariantSettings.Rotation.R90)
                );
    }

    BlockStateSupplier createPumpBlockState(Block block, Identifier pumpModel) {
        return VariantsBlockStateSupplier.create(block)
                .coordinate(
                        BlockStateVariantMap.create(AbstractPumpBlock.AXIS, AbstractPumpBlock.NEGATIVE_AXIS)
                                .register(Direction.Axis.Z, false, BlockStateVariant.create().put(VariantSettings.MODEL, pumpModel))
                                .register(Direction.Axis.Z, true, BlockStateVariant.create().put(VariantSettings.MODEL, pumpModel)
                                        .put(VariantSettings.Y, VariantSettings.Rotation.R180))
                                .register(Direction.Axis.X, false, BlockStateVariant.create().put(VariantSettings.MODEL, pumpModel)
                                        .put(VariantSettings.Y, VariantSettings.Rotation.R90))
                                .register(Direction.Axis.X, true, BlockStateVariant.create().put(VariantSettings.MODEL, pumpModel)
                                        .put(VariantSettings.Y, VariantSettings.Rotation.R270))
                                .register(Direction.Axis.Y, false, BlockStateVariant.create().put(VariantSettings.MODEL, pumpModel)
                                        .put(VariantSettings.Y, VariantSettings.Rotation.R90)
                                        .put(VariantSettings.X, VariantSettings.Rotation.R90))
                                .register(Direction.Axis.Y, true, BlockStateVariant.create().put(VariantSettings.MODEL, pumpModel)
                                        .put(VariantSettings.Y, VariantSettings.Rotation.R90)
                                        .put(VariantSettings.X, VariantSettings.Rotation.R270))
                );
    }

    BlockStateSupplier createPump(String pumpType, BlockStateModelGenerator blockModelGenerators, Block block, Block parentBlock, boolean cutout) {
        Identifier pump = ParentModel.create(pumpType + "_pump", "", TextureKey.TEXTURE, block, parentBlock, blockModelGenerators.modelCollector);

        BlockStateSupplier blockState = createPumpBlockState(block, pump);

        blockModelGenerators.registerParentedItemModel(block, pump);

        return blockState;
    }

    BlockStateSupplier createItemPump(BlockStateModelGenerator blockModelGenerators, Block block, Block parentBlock) {
        return createPump("item", blockModelGenerators, block, parentBlock, false);
    }

    BlockStateSupplier createFluidPump(BlockStateModelGenerator blockModelGenerators, Block block, Block parentBlock) {
        return createPump("fluid", blockModelGenerators, block, parentBlock, true);
    }

    BlockStateSupplier createFluidTank(BlockStateModelGenerator blockModelGenerators, Block block, Block parentBlock) {
        Identifier resourceLocation = ParentModel.create("fluid_tank", "", TextureKey.TEXTURE, block, parentBlock, blockModelGenerators.modelCollector);

        BlockStateSupplier blockState = VariantsBlockStateSupplier.create(block, BlockStateVariant.create()
                .put(VariantSettings.MODEL, resourceLocation)
                .put(VariantSettings.UVLOCK, false));

        blockModelGenerators.registerParentedItemModel(block, resourceLocation);

        return blockState;
    }

    BlockStateSupplier createPipe(String pipeType, BlockStateModelGenerator blockModelGenerators, Block block, Block parentBlock, boolean cutout)  {
        // Create models that inherit from the parent models
        Identifier pipeMiddle = ParentModel.create( pipeType + "_pipe_middle", "_middle", TextureKey.TEXTURE, block, parentBlock, blockModelGenerators.modelCollector);
        Identifier pipeHalf = ParentModel.create(pipeType + "_pipe_half", "_half", TextureKey.TEXTURE, block, parentBlock, blockModelGenerators.modelCollector);
        Identifier pipeInventory = ParentModel.create(pipeType + "_pipe_inventory", "_inventory", TextureKey.TEXTURE, block, parentBlock, blockModelGenerators.modelCollector);

        // Create the item pipe block state
        BlockStateSupplier blockState = createPipeBlockState(block, pipeMiddle, pipeHalf);

        // Create the block's item model
        blockModelGenerators.registerParentedItemModel(block, pipeInventory);

        return blockState;
    }

    BlockStateSupplier createItemPipe(BlockStateModelGenerator blockModelGenerators, Block block, Block parentBlock) {
        return createPipe("item", blockModelGenerators, block, parentBlock, false);
    }

    BlockStateSupplier createFluidPipe(BlockStateModelGenerator blockModelGenerators, Block block, Block parentBlock) {
        return createPipe("fluid", blockModelGenerators, block, parentBlock, true);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockModelGenerators) {
        new BlockFamilyProvider(Blocks.OAK_PLANKS, blockModelGenerators)
                .itemPipe(ModBlocks.WOODEN_ITEM_PIPE)
                .fluidPipe(ModBlocks.WOODEN_FLUID_PIPE)
                .itemPump(ModBlocks.WOODEN_ITEM_PUMP)
                .fluidPump(ModBlocks.WOODEN_FLUID_PUMP)
                .fluidTank(ModBlocks.WOODEN_FLUID_TANK);
        new BlockFamilyProvider(Blocks.COPPER_BLOCK, blockModelGenerators)
                .itemPipe(ModBlocks.COPPER_ITEM_PIPE)
                .fluidPipe(ModBlocks.COPPER_FLUID_PIPE)
                .itemPump(ModBlocks.COPPER_ITEM_PUMP)
                .fluidPump(ModBlocks.COPPER_FLUID_PUMP)
                .fluidTank(ModBlocks.COPPER_FLUID_TANK);
        new BlockFamilyProvider(Blocks.IRON_BLOCK, blockModelGenerators)
                .itemPipe(ModBlocks.IRON_ITEM_PIPE)
                .fluidPipe(ModBlocks.IRON_FLUID_PIPE)
                .itemPump(ModBlocks.IRON_ITEM_PUMP)
                .fluidPump(ModBlocks.IRON_FLUID_PUMP)
                .fluidTank(ModBlocks.IRON_FLUID_TANK);
        new BlockFamilyProvider(Blocks.GOLD_BLOCK, blockModelGenerators)
                .itemPipe(ModBlocks.GOLD_ITEM_PIPE)
                .fluidPipe(ModBlocks.GOLD_FLUID_PIPE)
                .itemPump(ModBlocks.GOLD_ITEM_PUMP)
                .fluidPump(ModBlocks.GOLD_FLUID_PUMP)
                .fluidTank(ModBlocks.GOLD_FLUID_TANK);
        new BlockFamilyProvider(Blocks.DIAMOND_BLOCK, blockModelGenerators)
                .itemPipe(ModBlocks.DIAMOND_ITEM_PIPE)
                .fluidPipe(ModBlocks.DIAMOND_FLUID_PIPE)
                .itemPump(ModBlocks.DIAMOND_ITEM_PUMP)
                .fluidPump(ModBlocks.DIAMOND_FLUID_PUMP)
                .fluidTank(ModBlocks.DIAMOND_FLUID_TANK);
        new BlockFamilyProvider(Blocks.NETHERITE_BLOCK, blockModelGenerators)
                .itemPipe(ModBlocks.NETHERITE_ITEM_PIPE)
                .fluidPipe(ModBlocks.NETHERITE_FLUID_PIPE)
                .itemPump(ModBlocks.NETHERITE_ITEM_PUMP)
                .fluidPump(ModBlocks.NETHERITE_FLUID_PUMP)
                .fluidTank(ModBlocks.NETHERITE_FLUID_TANK);
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerators) {
        itemModelGenerators.register(ModItems.PIPE_JOINT);

        itemModelGenerators.register(ModItems.WRENCH, Models.GENERATED);
    }


    public class BlockFamilyProvider {
        private final Block parentBlock;
        private final BlockStateModelGenerator blockModelGenerators;

        public BlockFamilyProvider(Block parentBlock, BlockStateModelGenerator blockModelGenerators) {
            this.parentBlock = parentBlock;
            this.blockModelGenerators = blockModelGenerators;
        }

        public ModModelsProvider.BlockFamilyProvider itemPipe(Block block) {
            BlockStateSupplier blockState = createItemPipe(this.blockModelGenerators, block, this.parentBlock);
            this.blockModelGenerators.blockStateCollector.accept(blockState);
            return this;
        }

        public ModModelsProvider.BlockFamilyProvider fluidPipe(Block block) {
            BlockStateSupplier blockState = createFluidPipe(this.blockModelGenerators, block, this.parentBlock);
            this.blockModelGenerators.blockStateCollector.accept(blockState);
            return this;
        }

        public ModModelsProvider.BlockFamilyProvider itemPump(Block block) {
            BlockStateSupplier blockState = createItemPump(this.blockModelGenerators, block, this.parentBlock);
            this.blockModelGenerators.blockStateCollector.accept(blockState);
            return this;
        }

        public ModModelsProvider.BlockFamilyProvider fluidPump(Block block) {
            BlockStateSupplier blockState = createFluidPump(this.blockModelGenerators, block, this.parentBlock);
            this.blockModelGenerators.blockStateCollector.accept(blockState);
            return this;
        }

        public ModModelsProvider.BlockFamilyProvider fluidTank(Block block) {
            BlockStateSupplier blockState = createFluidTank(this.blockModelGenerators, block, this.parentBlock);
            this.blockModelGenerators.blockStateCollector.accept(blockState);
            return this;
        }
    }
}
