package teddy.minecraftautomation.utils;

import com.mojang.serialization.DataResult;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleVariantStorage;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.RegistryOps;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import teddy.minecraftautomation.MinecraftAutomation;

public interface ImplementedFluidStorage {
    default SingleVariantStorage<FluidVariant> initializeFluidStorage(int maxFluidCapacityMb, BlockEntity blockEntity) {
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
                blockEntity.setChanged();
            }
        };
    }

    default void writeFluidStorageNbt(SingleVariantStorage<FluidVariant> fluidStorage, CompoundTag nbt, HolderLookup.Provider provider) {
        RegistryOps<Tag> ops = provider.createSerializationContext(NbtOps.INSTANCE);

        // Save all fields
        nbt.put("variant", FluidVariant.CODEC.encode(fluidStorage.variant, ops, nbt).getOrThrow(RuntimeException::new));
        nbt.putLong("amount", fluidStorage.amount);
        nbt.putLong("capacity", fluidStorage.getCapacity());
    }

    default SingleVariantStorage<FluidVariant> readFluidStorageNbt(CompoundTag nbt, HolderLookup.Provider provider, BlockEntity blockEntity) {
        // Used this method instead of the SingleVariantStorage.writeNbt() because it doesn't store and load the capacity
        RegistryOps<Tag> ops = provider.createSerializationContext(NbtOps.INSTANCE);
        DataResult<FluidVariant> result = FluidVariant.CODEC.parse(ops, nbt.getCompound("variant"));

        FluidVariant variant = FluidVariant.blank();

        // Get the fluid variant
        if (result.error().isEmpty() && result.result().isPresent())
            variant = result.result().get();

        long amount = nbt.getLong("amount");
        long capacity = nbt.getLong("capacity");

        SingleVariantStorage<FluidVariant> fluidStorage = new SingleVariantStorage<>() {
            @Override
            protected FluidVariant getBlankVariant() {
                return FluidVariant.blank();
            }

            @Override
            protected long getCapacity(FluidVariant fluidVariant) {
                return capacity;
            }

            @Override
            protected void onFinalCommit() {
                blockEntity.setChanged();
            }
        };

        fluidStorage.amount = amount;
        fluidStorage.variant = variant;

        return fluidStorage;
    }
}
