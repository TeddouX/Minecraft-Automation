package teddy.minecraftautomation.blocks;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import teddy.minecraftautomation.MinecraftAutomation;

import java.util.function.Function;

public class ModBlocks {
    public static final Block WOODEN_ITEM_PIPE = registerItemPipe("wooden_item_pipe", 16, 1, 7, Blocks.OAK_PLANKS);
    public static final Block WOODEN_ITEM_PUMP = register("wooden_item_pump",
            (BlockBehaviour.Properties properties) -> new ItemPumpBlock(16, 1, 7, properties),
            BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_WOOD), true);
    public static final Block WOODEN_FLUID_PIPE = register("wooden_fluid_pipe",
            (BlockBehaviour.Properties properties) -> new FluidPipeBlock(16, 100, 1000, 7, properties),
            BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_WOOD).noOcclusion().isViewBlocking(Blocks::never), true);
    public static final Block WOODEN_FLUID_PUMP = register("wooden_fluid_pump",
            (BlockBehaviour.Properties properties) -> new FluidPumpBlock(16, 100, 1000, 7, properties),
            BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_WOOD), true);


    static Block registerItemPipe(String path, int maxPressure, int itemsPerTransfer, int transferCooldown, Block blockCopy) {
        return register(path,
                (BlockBehaviour.Properties properties) -> new ItemPipeBlock(maxPressure, itemsPerTransfer, transferCooldown, properties),
                BlockBehaviour.Properties.ofFullCopy(blockCopy), true);
    }

    static Block register(String name, Function<BlockBehaviour.Properties, Block> factory, BlockBehaviour.Properties properties, boolean registerBlockItem) {
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(MinecraftAutomation.MOD_ID, name);
        ResourceKey<Block> blockResourceKey = ResourceKey.create(Registries.BLOCK, id);
        Block block = factory.apply(properties.setId(blockResourceKey));

        if (registerBlockItem) {
            ResourceKey<Item> itemResourceKey = ResourceKey.create(Registries.ITEM, id);
            Item blockItem = new BlockItem(block, new Item.Properties().stacksTo(64).setId(itemResourceKey));

            Registry.register(BuiltInRegistries.ITEM, id, blockItem);
        }

        return Registry.register(BuiltInRegistries.BLOCK, id, block);
    }

    public static void initialize() {
        MinecraftAutomation.LOGGER.info("Initializing ModBlocks");
    }
}
