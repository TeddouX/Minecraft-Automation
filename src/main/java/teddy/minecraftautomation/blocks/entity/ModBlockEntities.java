package teddy.minecraftautomation.blocks.entity;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import teddy.minecraftautomation.MinecraftAutomation;
import teddy.minecraftautomation.blocks.ModBlocks;

public class ModBlockEntities {
    public static final BlockEntityType<ItemPipeBlockEntity> ITEM_PIPE_BE =
            Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(MinecraftAutomation.MOD_ID, "item_pipe_be"),
                    FabricBlockEntityTypeBuilder.create((BlockPos pos, BlockState state) -> new ItemPipeBlockEntity(pos, state, 0, 0),
                            ModBlocks.WOODEN_ITEM_PIPE).build());

    public static final BlockEntityType<ItemPumpBlockEntity> ITEM_PUMP_BE =
            Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(MinecraftAutomation.MOD_ID, "item_pump_be"),
                    FabricBlockEntityTypeBuilder.create((BlockPos pos, BlockState state) -> new ItemPumpBlockEntity(pos, state, 0, 0, 0),
                            ModBlocks.WOODEN_ITEM_PUMP).build());

    public static void initialize() {
        MinecraftAutomation.LOGGER.info("Initializing ModBlockEntities");
    }
}
