package teddy.minecraftautomation.blocks.entity;

import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import teddy.minecraftautomation.MinecraftAutomation;
import teddy.minecraftautomation.blocks.AbstractPipeBlock;
import teddy.minecraftautomation.blocks.ItemPipeBlock;

import java.time.chrono.MinguoEra;

public class ContainerUtils {
    public static boolean handleDirection(Direction dir, ServerLevel serverLevel, BlockPos pipePos, BlockState pipeState, ItemPipeBlockEntity itemPipe) {
        BlockPos otherPos = pipePos.relative(dir);
        BlockState otherState = serverLevel.getBlockState(otherPos);

        return transfer(serverLevel, pipePos, pipeState, otherPos, otherState, dir, itemPipe.getItemsPerTransfer());
    }

    public static boolean isFlowIncoming(Direction direction, ServerLevel serverLevel, BlockPos blockPos, BlockState state) {
        BooleanProperty prop = AbstractPipeBlock.getFacingPropertyFromDirection(direction);

        // If the other pipe has a connection in this direction
        if (state.getValue(prop)) {
            BlockPos otherPos = blockPos.relative(direction);
            BlockState otherState = serverLevel.getBlockState(otherPos);

            if (otherState.getBlock() instanceof ItemPipeBlock)
                return false;
            else
                // If the pipe is currently powered (trying to extract from a container)
                return state.getValue(ItemPipeBlock.POWERED);
        }
        return false;
    }

    public static boolean isFlowOutgoing(Direction direction, ServerLevel serverLevel, BlockPos blockPos, BlockState state) {
        BooleanProperty prop = AbstractPipeBlock.getFacingPropertyFromDirection(direction);

        // If the other pipe has a connection in this direction
        if (state.getValue(prop)) {
            BlockPos otherPos = blockPos.relative(direction);
            BlockState otherState = serverLevel.getBlockState(otherPos);

            // A pipe can transfer items to another pipe
            if (otherState.getBlock() instanceof ItemPipeBlock) {
                ItemPipeBlockEntity otherBlockEntity = (ItemPipeBlockEntity) serverLevel.getBlockEntity(otherPos);

                if (otherBlockEntity == null) return false;

                NonNullList<ItemStack> otherInventory = otherBlockEntity.getItems();
                // Inventory is empty
                // Only check first slot because inventory is of size 1
                return otherInventory.getFirst() == ItemStack.EMPTY;
            }
            else
                // If the pipe isn't extracting from a container
                return !state.getValue(ItemPipeBlock.POWERED);
        }

        return false;
    }

    public static boolean transferFirstItem(ServerLevel level, BlockPos sourcePos, BlockPos destPos, Direction dir, int transferAmount) {
        Storage<ItemVariant> source = ItemStorage.SIDED.find(level, sourcePos, dir);
        Storage<ItemVariant> dest = ItemStorage.SIDED.find(level, destPos, dir.getOpposite());

        if (source == null || dest == null || !source.supportsExtraction() || !dest.supportsExtraction())
            return false;

        try (Transaction transaction = Transaction.openOuter()) {
            for (StorageView<ItemVariant> storageView : source.nonEmptyViews()) {
                if (storageView.isResourceBlank())
                    continue;

                ItemVariant itemVariant =  storageView.getResource();

                long extracted = storageView.extract(itemVariant, Math.min(storageView.getAmount(), transferAmount), transaction);
                long inserted = dest.insert(itemVariant, extracted, transaction);

                if (inserted != 0) {
                    transaction.commit();

                    return true;
                }
            }
        }

        return false;
    }

    public static boolean transfer(ServerLevel level, BlockPos sourcePos, BlockState sourceState, BlockPos destPos, BlockState destState, Direction dir, int transferAmount) {
        Storage<ItemVariant> destStorage = ItemStorage.SIDED.find(level, destPos, dir);

        if (destStorage == null || !(destStorage.supportsInsertion() || destStorage.supportsExtraction()))
            return false;

        if (isFlowIncoming(dir, level, sourcePos, sourceState)) {
            // Transfer from the dest container (ie: a chest) to the pipe
            return transferFirstItem(level, destPos, sourcePos, dir, transferAmount);
        } else if (isFlowOutgoing(dir, level, sourcePos, sourceState)) {
            // Transfer from the pipe to the destination container
            return transferFirstItem(level, sourcePos, destPos, dir, transferAmount);
        }

        return false;
    }
}
