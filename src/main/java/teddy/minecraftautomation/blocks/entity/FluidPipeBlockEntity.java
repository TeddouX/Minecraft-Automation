package teddy.minecraftautomation.blocks.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import teddy.minecraftautomation.blocks.AbstractPipeBlock;
import teddy.minecraftautomation.blocks.FluidPipeBlock;
import teddy.minecraftautomation.blocks.FluidPumpBlock;
import teddy.minecraftautomation.utils.ContainerUtils;

import java.util.ArrayList;

public class FluidPipeBlockEntity extends BlockEntityWithFluidStorage {
    private int maxPressure;
    private int flowPerTick;
    private int transferCooldown;
    public int cooldown = 0;
    public int directionIndex = 0;
    public int pressure = 0;

    public FluidPipeBlockEntity(BlockPos blockPos, BlockState blockState, int maxPressure, int flowPerTick, int maxFluidCapacityMb, int transferCooldown) {
        super(ModBlockEntities.FLUID_PIPE_BE, blockPos, blockState, maxFluidCapacityMb);

        this.maxPressure = maxPressure;
        this.flowPerTick = flowPerTick;
        this.transferCooldown = transferCooldown;
    }

    public static void tick(World level, BlockPos blockPos, BlockState state, FluidPipeBlockEntity fluidPipeBlockEntity) {
        if (level.isClient() || !(level instanceof ServerWorld serverLevel))
            return;

        // Update pressure and clamp it to the maxPressure
        fluidPipeBlockEntity.pressure = Math.min(getPressureAmountForBlock(level, state, blockPos), fluidPipeBlockEntity.maxPressure);

        if (fluidPipeBlockEntity.pressure <= 0) return;

        // Only transfer items when the cooldown reaches 0
        fluidPipeBlockEntity.cooldown++;
        if (fluidPipeBlockEntity.cooldown >= fluidPipeBlockEntity.transferCooldown)
            fluidPipeBlockEntity.cooldown = 0;
        else
            return;

        ArrayList<Direction> directions = AbstractPipeBlock.getConnectionsFromBlockState(state);

        if (directions == null) return;

        for (int i = 0; i < directions.size(); i++) {
            // Used so that it doesn't check the same direction two times in a row if it has other connections
            fluidPipeBlockEntity.directionIndex++;
            fluidPipeBlockEntity.directionIndex %= directions.size();

            boolean success = ContainerUtils.FluidContainer.handleDirection(
                    directions.get(fluidPipeBlockEntity.directionIndex),
                    serverLevel,
                    blockPos,
                    state,
                    fluidPipeBlockEntity,
                    ContainerUtils.Flow.OUTGOING,
                    fluidPipeBlockEntity.flowPerTick);

            if (success)
                break;
        }
    }

    static int getPressureAmountForBlock(World level, BlockState state, BlockPos pos) {
        Direction[] directions = Direction.values();
        BlockEntity blockEntity = level.getBlockEntity(pos);

        // Sanity checks
        if (!(state.getBlock() instanceof FluidPipeBlock) || !(blockEntity instanceof FluidPipeBlockEntity fluidPipeBlockEntity))
            return 0;

        int maxPressure = 0;
        for (Direction dir : directions) {
            BlockState relativeBlockState = level.getBlockState(pos.offset(dir));
            BlockEntity relativeBlockEntity = level.getBlockEntity(pos.offset(dir));

            // If the other block is an item pump and the pipe is connected to its output
            if (relativeBlockState.getBlock() instanceof FluidPumpBlock fluidPumpBlock
                    && state.get(AbstractPipeBlock.getFacingPropertyFromDirection(dir))
                    && fluidPumpBlock.getOutputDirections(relativeBlockState).contains(dir)) {

                if (!(relativeBlockEntity instanceof FluidPumpBlockEntity fluidPumpBlockEntity))
                    continue;

                maxPressure = Math.max(fluidPumpBlockEntity.inducedPressure, maxPressure);
            } else if (relativeBlockState.getBlock() instanceof FluidPipeBlock) {
                if (!(relativeBlockEntity instanceof FluidPipeBlockEntity relativeFluidPipeBlockEntity))
                    continue;

                maxPressure = Math.max(relativeFluidPipeBlockEntity.pressure - 1, maxPressure);
            }
        }

        return Math.min(fluidPipeBlockEntity.maxPressure, maxPressure);
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

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup provider) {
        nbt.putInt("maxPressure", maxPressure);
        nbt.putInt("flowPerTick", flowPerTick);
        nbt.putInt("cooldown", cooldown);
        nbt.putInt("transferCooldown", transferCooldown);
        nbt.putInt("directionIndex", directionIndex);
        nbt.putInt("pressure", pressure);

        super.writeNbt(nbt, provider);
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup provider) {
        super.readNbt(nbt, provider);

        this.maxPressure = nbt.getInt("maxPressure");
        this.flowPerTick = nbt.getInt("flowPerTick");
        this.cooldown = nbt.getInt("cooldown");
        this.transferCooldown = nbt.getInt("transferCooldown");
        this.directionIndex = nbt.getInt("directionIndex");
        this.pressure = nbt.getInt("pressure");
    }
}
