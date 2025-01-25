package teddy.minecraftautomation.blocks.entity;

import com.mojang.serialization.DataResult;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleVariantStorage;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
import net.minecraft.registry.RegistryOps;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.math.BlockPos;

public abstract class BlockEntityWithFluidStorage extends BlockEntity {
    public SingleVariantStorage<FluidVariant> fluidStorage;
    private int maxFluidCapacityMb;

    public BlockEntityWithFluidStorage(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState, int maxFluidCapacityMb) {
        super(blockEntityType, blockPos, blockState);

        this.maxFluidCapacityMb = maxFluidCapacityMb;
        this.fluidStorage = initializeFluidStorage(maxFluidCapacityMb, this);
    }

    @Override
    public void markDirty() {
        super.markDirty();
    }

    private SingleVariantStorage<FluidVariant> initializeFluidStorage(int maxFluidCapacityMb, BlockEntity blockEntity) {
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
                blockEntity.markDirty();
            }
        };
    }

    void writeFluidStorageNbt(SingleVariantStorage<FluidVariant> fluidStorage, NbtCompound nbt, RegistryWrapper.WrapperLookup provider) {
        RegistryOps<NbtElement> ops = provider.getOps(NbtOps.INSTANCE);

        // Save all fields
        nbt.put("variant", FluidVariant.CODEC.encode(fluidStorage.variant, ops, nbt).getOrThrow(RuntimeException::new));
        nbt.putLong("amount", fluidStorage.amount);
        nbt.putLong("capacity", fluidStorage.getCapacity());
    }

    SingleVariantStorage<FluidVariant> readFluidStorageNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup provider, BlockEntity blockEntity) {
        // Used this method instead of the SingleVariantStorage.writeNbt() because it doesn't store and load the capacity
        RegistryOps<NbtElement> ops = provider.getOps(NbtOps.INSTANCE);
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
                blockEntity.markDirty();
            }
        };

        fluidStorage.amount = amount;
        fluidStorage.variant = variant;

        return fluidStorage;
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup provider) {
        // Store the fluidStorage
        writeFluidStorageNbt(this.fluidStorage, nbt, provider);

        nbt.putInt("maxFluidCapacityMb", maxFluidCapacityMb);

        super.writeNbt(nbt, provider);
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup provider) {
        super.readNbt(nbt, provider);

        // Load the fluidStorage
        this.fluidStorage = readFluidStorageNbt(nbt, provider, this);

        this.maxFluidCapacityMb = nbt.getInt("maxFluidCapacityMb");
    }
}
