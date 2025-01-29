package teddy.minecraftautomation.items;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.text.Text;
import teddy.minecraftautomation.MinecraftAutomation;
import teddy.minecraftautomation.blocks.ModBlocks;

public class ModCreativeTab {
    public static final RegistryKey<ItemGroup> MOD_CREATIVE_TAB_KEY = RegistryKey.of(Registries.ITEM_GROUP.getKey(), MinecraftAutomation.id("creative_tab"));

    public static final ItemGroup MOD_CREATIVE_TAB = FabricItemGroup.builder()
            .icon(() -> new ItemStack(ModBlocks.WOODEN_ITEM_PIPE))
            .displayName(Text.translatable("creative_tab.mod_item_group"))
            .build();

    public static void initialize() {
        MinecraftAutomation.LOGGER.info("Initializing ModItemGroup");

        Registry.register(Registries.ITEM_GROUP, MOD_CREATIVE_TAB_KEY, MOD_CREATIVE_TAB);

        ItemGroupEvents.modifyEntriesEvent(MOD_CREATIVE_TAB_KEY).register(itemGroup -> {
            itemGroup.add(ModBlocks.WOODEN_ITEM_PIPE);
            itemGroup.add(ModBlocks.WOODEN_ITEM_PUMP);
            itemGroup.add(ModBlocks.WOODEN_FLUID_PIPE);
            itemGroup.add(ModBlocks.WOODEN_FLUID_PUMP);
            itemGroup.add(ModBlocks.WOODEN_FLUID_TANK);
            itemGroup.add(ModBlocks.COPPER_ITEM_PIPE);
            itemGroup.add(ModBlocks.COPPER_FLUID_PIPE);
            itemGroup.add(ModBlocks.COPPER_ITEM_PUMP);
            itemGroup.add(ModBlocks.COPPER_FLUID_PUMP);
            itemGroup.add(ModBlocks.COPPER_FLUID_TANK);
            itemGroup.add(ModBlocks.IRON_ITEM_PIPE);
            itemGroup.add(ModBlocks.IRON_FLUID_PIPE);
            itemGroup.add(ModBlocks.IRON_ITEM_PUMP);
            itemGroup.add(ModBlocks.IRON_FLUID_PUMP);
            itemGroup.add(ModBlocks.IRON_FLUID_TANK);
            itemGroup.add(ModBlocks.GOLD_ITEM_PIPE);
            itemGroup.add(ModBlocks.GOLD_FLUID_PIPE);
            itemGroup.add(ModBlocks.GOLD_ITEM_PUMP);
            itemGroup.add(ModBlocks.GOLD_FLUID_PUMP);
            itemGroup.add(ModBlocks.GOLD_FLUID_TANK);
            itemGroup.add(ModBlocks.DIAMOND_ITEM_PIPE);
            itemGroup.add(ModBlocks.DIAMOND_FLUID_PIPE);
            itemGroup.add(ModBlocks.DIAMOND_ITEM_PUMP);
            itemGroup.add(ModBlocks.DIAMOND_FLUID_PUMP);
            itemGroup.add(ModBlocks.DIAMOND_FLUID_TANK);
            itemGroup.add(ModBlocks.NETHERITE_ITEM_PIPE);
            itemGroup.add(ModBlocks.NETHERITE_FLUID_PIPE);
            itemGroup.add(ModBlocks.NETHERITE_ITEM_PUMP);
            itemGroup.add(ModBlocks.NETHERITE_FLUID_PUMP);
            itemGroup.add(ModBlocks.NETHERITE_FLUID_TANK);
            itemGroup.add(ModItems.PIPE_JOINT);
            itemGroup.add(ModItems.WRENCH);
        });
    }
}
