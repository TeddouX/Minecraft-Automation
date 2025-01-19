package teddy.minecraftautomation;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import teddy.minecraftautomation.datagen.ModEnglishLanguageProvider;
import teddy.minecraftautomation.datagen.ModModelProvider;

public class MinecraftAutomationDataGenerator implements DataGeneratorEntrypoint {

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator generator) {
        FabricDataGenerator.Pack pack = generator.createPack();

        pack.addProvider(ModEnglishLanguageProvider::new);
        pack.addProvider(ModModelProvider::new);
    }

}
