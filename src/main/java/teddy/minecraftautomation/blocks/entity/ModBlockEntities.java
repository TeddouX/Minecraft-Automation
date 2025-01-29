package teddy.minecraftautomation.blocks.entity;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import teddy.minecraftautomation.MinecraftAutomation;
import teddy.minecraftautomation.blocks.ModBlocks;

import java.util.function.BiFunction;

public class ModBlockEntities {
    public static final BlockEntityType<ItemPipeBlockEntity> ITEM_PIPE_BE = registerBlockEntity("item_pipe_be",
            (BlockPos pos, BlockState state) -> new ItemPipeBlockEntity(pos, state, 0, 0, 0),
            ModBlocks.WOODEN_ITEM_PIPE, ModBlocks.COPPER_ITEM_PIPE, ModBlocks.IRON_ITEM_PIPE, ModBlocks.GOLD_ITEM_PIPE, ModBlocks.DIAMOND_ITEM_PIPE, ModBlocks.NETHERITE_ITEM_PIPE);
    public static final BlockEntityType<ItemPumpBlockEntity> ITEM_PUMP_BE = registerBlockEntity("item_pump_be",
            (BlockPos pos, BlockState state) -> new ItemPumpBlockEntity(pos, state, 0, 0, 0),
            ModBlocks.WOODEN_ITEM_PUMP, ModBlocks.COPPER_ITEM_PUMP, ModBlocks.IRON_ITEM_PUMP, ModBlocks.GOLD_ITEM_PUMP, ModBlocks.DIAMOND_ITEM_PUMP, ModBlocks.NETHERITE_ITEM_PUMP);
    public static final BlockEntityType<FluidPipeBlockEntity> FLUID_PIPE_BE = registerBlockEntityWithFluidStorage("fluid_pipe_be",
            (BlockPos pos, BlockState state) -> new FluidPipeBlockEntity(pos, state, 0, 0, 0, 0),
            ((blockEntity, dir) -> blockEntity.fluidStorage),
            ModBlocks.WOODEN_FLUID_PIPE, ModBlocks.COPPER_FLUID_PIPE, ModBlocks.IRON_FLUID_PIPE, ModBlocks.GOLD_FLUID_PIPE, ModBlocks.DIAMOND_FLUID_PIPE, ModBlocks.NETHERITE_FLUID_PIPE);
    public static final BlockEntityType<FluidPumpBlockEntity> FLUID_PUMP_BE = registerBlockEntityWithFluidStorage("fluid_pump_be",
            (BlockPos pos, BlockState state) -> new FluidPumpBlockEntity(pos, state, 0, 0, 0, 0),
            ((blockEntity, dir) -> blockEntity.fluidStorage),
            ModBlocks.WOODEN_FLUID_PUMP, ModBlocks.COPPER_FLUID_PUMP, ModBlocks.IRON_FLUID_PUMP, ModBlocks.GOLD_FLUID_PUMP, ModBlocks.DIAMOND_FLUID_PUMP, ModBlocks.NETHERITE_FLUID_PUMP);
    public static final BlockEntityType<FluidTankBlockEntity> FLUID_TANK_BE = registerBlockEntityWithFluidStorage("fluid_tank_be",
            (BlockPos pos, BlockState state) -> new FluidTankBlockEntity(pos, state, 0),
            ((blockEntity, dir) -> blockEntity.fluidStorage),
            ModBlocks.WOODEN_FLUID_TANK, ModBlocks.COPPER_FLUID_TANK, ModBlocks.IRON_FLUID_TANK, ModBlocks.GOLD_FLUID_TANK, ModBlocks.DIAMOND_FLUID_TANK, ModBlocks.NETHERITE_FLUID_TANK);
    public static final BlockEntityType<CrusherBlockEntity> CRUSHER_BE = registerBlockEntityWithFluidStorage("crusher_be", CrusherBlockEntity::new,
            (blockEntity, dir) -> blockEntity.fluidStorage, ModBlocks.CRUSHER);

    static <T extends BlockEntity> BlockEntityType<T> registerBlockEntity(String path, FabricBlockEntityTypeBuilder.Factory<T> factory, Block... blocks) {
        return Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of(MinecraftAutomation.MOD_ID, path),
                FabricBlockEntityTypeBuilder.create(factory, blocks).build());
    }

    static <T extends BlockEntity> BlockEntityType<T> registerBlockEntityWithFluidStorage(String path, FabricBlockEntityTypeBuilder.Factory<T> factory, BiFunction<T, Direction, Storage<FluidVariant>> fluidStorage, Block... blocks) {
        BlockEntityType<T> blockEntity = Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of(MinecraftAutomation.MOD_ID, path),
                FabricBlockEntityTypeBuilder.create(factory, blocks).build());

        FluidStorage.SIDED.registerForBlockEntity(fluidStorage, blockEntity);

        return blockEntity;
    }

    public static void initialize() {
        MinecraftAutomation.LOGGER.info("Initializing ModBlockEntities");
    }
}
