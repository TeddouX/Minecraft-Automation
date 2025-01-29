package teddy.minecraftautomation.utils;

import com.mojang.serialization.DataResult;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleVariantStorage;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
import net.minecraft.registry.RegistryOps;
import net.minecraft.registry.RegistryWrapper;

public interface ImplementedFluidStorage {
    default SingleVariantStorage<FluidVariant> initializeFluidStorage(long maxFluidCapacityMb, Runnable onFinalCommitFunc) {
        return new SingleVariantStorage<>() {
            @Override
            protected FluidVariant getBlankVariant() {
                return FluidVariant.blank();
            }

            @Override
            protected long getCapacity(FluidVariant variant) {
                return maxFluidCapacityMb;
            }

            @Override
            protected void onFinalCommit() {
                onFinalCommitFunc.run();
            }
        };
    }

    default void writeFluidStorageNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup provider) {
        RegistryOps<NbtElement> ops = provider.getOps(NbtOps.INSTANCE);

        // Save all fields
        nbt.put("variant", FluidVariant.CODEC.encode(getStoredVariant(), ops, nbt).getOrThrow(RuntimeException::new));
        nbt.putLong("amount", getStoredAmount());
        nbt.putLong("capacity", getCapacity());
    }

    default SingleVariantStorage<FluidVariant> readFluidStorageNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup provider, Runnable onFinalCommitFunc) {
        // Used this method instead of the SingleVariantStorage.writeNbt() because it doesn't store and load the capacity
        RegistryOps<NbtElement> ops = provider.getOps(NbtOps.INSTANCE);
        DataResult<FluidVariant> result = FluidVariant.CODEC.parse(ops, nbt.getCompound("variant"));

        FluidVariant variant = FluidVariant.blank();

        // Get the fluid variant
        if (result.error().isEmpty() && result.result().isPresent())
            variant = result.result().get();

        long amount = nbt.getLong("amount");
        long capacity = nbt.getLong("capacity");

        SingleVariantStorage<FluidVariant> fluidStorage = initializeFluidStorage(capacity, onFinalCommitFunc);

        fluidStorage.amount = amount;
        fluidStorage.variant = variant;

        return fluidStorage;
    }

    default FluidVariant getStoredVariant() {
        return getFluidStorage().variant;
    }

    default long getStoredAmount() {
        return getFluidStorage().amount;
    }

    default long getCapacity() {
        return getFluidStorage().getCapacity();
    }

    SingleVariantStorage<FluidVariant> getFluidStorage();
}
