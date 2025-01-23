package teddy.minecraftautomation.datagen.utils;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import teddy.minecraftautomation.MinecraftAutomation;

public class ModBlockTags {
    public static final TagKey<Block> ITEM_PIPES = create("item_pipes");
    public static final TagKey<Block> FLUID_PIPES = create("fluid_pipes");
    public static final TagKey<Block> ITEM_PUMPS = create("item_pumps");
    public static final TagKey<Block> FLUID_PUMPS = create("fluid_pumps");
    public static final TagKey<Block> FLUID_TANKS = create("fluid_tanks");

    static TagKey<Block> create(String path) {
        return TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(MinecraftAutomation.MOD_ID, path));
    }
}
