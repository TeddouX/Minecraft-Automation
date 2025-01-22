package teddy.minecraftautomation.blocks;

import com.mojang.serialization.MapCodec;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Containers;
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
import teddy.minecraftautomation.blocks.entity.ItemPipeBlockEntity;
import teddy.minecraftautomation.blocks.entity.ItemPumpBlockEntity;
import teddy.minecraftautomation.blocks.entity.ModBlockEntities;
import teddy.minecraftautomation.utils.Tooltip;

import java.util.ArrayList;
import java.util.List;

public class ItemPumpBlock extends AbstractPumpBlock {
    private final int itemsPerTransfer;
    private final int transferCooldown;

    public ItemPumpBlock(int inducedPressure, int itemsPerTransfer, int transferCooldown, Properties properties) {
        super(inducedPressure, properties);

        this.itemsPerTransfer = itemsPerTransfer;
        this.transferCooldown = transferCooldown;
    }

    @Override
    public void appendHoverText(ItemStack itemStack, Item.TooltipContext tooltipContext, List<Component> list, TooltipFlag tooltipFlag) {
        super.appendHoverText(itemStack, tooltipContext, list, tooltipFlag);

        list.add(Component.translatable(ItemPipeBlock.ITEMS_PER_TRANSFER_TOOLTIP.getTranslationKey(), this.itemsPerTransfer).withStyle(ChatFormatting.DARK_GRAY));
        list.add(Component.translatable(ItemPipeBlock.TRANSFER_COOLDOW_TOOLTIP.getTranslationKey(), Tooltip.getSeconds(this.transferCooldown)).withStyle(ChatFormatting.DARK_GRAY));
    }

    @Override
    protected void onRemove(BlockState blockState, Level level, BlockPos blockPos, BlockState blockState2, boolean bl) {
        if (!blockState.is(blockState2.getBlock())) {
            BlockEntity blockEntity = level.getBlockEntity(blockPos);
            if (blockEntity instanceof ItemPumpBlockEntity itemPumpBlockEntity) {
                if (level instanceof ServerLevel) {
                    Containers.dropContents(level, blockPos, itemPumpBlockEntity);
                }

                super.onRemove(blockState, level, blockPos, blockState2, bl);
                level.updateNeighbourForOutputSignal(blockPos, this);
            } else {
                super.onRemove(blockState, level, blockPos, blockState2, bl);
            }
        }
    }

    @Override
    protected @NotNull MapCodec<? extends BaseEntityBlock> codec() {
        return simpleCodec((Properties properties) -> new ItemPumpBlock(0, 0, 0, properties));
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new ItemPumpBlockEntity(blockPos, blockState, this.inducedPressure, this.itemsPerTransfer, this.transferCooldown);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        return level.isClientSide() ? null : BaseEntityBlock.createTickerHelper(blockEntityType, ModBlockEntities.ITEM_PUMP_BE, ItemPumpBlockEntity::tick);
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
