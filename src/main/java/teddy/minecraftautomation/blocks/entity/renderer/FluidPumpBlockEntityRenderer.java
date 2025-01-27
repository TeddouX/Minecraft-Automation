package teddy.minecraftautomation.blocks.entity.renderer;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleVariantStorage;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import teddy.minecraftautomation.blocks.entity.FluidPumpBlockEntity;

import static teddy.minecraftautomation.blocks.entity.renderer.RenderingUtils.pixels;

public class FluidPumpBlockEntityRenderer implements BlockEntityRenderer<FluidPumpBlockEntity> {
    public FluidPumpBlockEntityRenderer(BlockEntityRendererFactory.Context ignoredContext) {}

    @Override
    public void render(FluidPumpBlockEntity blockEntity, float f, MatrixStack matrices, VertexConsumerProvider multiBufferSource, int light, int overlay) {
        float radius = pixels(3f);
        float heightOffset = pixels(5f);

        SingleVariantStorage<FluidVariant> fluidTank = blockEntity.fluidStorage;

        if (fluidTank.isResourceBlank() || fluidTank.amount <= 0)
            return;

        RenderingUtils.renderFluidVariant(fluidTank.variant, radius * 2, radius, heightOffset, blockEntity, multiBufferSource, matrices, light, overlay, true);
    }
}
