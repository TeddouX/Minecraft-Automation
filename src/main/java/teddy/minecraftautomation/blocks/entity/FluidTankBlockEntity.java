package teddy.minecraftautomation.blocks.entity;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleVariantStorage;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;
import teddy.minecraftautomation.utils.ImplementedFluidStorage;

public class FluidTankBlockEntity extends BlockEntity implements ImplementedFluidStorage {
    public SingleVariantStorage<FluidVariant> fluidStorage;

    public FluidTankBlockEntity(BlockPos blockPos, BlockState blockState, int maxFluidCapacityMb) {
        super(ModBlockEntities.FLUID_TANK_BE, blockPos, blockState);

        this.fluidStorage = initializeFluidStorage(maxFluidCapacityMb, this::markDirty);
    }

    @Override
    public void markDirty() {
        super.markDirty();

        if (this.getWorld() != null)
            this.getWorld().updateListeners(this.getPos(), this.getCachedState(), this.getCachedState(), 3);
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup provider) {
        writeFluidStorageNbt(nbt, provider);

        super.writeNbt(nbt, provider);
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup provider) {
        super.readNbt(nbt, provider);

        this.fluidStorage = readFluidStorageNbt(nbt, provider, this::markDirty);
    }

    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public @NotNull NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup provider) {
        NbtCompound nbt = new NbtCompound();

        this.writeNbt(nbt, provider);

        return nbt;
    }

    @Override
    public SingleVariantStorage<FluidVariant> getFluidStorage() {
        return this.fluidStorage;
    }
}
