package teddy.minecraftautomation.blocks.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.fabricmc.fabric.api.transfer.v1.client.fluid.FluidVariantRendering;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.level.block.entity.BlockEntity;

public class RenderingUtils {
    public static float pixels(float n) {
        return n / 16f;
    }

    public static void renderFluidVariant(FluidVariant fluidVariant, float radius, float heightOffset, BlockEntity blockEntity, MultiBufferSource multiBufferSource, PoseStack matrices, int light, int overlay) {
        float height = heightOffset + radius * 2f;
        float offset = 0.001f;
        float radiusPlus = pixels(8f) + radius;
        float radiusMinus = pixels(8f) - radius;

        int color = FluidVariantRendering.getColor(fluidVariant, blockEntity.getLevel(), blockEntity.getBlockPos());
        TextureAtlasSprite sprite = FluidVariantRendering.getSprites(fluidVariant)[0];
        VertexConsumer vertexConsumer = multiBufferSource.getBuffer(RenderType.entityTranslucent(sprite.atlasLocation()));
        PoseStack.Pose pose = matrices.last();

        float minU = sprite.getU(pixels(radiusMinus));
        float minV = sprite.getV(heightOffset);

        float maxU = sprite.getU(pixels(radiusPlus));
        float maxV = sprite.getV(height);

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

        minU = sprite.getU(radiusMinus);
        maxU = sprite.getU(radiusPlus);
        minV = sprite.getV(radiusMinus);
        maxV = sprite.getV(radiusPlus);

        // Up
        RenderingUtils.drawVerticalQuad(vertexConsumer, pose,
                radiusMinus, height - offset, radiusMinus,
                radiusPlus, radiusPlus,
                minU, minV, maxU, maxV, color, light, overlay);

        // Down
        RenderingUtils.drawVerticalQuad(vertexConsumer, pose,
                radiusMinus, heightOffset + offset, radiusMinus,
                radiusPlus, radiusPlus,
                minU, minV, maxU, maxV, color, light, overlay);
    }

    public static void drawHorizontalQuad(VertexConsumer vertexConsumer,
                                          PoseStack.Pose pose,
                                          float x1, float y1, float z1,
                                          float x2, float y2, float z2,
                                          float minU, float minV,
                                          float maxU, float maxV,
                                          int color,
                                          int light, int overlay) {

        vertexConsumer.addVertex(pose, x1, y1, z1)
                .setColor(color)
                .setUv(minU, minV)
                .setLight(light)
                .setOverlay(overlay)
                .setNormal(0.0F, 1.0F, 0.0F);

        vertexConsumer.addVertex(pose, x1, y2, z1)
                .setColor(color)
                .setUv(minU, maxV)
                .setLight(light)
                .setOverlay(overlay)
                .setNormal(0.0F, 1.0F, 0.0F);

        vertexConsumer.addVertex(pose, x2, y2, z2)
                .setColor(color)
                .setUv(maxU, maxV)
                .setLight(light)
                .setOverlay(overlay)
                .setNormal(0.0F, 1.0F, 0.0F);

        vertexConsumer.addVertex(pose, x2, y1, z2)
                .setColor(color)
                .setUv(maxU, minV)
                .setLight(light)
                .setOverlay(overlay)
                .setNormal(0.0F, 1.0F, 0.0F);
    }

    public static void drawVerticalQuad(VertexConsumer vertexConsumer,
                                        PoseStack.Pose pose,
                                        float x1, float y1, float z1,
                                        float x2, float z2,
                                        float minU, float minV,
                                        float maxU, float maxV,
                                        int color,
                                        int light, int overlay) {

        vertexConsumer.addVertex(pose, x1, y1, z1)
                .setColor(color)
                .setUv(minU, minV)
                .setLight(light)
                .setOverlay(overlay)
                .setNormal(0.0F, 1.0F, 0.0F);

        vertexConsumer.addVertex(pose, x1, y1, z2)
                .setColor(color)
                .setUv(minU, maxV)
                .setLight(light)
                .setOverlay(overlay)
                .setNormal(0.0F, 1.0F, 0.0F);

        vertexConsumer.addVertex(pose, x2, y1, z2)
                .setColor(color)
                .setUv(maxU, maxV)
                .setLight(light)
                .setOverlay(overlay)
                .setNormal(0.0F, 1.0F, 0.0F);

        vertexConsumer.addVertex(pose, x2, y1, z1)
                .setColor(color)
                .setUv(maxU, minV)
                .setLight(light)
                .setOverlay(overlay)
                .setNormal(0.0F, 1.0F, 0.0F);
    }
}
