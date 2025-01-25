package teddy.minecraftautomation.blocks.entity.renderer;

import net.fabricmc.fabric.api.transfer.v1.client.fluid.FluidVariantRendering;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.MatrixStack;

// All credit goes to https://github.com/DaRealTurtyWurty/1.21-Tutorial-Mod for the fluid rendering
// (got it from a tutorial on his YouTube channel https://www.youtube.com/@TurtyWurty)
public class RenderingUtils {
    public static float pixels(float n) {
        return n / 16f;
    }

    public static void renderFluidVariant(FluidVariant fluidVariant, float fluidHeight, float radius, float heightOffset, BlockEntity blockEntity, VertexConsumerProvider multiBufferSource, MatrixStack matrices, int light, int overlay, boolean renderBottom) {
        float height = (heightOffset + radius * 2f) - (radius * 2f - fluidHeight);
        float offset = 0.001f;
        float radiusPlus = pixels(8f) + radius;
        float radiusMinus = pixels(8f) - radius;

        int color = FluidVariantRendering.getColor(fluidVariant, blockEntity.getWorld(), blockEntity.getPos());
        Sprite sprite = FluidVariantRendering.getSprites(fluidVariant)[0];
        VertexConsumer vertexConsumer = multiBufferSource.getBuffer(RenderLayer.getEntityTranslucent(sprite.getAtlasId()));
        MatrixStack.Entry pose = matrices.peek();

        float minU = sprite.getFrameU(radiusMinus);
        float minV = sprite.getFrameV(heightOffset);

        float maxU = sprite.getFrameU(radiusPlus);
        float maxV = sprite.getFrameV(height);

        // North
        RenderingUtils.drawHorizontalQuad(vertexConsumer, pose,
                radiusMinus, heightOffset, radiusMinus + offset,
                radiusPlus, height, radiusMinus + offset,
                minU, minV, maxU, maxV, color, light, overlay);

        // South
        RenderingUtils.drawHorizontalQuad(vertexConsumer, pose,
                radiusMinus, heightOffset, radiusPlus - offset,
                radiusPlus, height, radiusPlus - offset,
                minU, minV, maxU, maxV, color, light, overlay);

        // East
        RenderingUtils.drawHorizontalQuad(vertexConsumer, pose,
                radiusMinus + offset, heightOffset, radiusMinus,
                radiusMinus + offset, height, radiusPlus,
                minU, minV, maxU, maxV, color, light, overlay);

        // South
        RenderingUtils.drawHorizontalQuad(vertexConsumer, pose,
                radiusPlus - offset, heightOffset, radiusMinus,
                radiusPlus - offset, height, radiusPlus,
                minU, minV, maxU, maxV, color, light, overlay);

        minU = sprite.getFrameU(radiusMinus);
        maxU = sprite.getFrameU(radiusPlus);
        minV = sprite.getFrameV(radiusMinus);
        maxV = sprite.getFrameV(radiusPlus);

        // Up
        RenderingUtils.drawVerticalQuad(vertexConsumer, pose,
                radiusMinus, height - offset, radiusMinus,
                radiusPlus, radiusPlus,
                minU, minV, maxU, maxV, color, light, overlay);

        // Down
        if (renderBottom)
            RenderingUtils.drawVerticalQuad(vertexConsumer, pose,
                    radiusMinus, heightOffset + offset, radiusMinus,
                    radiusPlus, radiusPlus,
                    minU, minV, maxU, maxV, color, light, overlay);
    }

    public static void drawHorizontalQuad(VertexConsumer vertexConsumer,
                                          MatrixStack.Entry pose,
                                          float x1, float y1, float z1,
                                          float x2, float y2, float z2,
                                          float minU, float minV,
                                          float maxU, float maxV,
                                          int color,
                                          int light, int overlay) {

        vertexConsumer.vertex(pose, x1, y1, z1)
                .color(color)
                .texture(minU, minV)
                .light(light)
                .overlay(overlay)
                .normal(0.0F, 1.0F, 0.0F);

        vertexConsumer.vertex(pose, x1, y2, z1)
                .color(color)
                .texture(minU, maxV)
                .light(light)
                .overlay(overlay)
                .normal(0.0F, 1.0F, 0.0F);

        vertexConsumer.vertex(pose, x2, y2, z2)
                .color(color)
                .texture(maxU, maxV)
                .light(light)
                .overlay(overlay)
                .normal(0.0F, 1.0F, 0.0F);

        vertexConsumer.vertex(pose, x2, y1, z2)
                .color(color)
                .texture(maxU, minV)
                .light(light)
                .overlay(overlay)
                .normal(0.0F, 1.0F, 0.0F);
    }

    public static void drawVerticalQuad(VertexConsumer vertexConsumer,
                                        MatrixStack.Entry pose,
                                        float x1, float y1, float z1,
                                        float x2, float z2,
                                        float minU, float minV,
                                        float maxU, float maxV,
                                        int color,
                                        int light, int overlay) {

        vertexConsumer.vertex(pose, x1, y1, z1)
                .color(color)
                .texture(minU, minV)
                .light(light)
                .overlay(overlay)
                .normal(0.0F, 1.0F, 0.0F);

        vertexConsumer.vertex(pose, x1, y1, z2)
                .color(color)
                .texture(minU, maxV)
                .light(light)
                .overlay(overlay)
                .normal(0.0F, 1.0F, 0.0F);

        vertexConsumer.vertex(pose, x2, y1, z2)
                .color(color)
                .texture(maxU, maxV)
                .light(light)
                .overlay(overlay)
                .normal(0.0F, 1.0F, 0.0F);

        vertexConsumer.vertex(pose, x2, y1, z1)
                .color(color)
                .texture(maxU, minV)
                .light(light)
                .overlay(overlay)
                .normal(0.0F, 1.0F, 0.0F);
    }
}
