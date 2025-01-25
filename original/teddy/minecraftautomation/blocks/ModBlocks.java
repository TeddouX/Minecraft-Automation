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
    public static final Block WOODEN_ITEM_PIPE = registerItemPipe("wooden_item_pipe", 10, 1, 18,
            BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS).strength(1f, 0.5f));
    public static final Block WOODEN_ITEM_PUMP = registerItemPump("wooden_item_pump", 10, 1, 18,
            BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS).strength(1f, 0.5f));
    public static final Block WOODEN_FLUID_PIPE = registerFluidPipe("wooden_fluid_pipe", 8, 75, 100, 18,
            BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS).strength(1f, 0.5f));
    public static final Block WOODEN_FLUID_PUMP = registerFluidPump("wooden_fluid_pump", 8, 75, 100, 18,
            BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS).strength(1f, 0.5f));
    public static final Block WOODEN_FLUID_TANK = registerFluidTank("wooden_fluid_tank", 2000,
            BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS).strength(1.5f, 1f));
    public static final Block COPPER_ITEM_PIPE = registerItemPipe("copper_item_pipe", 14, 1, 16,
            BlockBehaviour.Properties.ofFullCopy(Blocks.COPPER_BLOCK).strength(1f, 0.5f));
    public static final Block COPPER_ITEM_PUMP = registerItemPump("copper_item_pump", 14, 1, 16,
            BlockBehaviour.Properties.ofFullCopy(Blocks.COPPER_BLOCK).strength(1f, 0.5f));
    public static final Block COPPER_FLUID_PIPE = registerFluidPipe("copper_fluid_pipe", 24, 150, 250, 12,
            BlockBehaviour.Properties.ofFullCopy(Blocks.COPPER_BLOCK).strength(1f, 0.5f));
    public static final Block COPPER_FLUID_PUMP = registerFluidPump("copper_fluid_pump", 24, 150, 250, 12,
            BlockBehaviour.Properties.ofFullCopy(Blocks.COPPER_BLOCK).strength(1f, 0.5f));
    public static final Block COPPER_FLUID_TANK = registerFluidTank("copper_fluid_tank", 3000,
            BlockBehaviour.Properties.ofFullCopy(Blocks.COPPER_BLOCK).strength(1.5f, 1f));
    public static final Block IRON_ITEM_PIPE = registerItemPipe("iron_item_pipe", 24, 2, 12,
            BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).strength(1f, 0.5f));
    public static final Block IRON_ITEM_PUMP = registerItemPump("iron_item_pump", 24, 2, 12,
            BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).strength(1f, 0.5f));
    public static final Block IRON_FLUID_PIPE = registerFluidPipe("iron_fluid_pipe", 18, 175, 200, 12,
            BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).strength(1f, 0.5f));
    public static final Block IRON_FLUID_PUMP = registerFluidPump("iron_fluid_pump", 18, 175, 200, 12,
            BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).strength(1f, 0.5f));
    public static final Block IRON_FLUID_TANK = registerFluidTank("iron_fluid_tank", 4000,
            BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).strength(1.5f, 1f));
    public static final Block GOLD_ITEM_PIPE = registerItemPipe("gold_item_pipe", 22, 4, 8,
            BlockBehaviour.Properties.ofFullCopy(Blocks.GOLD_BLOCK).strength(1f, 0.5f));
    public static final Block GOLD_ITEM_PUMP = registerItemPump("gold_item_pump", 22, 4, 8,
            BlockBehaviour.Properties.ofFullCopy(Blocks.GOLD_BLOCK).strength(1f, 0.5f));
    public static final Block GOLD_FLUID_PIPE = registerFluidPipe("gold_fluid_pipe", 16, 225, 200, 8,
            BlockBehaviour.Properties.ofFullCopy(Blocks.GOLD_BLOCK).strength(1f, 0.5f));
    public static final Block GOLD_FLUID_PUMP = registerFluidPump("gold_fluid_pump", 16, 225, 200, 8,
            BlockBehaviour.Properties.ofFullCopy(Blocks.GOLD_BLOCK).strength(1f, 0.5f));
    public static final Block GOLD_FLUID_TANK = registerFluidTank("gold_fluid_tank", 5000,
            BlockBehaviour.Properties.ofFullCopy(Blocks.GOLD_BLOCK).strength(1.5f, 1f));
    public static final Block DIAMOND_ITEM_PIPE = registerItemPipe("diamond_item_pipe", 30, 6, 6,
            BlockBehaviour.Properties.ofFullCopy(Blocks.DIAMOND_BLOCK).strength(1f, 0.5f));
    public static final Block DIAMOND_ITEM_PUMP = registerItemPump("diamond_item_pump", 30, 6, 6,
            BlockBehaviour.Properties.ofFullCopy(Blocks.DIAMOND_BLOCK).strength(1f, 0.5f));
    public static final Block DIAMOND_FLUID_PIPE = registerFluidPipe("diamond_fluid_pipe", 28, 300, 200, 6,
            BlockBehaviour.Properties.ofFullCopy(Blocks.DIAMOND_BLOCK).strength(1f, 0.5f));
    public static final Block DIAMOND_FLUID_PUMP = registerFluidPump("diamond_fluid_pump", 28, 300, 200, 6,
            BlockBehaviour.Properties.ofFullCopy(Blocks.DIAMOND_BLOCK).strength(1f, 0.5f));
    public static final Block DIAMOND_FLUID_TANK = registerFluidTank("diamond_fluid_tank", 6000,
            BlockBehaviour.Properties.ofFullCopy(Blocks.DIAMOND_BLOCK).strength(1.5f, 1f));
    public static final Block NETHERITE_ITEM_PIPE = registerItemPipe("netherite_item_pipe", 40, 10, 2,
            BlockBehaviour.Properties.ofFullCopy(Blocks.NETHERITE_BLOCK).strength(1f, 0.5f));
    public static final Block NETHERITE_ITEM_PUMP = registerItemPump("netherite_item_pump", 40, 10, 2,
            BlockBehaviour.Properties.ofFullCopy(Blocks.NETHERITE_BLOCK).strength(1f, 0.5f));
    public static final Block NETHERITE_FLUID_PIPE = registerFluidPipe("netherite_fluid_pipe", 36, 350, 300, 2,
            BlockBehaviour.Properties.ofFullCopy(Blocks.NETHERITE_BLOCK).strength(1f, 0.5f));
    public static final Block NETHERITE_FLUID_PUMP = registerFluidPump("netherite_fluid_pump", 36, 350, 300, 2,
            BlockBehaviour.Properties.ofFullCopy(Blocks.NETHERITE_BLOCK).strength(1f, 0.5f));
    public static final Block NETHERITE_FLUID_TANK = registerFluidTank("netherite_fluid_tank", 7000,
            BlockBehaviour.Properties.ofFullCopy(Blocks.NETHERITE_BLOCK).strength(1.5f, 1f));

    static Block registerItemPipe(String path, int maxPressure, int itemsPerTransfer, int transferCooldown, BlockBehaviour.Properties properties) {
        return register(path,
                (BlockBehaviour.Properties props) -> new ItemPipeBlock(maxPressure, itemsPerTransfer, transferCooldown, props),
                properties, true);
    }

    static Block registerFluidPipe(String path, int maxPressure, int flowPerTick, int maxFluidCapacityMb, int transferCooldown, BlockBehaviour.Properties properties) {
        return register(path,
                (BlockBehaviour.Properties props) -> new FluidPipeBlock(maxPressure, flowPerTick, maxFluidCapacityMb, transferCooldown, props),
                properties.noOcclusion().isViewBlocking(Blocks::never), true);
    }

    static Block registerItemPump(String path, int inducedPressure, int itemsPerTransfer, int transferCooldown, BlockBehaviour.Properties properties) {
        return register(path,
                (BlockBehaviour.Properties props) -> new ItemPumpBlock(inducedPressure, itemsPerTransfer, transferCooldown, props),
                properties, true);
    }

    static Block registerFluidPump(String path, int inducedPressure, int flowPerTick, int maxFluidCapacityMb, int transferCooldown, BlockBehaviour.Properties properties) {
        return register(path,
                (BlockBehaviour.Properties props) -> new FluidPumpBlock(inducedPressure, flowPerTick, maxFluidCapacityMb, transferCooldown, props),
                properties.noOcclusion().isViewBlocking(Blocks::never), true);
    }

    static Block registerFluidTank(String path, int maxFluidCapacityMb, BlockBehaviour.Properties properties) {
        return register(path,
                (BlockBehaviour.Properties props) -> new FluidTankBlock(maxFluidCapacityMb, props),
                properties.noOcclusion().isViewBlocking(Blocks::never), true);
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
