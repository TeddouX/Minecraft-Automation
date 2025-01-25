package teddy.minecraftautomation.blocks;

import com.mojang.serialization.MapCodec;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.minecraft.block.Block;
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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import teddy.minecraftautomation.MinecraftAutomation;
import teddy.minecraftautomation.blocks.entity.FluidPipeBlockEntity;
import teddy.minecraftautomation.blocks.entity.ModBlockEntities;
import teddy.minecraftautomation.utils.OrientedContainer;
import teddy.minecraftautomation.utils.Tooltip;
import teddy.minecraftautomation.utils.WrenchableBlock;

import java.util.ArrayList;
import java.util.List;

public class FluidPipeBlock extends AbstractPipeBlock implements WrenchableBlock {
    private final int flowPerTick;
    private final int maxFluidCapacityMb;
    private final int transferCooldown;

    public static final Tooltip MB_PER_TRANSFER_TOOLTIP = new Tooltip(MinecraftAutomation.MOD_ID, "fluid_pipe", "mb_per_transfer", "mB per transfer: %smB");
    public static final Tooltip FLUID_CAPACITY_TOOLTIP = new Tooltip(MinecraftAutomation.MOD_ID, "fluid_pipe", "fluid_capacity", "Fluid capacity: %smB");

    public FluidPipeBlock(int maxPressure, int flowPerTick, int maxFluidCapacityMb, int transferCooldown, Settings properties) {
        super(maxPressure, properties);

        this.flowPerTick = flowPerTick;
        this.maxFluidCapacityMb = maxFluidCapacityMb;
        this.transferCooldown = transferCooldown;
    }

    @Override
    boolean canConnect(World world, BlockPos pos, Direction direction) {
        BlockState state = world.getBlockState(pos);
        Block block = state.getBlock();

        // Fluid pipe or fluid pump or any other class that implements OrientedInventory
        if (block instanceof OrientedContainer orientedContainer) {
            boolean b = orientedContainer.getFluidContainerInputDirections(state).contains(direction) ||
                    orientedContainer.getFluidContainerOutputDirections(state).contains(direction);

            if (block instanceof WrenchableBlock) {
                return b && !state.get(getWrenchedPropertyFromDirection(direction.getOpposite()));
            }

            return b;
        }

        Storage<FluidVariant> storage = FluidStorage.SIDED.find(world, pos, direction);

        return storage != null && (storage.supportsExtraction() || storage.supportsInsertion());
    }

    @Override
    public void appendTooltip(ItemStack itemStack, Item.TooltipContext tooltipContext, List<Text> list, TooltipType tooltipFlag) {
        super.appendTooltip(itemStack, tooltipContext, list, tooltipFlag);

        list.add(Text.translatable(MB_PER_TRANSFER_TOOLTIP.getTranslationKey(), this.flowPerTick).formatted(Formatting.DARK_GRAY));
        list.add(Text.translatable(FLUID_CAPACITY_TOOLTIP.getTranslationKey(), this.maxFluidCapacityMb).formatted(Formatting.DARK_GRAY));
        list.add(Text.translatable(ItemPipeBlock.TRANSFER_COOLDOW_TOOLTIP.getTranslationKey(), Tooltip.getSeconds(this.transferCooldown)).formatted(Formatting.DARK_GRAY));
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new FluidPipeBlockEntity(blockPos, blockState, this.maxPressure, this.flowPerTick, this.maxFluidCapacityMb, this.transferCooldown);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(World level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        return level.isClient() ? null : BlockWithEntity.validateTicker(blockEntityType, ModBlockEntities.FLUID_PIPE_BE, FluidPipeBlockEntity::tick);
    }

    @Override
    protected @NotNull MapCodec<? extends BlockWithEntity> getCodec() {
        return createCodec((Settings properties) -> new FluidPipeBlock(0, 0, 0, 0, properties));
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
