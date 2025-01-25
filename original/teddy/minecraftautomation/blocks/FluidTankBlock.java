package teddy.minecraftautomation.blocks;

import com.mojang.serialization.MapCodec;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import teddy.minecraftautomation.blocks.entity.FluidTankBlockEntity;
import teddy.minecraftautomation.utils.OrientedContainer;

import java.util.ArrayList;
import java.util.List;

public class FluidTankBlock extends BaseEntityBlock implements OrientedContainer {
    private final int maxFluidCapacityMb;

    public FluidTankBlock(int maxFluidCapacityMb, Properties properties) {
        super(properties);

        this.maxFluidCapacityMb = maxFluidCapacityMb;
    }

    @Override
    public void appendHoverText(ItemStack itemStack, Item.TooltipContext tooltipContext, List<Component> list, TooltipFlag tooltipFlag) {
        list.add(Component.translatable(AbstractPipeBlock.STATS_TOOLTIP.getTranslationKey()).withStyle(ChatFormatting.DARK_GRAY));
        list.add(Component.translatable(FluidPipeBlock.FLUID_CAPACITY_TOOLTIP.getTranslationKey(), this.maxFluidCapacityMb).withStyle(ChatFormatting.DARK_GRAY));
    }

    @Override
    protected @NotNull MapCodec<? extends BaseEntityBlock> codec() {
        return simpleCodec((properties) -> new FluidTankBlock(0, properties));
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
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
