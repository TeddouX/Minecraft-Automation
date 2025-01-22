package teddy.minecraftautomation.blocks;

import com.mojang.serialization.MapCodec;
import net.minecraft.ChatFormatting;
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
import teddy.minecraftautomation.MinecraftAutomation;
import teddy.minecraftautomation.blocks.entity.FluidPumpBlockEntity;
import teddy.minecraftautomation.blocks.entity.ModBlockEntities;
import teddy.minecraftautomation.utils.Tooltip;

import java.util.ArrayList;
import java.util.List;

public class FluidPumpBlock extends AbstractPumpBlock {
    private final int transferCooldown;
    private final int flowPerTick;
    private final int maxFluidCapacityMb;

    public FluidPumpBlock(int inducedPressure, int flowPerTick, int maxFluidCapacityMb, int transferCooldown, Properties properties) {
        super(inducedPressure, properties);

        this.transferCooldown = transferCooldown;
        this.flowPerTick = flowPerTick;
        this.maxFluidCapacityMb = maxFluidCapacityMb;
    }

    @Override
    public void appendHoverText(ItemStack itemStack, Item.TooltipContext tooltipContext, List<Component> list, TooltipFlag tooltipFlag) {
        super.appendHoverText(itemStack, tooltipContext, list, tooltipFlag);

        list.add(Component.translatable(FluidPipeBlock.MB_PER_TRANSFER_TOOLTIP.getTranslationKey(), this.flowPerTick).withStyle(ChatFormatting.DARK_GRAY));
        list.add(Component.translatable(FluidPipeBlock.FLUID_CAPACITY_TOOLTIP.getTranslationKey(), this.maxFluidCapacityMb).withStyle(ChatFormatting.DARK_GRAY));
        list.add(Component.translatable(ItemPipeBlock.TRANSFER_COOLDOW_TOOLTIP.getTranslationKey(), Tooltip.getSeconds(this.transferCooldown)).withStyle(ChatFormatting.DARK_GRAY));
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
