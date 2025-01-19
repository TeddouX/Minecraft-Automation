package teddy.minecraftautomation;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import teddy.minecraftautomation.blocks.ModBlocks;
import teddy.minecraftautomation.blocks.entity.ModBlockEntities;
import teddy.minecraftautomation.items.ModCreativeTab;

public class MinecraftAutomation implements ModInitializer {
	public static final String MOD_ID = "minecraft-automation";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModBlocks.initialize();
		ModBlockEntities.initialize();
		ModCreativeTab.initialize();
	}
}