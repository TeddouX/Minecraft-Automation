package teddy.minecraftautomation.datagen.utils;

import com.google.gson.JsonObject;
import teddy.minecraftautomation.MinecraftAutomation;

import java.util.function.BiConsumer;
import net.minecraft.block.Block;
import net.minecraft.client.data.ModelIds;
import net.minecraft.client.data.ModelSupplier;
import net.minecraft.client.data.TextureKey;
import net.minecraft.util.Identifier;

public class ParentModel {
    public final Identifier model;
    public final TextureKey textureSlot;
    public final String suffix;
    public ParentModel(String model, String suffix, TextureKey textureSlot) {
        this.model = Identifier.of(MinecraftAutomation.MOD_ID, "block/" + model);
        this.suffix = suffix;
        this.textureSlot = textureSlot;
    }

    public Identifier create(Block child, Block parent, BiConsumer<Identifier, ModelSupplier> biConsumer) {
        return this.createJson(ModelIds.getBlockSubModelId(child, suffix), ModelIds.getBlockModelId(parent), biConsumer);
    }

    public static Identifier create(String parentModel, String suffix, TextureKey textureSlot, Block child, Block parent, BiConsumer<Identifier, ModelSupplier> biConsumer) {
        return new ParentModel(parentModel, suffix, textureSlot)
                .createJson(ModelIds.getBlockSubModelId(child, suffix), ModelIds.getBlockModelId(parent), biConsumer);
    }

    private Identifier createJson(Identifier resourceLocation, Identifier parentTexture, BiConsumer<Identifier, ModelSupplier> biConsumer) {
        biConsumer.accept(resourceLocation, () -> {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("parent", this.model.toString());

            JsonObject jsonObject1 = new JsonObject();
            jsonObject1.addProperty(this.textureSlot.getName(), parentTexture.toString());

            jsonObject.add("textures", jsonObject1);

            return jsonObject;
        });

        return resourceLocation;
    }
}
