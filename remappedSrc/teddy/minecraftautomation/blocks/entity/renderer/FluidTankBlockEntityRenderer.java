package teddy.minecraftautomation.blocks.entity.renderer;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleVariantStorage;
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

        SingleVariantStorage<FluidVariant> fluidTank = blockEntity.fluidStorage;

        if (fluidTank == null || fluidTank.isResourceBlank() || fluidTank.amount <= 0)
            return;

        float fillPercentage = (float) fluidTank.amount / fluidTank.getCapacity();
        float fluidHeight = pixels(fillPercentage * 16f);

        RenderingUtils.renderFluidVariant(fluidTank.variant, fluidHeight, radius, 0f, blockEntity, multiBufferSource, matrices, light, overlay, false);
    }
}
