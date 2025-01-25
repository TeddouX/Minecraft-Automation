package teddy.minecraftautomation;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import teddy.minecraftautomation.blocks.ModBlocks;
import teddy.minecraftautomation.blocks.entity.ModBlockEntities;
import teddy.minecraftautomation.blocks.entity.renderer.FluidPipeBlockEntityRenderer;
import teddy.minecraftautomation.blocks.entity.renderer.FluidPumpBlockEntityRenderer;
import teddy.minecraftautomation.blocks.entity.renderer.FluidTankBlockEntityRenderer;

public class MinecraftAutomationClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(), ModBlocks.WOODEN_FLUID_PIPE);
		BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(), ModBlocks.WOODEN_FLUID_PUMP);
		BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(), ModBlocks.WOODEN_FLUID_TANK);
		BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(), ModBlocks.COPPER_FLUID_PIPE);
		BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(), ModBlocks.COPPER_FLUID_PUMP);
		BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(), ModBlocks.COPPER_FLUID_TANK);
		BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(), ModBlocks.IRON_FLUID_PIPE);
		BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(), ModBlocks.IRON_FLUID_PUMP);
		BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(), ModBlocks.IRON_FLUID_TANK);
		BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(), ModBlocks.GOLD_FLUID_PIPE);
		BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(), ModBlocks.GOLD_FLUID_PUMP);
		BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(), ModBlocks.GOLD_FLUID_TANK);
		BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(), ModBlocks.DIAMOND_FLUID_PIPE);
		BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(), ModBlocks.DIAMOND_FLUID_PUMP);
		BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(), ModBlocks.DIAMOND_FLUID_TANK);
		BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(), ModBlocks.NETHERITE_FLUID_PIPE);
		BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(), ModBlocks.NETHERITE_FLUID_PUMP);
		BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(), ModBlocks.NETHERITE_FLUID_TANK);

		BlockEntityRendererFactories.register(ModBlockEntities.FLUID_PIPE_BE, FluidPipeBlockEntityRenderer::new);
		BlockEntityRendererFactories.register(ModBlockEntities.FLUID_PUMP_BE, FluidPumpBlockEntityRenderer::new);
		BlockEntityRendererFactories.register(ModBlockEntities.FLUID_TANK_BE, FluidTankBlockEntityRenderer::new);
	}
}