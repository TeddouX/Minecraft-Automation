package teddy.minecraftautomation.blocks;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import teddy.minecraftautomation.blocks.entity.FluidPumpBlockEntity;
import teddy.minecraftautomation.blocks.entity.ModBlockEntities;

import java.util.ArrayList;
import java.util.List;

public class FluidPumpBlock extends AbstractPumpBlock {
    private final int transferCooldown;
    private final int flowPerTick;
    private final int inducedPressure;
    private final int maxFluidCapacityMb;

    public FluidPumpBlock(int inducedPressure, int flowPerTick, int maxFluidCapacityMb, int transferCooldown, Properties properties) {
        super(properties);

        this.transferCooldown = transferCooldown;
        this.flowPerTick = flowPerTick;
        this.inducedPressure = inducedPressure;
        this.maxFluidCapacityMb = maxFluidCapacityMb;
    }

    @Override
    public void appendHoverText(ItemStack itemStack, Item.TooltipContext tooltipContext, List<Component> list, TooltipFlag tooltipFlag) {

    }

    @Override
    protected @NotNull MapCodec<? extends BaseEntityBlock> codec() {
        return simpleCodec((Properties properties) -> new FluidPumpBlock(0, 0, 0, 0, properties));
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new FluidPumpBlockEntity(blockPos, blockState, this.inducedPressure, this.flowPerTick, this.maxFluidCapacityMb, this.transferCooldown);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        return level.isClientSide() ? null : BaseEntityBlock.createTickerHelper(blockEntityType, ModBlockEntities.FLUID_PUMP_BE, FluidPumpBlockEntity::tick);
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
    public ArrayList<Direction> getFluidContainerInputDirections(@NotNull BlockState blockState) {
        return this.getInputDirections(blockState);
    }

    @Override
    public ArrayList<Direction> getFluidContainerOutputDirections(@NotNull BlockState blockState) {
        return this.getOutputDirections(blockState);
    }
}
