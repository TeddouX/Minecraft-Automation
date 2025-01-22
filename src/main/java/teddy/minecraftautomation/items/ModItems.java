package teddy.minecraftautomation.items;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import teddy.minecraftautomation.MinecraftAutomation;

public class ModItems {
    public static final Item PIPE_JOINT = register("pipe_joint", new Item.Properties());
    public static final Item WRENCH = register("wrench", new Item.Properties().durability(100));

    public static Item register(String name, Item.Properties properties) {
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(MinecraftAutomation.MOD_ID, name);
        ResourceKey<Item> itemResourceKey = ResourceKey.create(Registries.ITEM, id);

        return Registry.register(BuiltInRegistries.ITEM, id, new Item(properties.setId(itemResourceKey)));
    }

    public static void initialize() {
        MinecraftAutomation.LOGGER.info("Initializing ModItems");
    }
}
