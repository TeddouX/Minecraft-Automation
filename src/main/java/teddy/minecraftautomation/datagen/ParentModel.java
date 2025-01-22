package teddy.minecraftautomation.datagen;

import com.google.gson.JsonObject;
import net.minecraft.client.data.models.model.ModelInstance;
import net.minecraft.client.data.models.model.ModelLocationUtils;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import teddy.minecraftautomation.MinecraftAutomation;

import java.util.function.BiConsumer;

public class ParentModel {
    public final ResourceLocation model;
    public final TextureSlot textureSlot;
    public final String suffix;
    public ParentModel(String model, String suffix, TextureSlot textureSlot) {
        this.model = ResourceLocation.fromNamespaceAndPath(MinecraftAutomation.MOD_ID, "block/" + model);
        this.suffix = suffix;
        this.textureSlot = textureSlot;
    }

    public ResourceLocation create(Block child, Block parent, BiConsumer<ResourceLocation, ModelInstance> biConsumer) {
        return this.createJson(ModelLocationUtils.getModelLocation(child, suffix), ModelLocationUtils.getModelLocation(parent), biConsumer);
    }

    public static ResourceLocation create(String parentModel, String suffix, TextureSlot textureSlot, Block child, Block parent, BiConsumer<ResourceLocation, ModelInstance> biConsumer) {
        return new ParentModel(parentModel, suffix, textureSlot)
                .createJson(ModelLocationUtils.getModelLocation(child, suffix), ModelLocationUtils.getModelLocation(parent), biConsumer);
    }

    private ResourceLocation createJson(ResourceLocation resourceLocation, ResourceLocation parentTexture, BiConsumer<ResourceLocation, ModelInstance> biConsumer) {
        biConsumer.accept(resourceLocation, () -> {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("parent", this.model.toString());

            JsonObject jsonObject1 = new JsonObject();
            jsonObject1.addProperty(this.textureSlot.getId(), parentTexture.toString());

            jsonObject.add("textures", jsonObject1);

            return jsonObject;
        });

        return resourceLocation;
    }
}
