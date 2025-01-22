package teddy.minecraftautomation.datagen;

import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.blockstates.*;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import teddy.minecraftautomation.blocks.AbstractPumpBlock;
import teddy.minecraftautomation.blocks.ModBlocks;
import teddy.minecraftautomation.items.ModItems;

public class ModModelProvider extends FabricModelProvider {
    public ModModelProvider(FabricDataOutput output) {
        super(output);
    }

    BlockStateGenerator createPipeBlockState(Block block, ResourceLocation middle, ResourceLocation half) {
        return MultiPartGenerator.multiPart(block)
                .with(Variant.variant().with(VariantProperties.MODEL, middle))
                .with(
                        Condition.condition().term(BlockStateProperties.NORTH, true),
                        Variant.variant().with(VariantProperties.MODEL, half)
                                .with(VariantProperties.UV_LOCK, false)
                ).with(
                        Condition.condition().term(BlockStateProperties.EAST, true),
                        Variant.variant().with(VariantProperties.MODEL, half)
                                .with(VariantProperties.UV_LOCK, false)
                                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)
                ).with(
                        Condition.condition().term(BlockStateProperties.SOUTH, true),
                        Variant.variant().with(VariantProperties.MODEL, half)
                                .with(VariantProperties.UV_LOCK, false)
                                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180)
                ).with(
                        Condition.condition().term(BlockStateProperties.WEST, true),
                        Variant.variant().with(VariantProperties.MODEL, half)
                                .with(VariantProperties.UV_LOCK, false)
                                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270)
                ).with(
                        Condition.condition().term(BlockStateProperties.UP, true),
                        Variant.variant().with(VariantProperties.MODEL, half)
                                .with(VariantProperties.UV_LOCK, false)
                                .with(VariantProperties.X_ROT, VariantProperties.Rotation.R270)
                ).with(
                        Condition.condition().term(BlockStateProperties.DOWN, true),
                        Variant.variant().with(VariantProperties.MODEL, half)
                                .with(VariantProperties.UV_LOCK, false)
                                .with(VariantProperties.X_ROT, VariantProperties.Rotation.R90)
                );
    }

    BlockStateGenerator createPumpBlockState(Block block, ResourceLocation pumpModel) {
        return MultiVariantGenerator.multiVariant(block)
                .with(
                        PropertyDispatch.properties(AbstractPumpBlock.AXIS, AbstractPumpBlock.NEGATIVE_AXIS)
                                .select(Direction.Axis.Z, false, Variant.variant().with(VariantProperties.MODEL, pumpModel))
                                .select(Direction.Axis.Z, true, Variant.variant().with(VariantProperties.MODEL, pumpModel)
                                        .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
                                .select(Direction.Axis.X, false, Variant.variant().with(VariantProperties.MODEL, pumpModel)
                                        .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
                                .select(Direction.Axis.X, true, Variant.variant().with(VariantProperties.MODEL, pumpModel)
                                        .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))
                                .select(Direction.Axis.Y, false, Variant.variant().with(VariantProperties.MODEL, pumpModel)
                                        .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)
                                        .with(VariantProperties.X_ROT, VariantProperties.Rotation.R90))
                                .select(Direction.Axis.Y, true, Variant.variant().with(VariantProperties.MODEL, pumpModel)
                                        .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)
                                        .with(VariantProperties.X_ROT, VariantProperties.Rotation.R270))
                );
    }

    BlockStateGenerator createPump(String pumpType, BlockModelGenerators blockModelGenerators, Block block, Block parentBlock, boolean cutout) {
        ResourceLocation pump = ParentModel.create(pumpType + "_pump", "", TextureSlot.TEXTURE, block, parentBlock, blockModelGenerators.modelOutput);

        BlockStateGenerator blockState = createPumpBlockState(block, pump);

        blockModelGenerators.registerSimpleItemModel(block, pump);

        return blockState;
    }

    BlockStateGenerator createItemPump(BlockModelGenerators blockModelGenerators, Block block, Block parentBlock) {
        return createPump("item", blockModelGenerators, block, parentBlock, false);
    }

    BlockStateGenerator createFluidPump(BlockModelGenerators blockModelGenerators, Block block, Block parentBlock) {
        return createPump("fluid", blockModelGenerators, block, parentBlock, true);
    }

    BlockStateGenerator createFluidTank(BlockModelGenerators blockModelGenerators, Block block, Block parentBlock) {
        ResourceLocation resourceLocation = ParentModel.create("fluid_tank", "", TextureSlot.TEXTURE, block, parentBlock, blockModelGenerators.modelOutput);

        BlockStateGenerator blockState = MultiVariantGenerator.multiVariant(block, Variant.variant()
                .with(VariantProperties.MODEL, resourceLocation)
                .with(VariantProperties.UV_LOCK, false));

        blockModelGenerators.registerSimpleItemModel(block, resourceLocation);

        return blockState;
    }

    BlockStateGenerator createPipe(String pipeType, BlockModelGenerators blockModelGenerators, Block block, Block parentBlock, boolean cutout)  {
        // Create models that inherit from the parent models
        ResourceLocation pipeMiddle = ParentModel.create( pipeType + "_pipe_middle", "_middle", TextureSlot.TEXTURE, block, parentBlock, blockModelGenerators.modelOutput);
        ResourceLocation pipeHalf = ParentModel.create(pipeType + "_pipe_half", "_half", TextureSlot.TEXTURE, block, parentBlock, blockModelGenerators.modelOutput);
        ResourceLocation pipeInventory = ParentModel.create(pipeType + "_pipe_inventory", "_inventory", TextureSlot.TEXTURE, block, parentBlock, blockModelGenerators.modelOutput);

        // Create the item pipe block state
        BlockStateGenerator blockState = createPipeBlockState(block, pipeMiddle, pipeHalf);

        // Create the block's item model
        blockModelGenerators.registerSimpleItemModel(block, pipeInventory);

        return blockState;
    }

    BlockStateGenerator createItemPipe(BlockModelGenerators blockModelGenerators, Block block, Block parentBlock) {
        return createPipe("item", blockModelGenerators, block, parentBlock, false);
    }

    BlockStateGenerator createFluidPipe(BlockModelGenerators blockModelGenerators, Block block, Block parentBlock) {
        return createPipe("fluid", blockModelGenerators, block, parentBlock, true);
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators blockModelGenerators) {
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
    public void generateItemModels(ItemModelGenerators itemModelGenerators) {
        itemModelGenerators.declareCustomModelItem(ModItems.PIPE_JOINT);

        itemModelGenerators.generateFlatItem(ModItems.WRENCH, ModelTemplates.FLAT_ITEM);
    }


    public class BlockFamilyProvider {
        private final Block parentBlock;
        private final BlockModelGenerators blockModelGenerators;

        public BlockFamilyProvider(Block parentBlock, BlockModelGenerators blockModelGenerators) {
            this.parentBlock = parentBlock;
            this.blockModelGenerators = blockModelGenerators;
        }

        public ModModelProvider.BlockFamilyProvider itemPipe(Block block) {
            BlockStateGenerator blockState = createItemPipe(this.blockModelGenerators, block, this.parentBlock);
            this.blockModelGenerators.blockStateOutput.accept(blockState);
            return this;
        }

        public ModModelProvider.BlockFamilyProvider fluidPipe(Block block) {
            BlockStateGenerator blockState = createFluidPipe(this.blockModelGenerators, block, this.parentBlock);
            this.blockModelGenerators.blockStateOutput.accept(blockState);
            return this;
        }

        public ModModelProvider.BlockFamilyProvider itemPump(Block block) {
            BlockStateGenerator blockState = createItemPump(this.blockModelGenerators, block, this.parentBlock);
            this.blockModelGenerators.blockStateOutput.accept(blockState);
            return this;
        }

        public ModModelProvider.BlockFamilyProvider fluidPump(Block block) {
            BlockStateGenerator blockState = createFluidPump(this.blockModelGenerators, block, this.parentBlock);
            this.blockModelGenerators.blockStateOutput.accept(blockState);
            return this;
        }

        public ModModelProvider.BlockFamilyProvider fluidTank(Block block) {
            BlockStateGenerator blockState = createFluidTank(this.blockModelGenerators, block, this.parentBlock);
            this.blockModelGenerators.blockStateOutput.accept(blockState);
            return this;
        }
    }
}
