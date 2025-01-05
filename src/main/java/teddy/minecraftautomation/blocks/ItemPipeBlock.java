package teddy.minecraftautomation.blocks;

import com.mojang.serialization.MapCodec;
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.redstone.Orientation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import teddy.minecraftautomation.blocks.entity.ItemPipeBlockEntity;
import teddy.minecraftautomation.blocks.entity.ModBlockEntities;

public class ItemPipeBlock extends AbstractPipeBlock {
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

    private final int itemsPerTransfer;
    private final int transferCooldown;

    public ItemPipeBlock(int itemsPerTransfer, int transferCooldown, Properties properties) {
        super(properties);

        super.registerDefaultState(super.defaultBlockState().setValue(POWERED, false));

        this.itemsPerTransfer = itemsPerTransfer;
        this.transferCooldown = transferCooldown;
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(NORTH, EAST, SOUTH, WEST, UP, DOWN, POWERED);
    }

    @Override
    boolean canConnect(Level world, BlockPos pos, Direction direction) {
        Block block = world.getBlockState(pos).getBlock();

        if (block instanceof ItemPipeBlock) return true;

        // If the block is a storage?
        Storage<ItemVariant> storage = ItemStorage.SIDED.find(world, pos, direction);

        return storage != null && (storage.supportsExtraction() || storage.supportsInsertion());
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new ItemPipeBlockEntity(blockPos, blockState, this.itemsPerTransfer, this.transferCooldown);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        return level.isClientSide ? null : createTickerHelper(blockEntityType, ModBlockEntities.ITEM_PIPE_BE, ItemPipeBlockEntity::tick);
    }

    @Override
    protected @NotNull MapCodec<? extends BaseEntityBlock> codec() {
        return simpleCodec((Properties properties) -> new ItemPipeBlock(0, 0, properties)); // Maybe useful in a later minecraft version
    }

    private void updatePoweredProperty(Level level, BlockState blockState, BlockPos blockPos) {
        boolean isPowered = level.hasNeighborSignal(blockPos);

        blockState = blockState.setValue(POWERED, isPowered);
        level.setBlock(blockPos, blockState, Block.UPDATE_NONE);
    }

    @Override
    protected void neighborChanged(BlockState blockState, Level level, BlockPos blockPos, Block block, @Nullable Orientation orientation, boolean bl) {
        super.neighborChanged(blockState, level, blockPos, block, orientation, bl);

        if (level.isClientSide) return;

        this.updatePoweredProperty(level, blockState, blockPos);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos blockPos, BlockState blockState, @Nullable LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(level, blockPos, blockState, placer, stack);

        if (level.isClientSide) return;

        this.updatePoweredProperty(level, blockState, blockPos);
    }
}
