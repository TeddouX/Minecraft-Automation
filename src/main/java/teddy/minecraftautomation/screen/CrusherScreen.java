package teddy.minecraftautomation.screen;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import teddy.minecraftautomation.screen.handlers.CrusherScreenHandler;
import teddy.minecraftautomation.screen.renderer.FluidStorageRenderer;

public class CrusherScreen extends HandledScreen<CrusherScreenHandler> {
    static final Identifier TEXTURE = Identifier.ofVanilla("textures/gui/container/dispenser.png");
    FluidStorageRenderer fluidStorageRenderer;

    public CrusherScreen(CrusherScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);

        this.fluidStorageRenderer = new FluidStorageRenderer(16, 32);
    }

    @Override
    protected void drawForeground(DrawContext context, int mouseX, int mouseY) {
        super.drawForeground(context, mouseX, mouseY);
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        context.drawTexture(RenderLayer::getGuiTextured, TEXTURE, this.x, this.y, 0, 0, this.backgroundWidth, this.backgroundHeight, 256, 256);

        int x = (this.width - this.backgroundWidth) / 2;
        int y = (this.height - this.backgroundHeight) / 2;

        this.fluidStorageRenderer.drawFluidStack(context, this.getScreenHandler().blockEntity.getFluidStorage(), x, y);
    }
}
