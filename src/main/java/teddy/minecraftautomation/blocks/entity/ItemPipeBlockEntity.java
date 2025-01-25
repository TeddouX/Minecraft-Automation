package teddy.minecraftautomation.blocks.entity;

import org.jetbrains.annotations.NotNull;
import teddy.minecraftautomation.blocks.AbstractPipeBlock;
import teddy.minecraftautomation.blocks.ItemPipeBlock;
import teddy.minecraftautomation.blocks.ItemPumpBlock;
import teddy.minecraftautomation.utils.ContainerUtils;
import teddy.minecraftautomation.utils.ImplementedInventory;

import java.util.ArrayList;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
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

// All credit goes to https://github.com/mestiez/unflavoured-pipes for the tick method logic
public class ItemPipeBlockEntity extends LockableContainerBlockEntity implements ImplementedInventory {
    private DefaultedList<ItemStack> items;
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

        this.items = DefaultedList.ofSize(1, ItemStack.EMPTY);
    }

    public static void tick(World level, BlockPos blockPos, BlockState state, ItemPipeBlockEntity itemPipeBlockEntity) {
        if (level.isClient() || !(level instanceof ServerWorld serverLevel))
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

    static int getPressureAmountForBlock(World level, BlockState state, BlockPos pos) {
        Direction[] directions = Direction.values();
        BlockEntity blockEntity = level.getBlockEntity(pos);

        if (!(state.getBlock() instanceof ItemPipeBlock) || !(blockEntity instanceof ItemPipeBlockEntity))
            return 0;

        int maxPressure = 0;
        for (Direction dir : directions) {
            BlockState relativeBlockState = level.getBlockState(pos.offset(dir));
            BlockEntity relativeBlockEntity = level.getBlockEntity(pos.offset(dir));

            // If the other block is an item pump and the pipe is connected to its output
            if (relativeBlockState.getBlock() instanceof ItemPumpBlock itemPumpBlock
                    && state.get(ItemPipeBlock.getFacingPropertyFromDirection(dir))
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
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup provider) {
        Inventories.writeNbt(nbt, this.items, provider);

        nbt.putInt("timer", this.cooldown);
        nbt.putInt("directionIndex", this.directionIndex);
        nbt.putInt("itemsPerTransfer", this.itemsPerTransfer);
        nbt.putInt("transferCooldown", this.transferCooldown);
        nbt.putInt("pressure", this.pressure);
        nbt.putInt("maxPressure", this.maxPressure);

        super.writeNbt(nbt, provider);
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup provider) {
        super.readNbt(nbt, provider);

        this.items = DefaultedList.ofSize(this.size(), ItemStack.EMPTY);
        Inventories.readNbt(nbt, this.items, provider);

        this.cooldown = nbt.getInt("timer");
        this.directionIndex = nbt.getInt("directionIndex");
        this.itemsPerTransfer = nbt.getInt("itemsPerTransfer");
        this.transferCooldown = nbt.getInt("transferCooldown");
        this.pressure = nbt.getInt("pressure");
        this.maxPressure = nbt.getInt("maxPressure");
    }

    @Override
    protected void setHeldStacks(DefaultedList<ItemStack> items) {
        this.items = items;
    }

    @Override
    protected ScreenHandler createScreenHandler(int i, PlayerInventory inventory) {
        return null;
    }

    @Override
    protected Text getContainerName() {
        return null;
    }

    @Override
    protected DefaultedList<ItemStack> getHeldStacks() {
        return this.items;
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return this.items;
    }
}
