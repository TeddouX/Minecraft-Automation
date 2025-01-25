package teddy.minecraftautomation.blocks;

import com.mojang.serialization.MapCodec;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import teddy.minecraftautomation.blocks.entity.FluidPumpBlockEntity;
import teddy.minecraftautomation.blocks.entity.ModBlockEntities;
import teddy.minecraftautomation.utils.Tooltip;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class FluidPumpBlock extends AbstractPumpBlock {
    private final int transferCooldown;
    private final int flowPerTick;
    private final int maxFluidCapacityMb;

    public FluidPumpBlock(int inducedPressure, int flowPerTick, int maxFluidCapacityMb, int transferCooldown, Settings properties) {
        super(inducedPressure, properties);

        this.transferCooldown = transferCooldown;
        this.flowPerTick = flowPerTick;
        this.maxFluidCapacityMb = maxFluidCapacityMb;
    }

    @Override
    public void appendTooltip(ItemStack itemStack, Item.TooltipContext tooltipContext, List<Text> list, TooltipType tooltipFlag) {
        super.appendTooltip(itemStack, tooltipContext, list, tooltipFlag);

        list.add(Text.translatable(FluidPipeBlock.MB_PER_TRANSFER_TOOLTIP.getTranslationKey(), this.flowPerTick).formatted(Formatting.DARK_GRAY));
        list.add(Text.translatable(FluidPipeBlock.FLUID_CAPACITY_TOOLTIP.getTranslationKey(), this.maxFluidCapacityMb).formatted(Formatting.DARK_GRAY));
        list.add(Text.translatable(ItemPipeBlock.TRANSFER_COOLDOW_TOOLTIP.getTranslationKey(), Tooltip.getSeconds(this.transferCooldown)).formatted(Formatting.DARK_GRAY));
    }

    @Override
    protected @NotNull MapCodec<? extends BlockWithEntity> getCodec() {
        return createCodec((Settings properties) -> new FluidPumpBlock(0, 0, 0, 0, properties));
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new FluidPumpBlockEntity(blockPos, blockState, this.inducedPressure, this.flowPerTick, this.maxFluidCapacityMb, this.transferCooldown);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(World level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        return level.isClient() ? null : BlockWithEntity.validateTicker(blockEntityType, ModBlockEntities.FLUID_PUMP_BE, FluidPumpBlockEntity::tick);
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
