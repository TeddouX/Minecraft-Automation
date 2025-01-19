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
            ResourceLocation.fromNamespaceAndPath(MinecraftAutomation.MOD_ID, "item_group"));

    public static final CreativeModeTab MOD_CREATIVE_TAB = FabricItemGroup.builder()
            .icon(() -> new ItemStack(ModBlocks.WOODEN_ITEM_PIPE))
            .title(Component.translatable("itemGroup.mod_item_group"))
            .build();

    public static void initialize() {
        MinecraftAutomation.LOGGER.info("Initializing ModItemGroup");

        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, MOD_CREATIVE_TAB_KEY, MOD_CREATIVE_TAB);

        ItemGroupEvents.modifyEntriesEvent(MOD_CREATIVE_TAB_KEY).register(itemGroup -> {
            // Item pipes
            itemGroup.accept(ModBlocks.WOODEN_ITEM_PIPE);
            // Item pumps
            itemGroup.accept(ModBlocks.WOODEN_ITEM_PUMP);
            // Fluid pipes
            itemGroup.accept(ModBlocks.WOODEN_FLUID_PIPE);
            // Fluid pump
            itemGroup.accept(ModBlocks.WOODEN_FLUID_PUMP);
        });
    }
}
