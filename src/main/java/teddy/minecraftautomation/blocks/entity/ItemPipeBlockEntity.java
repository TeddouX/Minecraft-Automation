package teddy.minecraftautomation.blocks.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import teddy.minecraftautomation.blocks.ItemPipeBlock;
import teddy.minecraftautomation.blocks.ItemPumpBlock;
import teddy.minecraftautomation.utils.ContainerUtils;

public class ItemPipeBlockEntity extends BaseContainerBlockEntity {
    private NonNullList<ItemStack> items;
    private int itemsPerTransfer;
    private int transferCooldown;
    public int cooldown = 0;
    public int directionIndex = 0;
    public int pressure = 0;
    public BlockPos itemPumpPos = null;


    public ItemPipeBlockEntity(BlockPos blockPos, BlockState blockState, int itemsPerTransfer, int transferCooldown) {
        super(ModBlockEntities.ITEM_PIPE_BE, blockPos, blockState);

        this.itemsPerTransfer = itemsPerTransfer;
        this.transferCooldown = transferCooldown;

        this.items = NonNullList.withSize(1, ItemStack.EMPTY);
    }

    public static void tick(Level level, BlockPos blockPos, BlockState state, ItemPipeBlockEntity itemPipeBlockEntity) {
        if (level.isClientSide() || !(level instanceof ServerLevel))
            return;

        ServerLevel serverLevel = (ServerLevel) level;

        // Update pressure
        itemPipeBlockEntity.pressure = getPressureAmountForBlock(level, state, blockPos);

        if (itemPipeBlockEntity.pressure <= 0) return;

        // Only transfer items when the cooldown reaches 0
        itemPipeBlockEntity.cooldown++;
        if (itemPipeBlockEntity.cooldown >= itemPipeBlockEntity.transferCooldown)
            itemPipeBlockEntity.cooldown = 0;
        else
            return;

        Direction[] directions = Direction.values();
        for (int i = 0; i < directions.length; i++) {
            // Used so that it doesn't check the same direction two times in a row if it has other connections
            itemPipeBlockEntity.directionIndex++;
            itemPipeBlockEntity.directionIndex %= directions.length;

            boolean success = ContainerUtils.handleDirection(
                    directions[itemPipeBlockEntity.directionIndex],
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

    public static int getPressureAmountForBlock(Level level, BlockState state, BlockPos pos) {
        Direction[] directions = Direction.values();
        BlockEntity blockEntity = level.getBlockEntity(pos);

        if (!(state.getBlock() instanceof ItemPipeBlock) || !(blockEntity instanceof ItemPipeBlockEntity))
            return 0;

        int maxPressure = 0;
        for (Direction dir : directions) {
            BlockState relativeBlockState = level.getBlockState(pos.relative(dir));
            BlockEntity relativeBlockEntity = level.getBlockEntity(pos.relative(dir));

            // If the other block is an item pump and the pipe is connected to its input
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
        super.saveAdditional(nbt, provider);

        ContainerHelper.saveAllItems(nbt, this.items, provider);

        nbt.putInt("timer", this.cooldown);
        nbt.putInt("direction_index", this.directionIndex);
        nbt.putInt("items_per_transfer", this.itemsPerTransfer);
        nbt.putInt("transfer_cooldown", this.transferCooldown);
        nbt.putInt("pressure", this.pressure);
    }

    @Override
    protected void loadAdditional(CompoundTag nbt, HolderLookup.Provider provider) {
        super.loadAdditional(nbt, provider);
        this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);

        ContainerHelper.loadAllItems(nbt, this.items, provider);

        this.cooldown = nbt.getInt("timer");
        this.directionIndex = nbt.getInt("direction_index");
        this.itemsPerTransfer = nbt.getInt("items_per_transfer");
        this.transferCooldown = nbt.getInt("transfer_cooldown");
        this.pressure = nbt.getInt("pressure");
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
    public int getContainerSize() {
        return this.getItems().size();
    }

    @Override
    public boolean isEmpty() {
        return this.getItems().stream().allMatch(ItemStack::isEmpty);
    }

    @Override
    public @NotNull ItemStack getItem(int index) {
        return this.getItems().get(index);
    }

    @Override
    public @NotNull ItemStack removeItem(int index, int amount) {
        ItemStack itemStack = ContainerHelper.removeItem(this.getItems(), index, amount);
        if (!itemStack.isEmpty()) {
            setChanged();
        }

        return itemStack;
    }

    @Override
    public @NotNull ItemStack removeItemNoUpdate(int i) {
        return ContainerHelper.takeItem(getItems(), i);
    }

    @Override
    public void setItem(int i, ItemStack itemStack) {
        getItems().set(i, itemStack);
        if (itemStack.getCount() > getMaxStackSize())
            itemStack.setCount(getMaxStackSize());
        setChanged();
    }

    @Override
    public boolean stillValid(Player player) {
        return Container.stillValidBlockEntity(this, player);
    }

    @Override
    public void clearContent() {
        getItems().clear();
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