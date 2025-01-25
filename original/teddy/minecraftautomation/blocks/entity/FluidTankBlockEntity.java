package teddy.minecraftautomation.blocks.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class FluidTankBlockEntity extends BlockEntityWithFluidStorage {
    public FluidTankBlockEntity(BlockPos blockPos, BlockState blockState, int maxFluidCapacityMb) {
        super(ModBlockEntities.FLUID_TANK_BE, blockPos, blockState, maxFluidCapacityMb);
    }

    @Override
    public void setChanged() {
        super.setChanged();

        if (this.getLevel() != null)
            this.getLevel().sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 3);
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public @NotNull CompoundTag getUpdateTag(HolderLookup.Provider provider) {
        CompoundTag nbt = new CompoundTag();

        this.saveAdditional(nbt, provider);

        return nbt;
    }
}
