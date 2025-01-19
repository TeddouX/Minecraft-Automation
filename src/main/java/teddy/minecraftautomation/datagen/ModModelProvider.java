package teddy.minecraftautomation.datagen;

import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.blockstates.*;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import teddy.minecraftautomation.blocks.AbstractPumpBlock;
import teddy.minecraftautomation.blocks.ModBlocks;

import java.util.function.Consumer;

public class ModModelProvider extends FabricModelProvider {
    public ModModelProvider(FabricDataOutput output) {
        super(output);
    }

    public void createPipeBlockState(Consumer<BlockStateGenerator> blockStateOutput, Block block, ResourceLocation middle, ResourceLocation half) {
        blockStateOutput.accept(MultiPartGenerator.multiPart(block)
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
                ));
    }

    public void createPumpBlockState(Consumer<BlockStateGenerator> blockStateOutput, Block block, ResourceLocation pumpModel) {
        blockStateOutput.accept(MultiVariantGenerator.multiVariant(block)
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
                ));
    }

    public void createPipe(String pipeType, BlockModelGenerators blockModelGenerators, Block block, Block parentBlock)  {
        // Create models that inherit from the parent models
        ResourceLocation itemPipeMiddle = ParentModel.create( pipeType + "_pipe_middle", "_middle", TextureSlot.TEXTURE, block, parentBlock, blockModelGenerators.modelOutput);
        ResourceLocation itemPipeHalf = ParentModel.create(pipeType + "_pipe_half", "_half", TextureSlot.TEXTURE, block, parentBlock, blockModelGenerators.modelOutput);
        ResourceLocation itemPipeStraight = ParentModel.create(pipeType + "_pipe_inventory", "_inventory", TextureSlot.TEXTURE, block, parentBlock, blockModelGenerators.modelOutput);

        // Create the item pipe block state
        createPipeBlockState(blockModelGenerators.blockStateOutput, block, itemPipeMiddle, itemPipeHalf);

        // Create the block's item model
        blockModelGenerators.registerSimpleItemModel(block, itemPipeStraight);
    }

    public void createItemPipe(BlockModelGenerators blockModelGenerators, Block block, Block parentBlock) {
        createPipe("item", blockModelGenerators, block, parentBlock);
    }

    public void createFluidPipe(BlockModelGenerators blockModelGenerators, Block block, Block parentBlock) {
        createPipe("fluid", blockModelGenerators, block, parentBlock);
    }

    public void createPump(String pumpType, BlockModelGenerators blockModelGenerators, Block block, Block parentBlock) {
        ResourceLocation fluidPump = ParentModel.create(pumpType + "_pump", "", TextureSlot.TEXTURE, block, parentBlock, blockModelGenerators.modelOutput);

        createPumpBlockState(blockModelGenerators.blockStateOutput, block, fluidPump);

        blockModelGenerators.registerSimpleItemModel(block, fluidPump);
    }

    public void createItemPump(BlockModelGenerators blockModelGenerators, Block block, Block parentBlock) {
        createPump("item", blockModelGenerators, block, parentBlock);
    }

    public void createFluidPump(BlockModelGenerators blockModelGenerators, Block block, Block parentBlock) {
        createPump("fluid", blockModelGenerators, block, parentBlock);
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators blockModelGenerators) {
        createItemPipe(blockModelGenerators, ModBlocks.WOODEN_ITEM_PIPE, Blocks.OAK_PLANKS);

        createFluidPipe(blockModelGenerators, ModBlocks.WOODEN_FLUID_PIPE, Blocks.OAK_PLANKS);

        createItemPump(blockModelGenerators, ModBlocks.WOODEN_ITEM_PUMP, Blocks.OAK_PLANKS);

        createFluidPump(blockModelGenerators, ModBlocks.WOODEN_FLUID_PUMP, Blocks.OAK_PLANKS);
    }

    @Override
    public void generateItemModels(ItemModelGenerators itemModelGenerators) {

    }
}
