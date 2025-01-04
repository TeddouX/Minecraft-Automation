package teddy.minecraftautomation.blocks.entity;

import teddy.minecraftautomation.MinecraftAutomation;

public class ModBlockEntities {
    /*public static final BlockEntityType<FluidPipeBlockEntity> FLUID_PIPE_BLOCK_ENTITY_BLOCK_ENTITY =
            Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of(MinecraftAutomation.MOD_ID, "fluid_pipe_be"),
                    FabricBlockEntityTypeBuilder.create((BlockPos pos, BlockState state) -> new FluidPipeBlockEntity(pos, state, 0, 0),
                            ModBlocks.WOODEN_FLUID_PIPE).build());*/

    public static void initialize() {
        MinecraftAutomation.LOGGER.info("Initializing ModBlockEntities");
    }
}
