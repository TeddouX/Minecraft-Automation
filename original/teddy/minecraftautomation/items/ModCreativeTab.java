package teddy.minecraftautomation.items;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import teddy.minecraftautomation.MinecraftAutomation;
import teddy.minecraftautomation.blocks.ModBlocks;

public class ModCreativeTab {
    public static final ResourceKey<CreativeModeTab> MOD_CREATIVE_TAB_KEY = ResourceKey.create(
            BuiltInRegistries.CREATIVE_MODE_TAB.key(),
            ResourceLocation.fromNamespaceAndPath(MinecraftAutomation.MOD_ID, "creative_tab"));

    public static final CreativeModeTab MOD_CREATIVE_TAB = FabricItemGroup.builder()
            .icon(() -> new ItemStack(ModBlocks.WOODEN_ITEM_PIPE))
            .title(Component.translatable("creative_tab.mod_item_group"))
            .build();

    public static void initialize() {
        MinecraftAutomation.LOGGER.info("Initializing ModItemGroup");

        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, MOD_CREATIVE_TAB_KEY, MOD_CREATIVE_TAB);

        ItemGroupEvents.modifyEntriesEvent(MOD_CREATIVE_TAB_KEY).register(itemGroup -> {
            itemGroup.accept(ModBlocks.WOODEN_ITEM_PIPE);
            itemGroup.accept(ModBlocks.WOODEN_ITEM_PUMP);
            itemGroup.accept(ModBlocks.WOODEN_FLUID_PIPE);
            itemGroup.accept(ModBlocks.WOODEN_FLUID_PUMP);
            itemGroup.accept(ModBlocks.WOODEN_FLUID_TANK);
            itemGroup.accept(ModBlocks.COPPER_ITEM_PIPE);
            itemGroup.accept(ModBlocks.COPPER_FLUID_PIPE);
            itemGroup.accept(ModBlocks.COPPER_ITEM_PUMP);
            itemGroup.accept(ModBlocks.COPPER_FLUID_PUMP);
            itemGroup.accept(ModBlocks.COPPER_FLUID_TANK);
            itemGroup.accept(ModBlocks.IRON_ITEM_PIPE);
            itemGroup.accept(ModBlocks.IRON_FLUID_PIPE);
            itemGroup.accept(ModBlocks.IRON_ITEM_PUMP);
            itemGroup.accept(ModBlocks.IRON_FLUID_PUMP);
            itemGroup.accept(ModBlocks.IRON_FLUID_TANK);
            itemGroup.accept(ModBlocks.GOLD_ITEM_PIPE);
            itemGroup.accept(ModBlocks.GOLD_FLUID_PIPE);
            itemGroup.accept(ModBlocks.GOLD_ITEM_PUMP);
            itemGroup.accept(ModBlocks.GOLD_FLUID_PUMP);
            itemGroup.accept(ModBlocks.GOLD_FLUID_TANK);
            itemGroup.accept(ModBlocks.DIAMOND_ITEM_PIPE);
            itemGroup.accept(ModBlocks.DIAMOND_FLUID_PIPE);
            itemGroup.accept(ModBlocks.DIAMOND_ITEM_PUMP);
            itemGroup.accept(ModBlocks.DIAMOND_FLUID_PUMP);
            itemGroup.accept(ModBlocks.DIAMOND_FLUID_TANK);
            itemGroup.accept(ModBlocks.NETHERITE_ITEM_PIPE);
            itemGroup.accept(ModBlocks.NETHERITE_FLUID_PIPE);
            itemGroup.accept(ModBlocks.NETHERITE_ITEM_PUMP);
            itemGroup.accept(ModBlocks.NETHERITE_FLUID_PUMP);
            itemGroup.accept(ModBlocks.NETHERITE_FLUID_TANK);
            itemGroup.accept(ModItems.PIPE_JOINT);
            itemGroup.accept(ModItems.WRENCH);
        });
    }
}
