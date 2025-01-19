package teddy.minecraftautomation.blocks.entity;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleVariantStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import teddy.minecraftautomation.MinecraftAutomation;
import teddy.minecraftautomation.blocks.AbstractPipeBlock;
import teddy.minecraftautomation.blocks.FluidPipeBlock;
import teddy.minecraftautomation.blocks.FluidPumpBlock;
import teddy.minecraftautomation.utils.ContainerUtils;
import teddy.minecraftautomation.utils.ImplementedFluidStorage;

public class FluidPipeBlockEntity extends BlockEntity implements ImplementedFluidStorage {
    private int maxPressure;
    private int flowPerTick;
    private int transferCooldown;
    private int maxFluidCapacityMb;

    public int cooldown = 0;
    public int directionIndex = 0;
    public int pressure = 0;

    public SingleVariantStorage<FluidVariant> fluidStorage;

    public FluidPipeBlockEntity(BlockPos blockPos, BlockState blockState, int maxPressure, int flowPerTick, int maxFluidCapacityMb, int transferCooldown) {
        super(ModBlockEntities.FLUID_PIPE_BE, blockPos, blockState);

        this.maxPressure = maxPressure;
        this.flowPerTick = flowPerTick;
        this.maxFluidCapacityMb = maxFluidCapacityMb;
        this.transferCooldown = transferCooldown;

        this.fluidStorage = initializeFluidStorage(maxFluidCapacityMb, this);
    }

    public static void tick(Level level, BlockPos blockPos, BlockState state, FluidPipeBlockEntity fluidPipeBlockEntity) {
        if (level.isClientSide() || !(level instanceof ServerLevel serverLevel))
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

        Direction[] directions = Direction.values();
        for (int i = 0; i < directions.length; i++) {
            // Used so that it doesn't check the same direction two times in a row if it has other connections
            fluidPipeBlockEntity.directionIndex++;
            fluidPipeBlockEntity.directionIndex %= directions.length;

            boolean success = ContainerUtils.FluidContainer.handleDirection(
                    directions[fluidPipeBlockEntity.directionIndex],
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

    static int getPressureAmountForBlock(Level level, BlockState state, BlockPos pos) {
        Direction[] directions = Direction.values();
        BlockEntity blockEntity = level.getBlockEntity(pos);

        // Sanity checks
        if (!(state.getBlock() instanceof FluidPipeBlock) || !(blockEntity instanceof FluidPipeBlockEntity fluidPipeBlockEntity))
            return 0;

        int maxPressure = 0;
        for (Direction dir : directions) {
            BlockState relativeBlockState = level.getBlockState(pos.relative(dir));
            BlockEntity relativeBlockEntity = level.getBlockEntity(pos.relative(dir));

            // If the other block is an item pump and the pipe is connected to its input
            if (relativeBlockState.getBlock() instanceof FluidPumpBlock fluidPumpBlock
                    && state.getValue(AbstractPipeBlock.getFacingPropertyFromDirection(dir))
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
        // Store the fluidStorage
        writeFluidStorageNbt(this.fluidStorage, nbt, provider);

        nbt.putInt("maxPressure", maxPressure);
        nbt.putInt("flowPerTick", flowPerTick);
        nbt.putInt("maxFluidCapacityMb", maxFluidCapacityMb);
        nbt.putInt("cooldown", cooldown);
        nbt.putInt("transferCooldown", transferCooldown);
        nbt.putInt("directionIndex", directionIndex);
        nbt.putInt("pressure", pressure);

        super.saveAdditional(nbt, provider);
    }

    @Override
    protected void loadAdditional(CompoundTag nbt, HolderLookup.Provider provider) {
        super.loadAdditional(nbt, provider);

        // Load the fluidStorage
        this.fluidStorage = readFluidStorageNbt(nbt, provider, this);

        this.maxPressure = nbt.getInt("maxPressure");
        this.flowPerTick = nbt.getInt("flowPerTick");
        this.maxFluidCapacityMb = nbt.getInt("maxFluidCapacityMb");
        this.cooldown = nbt.getInt("cooldown");
        this.transferCooldown = nbt.getInt("transferCooldown");
        this.directionIndex = nbt.getInt("directionIndex");
        this.pressure = nbt.getInt("pressure");
    }
}
