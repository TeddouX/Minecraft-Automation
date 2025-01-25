package teddy.minecraftautomation.blocks;

import com.mojang.serialization.MapCodec;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import teddy.minecraftautomation.blocks.entity.ItemPumpBlockEntity;
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
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class ItemPumpBlock extends AbstractPumpBlock {
    private final int itemsPerTransfer;
    private final int transferCooldown;

    public ItemPumpBlock(int inducedPressure, int itemsPerTransfer, int transferCooldown, Settings properties) {
        super(inducedPressure, properties);

        this.itemsPerTransfer = itemsPerTransfer;
        this.transferCooldown = transferCooldown;
    }

    @Override
    public void appendTooltip(ItemStack itemStack, Item.TooltipContext tooltipContext, List<Text> list, TooltipType tooltipFlag) {
        super.appendTooltip(itemStack, tooltipContext, list, tooltipFlag);

        list.add(Text.translatable(ItemPipeBlock.ITEMS_PER_TRANSFER_TOOLTIP.getTranslationKey(), this.itemsPerTransfer).formatted(Formatting.DARK_GRAY));
        list.add(Text.translatable(ItemPipeBlock.TRANSFER_COOLDOW_TOOLTIP.getTranslationKey(), Tooltip.getSeconds(this.transferCooldown)).formatted(Formatting.DARK_GRAY));
    }

    @Override
    protected void onStateReplaced(BlockState blockState, World level, BlockPos blockPos, BlockState blockState2, boolean bl) {
        if (!blockState.isOf(blockState2.getBlock())) {
            BlockEntity blockEntity = level.getBlockEntity(blockPos);
            if (blockEntity instanceof ItemPumpBlockEntity itemPumpBlockEntity) {
                if (level instanceof ServerWorld) {
                    ItemScatterer.spawn(level, blockPos, itemPumpBlockEntity);
                }

                super.onStateReplaced(blockState, level, blockPos, blockState2, bl);
                level.updateComparators(blockPos, this);
            } else {
                super.onStateReplaced(blockState, level, blockPos, blockState2, bl);
            }
        }
    }

    @Override
    protected @NotNull MapCodec<? extends BlockWithEntity> getCodec() {
        return createCodec((Settings properties) -> new ItemPumpBlock(0, 0, 0, properties));
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new ItemPumpBlockEntity(blockPos, blockState, this.inducedPressure, this.itemsPerTransfer, this.transferCooldown);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        return level.isClient() ? null : BlockWithEntity.validateTicker(blockEntityType, ModBlockEntities.ITEM_PUMP_BE, ItemPumpBlockEntity::tick);
    }

    @Override
    public ArrayList<Direction> getItemInventoryInputDirections(BlockState blockState) {
        return this.getInputDirections(blockState);
    }

    @Override
    public ArrayList<Direction> getItemInventoryOutputDirections(BlockState blockState) {
        return this.getOutputDirections(blockState);
    }

    @Override
    public ArrayList<Direction> getFluidContainerInputDirections(BlockState blockState) {
        return new ArrayList<>();
    }

    @Override
    public ArrayList<Direction> getFluidContainerOutputDirections(BlockState blockState) {
        return new ArrayList<>();
    }
}
