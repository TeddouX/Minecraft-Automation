package teddy.minecraftautomation.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.redstone.Orientation;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractPipeBlock extends BaseEntityBlock {
    public static final BooleanProperty NORTH = BlockStateProperties.NORTH;
    public static final BooleanProperty EAST = BlockStateProperties.EAST;
    public static final BooleanProperty SOUTH = BlockStateProperties.SOUTH;
    public static final BooleanProperty WEST = BlockStateProperties.WEST;
    public static final BooleanProperty UP = BlockStateProperties.UP;
    public static final BooleanProperty DOWN = BlockStateProperties.DOWN;
    protected final Map<String, VoxelShape> SHAPES;

    public AbstractPipeBlock(Properties properties) {
        super(properties);

        this.registerDefaultState(super.defaultBlockState()
                .setValue(NORTH, false)
                .setValue(EAST, false)
                .setValue(SOUTH, false)
                .setValue(WEST, false)
                .setValue(UP, false)
                .setValue(DOWN, false));

        this.SHAPES = createShapes();
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(NORTH, EAST, SOUTH, WEST, UP, DOWN);
    }

    protected Map<String, VoxelShape> createShapes() {
        float heightOffset = 5f;
        float radius = 3f;
        float height = heightOffset + radius * 2;

        float radiusPlus = 8f + radius;
        float radiusMinus = 8f - radius;

        // Create all boxes
        VoxelShape middle = Block.box(radiusMinus, heightOffset, radiusMinus, radiusPlus, height, radiusPlus);
        VoxelShape N = Block.box(radiusMinus, heightOffset, 0f, radiusPlus, height, radiusPlus);
        VoxelShape E = Block.box(radiusPlus, heightOffset, radiusMinus, 16f, height, radiusPlus);
        VoxelShape S = Block.box(radiusMinus, heightOffset, radiusMinus, radiusPlus, height, 16f);
        VoxelShape W = Block.box(0f, heightOffset, radiusMinus, radiusMinus, height, radiusPlus);
        VoxelShape U = Block.box(radiusMinus, radiusPlus, radiusMinus, radiusPlus, 16f, radiusPlus);
        VoxelShape D = Block.box(radiusMinus, 0f, radiusMinus, radiusPlus, radiusMinus, radiusPlus);

        Map<String, VoxelShape> voxelShapes = new HashMap<>();
        voxelShapes.put("middle", middle);
        voxelShapes.put("north", N);
        voxelShapes.put("east", E);
        voxelShapes.put("south", S);
        voxelShapes.put("west", W);
        voxelShapes.put("up", U);
        voxelShapes.put("down", D);

        return voxelShapes;
    }

    public static BooleanProperty getFacingPropertyFromDirection(Direction dir) {
        return switch (dir) {
            case NORTH -> NORTH;
            case EAST -> EAST;
            case SOUTH -> SOUTH;
            case WEST -> WEST;
            case UP -> UP;
            case DOWN -> DOWN;
        };
    }

    @Override
    public @NotNull VoxelShape getInteractionShape(BlockState state, BlockGetter blockGetter, BlockPos blockPos) {
        VoxelShape shape = this.SHAPES.get("middle");

        if (state.getValue(NORTH)) shape = Shapes.or(shape, this.SHAPES.get("north"));
        if (state.getValue(EAST)) shape = Shapes.or(shape, this.SHAPES.get("east"));
        if (state.getValue(SOUTH)) shape = Shapes.or(shape, this.SHAPES.get("south"));
        if (state.getValue(WEST)) shape = Shapes.or(shape, this.SHAPES.get("west"));
        if (state.getValue(UP)) shape = Shapes.or(shape, this.SHAPES.get("up"));
        if (state.getValue(DOWN)) shape = Shapes.or(shape, this.SHAPES.get("down"));

        return shape;
    }

    @Override
    public @NotNull VoxelShape getCollisionShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return this.getInteractionShape(blockState, blockGetter, blockPos);
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return this.getInteractionShape(state, blockGetter, blockPos);
    }

    abstract boolean canConnect(Level world, BlockPos pos, Direction direction);

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        Level world = ctx.getLevel();
        BlockPos pos = ctx.getClickedPos();

        return this.defaultBlockState()
                .setValue(NORTH, canConnect(world, pos.north(), Direction.NORTH))
                .setValue(EAST, canConnect(world, pos.east(), Direction.EAST))
                .setValue(SOUTH, canConnect(world, pos.south(), Direction.SOUTH))
                .setValue(WEST, canConnect(world, pos.west(), Direction.WEST))
                .setValue(UP, canConnect(world, pos.above(), Direction.UP))
                .setValue(DOWN, canConnect(world, pos.below(), Direction.DOWN));
    }

    @Override
    protected @NotNull BlockState updateShape(BlockState blockState, LevelReader levelReader, ScheduledTickAccess scheduledTickAccess, BlockPos blockPos, Direction direction, BlockPos blockPos2, BlockState blockState2, RandomSource randomSource) {
        return blockState.setValue(
                getFacingPropertyFromDirection(direction),
                canConnect((Level) levelReader, blockPos2, direction));
    }

    @Override
    public boolean isCollisionShapeFullBlock(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        return false;
    }

    @Override
    protected boolean isPathfindable(BlockState blockState, PathComputationType pathComputationType) {
        return false;
    }

    @Override
    public boolean hasDynamicShape() {
        return true;
    }

    @Override
    public @NotNull RenderShape getRenderShape(BlockState blockState) {
        return RenderShape.MODEL;
    }

    @Override
    public @NotNull VoxelShape getVisualShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return super.getVisualShape(blockState, blockGetter, blockPos, collisionContext);
    }

    @Override
    public abstract @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState);

    @Nullable
    @Override
    public abstract <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType);
}
