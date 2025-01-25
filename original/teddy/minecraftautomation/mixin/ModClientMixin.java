package teddy.minecraftautomation.mixin;

import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import teddy.minecraftautomation.MinecraftAutomation;

@Mixin(Minecraft.class)
public class ModClientMixin {
    @Inject(at = @At("HEAD"), method="run")
    private void init(CallbackInfo info) {
        MinecraftAutomation.LOGGER.info("Hello from the ClientModMixin");
    }
}
