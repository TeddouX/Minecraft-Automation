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
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import teddy.minecraftautomation.blocks.ItemPumpBlock;
import teddy.minecraftautomation.utils.ContainerUtils;
import teddy.minecraftautomation.utils.ImplementedInventory;

public class ItemPumpBlockEntity extends BaseContainerBlockEntity implements ImplementedInventory {
    private NonNullList<ItemStack> items;
    private int itemsPerTransfer;
    private int transferCooldown;
    public int inducedPressure;
    public int cooldown = 0;
    public int directionIndex = 0;

    public ItemPumpBlockEntity(BlockPos blockPos, BlockState blockState, int inducedPressure, int itemsPerTransfer, int transferCooldown) {
        super(ModBlockEntities.ITEM_PUMP_BE, blockPos, blockState);

        this.inducedPressure = inducedPressure;
        this.itemsPerTransfer = itemsPerTransfer;
        this.transferCooldown = transferCooldown;

        this.items = NonNullList.withSize(1, ItemStack.EMPTY);
    }

    public static void tick(Level level, BlockPos blockPos, BlockState state, ItemPumpBlockEntity itemPumpBlockEntity) {
        if (level.isClientSide() || !(level instanceof ServerLevel serverLevel))
            return;

        // Only transfer items when the cooldown reaches 0
        itemPumpBlockEntity.cooldown++;
        if (itemPumpBlockEntity.cooldown >= itemPumpBlockEntity.transferCooldown)
            itemPumpBlockEntity.cooldown = 0;
        else
            return;

        ItemPumpBlock itemPumpBlock = (ItemPumpBlock) state.getBlock();
        Direction[] directions = Direction.values();
        for (int i = 0; i < directions.length; i++) {
            // Used so that it doesn't check the same direction two times in a row if it has other connections
            itemPumpBlockEntity.directionIndex++;
            itemPumpBlockEntity.directionIndex %= directions.length;
            Direction dir = directions[itemPumpBlockEntity.directionIndex];
            boolean success = false;

            if (itemPumpBlock.getOutputDirections(state).contains(dir)) {
                success = ContainerUtils.ItemContainer.handleDirection(dir,
                        serverLevel,
                        blockPos,
                        state,
                        itemPumpBlockEntity,
                        ContainerUtils.Flow.INCOMING,
                        itemPumpBlockEntity.itemsPerTransfer);

            } else if (itemPumpBlock.getInputDirections(state).contains(dir)) {
                success = ContainerUtils.ItemContainer.handleDirection(dir,
                        serverLevel,
                        blockPos,
                        state,
                        itemPumpBlockEntity,
                        ContainerUtils.Flow.OUTGOING,
                        itemPumpBlockEntity.itemsPerTransfer);
            }

            if (success)
                break;
        }
    }

    @Override
    protected void saveAdditional(CompoundTag nbt, HolderLookup.Provider provider) {
        // Save the inventory
        ContainerHelper.saveAllItems(nbt, this.items, provider);

        nbt.putInt("timer", this.cooldown);
        nbt.putInt("directionIndex", this.directionIndex);
        nbt.putInt("itemsPerTransfer", this.itemsPerTransfer);
        nbt.putInt("transferCooldown", this.transferCooldown);
        nbt.putInt("inducedPressure", this.inducedPressure);

        super.saveAdditional(nbt, provider);
    }

    @Override
    protected void loadAdditional(CompoundTag nbt, HolderLookup.Provider provider) {
        super.loadAdditional(nbt, provider);

        this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(nbt, this.items, provider); // Load the inventory

        this.cooldown = nbt.getInt("timer");
        this.directionIndex = nbt.getInt("directionIndex");
        this.itemsPerTransfer = nbt.getInt("itemsPerTransfer");
        this.transferCooldown = nbt.getInt("transferCooldown");
        this.inducedPressure = nbt.getInt("inducedPressure");
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
    protected @NotNull Component getDefaultName() {
        return Component.empty();
    }

    @Override
    protected AbstractContainerMenu createMenu(int i, Inventory inventory) {
        return null;
    }
}
