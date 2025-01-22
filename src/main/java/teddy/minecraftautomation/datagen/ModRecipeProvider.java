package teddy.minecraftautomation.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.client.data.models.blockstates.BlockStateGenerator;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.NotNull;
import teddy.minecraftautomation.blocks.ModBlocks;
import teddy.minecraftautomation.items.ModItems;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends FabricRecipeProvider {
    public ModRecipeProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    Criterion<InventoryChangeTrigger.TriggerInstance> has(HolderLookup.Provider provider, Item item) {
        return RecipeProvider.inventoryTrigger(ItemPredicate.Builder.item().of(provider.lookupOrThrow(Registries.ITEM), item));
    }

    void createItemPipe(HolderLookup.Provider provider, RecipeOutput output, Item inherited, Item obtained) {
        ShapedRecipeBuilder.shaped(provider.lookupOrThrow(Registries.ITEM), RecipeCategory.MISC, obtained, 4)
                .define('O', ModItems.PIPE_JOINT)
                .define('C', Blocks.CHEST)
                .define('#', inherited)
                .pattern("O#O")
                .pattern(" C ")
                .pattern("O#O")
                .unlockedBy("has_item_joint", has(provider, ModItems.PIPE_JOINT))
                .save(output);
    }

    void createFluidPipe(HolderLookup.Provider provider, RecipeOutput output, Item inherited, Item obtained) {
        ShapedRecipeBuilder.shaped(provider.lookupOrThrow(Registries.ITEM), RecipeCategory.MISC, obtained, 4)
                .define('O', ModItems.PIPE_JOINT)
                .define('G', Blocks.GLASS)
                .define('#', inherited)
                .pattern("O#O")
                .pattern(" G ")
                .pattern("O#O")
                .unlockedBy("has_item_joint", has(provider, ModItems.PIPE_JOINT))
                .save(output);
    }

    void createItemPump(HolderLookup.Provider provider, RecipeOutput output, Item inherited, Item obtained, Item itemPipe) {
        ShapedRecipeBuilder.shaped(provider.lookupOrThrow(Registries.ITEM), RecipeCategory.MISC, obtained, 1)
                .define('O', ModItems.PIPE_JOINT)
                .define('C', Blocks.CHEST)
                .define('P', itemPipe)
                .define('#', inherited)
                .pattern("O#O")
                .pattern("CPC")
                .pattern("O#O")
                .unlockedBy("has_item_pipe", has(provider, itemPipe))
                .save(output);
    }

    void createFluidPump(HolderLookup.Provider provider, RecipeOutput output, Item inherited, Item obtained, Item fluidPipe) {
        ShapedRecipeBuilder.shaped(provider.lookupOrThrow(Registries.ITEM), RecipeCategory.MISC, obtained, 1)
                .define('O', ModItems.PIPE_JOINT)
                .define('G', Blocks.GLASS)
                .define('P', fluidPipe)
                .define('#', inherited)
                .pattern("O#O")
                .pattern("GPG")
                .pattern("O#O")
                .unlockedBy("has_fluid_pipe", has(provider, fluidPipe))
                .save(output);
    }

    void createFluidTank(HolderLookup.Provider provider, RecipeOutput output, Item inherited, Item obtained, Item fluidPipe) {
        ShapedRecipeBuilder.shaped(provider.lookupOrThrow(Registries.ITEM), RecipeCategory.MISC, obtained, 1)
                .define('D', Blocks.POLISHED_DEEPSLATE)
                .define('G', Blocks.GLASS)
                .define('#', inherited)
                .pattern("#D#")
                .pattern("GGG")
                .pattern("#D#")
                .unlockedBy("has_fluid_pipe", has(provider, fluidPipe))
                .save(output);
    }

    @Override
    protected @NotNull RecipeProvider createRecipeProvider(HolderLookup.Provider provider, RecipeOutput recipeOutput) {
        return new RecipeProvider(provider, recipeOutput) {
            @Override
            public void buildRecipes() {
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
        private final HolderLookup.Provider provider;
        private final RecipeOutput recipeOutput;

        Item itemPipe;
        Item fluidPipe;

        public RecipeFamilyProvider(HolderLookup.Provider provider, RecipeOutput recipeOutput, Item usedItem) {
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
