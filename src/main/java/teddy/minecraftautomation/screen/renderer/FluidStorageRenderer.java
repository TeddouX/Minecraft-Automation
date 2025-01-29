package teddy.minecraftautomation.screen.renderer;

import net.fabricmc.fabric.api.transfer.v1.client.fluid.FluidVariantRendering;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleVariantStorage;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.texture.Sprite;

public class FluidStorageRenderer {
    private final int width;
    private final int height;

    public FluidStorageRenderer(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void drawFluidStack(DrawContext drawContext, SingleVariantStorage<FluidVariant> fluidStorage, int x, int y) {
        FluidVariant fluidVariant = fluidStorage.getResource();

        if (fluidVariant.isBlank())
            return;

        int color = FluidVariantRendering.getColor(fluidVariant);
        Sprite sprite = FluidVariantRendering.getSprite(fluidVariant);

        float fluidStorageFill = (float) fluidStorage.getAmount() / fluidStorage.getCapacity();
        int fluidHeight = (int) (fluidStorageFill * this.height);

        drawContext.drawSpriteStretched(RenderLayer::getGuiTextured, sprite, x, y - fluidHeight, this.width, fluidHeight, color);
    }
}
