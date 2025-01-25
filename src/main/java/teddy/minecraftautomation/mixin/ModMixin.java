package teddy.minecraftautomation.mixin;

import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import teddy.minecraftautomation.MinecraftAutomation;

@Mixin(MinecraftServer.class)
public class ModMixin {
	@Inject(at = @At("HEAD"), method = "loadWorld")
	private void init(CallbackInfo info) {
		MinecraftAutomation.LOGGER.info("Hello from the ModMixin");
	}
}