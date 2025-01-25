package teddy.minecraftautomation.blocks;

import teddy.minecraftautomation.MinecraftAutomation;

import java.util.function.Function;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class ModBlocks {
    public static final Block WOODEN_ITEM_PIPE = registerItemPipe("wooden_item_pipe", 10, 1, 18,
            AbstractBlock.Settings.copy(Blocks.OAK_PLANKS).strength(1f, 0.5f));
    public static final Block WOODEN_ITEM_PUMP = registerItemPump("wooden_item_pump", 10, 1, 18,
            AbstractBlock.Settings.copy(Blocks.OAK_PLANKS).strength(1f, 0.5f));
    public static final Block WOODEN_FLUID_PIPE = registerFluidPipe("wooden_fluid_pipe", 8, 75, 100, 18,
            AbstractBlock.Settings.copy(Blocks.OAK_PLANKS).strength(1f, 0.5f));
    public static final Block WOODEN_FLUID_PUMP = registerFluidPump("wooden_fluid_pump", 8, 75, 100, 18,
            AbstractBlock.Settings.copy(Blocks.OAK_PLANKS).strength(1f, 0.5f));
    public static final Block WOODEN_FLUID_TANK = registerFluidTank("wooden_fluid_tank", 2000,
            AbstractBlock.Settings.copy(Blocks.OAK_PLANKS).strength(1.5f, 1f));
    public static final Block COPPER_ITEM_PIPE = registerItemPipe("copper_item_pipe", 14, 1, 16,
            AbstractBlock.Settings.copy(Blocks.COPPER_BLOCK).strength(1f, 0.5f));
    public static final Block COPPER_ITEM_PUMP = registerItemPump("copper_item_pump", 14, 1, 16,
            AbstractBlock.Settings.copy(Blocks.COPPER_BLOCK).strength(1f, 0.5f));
    public static final Block COPPER_FLUID_PIPE = registerFluidPipe("copper_fluid_pipe", 24, 150, 250, 12,
            AbstractBlock.Settings.copy(Blocks.COPPER_BLOCK).strength(1f, 0.5f));
    public static final Block COPPER_FLUID_PUMP = registerFluidPump("copper_fluid_pump", 24, 150, 250, 12,
            AbstractBlock.Settings.copy(Blocks.COPPER_BLOCK).strength(1f, 0.5f));
    public static final Block COPPER_FLUID_TANK = registerFluidTank("copper_fluid_tank", 3000,
            AbstractBlock.Settings.copy(Blocks.COPPER_BLOCK).strength(1.5f, 1f));
    public static final Block IRON_ITEM_PIPE = registerItemPipe("iron_item_pipe", 24, 2, 12,
            AbstractBlock.Settings.copy(Blocks.IRON_BLOCK).strength(1f, 0.5f));
    public static final Block IRON_ITEM_PUMP = registerItemPump("iron_item_pump", 24, 2, 12,
            AbstractBlock.Settings.copy(Blocks.IRON_BLOCK).strength(1f, 0.5f));
    public static final Block IRON_FLUID_PIPE = registerFluidPipe("iron_fluid_pipe", 18, 175, 200, 12,
            AbstractBlock.Settings.copy(Blocks.IRON_BLOCK).strength(1f, 0.5f));
    public static final Block IRON_FLUID_PUMP = registerFluidPump("iron_fluid_pump", 18, 175, 200, 12,
            AbstractBlock.Settings.copy(Blocks.IRON_BLOCK).strength(1f, 0.5f));
    public static final Block IRON_FLUID_TANK = registerFluidTank("iron_fluid_tank", 4000,
            AbstractBlock.Settings.copy(Blocks.IRON_BLOCK).strength(1.5f, 1f));
    public static final Block GOLD_ITEM_PIPE = registerItemPipe("gold_item_pipe", 22, 4, 8,
            AbstractBlock.Settings.copy(Blocks.GOLD_BLOCK).strength(1f, 0.5f));
    public static final Block GOLD_ITEM_PUMP = registerItemPump("gold_item_pump", 22, 4, 8,
            AbstractBlock.Settings.copy(Blocks.GOLD_BLOCK).strength(1f, 0.5f));
    public static final Block GOLD_FLUID_PIPE = registerFluidPipe("gold_fluid_pipe", 16, 225, 200, 8,
            AbstractBlock.Settings.copy(Blocks.GOLD_BLOCK).strength(1f, 0.5f));
    public static final Block GOLD_FLUID_PUMP = registerFluidPump("gold_fluid_pump", 16, 225, 200, 8,
            AbstractBlock.Settings.copy(Blocks.GOLD_BLOCK).strength(1f, 0.5f));
    public static final Block GOLD_FLUID_TANK = registerFluidTank("gold_fluid_tank", 5000,
            AbstractBlock.Settings.copy(Blocks.GOLD_BLOCK).strength(1.5f, 1f));
    public static final Block DIAMOND_ITEM_PIPE = registerItemPipe("diamond_item_pipe", 30, 6, 6,
            AbstractBlock.Settings.copy(Blocks.DIAMOND_BLOCK).strength(1f, 0.5f));
    public static final Block DIAMOND_ITEM_PUMP = registerItemPump("diamond_item_pump", 30, 6, 6,
            AbstractBlock.Settings.copy(Blocks.DIAMOND_BLOCK).strength(1f, 0.5f));
    public static final Block DIAMOND_FLUID_PIPE = registerFluidPipe("diamond_fluid_pipe", 28, 300, 200, 6,
            AbstractBlock.Settings.copy(Blocks.DIAMOND_BLOCK).strength(1f, 0.5f));
    public static final Block DIAMOND_FLUID_PUMP = registerFluidPump("diamond_fluid_pump", 28, 300, 200, 6,
            AbstractBlock.Settings.copy(Blocks.DIAMOND_BLOCK).strength(1f, 0.5f));
    public static final Block DIAMOND_FLUID_TANK = registerFluidTank("diamond_fluid_tank", 6000,
            AbstractBlock.Settings.copy(Blocks.DIAMOND_BLOCK).strength(1.5f, 1f));
    public static final Block NETHERITE_ITEM_PIPE = registerItemPipe("netherite_item_pipe", 40, 10, 2,
            AbstractBlock.Settings.copy(Blocks.NETHERITE_BLOCK).strength(1f, 0.5f));
    public static final Block NETHERITE_ITEM_PUMP = registerItemPump("netherite_item_pump", 40, 10, 2,
            AbstractBlock.Settings.copy(Blocks.NETHERITE_BLOCK).strength(1f, 0.5f));
    public static final Block NETHERITE_FLUID_PIPE = registerFluidPipe("netherite_fluid_pipe", 36, 350, 300, 2,
            AbstractBlock.Settings.copy(Blocks.NETHERITE_BLOCK).strength(1f, 0.5f));
    public static final Block NETHERITE_FLUID_PUMP = registerFluidPump("netherite_fluid_pump", 36, 350, 300, 2,
            AbstractBlock.Settings.copy(Blocks.NETHERITE_BLOCK).strength(1f, 0.5f));
    public static final Block NETHERITE_FLUID_TANK = registerFluidTank("netherite_fluid_tank", 7000,
            AbstractBlock.Settings.copy(Blocks.NETHERITE_BLOCK).strength(1.5f, 1f));

    static Block registerItemPipe(String path, int maxPressure, int itemsPerTransfer, int transferCooldown, AbstractBlock.Settings properties) {
        return register(path,
                (AbstractBlock.Settings props) -> new ItemPipeBlock(maxPressure, itemsPerTransfer, transferCooldown, props),
                properties, true);
    }

    static Block registerFluidPipe(String path, int maxPressure, int flowPerTick, int maxFluidCapacityMb, int transferCooldown, AbstractBlock.Settings properties) {
        return register(path,
                (AbstractBlock.Settings props) -> new FluidPipeBlock(maxPressure, flowPerTick, maxFluidCapacityMb, transferCooldown, props),
                properties.nonOpaque().blockVision(Blocks::never), true);
    }

    static Block registerItemPump(String path, int inducedPressure, int itemsPerTransfer, int transferCooldown, AbstractBlock.Settings properties) {
        return register(path,
                (AbstractBlock.Settings props) -> new ItemPumpBlock(inducedPressure, itemsPerTransfer, transferCooldown, props),
                properties, true);
    }

    static Block registerFluidPump(String path, int inducedPressure, int flowPerTick, int maxFluidCapacityMb, int transferCooldown, AbstractBlock.Settings properties) {
        return register(path,
                (AbstractBlock.Settings props) -> new FluidPumpBlock(inducedPressure, flowPerTick, maxFluidCapacityMb, transferCooldown, props),
                properties.nonOpaque().blockVision(Blocks::never), true);
    }

    static Block registerFluidTank(String path, int maxFluidCapacityMb, AbstractBlock.Settings properties) {
        return register(path,
                (AbstractBlock.Settings props) -> new FluidTankBlock(maxFluidCapacityMb, props),
                properties.nonOpaque().blockVision(Blocks::never), true);
    }

    static Block register(String name, Function<AbstractBlock.Settings, Block> factory, AbstractBlock.Settings properties, boolean registerBlockItem) {
        Identifier id = Identifier.of(MinecraftAutomation.MOD_ID, name);
        RegistryKey<Block> blockResourceKey = RegistryKey.of(RegistryKeys.BLOCK, id);
        Block block = factory.apply(properties.registryKey(blockResourceKey));

        if (registerBlockItem) {
            RegistryKey<Item> itemResourceKey = RegistryKey.of(RegistryKeys.ITEM, id);
            Item blockItem = new BlockItem(block, new Item.Settings().maxCount(64).registryKey(itemResourceKey));

            Registry.register(Registries.ITEM, id, blockItem);
        }

        return Registry.register(Registries.BLOCK, id, block);
    }

    public static void initialize() {
        MinecraftAutomation.LOGGER.info("Initializing ModBlocks");
    }
}
