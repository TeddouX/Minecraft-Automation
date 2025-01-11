package teddy.minecraftautomation.blocks;

import com.mojang.serialization.MapCodec;
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.util.StringUtil;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.player.Player;
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
import teddy.minecraftautomation.blocks.entity.ItemPipeBlockEntity;
import teddy.minecraftautomation.blocks.entity.ModBlockEntities;
import teddy.minecraftautomation.utils.OrientedInventory;

import java.util.ArrayList;
import java.util.List;

public class ItemPipeBlock extends AbstractPipeBlock implements OrientedInventory {
    private final int itemsPerTransfer;
    private final int transferCooldown;

    public ItemPipeBlock(int maxPressure, int itemsPerTransfer, int transferCooldown, Properties properties) {
        super(maxPressure, properties);

        this.itemsPerTransfer = itemsPerTransfer;
        this.transferCooldown = transferCooldown;
    }

    @Override
    public void appendHoverText(ItemStack itemStack, Item.TooltipContext tooltipContext, List<Component> list, TooltipFlag tooltipFlag) {
        list.add(Component.translatable("block.minecraft-automation.item_pipe.tooltip.items_per_transfer", this.itemsPerTransfer)
                .withStyle(ChatFormatting.DARK_GRAY));

        float transferCooldownSeconds = (((float) this.transferCooldown)) / 20f;

        list.add(Component.translatable("block.minecraft-automation.item_pipe.tooltip.transfer_cooldown",
                        // Round to .01
                        ((float) Math.round(transferCooldownSeconds * 100f)) / 100f).withStyle(ChatFormatting.DARK_GRAY));
    }

    @Override
    boolean canConnect(Level world, BlockPos pos, Direction direction) {
        BlockState state = world.getBlockState(pos);
        Block block = state.getBlock();

        if (block instanceof OrientedInventory orientedInventory)
            return orientedInventory.getInputDirections(state).contains(direction) ||
                    orientedInventory.getOutputDirections(state).contains(direction);


        // If the block is a storage
        Storage<ItemVariant> storage = ItemStorage.SIDED.find(world, pos, direction);

        return storage != null && (storage.supportsExtraction() || storage.supportsInsertion());
    }

    @Override
    public void playerDestroy(Level level, Player player, BlockPos blockPos, BlockState blockState, @Nullable BlockEntity blockEntity, ItemStack itemStack) {
        if (blockEntity != null) {
            if (blockEntity instanceof ItemPipeBlockEntity itemPipeBlockEntity) {
                Containers.dropContents(level, blockPos, itemPipeBlockEntity);
            }
        }

        super.playerDestroy(level, player, blockPos, blockState, blockEntity, itemStack);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new ItemPipeBlockEntity(blockPos, blockState, this.itemsPerTransfer, this.transferCooldown);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        return level.isClientSide() ? null : BaseEntityBlock.createTickerHelper(blockEntityType, ModBlockEntities.ITEM_PIPE_BE, ItemPipeBlockEntity::tick);
    }

    @Override
    protected @NotNull MapCodec<? extends BaseEntityBlock> codec() {
        return simpleCodec((Properties properties) -> new ItemPipeBlock(0, 0, 0, properties)); // Maybe useful in a later minecraft version
    }

    public ArrayList<Direction> getInputDirections(BlockState blockState) {
        return new ArrayList<>(List.of(Direction.values())); // A pipe can connect on all sides
    }

    public ArrayList<Direction> getOutputDirections(BlockState blockState) {
        return new ArrayList<>(List.of(Direction.values()));
    }
}
