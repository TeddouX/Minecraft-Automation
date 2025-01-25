package teddy.minecraftautomation.blocks.entity;

import com.mojang.serialization.DataResult;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleVariantStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.RegistryOps;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class BlockEntityWithFluidStorage extends BlockEntity {
    public SingleVariantStorage<FluidVariant> fluidStorage;
    private int maxFluidCapacityMb;

    public BlockEntityWithFluidStorage(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState, int maxFluidCapacityMb) {
        super(blockEntityType, blockPos, blockState);

        this.maxFluidCapacityMb = maxFluidCapacityMb;
        this.fluidStorage = initializeFluidStorage(maxFluidCapacityMb, this);
    }

    @Override
    public void setChanged() {
        super.setChanged();
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
                blockEntity.setChanged();
            }
        };
    }

    void writeFluidStorageNbt(SingleVariantStorage<FluidVariant> fluidStorage, CompoundTag nbt, HolderLookup.Provider provider) {
        RegistryOps<Tag> ops = provider.createSerializationContext(NbtOps.INSTANCE);

        // Save all fields
        nbt.put("variant", FluidVariant.CODEC.encode(fluidStorage.variant, ops, nbt).getOrThrow(RuntimeException::new));
        nbt.putLong("amount", fluidStorage.amount);
        nbt.putLong("capacity", fluidStorage.getCapacity());
    }

    SingleVariantStorage<FluidVariant> readFluidStorageNbt(CompoundTag nbt, HolderLookup.Provider provider, BlockEntity blockEntity) {
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

    @Override
    protected void saveAdditional(CompoundTag nbt, HolderLookup.Provider provider) {
        // Store the fluidStorage
        writeFluidStorageNbt(this.fluidStorage, nbt, provider);

        nbt.putInt("maxFluidCapacityMb", maxFluidCapacityMb);

        super.saveAdditional(nbt, provider);
    }

    @Override
    protected void loadAdditional(CompoundTag nbt, HolderLookup.Provider provider) {
        super.loadAdditional(nbt, provider);

        // Load the fluidStorage
        this.fluidStorage = readFluidStorageNbt(nbt, provider, this);

        this.maxFluidCapacityMb = nbt.getInt("maxFluidCapacityMb");
    }
}
