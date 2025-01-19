package teddy.minecraftautomation.blocks.entity;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleVariantStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import teddy.minecraftautomation.blocks.FluidPumpBlock;
import teddy.minecraftautomation.utils.ContainerUtils;
import teddy.minecraftautomation.utils.ImplementedFluidStorage;

public class FluidPumpBlockEntity extends BlockEntity implements ImplementedFluidStorage {
    private int flowPerTick;
    private int transferCooldown;
    private int maxFluidCapacityMb;
    public int inducedPressure;

    public int cooldown = 0;
    public int directionIndex = 0;

    public SingleVariantStorage<FluidVariant> fluidStorage;

    public FluidPumpBlockEntity(BlockPos blockPos, BlockState blockState, int inducedPressure, int flowPerTick, int maxFluidCapacityMb, int transferCooldown) {
        super(ModBlockEntities.FLUID_PUMP_BE, blockPos, blockState);

        this.inducedPressure = inducedPressure;
        this.flowPerTick = flowPerTick;
        this.maxFluidCapacityMb = maxFluidCapacityMb;
        this.transferCooldown = transferCooldown;

        this.fluidStorage = initializeFluidStorage(maxFluidCapacityMb, this);
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
    protected void saveAdditional(CompoundTag nbt, HolderLookup.Provider provider) {
        writeFluidStorageNbt(this.fluidStorage, nbt, provider);

        nbt.putInt("cooldown", this.cooldown);
        nbt.putInt("transferCooldown", this.transferCooldown);
        nbt.putInt("directionIndex", this.directionIndex);
        nbt.putInt("flowPerTick", this.flowPerTick);
        nbt.putInt("maxFluidCapacityMb", this.maxFluidCapacityMb);
        nbt.putInt("inducedPressure", this.inducedPressure);

        super.saveAdditional(nbt, provider);
    }

    @Override
    protected void loadAdditional(CompoundTag nbt, HolderLookup.Provider provider) {
        super.loadAdditional(nbt, provider);

        this.fluidStorage = readFluidStorageNbt(nbt, provider, this);

        this.cooldown = nbt.getInt("cooldown");
        this.transferCooldown = nbt.getInt("transferCooldown");
        this.directionIndex = nbt.getInt("directionIndex");
        this.maxFluidCapacityMb = nbt.getInt("maxFluidCapacityMb");
        this.flowPerTick = nbt.getInt("flowPerTick");
        this.inducedPressure = nbt.getInt("inducedPressure");
    }
}
