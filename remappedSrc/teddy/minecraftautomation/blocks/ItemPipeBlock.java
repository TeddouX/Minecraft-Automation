package teddy.minecraftautomation.blocks;

import com.mojang.serialization.MapCodec;
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
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
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import teddy.minecraftautomation.MinecraftAutomation;
import teddy.minecraftautomation.blocks.entity.ItemPipeBlockEntity;
import teddy.minecraftautomation.blocks.entity.ModBlockEntities;
import teddy.minecraftautomation.utils.OrientedContainer;
import teddy.minecraftautomation.utils.Tooltip;
import teddy.minecraftautomation.utils.WrenchableBlock;

import java.util.ArrayList;
import java.util.List;

public class ItemPipeBlock extends AbstractPipeBlock implements WrenchableBlock {
    private final int itemsPerTransfer;
    private final int transferCooldown;
    private final DefaultedList<Direction> wrenchedDirections = DefaultedList.of();

    public static final Tooltip ITEMS_PER_TRANSFER_TOOLTIP = new Tooltip(MinecraftAutomation.MOD_ID, "item_pipe", "items_per_transfer", "Items per transfer: %s");
    public static final Tooltip TRANSFER_COOLDOW_TOOLTIP = new Tooltip(MinecraftAutomation.MOD_ID, "item_pipe", "transfer_cooldown", "Transfer cooldown: %ss");

    public ItemPipeBlock(int maxPressure, int itemsPerTransfer, int transferCooldown, Settings properties) {
        super(maxPressure, properties);

        this.itemsPerTransfer = itemsPerTransfer;
        this.transferCooldown = transferCooldown;
    }

    @Override
    public void appendTooltip(ItemStack itemStack, Item.TooltipContext tooltipContext, List<Text> list, TooltipType tooltipFlag) {
        super.appendTooltip(itemStack, tooltipContext, list, tooltipFlag);

        list.add(Text.translatable(ITEMS_PER_TRANSFER_TOOLTIP.getTranslationKey(), this.itemsPerTransfer).formatted(Formatting.DARK_GRAY));
        list.add(Text.translatable(TRANSFER_COOLDOW_TOOLTIP.getTranslationKey(), Tooltip.getSeconds(this.transferCooldown)).formatted(Formatting.DARK_GRAY));
    }

    @Override
    boolean canConnect(World world, BlockPos pos, Direction direction) {
        BlockState state = world.getBlockState(pos);
        Block block = state.getBlock();

        // Item pipe or item pump or any other class that implements OrientedInventory
        if (block instanceof OrientedContainer orientedContainer) {
            boolean b = orientedContainer.getItemInventoryInputDirections(state).contains(direction) ||
                    orientedContainer.getItemInventoryOutputDirections(state).contains(direction);

            if (block instanceof WrenchableBlock) {
                return b && !state.get(getWrenchedPropertyFromDirection(direction.getOpposite()));
            }

            return b;
        }

        // If the block is a storage
        Storage<ItemVariant> storage = ItemStorage.SIDED.find(world, pos, direction);

        return storage != null && (storage.supportsExtraction() || storage.supportsInsertion());
    }

    @Override
    protected void onStateReplaced(BlockState blockState, World level, BlockPos blockPos, BlockState blockState2, boolean bl) {
        if (!blockState.isOf(blockState2.getBlock())) {
            BlockEntity blockEntity = level.getBlockEntity(blockPos);
            if (blockEntity instanceof ItemPipeBlockEntity itemPipeBlockEntity) {
                if (level instanceof ServerWorld) {
                    ItemScatterer.spawn(level, blockPos, itemPipeBlockEntity);
                }

                super.onStateReplaced(blockState, level, blockPos, blockState2, bl);
                level.updateComparators(blockPos, this);
            } else {
                super.onStateReplaced(blockState, level, blockPos, blockState2, bl);
            }
        }
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new ItemPipeBlockEntity(blockPos, blockState, this.itemsPerTransfer, this.transferCooldown, this.maxPressure);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(World level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        return level.isClient() ? null : BlockWithEntity.validateTicker(blockEntityType, ModBlockEntities.ITEM_PIPE_BE, ItemPipeBlockEntity::tick);
    }

    @Override
    protected @NotNull MapCodec<? extends BlockWithEntity> getCodec() {
        return createCodec((Settings properties) -> new ItemPipeBlock(0, 0, 0, properties)); // Maybe useful in a later minecraft version
    }

    @Override
    public ArrayList<Direction> getItemInventoryInputDirections(BlockState blockState) {
        return this.getInputAndOutputDirections();
    }

    @Override
    public ArrayList<Direction> getItemInventoryOutputDirections(BlockState blockState) {
        return this.getInputAndOutputDirections();
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
