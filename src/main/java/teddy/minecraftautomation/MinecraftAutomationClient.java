package teddy.minecraftautomation;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import teddy.minecraftautomation.blocks.ModBlocks;
import teddy.minecraftautomation.blocks.entity.ModBlockEntities;
import teddy.minecraftautomation.blocks.entity.renderer.FluidPipeBlockEntityRenderer;

public class MinecraftAutomationClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		BlockRenderLayerMap.INSTANCE.putBlocks(RenderType.translucent(), ModBlocks.WOODEN_FLUID_PIPE);

		BlockEntityRenderers.register(ModBlockEntities.FLUID_PIPE_BE, FluidPipeBlockEntityRenderer::new);
	}
}