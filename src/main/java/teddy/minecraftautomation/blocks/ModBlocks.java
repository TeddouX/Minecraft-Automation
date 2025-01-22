package teddy.minecraftautomation.blocks;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
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
    public static final Block WOODEN_ITEM_PIPE = registerItemPipe("wooden_item_pipe", 10, 1, 18, Blocks.OAK_PLANKS);
    public static final Block WOODEN_ITEM_PUMP = registerItemPump("wooden_item_pump", 10, 1, 18, Blocks.OAK_PLANKS);
    public static final Block WOODEN_FLUID_PIPE = registerFluidPipe("wooden_fluid_pipe", 8, 75, 100, 18, Blocks.OAK_PLANKS);
    public static final Block WOODEN_FLUID_PUMP = registerFluidPump("wooden_fluid_pump", 8, 75, 100, 18, Blocks.OAK_PLANKS);
    public static final Block WOODEN_FLUID_TANK = registerFluidTank("wooden_fluid_tank", 2000, Blocks.OAK_PLANKS);

    public static final Block COPPER_ITEM_PIPE = registerItemPipe("copper_item_pipe", 14, 1, 16, Blocks.COPPER_BLOCK);
    public static final Block COPPER_ITEM_PUMP = registerItemPump("copper_item_pump", 14, 1, 16, Blocks.COPPER_BLOCK);
    public static final Block COPPER_FLUID_PIPE = registerFluidPipe("copper_fluid_pipe", 24, 150, 250, 12, Blocks.COPPER_BLOCK);
    public static final Block COPPER_FLUID_PUMP = registerFluidPump("copper_fluid_pump", 24, 150, 250, 12, Blocks.COPPER_BLOCK);
    public static final Block COPPER_FLUID_TANK = registerFluidTank("copper_fluid_tank", 3000, Blocks.COPPER_BLOCK);

    public static final Block IRON_ITEM_PIPE = registerItemPipe("iron_item_pipe", 24, 2, 12, Blocks.IRON_BLOCK);
    public static final Block IRON_ITEM_PUMP = registerItemPump("iron_item_pump", 24, 2, 12, Blocks.IRON_BLOCK);
    public static final Block IRON_FLUID_PIPE = registerFluidPipe("iron_fluid_pipe", 18, 175, 200, 12, Blocks.IRON_BLOCK);
    public static final Block IRON_FLUID_PUMP = registerFluidPump("iron_fluid_pump", 18, 175, 200, 12, Blocks.IRON_BLOCK);
    public static final Block IRON_FLUID_TANK = registerFluidTank("iron_fluid_tank", 4000, Blocks.IRON_BLOCK);

    public static final Block GOLD_ITEM_PIPE = registerItemPipe("gold_item_pipe", 22, 4, 8, Blocks.GOLD_BLOCK);
    public static final Block GOLD_ITEM_PUMP = registerItemPump("gold_item_pump", 22, 4, 8, Blocks.GOLD_BLOCK);
    public static final Block GOLD_FLUID_PIPE = registerFluidPipe("gold_fluid_pipe", 16, 225, 200, 8, Blocks.GOLD_BLOCK);
    public static final Block GOLD_FLUID_PUMP = registerFluidPump("gold_fluid_pump", 16, 225, 200, 8, Blocks.GOLD_BLOCK);
    public static final Block GOLD_FLUID_TANK = registerFluidTank("gold_fluid_tank", 5000, Blocks.GOLD_BLOCK);

    public static final Block DIAMOND_ITEM_PIPE = registerItemPipe("diamond_item_pipe", 30, 6, 6, Blocks.DIAMOND_BLOCK);
    public static final Block DIAMOND_ITEM_PUMP = registerItemPump("diamond_item_pump", 30, 6, 6, Blocks.DIAMOND_BLOCK);
    public static final Block DIAMOND_FLUID_PIPE = registerFluidPipe("diamond_fluid_pipe", 28, 300, 200, 6, Blocks.DIAMOND_BLOCK);
    public static final Block DIAMOND_FLUID_PUMP = registerFluidPump("diamond_fluid_pump", 28, 300, 200, 6, Blocks.DIAMOND_BLOCK);
    public static final Block DIAMOND_FLUID_TANK = registerFluidTank("diamond_fluid_tank", 6000, Blocks.DIAMOND_BLOCK);

    public static final Block NETHERITE_ITEM_PIPE = registerItemPipe("netherite_item_pipe", 40, 10, 2, Blocks.NETHERITE_BLOCK);
    public static final Block NETHERITE_ITEM_PUMP = registerItemPump("netherite_item_pump", 40, 10, 2, Blocks.NETHERITE_BLOCK);
    public static final Block NETHERITE_FLUID_PIPE = registerFluidPipe("netherite_fluid_pipe", 36, 350, 300, 2, Blocks.NETHERITE_BLOCK);
    public static final Block NETHERITE_FLUID_PUMP = registerFluidPump("netherite_fluid_pump", 36, 350, 300, 2, Blocks.NETHERITE_BLOCK);
    public static final Block NETHERITE_FLUID_TANK = registerFluidTank("netherite_fluid_tank", 7000, Blocks.NETHERITE_BLOCK);

    static Block registerItemPipe(String path, int maxPressure, int itemsPerTransfer, int transferCooldown, Block copy) {
        return register(path,
                (BlockBehaviour.Properties properties) -> new ItemPipeBlock(maxPressure, itemsPerTransfer, transferCooldown, properties),
                BlockBehaviour.Properties.ofFullCopy(copy), true);
    }

    static Block registerFluidPipe(String path, int maxPressure, int flowPerTick, int maxFluidCapacityMb, int transferCooldown, Block copy) {
        return register(path,
                (BlockBehaviour.Properties properties) -> new FluidPipeBlock(maxPressure, flowPerTick, maxFluidCapacityMb, transferCooldown, properties),
                BlockBehaviour.Properties.ofFullCopy(copy).noOcclusion().isViewBlocking(Blocks::never), true);
    }

    static Block registerItemPump(String path, int inducedPressure, int itemsPerTransfer, int transferCooldown, Block copy) {
        return register(path,
                (BlockBehaviour.Properties properties) -> new ItemPumpBlock(inducedPressure, itemsPerTransfer, transferCooldown, properties),
                BlockBehaviour.Properties.ofFullCopy(copy), true);
    }

    static Block registerFluidPump(String path, int inducedPressure, int flowPerTick, int maxFluidCapacityMb, int transferCooldown, Block copy) {
        return register(path,
                (BlockBehaviour.Properties properties) -> new FluidPumpBlock(inducedPressure, flowPerTick, maxFluidCapacityMb, transferCooldown, properties),
                BlockBehaviour.Properties.ofFullCopy(copy).noOcclusion().isViewBlocking(Blocks::never), true);
    }

    static Block registerFluidTank(String path, int maxFluidCapacityMb, Block copy) {
        return register(path,
                (BlockBehaviour.Properties properties) -> new FluidTankBlock(maxFluidCapacityMb, properties),
                BlockBehaviour.Properties.ofFullCopy(copy).noOcclusion().isViewBlocking(Blocks::never), true);
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
