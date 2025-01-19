package teddy.minecraftautomation.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.core.HolderLookup;
import teddy.minecraftautomation.blocks.AbstractPipeBlock;
import teddy.minecraftautomation.blocks.ItemPipeBlock;
import teddy.minecraftautomation.blocks.ItemPumpBlock;
import teddy.minecraftautomation.blocks.ModBlocks;
import teddy.minecraftautomation.items.ModCreativeTab;

import java.util.concurrent.CompletableFuture;

public class ModEnglishLanguageProvider extends FabricLanguageProvider {
    public ModEnglishLanguageProvider(FabricDataOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generateTranslations(HolderLookup.Provider provider, TranslationBuilder translationBuilder) {
        translationBuilder.add(ModBlocks.WOODEN_ITEM_PIPE.asItem(), "Wooden Item Pipe");
        translationBuilder.add(ModBlocks.WOODEN_ITEM_PUMP.asItem(), "Wooden Item Pump");
        translationBuilder.add(ModBlocks.WOODEN_FLUID_PIPE.asItem(), "Wooden Fluid Pipe");
        translationBuilder.add(ModBlocks.WOODEN_FLUID_PUMP.asItem(), "Wooden Fluid Pump");

        translationBuilder.add(AbstractPipeBlock.MAX_PRESSURE_TOOLTIP.getTranslationKey(), AbstractPipeBlock.MAX_PRESSURE_TOOLTIP.getValue());
        translationBuilder.add(ItemPipeBlock.ITEMS_PER_TRANSFER_TOOLTIP.getTranslationKey(), ItemPipeBlock.ITEMS_PER_TRANSFER_TOOLTIP.getValue());
        translationBuilder.add(ItemPipeBlock.TRANSFER_COOLDOW_TOOLTIP.getTranslationKey(), ItemPipeBlock.TRANSFER_COOLDOW_TOOLTIP.getValue());

        translationBuilder.add(ItemPumpBlock.ITEMS_PER_TRANSFER_TOOLTIP.getTranslationKey(), ItemPumpBlock.ITEMS_PER_TRANSFER_TOOLTIP.getValue());
        translationBuilder.add(ItemPumpBlock.TRANSFER_COOLDOW_TOOLTIP.getTranslationKey(), ItemPumpBlock.TRANSFER_COOLDOW_TOOLTIP.getValue());
        translationBuilder.add(ItemPumpBlock.INDUCED_PRESSURE_TOOLTIP.getTranslationKey(), ItemPumpBlock.INDUCED_PRESSURE_TOOLTIP.getValue());

        translationBuilder.add(ModCreativeTab.MOD_CREATIVE_TAB_KEY, "Minecraft Automation");
    }
}