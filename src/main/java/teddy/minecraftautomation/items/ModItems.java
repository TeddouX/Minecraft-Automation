package teddy.minecraftautomation.items;

import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import teddy.minecraftautomation.MinecraftAutomation;

public class ModItems {
    public static final Item PIPE_JOINT = register("pipe_joint", new Item.Settings());
    public static final Item WRENCH = register("wrench", new Item.Settings().maxDamage(100));

    public static Item register(String name, Item.Settings properties) {
        Identifier id = MinecraftAutomation.id(name);
        RegistryKey<Item> itemResourceKey = RegistryKey.of(RegistryKeys.ITEM, id);

        return Registry.register(Registries.ITEM, id, new Item(properties.registryKey(itemResourceKey)));
    }

    public static void initialize() {
        MinecraftAutomation.LOGGER.info("Initializing ModItems");
    }
}
