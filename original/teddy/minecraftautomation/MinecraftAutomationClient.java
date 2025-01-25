package teddy.minecraftautomation;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import teddy.minecraftautomation.blocks.ModBlocks;
import teddy.minecraftautomation.blocks.entity.ModBlockEntities;
import teddy.minecraftautomation.blocks.entity.renderer.FluidPipeBlockEntityRenderer;
import teddy.minecraftautomation.blocks.entity.renderer.FluidPumpBlockEntityRenderer;
import teddy.minecraftautomation.blocks.entity.renderer.FluidTankBlockEntityRenderer;

public class MinecraftAutomationClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		BlockRenderLayerMap.INSTANCE.putBlocks(RenderType.cutout(), ModBlocks.WOODEN_FLUID_PIPE);
		BlockRenderLayerMap.INSTANCE.putBlocks(RenderType.cutout(), ModBlocks.WOODEN_FLUID_PUMP);
		BlockRenderLayerMap.INSTANCE.putBlocks(RenderType.cutout(), ModBlocks.WOODEN_FLUID_TANK);
		BlockRenderLayerMap.INSTANCE.putBlocks(RenderType.cutout(), ModBlocks.COPPER_FLUID_PIPE);
		BlockRenderLayerMap.INSTANCE.putBlocks(RenderType.cutout(), ModBlocks.COPPER_FLUID_PUMP);
		BlockRenderLayerMap.INSTANCE.putBlocks(RenderType.cutout(), ModBlocks.COPPER_FLUID_TANK);
		BlockRenderLayerMap.INSTANCE.putBlocks(RenderType.cutout(), ModBlocks.IRON_FLUID_PIPE);
		BlockRenderLayerMap.INSTANCE.putBlocks(RenderType.cutout(), ModBlocks.IRON_FLUID_PUMP);
		BlockRenderLayerMap.INSTANCE.putBlocks(RenderType.cutout(), ModBlocks.IRON_FLUID_TANK);
		BlockRenderLayerMap.INSTANCE.putBlocks(RenderType.cutout(), ModBlocks.GOLD_FLUID_PIPE);
		BlockRenderLayerMap.INSTANCE.putBlocks(RenderType.cutout(), ModBlocks.GOLD_FLUID_PUMP);
		BlockRenderLayerMap.INSTANCE.putBlocks(RenderType.cutout(), ModBlocks.GOLD_FLUID_TANK);
		BlockRenderLayerMap.INSTANCE.putBlocks(RenderType.cutout(), ModBlocks.DIAMOND_FLUID_PIPE);
		BlockRenderLayerMap.INSTANCE.putBlocks(RenderType.cutout(), ModBlocks.DIAMOND_FLUID_PUMP);
		BlockRenderLayerMap.INSTANCE.putBlocks(RenderType.cutout(), ModBlocks.DIAMOND_FLUID_TANK);
		BlockRenderLayerMap.INSTANCE.putBlocks(RenderType.cutout(), ModBlocks.NETHERITE_FLUID_PIPE);
		BlockRenderLayerMap.INSTANCE.putBlocks(RenderType.cutout(), ModBlocks.NETHERITE_FLUID_PUMP);
		BlockRenderLayerMap.INSTANCE.putBlocks(RenderType.cutout(), ModBlocks.NETHERITE_FLUID_TANK);

		BlockEntityRenderers.register(ModBlockEntities.FLUID_PIPE_BE, FluidPipeBlockEntityRenderer::new);
		BlockEntityRenderers.register(ModBlockEntities.FLUID_PUMP_BE, FluidPumpBlockEntityRenderer::new);
		BlockEntityRenderers.register(ModBlockEntities.FLUID_TANK_BE, FluidTankBlockEntityRenderer::new);
	}
}