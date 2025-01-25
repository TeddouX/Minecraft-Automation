package teddy.minecraftautomation.blocks.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import teddy.minecraftautomation.blocks.FluidPumpBlock;
import teddy.minecraftautomation.utils.ContainerUtils;

public class FluidPumpBlockEntity extends BlockEntityWithFluidStorage {
    private int flowPerTick;
    private int transferCooldown;
    public int inducedPressure;

    public int cooldown = 0;
    public int directionIndex = 0;

    public FluidPumpBlockEntity(BlockPos blockPos, BlockState blockState, int inducedPressure, int flowPerTick, int maxFluidCapacityMb, int transferCooldown) {
        super(ModBlockEntities.FLUID_PUMP_BE, blockPos, blockState, maxFluidCapacityMb);

        this.inducedPressure = inducedPressure;
        this.flowPerTick = flowPerTick;
        this.transferCooldown = transferCooldown;
    }

    public static void tick(Level level, BlockPos blockPos, BlockState state, FluidPumpBlockEntity fluidPumpBlockEntity) {
        if (level.isClientSide() || !(level instanceof ServerLevel serverLevel))
            return;

        // Only transfer items when the cooldown reaches 0
        fluidPumpBlockEntity.cooldown++;
        if (fluidPumpBlockEntity.cooldown >= fluidPumpBlockEntity.transferCooldown)
            fluidPumpBlockEntity.cooldown = 0;
        else
            return;

        FluidPumpBlock fluidPumpBlock = (FluidPumpBlock) state.getBlock();
        Direction[] directions = Direction.values();
        for (int i = 0; i < directions.length; i++) {
            // Used so that it doesn't check the same direction two times in a row if it has other connections
            fluidPumpBlockEntity.directionIndex++;
            fluidPumpBlockEntity.directionIndex %= directions.length;
            Direction dir = directions[fluidPumpBlockEntity.directionIndex];

            boolean success = false;
            if (fluidPumpBlock.getInputDirections(state).contains(dir)) {
                success = ContainerUtils.FluidContainer.handleDirection(dir,
                        serverLevel,
                        blockPos,
                        state,
                        fluidPumpBlockEntity,
                        ContainerUtils.Flow.OUTGOING,
                        fluidPumpBlockEntity.flowPerTick);
            } else if (fluidPumpBlock.getOutputDirections(state).contains(dir)) {
                success = ContainerUtils.FluidContainer.handleDirection(dir,
                        serverLevel,
                        blockPos,
                        state,
                        fluidPumpBlockEntity,
                        ContainerUtils.Flow.INCOMING,
                        fluidPumpBlockEntity.flowPerTick);

            }

            if (success)
                break;
        }
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

    @Override
    protected void saveAdditional(CompoundTag nbt, HolderLookup.Provider provider) {
        writeFluidStorageNbt(this.fluidStorage, nbt, provider);

        nbt.putInt("cooldown", this.cooldown);
        nbt.putInt("transferCooldown", this.transferCooldown);
        nbt.putInt("directionIndex", this.directionIndex);
        nbt.putInt("flowPerTick", this.flowPerTick);
        nbt.putInt("inducedPressure", this.inducedPressure);

        super.saveAdditional(nbt, provider);
    }

    @Override
    protected void loadAdditional(CompoundTag nbt, HolderLookup.Provider provider) {
        super.loadAdditional(nbt, provider);

        this.cooldown = nbt.getInt("cooldown");
        this.transferCooldown = nbt.getInt("transferCooldown");
        this.directionIndex = nbt.getInt("directionIndex");
        this.flowPerTick = nbt.getInt("flowPerTick");
        this.inducedPressure = nbt.getInt("inducedPressure");
    }
}
