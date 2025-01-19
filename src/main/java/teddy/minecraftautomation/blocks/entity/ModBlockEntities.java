package teddy.minecraftautomation.blocks.entity;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import teddy.minecraftautomation.MinecraftAutomation;
import teddy.minecraftautomation.blocks.ModBlocks;

import java.util.function.BiFunction;

public class ModBlockEntities {
    public static final BlockEntityType<ItemPipeBlockEntity> ITEM_PIPE_BE = registerBlockEntity("item_pipe_be",
            (BlockPos pos, BlockState state) -> new ItemPipeBlockEntity(pos, state, 0, 0, 0), ModBlocks.WOODEN_ITEM_PIPE);
    public static final BlockEntityType<ItemPumpBlockEntity> ITEM_PUMP_BE = registerBlockEntity("item_pump_be",
            (BlockPos pos, BlockState state) -> new ItemPumpBlockEntity(pos, state, 0, 0, 0), ModBlocks.WOODEN_ITEM_PUMP);
    public static final BlockEntityType<FluidPipeBlockEntity> FLUID_PIPE_BE = registerBlockEntityWithFluidStorage("fluid_pipe_be",
            (BlockPos pos, BlockState state) -> new FluidPipeBlockEntity(pos, state, 0, 0, 0, 0),
            ((blockEntity, dir) -> blockEntity.fluidStorage), ModBlocks.WOODEN_FLUID_PIPE);
    public static final BlockEntityType<FluidPumpBlockEntity> FLUID_PUMP_BE = registerBlockEntityWithFluidStorage("fluid_pump_be",
            (BlockPos pos, BlockState state) -> new FluidPumpBlockEntity(pos, state, 0, 0, 0, 0),
            ((blockEntity, dir) -> blockEntity.fluidStorage), ModBlocks.WOODEN_FLUID_PUMP);


    static <T extends BlockEntity> BlockEntityType<T> registerBlockEntity(String path, FabricBlockEntityTypeBuilder.Factory<T> factory, Block... blocks) {
        return Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(MinecraftAutomation.MOD_ID, path),
                FabricBlockEntityTypeBuilder.create(factory, blocks).build());
    }

    static <T extends BlockEntity> BlockEntityType<T> registerBlockEntityWithFluidStorage(String path, FabricBlockEntityTypeBuilder.Factory<T> factory, BiFunction<T, Direction, Storage<FluidVariant>> fluidStorage, Block... blocks) {
        BlockEntityType<T> blockEntity = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(MinecraftAutomation.MOD_ID, path),
                FabricBlockEntityTypeBuilder.create(factory, blocks).build());

        FluidStorage.SIDED.registerForBlockEntity(fluidStorage, blockEntity);

        return blockEntity;
    }


    public static void initialize() {
        initializeFluidStorages();

        MinecraftAutomation.LOGGER.info("Initializing ModBlockEntities");
    }

    static void initializeFluidStorages() {
        MinecraftAutomation.LOGGER.info("Initializing FluidStorages");

        FluidStorage.SIDED.registerForBlockEntity((fluidPipe, direction) -> fluidPipe.fluidStorage, FLUID_PIPE_BE);
        FluidStorage.SIDED.registerForBlockEntity((fluidPump, direction) -> fluidPump.fluidStorage, FLUID_PUMP_BE);
    }
}
