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
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class ItemPipeBlockEntity extends RandomizableContainerBlockEntity {
    private NonNullList<ItemStack> items;
    private int itemsPerTransfer;
    private int transferCooldown;
    public int cooldown = 0;
    public int directionIndex = 0;
    public Flow flow;


    public ItemPipeBlockEntity(BlockPos blockPos, BlockState blockState, int itemsPerTransfer, int transferCooldown) {
        super(ModBlockEntities.ITEM_PIPE_BE, blockPos, blockState);

        this.itemsPerTransfer = itemsPerTransfer;
        this.transferCooldown = transferCooldown;

        this.items = NonNullList.withSize(1, ItemStack.EMPTY);
    }

    public static void tick(Level level, BlockPos blockPos, BlockState state, ItemPipeBlockEntity itemPipe) {
        if (level.isClientSide() || !(level instanceof ServerLevel))
            return;

        ServerLevel serverLevel = (ServerLevel) level;

        // Only transfer items when the cooldown reaches 0
        itemPipe.cooldown++;
        if (itemPipe.cooldown >= itemPipe.transferCooldown)
            itemPipe.cooldown = 0;
        else
            return;

        // Iterate through all directions (north, east, south, west, up, down) and see if there is any flow incoming
        Direction[] directions = Direction.values();
        for (Direction dir : directions) {
            ContainerUtils.handleDirection(dir, serverLevel, blockPos, state, itemPipe);
        }

        for (int i = 0; i < directions.length; i++) {
            // Used so that it doesn't check the same direction two times in a row if it has other connections
            itemPipe.directionIndex++;
            itemPipe.directionIndex %= directions.length;

            if (ContainerUtils.handleDirection(directions[itemPipe.directionIndex], serverLevel, blockPos, state, itemPipe))
                break;
        }
    }

    public int getItemsPerTransfer() {
        return this.itemsPerTransfer;
    }

    @Override
    protected void saveAdditional(CompoundTag nbt, HolderLookup.Provider provider) {
        super.saveAdditional(nbt, provider);

        // Save the inventory
        if (!this.trySaveLootTable(nbt))
            ContainerHelper.saveAllItems(nbt, this.items, provider);

        nbt.putInt("timer", this.cooldown);
        nbt.putInt("direction_index", this.directionIndex);
        nbt.putInt("items_per_transfer", this.itemsPerTransfer);
        nbt.putInt("transfer_cooldown", this.transferCooldown);
    }

    @Override
    protected void loadAdditional(CompoundTag nbt, HolderLookup.Provider provider) {
        super.loadAdditional(nbt, provider);
        this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);

        // Load the inventory
        if (!this.tryLoadLootTable(nbt))
            ContainerHelper.loadAllItems(nbt, this.items, provider);

        this.cooldown = nbt.getInt("timer");
        this.directionIndex = nbt.getInt("direction_index");
        this.itemsPerTransfer = nbt.getInt("items_per_transfer");
        this.transferCooldown = nbt.getInt("transfer_cooldown");
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
    protected @NotNull Component getDefaultName() {
        return Component.empty();
    }

    @Override
    protected AbstractContainerMenu createMenu(int i, Inventory inventory) {
        return null;
    }

    public enum Flow {
        OUTGOING,
        INCOMING
    }
}
