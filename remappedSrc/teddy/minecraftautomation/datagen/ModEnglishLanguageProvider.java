package teddy.minecraftautomation.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.registry.RegistryWrapper;
import teddy.minecraftautomation.blocks.*;
import teddy.minecraftautomation.items.ModCreativeTab;
import teddy.minecraftautomation.items.ModItems;

import java.util.concurrent.CompletableFuture;

public class ModEnglishLanguageProvider extends FabricLanguageProvider {
    public ModEnglishLanguageProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generateTranslations(RegistryWrapper.WrapperLookup provider, TranslationBuilder translationBuilder) {
        translationBuilder.add(ModBlocks.WOODEN_ITEM_PIPE.asItem(), "Wooden Item Pipe");
        translationBuilder.add(ModBlocks.WOODEN_ITEM_PUMP.asItem(), "Wooden Item Pump");
        translationBuilder.add(ModBlocks.WOODEN_FLUID_PIPE.asItem(), "Wooden Fluid Pipe");
        translationBuilder.add(ModBlocks.WOODEN_FLUID_PUMP.asItem(), "Wooden Fluid Pump");
        translationBuilder.add(ModBlocks.WOODEN_FLUID_TANK.asItem(), "Wooden Fluid Tank");
        translationBuilder.add(ModBlocks.COPPER_ITEM_PIPE.asItem(), "Copper Item Pipe");
        translationBuilder.add(ModBlocks.COPPER_ITEM_PUMP.asItem(), "Copper Item Pump");
        translationBuilder.add(ModBlocks.COPPER_FLUID_PIPE.asItem(), "Copper Fluid Pipe");
        translationBuilder.add(ModBlocks.COPPER_FLUID_PUMP.asItem(), "Copper Fluid Pump");
        translationBuilder.add(ModBlocks.COPPER_FLUID_TANK.asItem(), "Copper Fluid Tank");
        translationBuilder.add(ModBlocks.IRON_ITEM_PIPE.asItem(), "Iron Item Pipe");
        translationBuilder.add(ModBlocks.IRON_ITEM_PUMP.asItem(), "Iron Item Pump");
        translationBuilder.add(ModBlocks.IRON_FLUID_PIPE.asItem(), "Iron Fluid Pipe");
        translationBuilder.add(ModBlocks.IRON_FLUID_PUMP.asItem(), "Iron Fluid Pump");
        translationBuilder.add(ModBlocks.IRON_FLUID_TANK.asItem(), "Iron Fluid Tank");
        translationBuilder.add(ModBlocks.GOLD_ITEM_PIPE.asItem(), "Gold Item Pipe");
        translationBuilder.add(ModBlocks.GOLD_ITEM_PUMP.asItem(), "Gold Item Pump");
        translationBuilder.add(ModBlocks.GOLD_FLUID_PIPE.asItem(), "Gold Fluid Pipe");
        translationBuilder.add(ModBlocks.GOLD_FLUID_PUMP.asItem(), "Gold Fluid Pump");
        translationBuilder.add(ModBlocks.GOLD_FLUID_TANK.asItem(), "Gold Fluid Tank");
        translationBuilder.add(ModBlocks.DIAMOND_ITEM_PIPE.asItem(), "Diamond Item Pipe");
        translationBuilder.add(ModBlocks.DIAMOND_ITEM_PUMP.asItem(), "Diamond Item Pump");
        translationBuilder.add(ModBlocks.DIAMOND_FLUID_PIPE.asItem(), "Diamond Fluid Pipe");
        translationBuilder.add(ModBlocks.DIAMOND_FLUID_PUMP.asItem(), "Diamond Fluid Pump");
        translationBuilder.add(ModBlocks.DIAMOND_FLUID_TANK.asItem(), "Diamond Fluid Tank");
        translationBuilder.add(ModBlocks.NETHERITE_ITEM_PIPE.asItem(), "Netherite Item Pipe");
        translationBuilder.add(ModBlocks.NETHERITE_ITEM_PUMP.asItem(), "Netherite Item Pump");
        translationBuilder.add(ModBlocks.NETHERITE_FLUID_PIPE.asItem(), "Netherite Fluid Pipe");
        translationBuilder.add(ModBlocks.NETHERITE_FLUID_PUMP.asItem(), "Netherite Fluid Pump");
        translationBuilder.add(ModBlocks.NETHERITE_FLUID_TANK.asItem(), "Netherite Fluid Tank");

        translationBuilder.add(ModItems.PIPE_JOINT, "Pipe Joint");
        translationBuilder.add(ModItems.WRENCH, "Wrench");

        translationBuilder.add(AbstractPipeBlock.MAX_PRESSURE_TOOLTIP.getTranslationKey(), AbstractPipeBlock.MAX_PRESSURE_TOOLTIP.getValue());
        translationBuilder.add(AbstractPipeBlock.STATS_TOOLTIP.getTranslationKey(), AbstractPipeBlock.STATS_TOOLTIP.getValue());
        translationBuilder.add(AbstractPumpBlock.INDUCED_PRESSURE_TOOLTIP.getTranslationKey(), ItemPumpBlock.INDUCED_PRESSURE_TOOLTIP.getValue());
        translationBuilder.add(ItemPipeBlock.ITEMS_PER_TRANSFER_TOOLTIP.getTranslationKey(), ItemPipeBlock.ITEMS_PER_TRANSFER_TOOLTIP.getValue());
        translationBuilder.add(ItemPipeBlock.TRANSFER_COOLDOW_TOOLTIP.getTranslationKey(), ItemPipeBlock.TRANSFER_COOLDOW_TOOLTIP.getValue());
        translationBuilder.add(FluidPipeBlock.MB_PER_TRANSFER_TOOLTIP.getTranslationKey(), FluidPipeBlock.MB_PER_TRANSFER_TOOLTIP.getValue());
        translationBuilder.add(FluidPipeBlock.FLUID_CAPACITY_TOOLTIP.getTranslationKey(), FluidPipeBlock.FLUID_CAPACITY_TOOLTIP.getValue());

        translationBuilder.add(ModCreativeTab.MOD_CREATIVE_TAB_KEY, "Minecraft Automation");
    }
}