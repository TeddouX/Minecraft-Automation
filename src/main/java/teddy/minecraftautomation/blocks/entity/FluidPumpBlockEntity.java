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
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import teddy.minecraftautomation.blocks.FluidPumpBlock;
import teddy.minecraftautomation.utils.ContainerUtils;
import teddy.minecraftautomation.utils.ImplementedFluidStorage;

public class FluidPumpBlockEntity extends BlockEntity implements ImplementedFluidStorage {
    private int flowPerTick;
    private int transferCooldown;
    public int inducedPressure;

    public int cooldown = 0;
    public int directionIndex = 0;

    public SingleVariantStorage<FluidVariant> fluidStorage;

    public FluidPumpBlockEntity(BlockPos blockPos, BlockState blockState, int inducedPressure, int flowPerTick, int maxFluidCapacityMb, int transferCooldown) {
        super(ModBlockEntities.FLUID_PUMP_BE, blockPos, blockState);

        this.inducedPressure = inducedPressure;
        this.flowPerTick = flowPerTick;
        this.transferCooldown = transferCooldown;

        this.fluidStorage = initializeFluidStorage(maxFluidCapacityMb, this::markDirty);
    }

    public static void tick(World level, BlockPos blockPos, BlockState state, FluidPumpBlockEntity fluidPumpBlockEntity) {
        if (level.isClient() || !(level instanceof ServerWorld serverLevel))
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
        writeFluidStorageNbt(nbt, provider);

        nbt.putInt("cooldown", this.cooldown);
        nbt.putInt("transferCooldown", this.transferCooldown);
        nbt.putInt("directionIndex", this.directionIndex);
        nbt.putInt("flowPerTick", this.flowPerTick);
        nbt.putInt("inducedPressure", this.inducedPressure);

        super.writeNbt(nbt, provider);
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup provider) {
        super.readNbt(nbt, provider);

        this.fluidStorage = readFluidStorageNbt(nbt, provider, this::markDirty);

        this.cooldown = nbt.getInt("cooldown");
        this.transferCooldown = nbt.getInt("transferCooldown");
        this.directionIndex = nbt.getInt("directionIndex");
        this.flowPerTick = nbt.getInt("flowPerTick");
        this.inducedPressure = nbt.getInt("inducedPressure");
    }

    @Override
    public SingleVariantStorage<FluidVariant> getFluidStorage() {
        return this.fluidStorage;
    }
}
