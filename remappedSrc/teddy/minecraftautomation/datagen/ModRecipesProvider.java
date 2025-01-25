package teddy.minecraftautomation.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.advancement.AdvancementCriterion;
import net.minecraft.advancement.criterion.InventoryChangedCriterion;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.data.recipe.RecipeExporter;
import net.minecraft.data.recipe.RecipeGenerator;
import net.minecraft.data.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import org.jetbrains.annotations.NotNull;
import teddy.minecraftautomation.blocks.ModBlocks;
import teddy.minecraftautomation.items.ModItems;

import java.util.concurrent.CompletableFuture;

public class ModRecipesProvider extends FabricRecipeProvider {
    public ModRecipesProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    AdvancementCriterion<InventoryChangedCriterion.Conditions> has(RegistryWrapper.WrapperLookup provider, Item item) {
        return RecipeGenerator.conditionsFromPredicates(ItemPredicate.Builder.create().items(provider.getOrThrow(RegistryKeys.ITEM), item));
    }

    void createItemPipe(RegistryWrapper.WrapperLookup provider, RecipeExporter output, Item inherited, Item obtained) {
        ShapedRecipeJsonBuilder.create(provider.getOrThrow(RegistryKeys.ITEM), RecipeCategory.MISC, obtained, 4)
                .input('O', ModItems.PIPE_JOINT)
                .input('C', Blocks.CHEST)
                .input('#', inherited)
                .pattern("O#O")
                .pattern(" C ")
                .pattern("O#O")
                .criterion("has_item_joint", has(provider, ModItems.PIPE_JOINT))
                .offerTo(output);
    }

    void createFluidPipe(RegistryWrapper.WrapperLookup provider, RecipeExporter output, Item inherited, Item obtained) {
        ShapedRecipeJsonBuilder.create(provider.getOrThrow(RegistryKeys.ITEM), RecipeCategory.MISC, obtained, 4)
                .input('O', ModItems.PIPE_JOINT)
                .input('G', Blocks.GLASS)
                .input('#', inherited)
                .pattern("O#O")
                .pattern(" G ")
                .pattern("O#O")
                .criterion("has_item_joint", has(provider, ModItems.PIPE_JOINT))
                .offerTo(output);
    }

    void createItemPump(RegistryWrapper.WrapperLookup provider, RecipeExporter output, Item inherited, Item obtained, Item itemPipe) {
        ShapedRecipeJsonBuilder.create(provider.getOrThrow(RegistryKeys.ITEM), RecipeCategory.MISC, obtained, 1)
                .input('O', ModItems.PIPE_JOINT)
                .input('C', Blocks.CHEST)
                .input('P', itemPipe)
                .input('#', inherited)
                .pattern("O#O")
                .pattern("CPC")
                .pattern("O#O")
                .criterion("has_item_pipe", has(provider, itemPipe))
                .offerTo(output);
    }

    void createFluidPump(RegistryWrapper.WrapperLookup provider, RecipeExporter output, Item inherited, Item obtained, Item fluidPipe) {
        ShapedRecipeJsonBuilder.create(provider.getOrThrow(RegistryKeys.ITEM), RecipeCategory.MISC, obtained, 1)
                .input('O', ModItems.PIPE_JOINT)
                .input('G', Blocks.GLASS)
                .input('P', fluidPipe)
                .input('#', inherited)
                .pattern("O#O")
                .pattern("GPG")
                .pattern("O#O")
                .criterion("has_fluid_pipe", has(provider, fluidPipe))
                .offerTo(output);
    }

    void createFluidTank(RegistryWrapper.WrapperLookup provider, RecipeExporter output, Item inherited, Item obtained, Item fluidPipe) {
        ShapedRecipeJsonBuilder.create(provider.getOrThrow(RegistryKeys.ITEM), RecipeCategory.MISC, obtained, 1)
                .input('D', Blocks.POLISHED_DEEPSLATE)
                .input('G', Blocks.GLASS)
                .input('#', inherited)
                .pattern("#D#")
                .pattern("GGG")
                .pattern("#D#")
                .criterion("has_fluid_pipe", has(provider, fluidPipe))
                .offerTo(output);
    }

    @Override
    protected @NotNull RecipeGenerator getRecipeGenerator(RegistryWrapper.WrapperLookup provider, RecipeExporter recipeOutput) {
        return new RecipeGenerator(provider, recipeOutput) {
            @Override
            public void generate() {
                new RecipeFamilyProvider(provider, recipeOutput, Blocks.OAK_PLANKS.asItem())
                        .itemPipe(ModBlocks.WOODEN_ITEM_PIPE)
                        .fluidPipe(ModBlocks.WOODEN_FLUID_PIPE)
                        .itemPump(ModBlocks.WOODEN_ITEM_PUMP)
                        .fluidPump(ModBlocks.WOODEN_FLUID_PUMP)
                        .fluidTank(ModBlocks.WOODEN_FLUID_TANK);

                new RecipeFamilyProvider(provider, recipeOutput, Items.COPPER_INGOT)
                        .itemPipe(ModBlocks.COPPER_ITEM_PIPE)
                        .fluidPipe(ModBlocks.COPPER_FLUID_PIPE)
                        .itemPump(ModBlocks.COPPER_ITEM_PUMP)
                        .fluidPump(ModBlocks.COPPER_FLUID_PUMP)
                        .fluidTank(ModBlocks.COPPER_FLUID_TANK);
                new RecipeFamilyProvider(provider, recipeOutput, Items.IRON_INGOT)
                        .itemPipe(ModBlocks.IRON_ITEM_PIPE)
                        .fluidPipe(ModBlocks.IRON_FLUID_PIPE)
                        .itemPump(ModBlocks.IRON_ITEM_PUMP)
                        .fluidPump(ModBlocks.IRON_FLUID_PUMP)
                        .fluidTank(ModBlocks.IRON_FLUID_TANK);
                new RecipeFamilyProvider(provider, recipeOutput, Items.GOLD_INGOT)
                        .itemPipe(ModBlocks.GOLD_ITEM_PIPE)
                        .fluidPipe(ModBlocks.GOLD_FLUID_PIPE)
                        .itemPump(ModBlocks.GOLD_ITEM_PUMP)
                        .fluidPump(ModBlocks.GOLD_FLUID_PUMP)
                        .fluidTank(ModBlocks.GOLD_FLUID_TANK);
                new RecipeFamilyProvider(provider, recipeOutput, Items.DIAMOND)
                        .itemPipe(ModBlocks.DIAMOND_ITEM_PIPE)
                        .fluidPipe(ModBlocks.DIAMOND_FLUID_PIPE)
                        .itemPump(ModBlocks.DIAMOND_ITEM_PUMP)
                        .fluidPump(ModBlocks.DIAMOND_FLUID_PUMP)
                        .fluidTank(ModBlocks.DIAMOND_FLUID_TANK);
                new RecipeFamilyProvider(provider, recipeOutput, Items.NETHERITE_INGOT)
                        .itemPipe(ModBlocks.NETHERITE_ITEM_PIPE)
                        .fluidPipe(ModBlocks.NETHERITE_FLUID_PIPE)
                        .itemPump(ModBlocks.NETHERITE_ITEM_PUMP)
                        .fluidPump(ModBlocks.NETHERITE_FLUID_PUMP)
                        .fluidTank(ModBlocks.NETHERITE_FLUID_TANK);
            }
        };
    }

    @Override
    public @NotNull String getName() {
        return "";
    }

    class RecipeFamilyProvider {
        private final Item usedItem;
        private final RegistryWrapper.WrapperLookup provider;
        private final RecipeExporter recipeOutput;

        Item itemPipe;
        Item fluidPipe;

        public RecipeFamilyProvider(RegistryWrapper.WrapperLookup provider, RecipeExporter recipeOutput, Item usedItem) {
            this.usedItem = usedItem;
            this.provider = provider;
            this.recipeOutput = recipeOutput;
        }

        public RecipeFamilyProvider itemPipe(Block block) {
            createItemPipe(this.provider, this.recipeOutput, this.usedItem, block.asItem());
            this.itemPipe = block.asItem();
            return this;
        }

        public RecipeFamilyProvider fluidPipe(Block block) {
            createFluidPipe(this.provider, this.recipeOutput, this.usedItem, block.asItem());
            this.fluidPipe = block.asItem();
            return this;
        }

        public RecipeFamilyProvider itemPump(Block block) {
            createItemPump(this.provider, this.recipeOutput, this.usedItem, block.asItem(), this.itemPipe);
            return this;
        }

        public RecipeFamilyProvider fluidPump(Block block) {
            createFluidPump(this.provider, this.recipeOutput, this.usedItem, block.asItem(), this.fluidPipe);
            return this;
        }

        public RecipeFamilyProvider fluidTank(Block block) {
            createFluidTank(this.provider, this.recipeOutput, this.usedItem, block.asItem(), this.fluidPipe);
            return this;
        }
    }
}
