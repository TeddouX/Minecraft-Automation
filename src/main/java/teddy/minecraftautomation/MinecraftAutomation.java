package teddy.minecraftautomation;

import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import teddy.minecraftautomation.blocks.ModBlocks;
import teddy.minecraftautomation.blocks.entity.ModBlockEntities;
import teddy.minecraftautomation.items.ModCreativeTab;
import teddy.minecraftautomation.items.ModItems;
import teddy.minecraftautomation.screen.handlers.ModScreenHandlers;

public class MinecraftAutomation implements ModInitializer {
	public static final String MOD_ID = "minecraft-automation";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static Identifier id(String name) {
		return Identifier.of(MOD_ID, name);
	}

	@Override
	public void onInitialize() {
		ModBlocks.initialize();
		ModItems.initialize();
		ModBlockEntities.initialize();
		ModCreativeTab.initialize();
		ModScreenHandlers.initialize();
	}
}