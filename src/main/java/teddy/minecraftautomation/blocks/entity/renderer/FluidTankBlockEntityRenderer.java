package teddy.minecraftautomation.blocks.entity.renderer;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import teddy.minecraftautomation.blocks.entity.FluidTankBlockEntity;

import static teddy.minecraftautomation.blocks.entity.renderer.RenderingUtils.pixels;

public class FluidTankBlockEntityRenderer implements BlockEntityRenderer<FluidTankBlockEntity> {
    public FluidTankBlockEntityRenderer(BlockEntityRendererFactory.Context ignoredContext) {}

    @Override
    public void render(FluidTankBlockEntity blockEntity, float tickDelta, MatrixStack matrices, VertexConsumerProvider multiBufferSource, int light, int overlay) {
        float radius = pixels(8f);

        if (blockEntity.getStoredVariant() == FluidVariant.blank() || blockEntity.getStoredAmount() <= 0)
            return;

        float fillPercentage = (float) blockEntity.getStoredAmount() / blockEntity.getCapacity();
        float fluidHeight = pixels(fillPercentage * 16f);

        RenderingUtils.renderFluidVariant(blockEntity.getStoredVariant(), fluidHeight, radius, 0f, blockEntity, multiBufferSource, matrices, light, overlay, false);
    }
}
