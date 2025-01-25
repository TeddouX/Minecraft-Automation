package teddy.minecraftautomation.utils;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import teddy.minecraftautomation.blocks.*;
import teddy.minecraftautomation.blocks.entity.FluidPipeBlockEntity;
import teddy.minecraftautomation.blocks.entity.ItemPipeBlockEntity;

public class ContainerUtils {
    public static class ItemContainer {
        public static boolean handleDirection(Direction dir, ServerWorld serverLevel, BlockPos pos, BlockState state, BlockEntity entity, Flow flow, int numItems) {
            BlockPos otherPos = pos.offset(dir);
            BlockState otherState = serverLevel.getBlockState(otherPos);

            return transfer(serverLevel, pos, state, otherPos, otherState, dir, flow, entity, numItems);
        }

        static boolean canExtract(Direction direction, ServerWorld serverLevel, BlockPos blockPos, BlockState state) {
            return state.getBlock() instanceof ItemPumpBlock;
        }

        static boolean canPushOut(Direction direction, ServerWorld serverLevel, BlockPos blockPos, BlockState state, BlockEntity blockEntity) {
            BlockPos otherPos = blockPos.offset(direction);
            BlockState otherState = serverLevel.getBlockState(otherPos);

            // An item pump can always push out
            if (state.getBlock() instanceof ItemPumpBlock)
                return true;
            else if (state.getBlock() instanceof ItemPipeBlock) {
                if (!(blockEntity instanceof ItemPipeBlockEntity itemPipe))
                    return false;

                // A pipe can't transfer items to an item pump
                if (otherState.getBlock() instanceof ItemPumpBlock)
                    return false;

                // A pipe can transfer items to another pipe
                if (otherState.getBlock() instanceof ItemPipeBlock) {
                    BooleanProperty prop = AbstractPipeBlock.getFacingPropertyFromDirection(direction.getOpposite());

                    if (!otherState.get(prop))
                        return false;

                    BlockEntity otherBlockEntity = serverLevel.getBlockEntity(otherPos);

                    if (!(otherBlockEntity instanceof ItemPipeBlockEntity otherPipe))
                        return false;

                    return itemPipe.pressure >= otherPipe.pressure;
                } else
                    // If the pressure is greater than 0, the item pipe can push to another container (ie: chest)
                    return itemPipe.pressure > 0 && !(otherState.getBlock() instanceof ItemPumpBlock);
            } else
                return false;
        }

        static boolean transferFirstItem(ServerWorld level, BlockPos sourcePos, BlockPos destPos, Direction dir, int transferAmount) {
            Storage<ItemVariant> source = ItemStorage.SIDED.find(level, sourcePos, dir);
            Storage<ItemVariant> dest = ItemStorage.SIDED.find(level, destPos, dir.getOpposite());

            if (source == null || dest == null || !source.supportsExtraction() || !dest.supportsExtraction())
                return false;

            try (Transaction transaction = Transaction.openOuter()) {
                for (StorageView<ItemVariant> storageView : source.nonEmptyViews()) {
                    if (storageView.isResourceBlank())
                        continue;

                    ItemVariant itemVariant = storageView.getResource();

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

        static boolean transfer(ServerWorld level,
                                       BlockPos sourcePos,
                                       BlockState sourceState,
                                       BlockPos destPos,
                                       BlockState destState,
                                       Direction dir,
                                       Flow flow,
                                       BlockEntity blockEntity,
                                       int transferAmount) {

            Storage<ItemVariant> destStorage = ItemStorage.SIDED.find(level, destPos, dir);

            if (destStorage == null || !(destStorage.supportsInsertion() || destStorage.supportsExtraction()))
                return false;

            if (flow == Flow.INCOMING && canExtract(dir, level, sourcePos, sourceState))
                // Transfer from a dest container (ie: a chest)
                return transferFirstItem(level, destPos, sourcePos, dir, transferAmount);
            else if (flow == Flow.OUTGOING && canPushOut(dir, level, sourcePos, sourceState, blockEntity))
                // Transfer from the block to the destination container
                return transferFirstItem(level, sourcePos, destPos, dir, transferAmount);

            return false;
        }
    }

    public static class FluidContainer {
        public static boolean handleDirection(Direction dir, ServerWorld serverLevel, BlockPos pos, BlockState state, BlockEntity entity, Flow flow, long amount) {
            BlockPos otherPos = pos.offset(dir);
            BlockState otherState = serverLevel.getBlockState(otherPos);

            return transfer(serverLevel, pos, state, otherPos, otherState, dir, flow, entity, amount);
        }

        static boolean canExtract(Direction direction, ServerWorld serverLevel, BlockPos blockPos, BlockState state) {
            return state.getBlock() instanceof FluidPumpBlock;
        }

        static boolean canPushOut(Direction direction, ServerWorld serverLevel, BlockPos blockPos, BlockState state, BlockEntity blockEntity) {
            BlockPos otherPos = blockPos.offset(direction);
            BlockState otherState = serverLevel.getBlockState(otherPos);

            if (state.getBlock() instanceof FluidPumpBlock)
                return true;
            else if (state.getBlock() instanceof FluidPipeBlock) {
                if (!(blockEntity instanceof FluidPipeBlockEntity fluidPipe))
                    return false;

                if (otherState.getBlock() instanceof FluidPipeBlock) {
                    BooleanProperty prop = AbstractPipeBlock.getFacingPropertyFromDirection(direction.getOpposite());

                    if (!otherState.get(prop))
                        return false;

                    BlockEntity otherBlockEntity = serverLevel.getBlockEntity(otherPos);

                    if (!(otherBlockEntity instanceof FluidPipeBlockEntity otherPipe))
                        return false;

                    return fluidPipe.pressure >= otherPipe.pressure;
                } else
                    return fluidPipe.pressure > 0 && !(otherState.getBlock() instanceof FluidPumpBlock);
            } else
                return false;
        }

        static boolean transferAmountToContainer(ServerWorld level, BlockPos destPos, Direction dir, FluidVariant fluidVariant, long amount) {
            Storage<FluidVariant> dest = FluidStorage.SIDED.find(level, destPos, dir);

            if (dest == null || !dest.supportsInsertion())
                return false;

            try (Transaction transaction = Transaction.openOuter()) {
                long amountInserted = dest.insert(fluidVariant, amount, transaction);

                if (amountInserted != 0) {
                    transaction.commit();

                    return true;
                }
            }
            return false;
        }

        static boolean transferFromContainerToContainer(ServerWorld level, BlockPos sourcePos, BlockPos destPos, Direction dir, long amount) {
            Storage<FluidVariant> source = FluidStorage.SIDED.find(level, sourcePos, dir);
            Storage<FluidVariant> dest = FluidStorage.SIDED.find(level, destPos, dir.getOpposite());

            if (source == null || dest == null || !source.supportsExtraction() || !dest.supportsInsertion())
                return false;

            try (Transaction transaction = Transaction.openOuter()) {
                for (StorageView<FluidVariant> storageView : source.nonEmptyViews()) {
                    if (storageView.isResourceBlank())
                        continue;

                    FluidVariant fluidVariant = storageView.getResource();

                    long extracted = storageView.extract(fluidVariant, Math.min(storageView.getAmount(), amount), transaction);
                    long inserted = dest.insert(fluidVariant, extracted, transaction);

                    if (inserted != 0) {
                        transaction.commit();

                        return true;
                    }
                }
            }

            return false;
        }

        static boolean transfer(ServerWorld level,
                                BlockPos sourcePos,
                                BlockState sourceState,
                                BlockPos destPos,
                                BlockState destState,
                                Direction dir,
                                Flow flow,
                                BlockEntity blockEntity,
                                long amount) {

            Storage<FluidVariant> destStorage = FluidStorage.SIDED.find(level, destPos, dir);

            if (destStorage == null) {
                if (level.getFluidState(destPos) != Fluids.EMPTY.getDefaultState() && canExtract(dir, level, sourcePos, sourceState) && flow == Flow.INCOMING) {
                    FluidState fluidState = level.getFluidState(destPos);

                    if (!fluidState.isStill())
                        return false;

                    FluidVariant fluidVariant = FluidVariant.of(fluidState.getFluid());

                    // This is if the block is a Fluid
                    return transferAmountToContainer(level, sourcePos, dir, fluidVariant, Math.min(FluidConstants.BUCKET, amount));
                }
                return false;
            }

            if (!(destStorage.supportsInsertion() || destStorage.supportsExtraction()))
                return false;

            if (flow == Flow.INCOMING && canExtract(dir, level, sourcePos, sourceState))
                // Transfer fluid from container (ie: Fluid Tank) to the source
                return transferFromContainerToContainer(level, destPos, sourcePos, dir, amount);
            else if (flow == Flow.OUTGOING && canPushOut(dir, level, sourcePos, sourceState, blockEntity))
                return transferFromContainerToContainer(level, sourcePos, destPos, dir, amount);

            return false;
        }
    }

    public enum Flow {
        OUTGOING,
        INCOMING
    }
}
