package teddy.minecraftautomation.blocks;

import com.mojang.serialization.MapCodec;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import teddy.minecraftautomation.blocks.entity.FluidPipeBlockEntity;
import teddy.minecraftautomation.blocks.entity.ModBlockEntities;
import teddy.minecraftautomation.utils.OrientedContainer;

import java.util.ArrayList;
import java.util.List;

public class FluidPipeBlock extends AbstractPipeBlock {
    private final int flowPerTick;
    private final int maxFluidCapacityMb;
    private final int transferCooldown;

    public FluidPipeBlock(int maxPressure, int flowPerTick, int maxFluidCapacityMb, int transferCooldown, Properties properties) {
        super(maxPressure, properties);

        this.flowPerTick = flowPerTick;
        this.maxFluidCapacityMb = maxFluidCapacityMb;
        this.transferCooldown = transferCooldown;
    }

    @Override
    boolean canConnect(Level world, BlockPos pos, Direction direction) {
        BlockState state = world.getBlockState(pos);
        Block block = state.getBlock();

        // Fluid pipe or fluid pump or any other class that implements OrientedInventory
        if (block instanceof OrientedContainer orientedContainer)
            return orientedContainer.getFluidContainerInputDirections(state).contains(direction) ||
                    orientedContainer.getFluidContainerOutputDirections(state).contains(direction);

        Storage<FluidVariant> storage = FluidStorage.SIDED.find(world, pos, direction);

        return storage != null && (storage.supportsExtraction() || storage.supportsInsertion());
    }

    @Override
    public void appendHoverText(ItemStack itemStack, Item.TooltipContext tooltipContext, List<Component> list, TooltipFlag tooltipFlag) {
        // ----------------------------- //
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new FluidPipeBlockEntity(blockPos, blockState, this.maxPressure, this.flowPerTick, this.maxFluidCapacityMb, this.transferCooldown);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        return level.isClientSide() ? null : BaseEntityBlock.createTickerHelper(blockEntityType, ModBlockEntities.FLUID_PIPE_BE, FluidPipeBlockEntity::tick);
    }

    @Override
    protected @NotNull MapCodec<? extends BaseEntityBlock> codec() {
        return simpleCodec((Properties properties) -> new FluidPipeBlock(0, 0, 0, 0, properties));
    }

    @Override
    public ArrayList<Direction> getItemInventoryInputDirections(BlockState blockState) {
        return new ArrayList<>();
    }

    @Override
    public ArrayList<Direction> getItemInventoryOutputDirections(BlockState blockState) {
        return new ArrayList<>();
    }

    @Override
    public ArrayList<Direction> getFluidContainerInputDirections(BlockState blockState) {
        return new ArrayList<>(List.of(Direction.values()));
    }

    @Override
    public ArrayList<Direction> getFluidContainerOutputDirections(BlockState blockState) {
        return new ArrayList<>(List.of(Direction.values()));
    }
}
