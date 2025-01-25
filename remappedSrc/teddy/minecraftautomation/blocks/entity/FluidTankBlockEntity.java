package teddy.minecraftautomation.blocks.entity;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;

public class FluidTankBlockEntity extends BlockEntityWithFluidStorage {
    public FluidTankBlockEntity(BlockPos blockPos, BlockState blockState, int maxFluidCapacityMb) {
        super(ModBlockEntities.FLUID_TANK_BE, blockPos, blockState, maxFluidCapacityMb);
    }

    @Override
    public void markDirty() {
        super.markDirty();

        if (this.getWorld() != null)
            this.getWorld().updateListeners(this.getPos(), this.getCachedState(), this.getCachedState(), 3);
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
}
