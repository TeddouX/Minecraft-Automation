package teddy.minecraftautomation.blocks;

import com.mojang.serialization.MapCodec;
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.minecraft.ChatFormatting;
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
import teddy.minecraftautomation.MinecraftAutomation;
import teddy.minecraftautomation.blocks.entity.ItemPipeBlockEntity;
import teddy.minecraftautomation.blocks.entity.ModBlockEntities;
import teddy.minecraftautomation.utils.OrientedContainer;
import teddy.minecraftautomation.utils.Tooltip;

import java.util.ArrayList;
import java.util.List;

public class ItemPipeBlock extends AbstractPipeBlock {
    private final int itemsPerTransfer;
    private final int transferCooldown;

    public static final Tooltip ITEMS_PER_TRANSFER_TOOLTIP = new Tooltip(MinecraftAutomation.MOD_ID, "item_pipe", "items_per_transfer", "Items per transfer: %s");
    public static final Tooltip TRANSFER_COOLDOW_TOOLTIP = new Tooltip(MinecraftAutomation.MOD_ID, "item_pipe", "transfer_cooldown", "Transfer cooldown: %ss");

    public ItemPipeBlock(int maxPressure, int itemsPerTransfer, int transferCooldown, Properties properties) {
        super(maxPressure, properties);

        this.itemsPerTransfer = itemsPerTransfer;
        this.transferCooldown = transferCooldown;
    }

    @Override
    public void appendHoverText(ItemStack itemStack, Item.TooltipContext tooltipContext, List<Component> list, TooltipFlag tooltipFlag) {
        list.add(Component.translatable(ITEMS_PER_TRANSFER_TOOLTIP.getTranslationKey(), this.itemsPerTransfer)
                .withStyle(ChatFormatting.DARK_GRAY));

        float transferCooldownSeconds = (((float) this.transferCooldown)) / 20f;

        list.add(Component.translatable(TRANSFER_COOLDOW_TOOLTIP.getTranslationKey(),
                        // Round to .01
                        ((float) Math.round(transferCooldownSeconds * 100f)) / 100f).withStyle(ChatFormatting.DARK_GRAY));

        super.appendHoverText(itemStack, tooltipContext, list, tooltipFlag);
    }

    @Override
    boolean canConnect(Level world, BlockPos pos, Direction direction) {
        BlockState state = world.getBlockState(pos);
        Block block = state.getBlock();

        // Item pipe or item pump or any other class that implements OrientedInventory
        if (block instanceof OrientedContainer orientedContainer)
            return orientedContainer.getItemInventoryInputDirections(state).contains(direction) ||
                    orientedContainer.getItemInventoryOutputDirections(state).contains(direction);


        // If the block is a storage
        Storage<ItemVariant> storage = ItemStorage.SIDED.find(world, pos, direction);

        return storage != null && (storage.supportsExtraction() || storage.supportsInsertion());
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new ItemPipeBlockEntity(blockPos, blockState, this.itemsPerTransfer, this.transferCooldown, this.maxPressure);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        return level.isClientSide() ? null : BaseEntityBlock.createTickerHelper(blockEntityType, ModBlockEntities.ITEM_PIPE_BE, ItemPipeBlockEntity::tick);
    }

    @Override
    protected @NotNull MapCodec<? extends BaseEntityBlock> codec() {
        return simpleCodec((Properties properties) -> new ItemPipeBlock(0, 0, 0, properties)); // Maybe useful in a later minecraft version
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
