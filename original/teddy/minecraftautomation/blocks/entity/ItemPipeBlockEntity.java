package teddy.minecraftautomation.blocks.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import teddy.minecraftautomation.blocks.AbstractPipeBlock;
import teddy.minecraftautomation.blocks.ItemPipeBlock;
import teddy.minecraftautomation.blocks.ItemPumpBlock;
import teddy.minecraftautomation.utils.ContainerUtils;
import teddy.minecraftautomation.utils.ImplementedInventory;

import java.util.ArrayList;

// All credit goes to https://github.com/mestiez/unflavoured-pipes for the tick method logic
public class ItemPipeBlockEntity extends BaseContainerBlockEntity implements ImplementedInventory {
    private NonNullList<ItemStack> items;
    private int maxPressure;
    private int itemsPerTransfer;
    private int transferCooldown;
    public int cooldown = 0;
    public int directionIndex = 0;
    public int pressure = 0;

    public ItemPipeBlockEntity(BlockPos blockPos, BlockState blockState, int itemsPerTransfer, int transferCooldown, int maxPressure) {
        super(ModBlockEntities.ITEM_PIPE_BE, blockPos, blockState);

        this.itemsPerTransfer = itemsPerTransfer;
        this.transferCooldown = transferCooldown;
        this.maxPressure = maxPressure;

        this.items = NonNullList.withSize(1, ItemStack.EMPTY);
    }

    public static void tick(Level level, BlockPos blockPos, BlockState state, ItemPipeBlockEntity itemPipeBlockEntity) {
        if (level.isClientSide() || !(level instanceof ServerLevel serverLevel))
            return;

        // Update pressure
        // Clamp it to the maxPressure
        itemPipeBlockEntity.pressure = Math.min(getPressureAmountForBlock(level, state, blockPos), itemPipeBlockEntity.maxPressure);

        if (itemPipeBlockEntity.pressure <= 0) return;

        // Only transfer items when the cooldown reaches 0
        itemPipeBlockEntity.cooldown++;
        if (itemPipeBlockEntity.cooldown >= itemPipeBlockEntity.transferCooldown)
            itemPipeBlockEntity.cooldown = 0;
        else
            return;

        ArrayList<Direction> directions = AbstractPipeBlock.getConnectionsFromBlockState(state);

        if (directions == null) return;

        for (int i = 0; i < directions.size(); i++) {
            // Used so that it doesn't check the same direction two times in a row if it has other connections
            itemPipeBlockEntity.directionIndex++;
            itemPipeBlockEntity.directionIndex %= directions.size();

            boolean success = ContainerUtils.ItemContainer.handleDirection(
                    directions.get(itemPipeBlockEntity.directionIndex),
                    serverLevel,
                    blockPos,
                    state,
                    itemPipeBlockEntity,
                    ContainerUtils.Flow.OUTGOING,
                    itemPipeBlockEntity.itemsPerTransfer);

            if (success)
                break;
        }
    }

    static int getPressureAmountForBlock(Level level, BlockState state, BlockPos pos) {
        Direction[] directions = Direction.values();
        BlockEntity blockEntity = level.getBlockEntity(pos);

        if (!(state.getBlock() instanceof ItemPipeBlock) || !(blockEntity instanceof ItemPipeBlockEntity))
            return 0;

        int maxPressure = 0;
        for (Direction dir : directions) {
            BlockState relativeBlockState = level.getBlockState(pos.relative(dir));
            BlockEntity relativeBlockEntity = level.getBlockEntity(pos.relative(dir));

            // If the other block is an item pump and the pipe is connected to its output
            if (relativeBlockState.getBlock() instanceof ItemPumpBlock itemPumpBlock
                    && state.getValue(ItemPipeBlock.getFacingPropertyFromDirection(dir))
                    && itemPumpBlock.getOutputDirections(relativeBlockState).contains(dir)) {

                if (!(relativeBlockEntity instanceof ItemPumpBlockEntity itemPumpBlockEntity))
                    continue;

                maxPressure = Math.max(itemPumpBlockEntity.inducedPressure, maxPressure);
            } else if (relativeBlockState.getBlock() instanceof ItemPipeBlock) {
                if (!(relativeBlockEntity instanceof ItemPipeBlockEntity relativeItemPipeBlockEntity))
                    continue;

                maxPressure = Math.max(relativeItemPipeBlockEntity.pressure - 1, maxPressure);
            }
        }

        return maxPressure;
    }

    @Override
    protected void saveAdditional(CompoundTag nbt, HolderLookup.Provider provider) {
        ContainerHelper.saveAllItems(nbt, this.items, provider);

        nbt.putInt("timer", this.cooldown);
        nbt.putInt("directionIndex", this.directionIndex);
        nbt.putInt("itemsPerTransfer", this.itemsPerTransfer);
        nbt.putInt("transferCooldown", this.transferCooldown);
        nbt.putInt("pressure", this.pressure);
        nbt.putInt("maxPressure", this.maxPressure);

        super.saveAdditional(nbt, provider);
    }

    @Override
    protected void loadAdditional(CompoundTag nbt, HolderLookup.Provider provider) {
        super.loadAdditional(nbt, provider);

        this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(nbt, this.items, provider);

        this.cooldown = nbt.getInt("timer");
        this.directionIndex = nbt.getInt("directionIndex");
        this.itemsPerTransfer = nbt.getInt("itemsPerTransfer");
        this.transferCooldown = nbt.getInt("transferCooldown");
        this.pressure = nbt.getInt("pressure");
        this.maxPressure = nbt.getInt("maxPressure");
    }

    @Override
    public @NotNull NonNullList<ItemStack> getItems() {
        return items;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> items) {
        this.items = items;
    }

    @Override
    protected AbstractContainerMenu createMenu(int i, Inventory inventory) {
        return null;
    }

    @Override
    protected Component getDefaultName() {
        return null;
    }
}
