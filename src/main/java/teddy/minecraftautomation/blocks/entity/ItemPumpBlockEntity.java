package teddy.minecraftautomation.blocks.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.LockableContainerBlockEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import teddy.minecraftautomation.blocks.ItemPumpBlock;
import teddy.minecraftautomation.utils.ContainerUtils;
import teddy.minecraftautomation.utils.ImplementedInventory;

public class ItemPumpBlockEntity extends LockableContainerBlockEntity implements ImplementedInventory {
    private DefaultedList<ItemStack> items;
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

        this.items = DefaultedList.ofSize(1, ItemStack.EMPTY);
    }

    public static void tick(World level, BlockPos blockPos, BlockState state, ItemPumpBlockEntity itemPumpBlockEntity) {
        if (level.isClient() || !(level instanceof ServerWorld serverLevel))
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
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup provider) {
        // Save the inventory
        Inventories.writeNbt(nbt, this.items, provider);

        nbt.putInt("timer", this.cooldown);
        nbt.putInt("directionIndex", this.directionIndex);
        nbt.putInt("itemsPerTransfer", this.itemsPerTransfer);
        nbt.putInt("transferCooldown", this.transferCooldown);
        nbt.putInt("inducedPressure", this.inducedPressure);

        super.writeNbt(nbt, provider);
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup provider) {
        super.readNbt(nbt, provider);

        this.items = DefaultedList.ofSize(this.size(), ItemStack.EMPTY);
        Inventories.readNbt(nbt, this.items, provider); // Load the inventory

        this.cooldown = nbt.getInt("timer");
        this.directionIndex = nbt.getInt("directionIndex");
        this.itemsPerTransfer = nbt.getInt("itemsPerTransfer");
        this.transferCooldown = nbt.getInt("transferCooldown");
        this.inducedPressure = nbt.getInt("inducedPressure");
    }

    @Override
    public @NotNull DefaultedList<ItemStack> getHeldStacks() {
        return items;
    }

    @Override
    protected void setHeldStacks(DefaultedList<ItemStack> items) {
        this.items = items;
    }

    @Override
    protected @NotNull Text getContainerName() {
        return Text.empty();
    }

    @Override
    protected ScreenHandler createScreenHandler(int i, PlayerInventory inventory) {
        return null;
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return this.items;
    }
}
