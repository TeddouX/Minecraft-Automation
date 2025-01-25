package teddy.minecraftautomation.blocks;

import com.mojang.serialization.MapCodec;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import teddy.minecraftautomation.blocks.entity.FluidTankBlockEntity;
import teddy.minecraftautomation.utils.OrientedContainer;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class FluidTankBlock extends BlockWithEntity implements OrientedContainer {
    private final int maxFluidCapacityMb;

    public FluidTankBlock(int maxFluidCapacityMb, Settings properties) {
        super(properties);

        this.maxFluidCapacityMb = maxFluidCapacityMb;
    }

    @Override
    public void appendTooltip(ItemStack itemStack, Item.TooltipContext tooltipContext, List<Text> list, TooltipType tooltipFlag) {
        list.add(Text.translatable(AbstractPipeBlock.STATS_TOOLTIP.getTranslationKey()).formatted(Formatting.DARK_GRAY));
        list.add(Text.translatable(FluidPipeBlock.FLUID_CAPACITY_TOOLTIP.getTranslationKey(), this.maxFluidCapacityMb).formatted(Formatting.DARK_GRAY));
    }

    @Override
    protected @NotNull MapCodec<? extends BlockWithEntity> getCodec() {
        return createCodec((properties) -> new FluidTankBlock(0, properties));
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new FluidTankBlockEntity(blockPos, blockState, this.maxFluidCapacityMb);
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
