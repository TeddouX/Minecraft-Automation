package teddy.minecraftautomation.datagen.utils;

import net.minecraft.block.Block;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import teddy.minecraftautomation.MinecraftAutomation;

public class ModBlockTags {
    public static final TagKey<Block> ITEM_PIPES = create("item_pipes");
    public static final TagKey<Block> FLUID_PIPES = create("fluid_pipes");
    public static final TagKey<Block> ITEM_PUMPS = create("item_pumps");
    public static final TagKey<Block> FLUID_PUMPS = create("fluid_pumps");
    public static final TagKey<Block> FLUID_TANKS = create("fluid_tanks");

    static TagKey<Block> create(String path) {
        return TagKey.of(RegistryKeys.BLOCK, MinecraftAutomation.id(path));
    }
}
