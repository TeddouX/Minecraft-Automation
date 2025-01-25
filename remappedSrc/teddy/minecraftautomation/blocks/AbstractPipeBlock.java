package teddy.minecraftautomation.blocks;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import teddy.minecraftautomation.MinecraftAutomation;
import teddy.minecraftautomation.items.ModItems;
import teddy.minecraftautomation.utils.OrientedContainer;
import teddy.minecraftautomation.utils.Tooltip;
import teddy.minecraftautomation.utils.WrenchableBlock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.minecraft.world.tick.ScheduledTickView;

public abstract class AbstractPipeBlock extends BlockWithEntity implements OrientedContainer, WrenchableBlock {
    final int maxPressure;

    public static final BooleanProperty NORTH = Properties.NORTH;
    public static final BooleanProperty EAST = Properties.EAST;
    public static final BooleanProperty SOUTH = Properties.SOUTH;
    public static final BooleanProperty WEST = Properties.WEST;
    public static final BooleanProperty UP = Properties.UP;
    public static final BooleanProperty DOWN = Properties.DOWN;

    protected final Map<String, VoxelShape> SHAPES;

    public static final Tooltip STATS_TOOLTIP = new Tooltip(MinecraftAutomation.MOD_ID, "block_stats", "stats", "Stats:");
    public static final Tooltip MAX_PRESSURE_TOOLTIP = new Tooltip(MinecraftAutomation.MOD_ID, "pipe", "max_pressure", "Max pressure: %spu") ;

    public AbstractPipeBlock(int maxPressure, Settings properties) {
        super(properties);

        this.maxPressure = maxPressure;

        this.setDefaultState(super.getDefaultState()
                .with(NORTH, false)
                .with(EAST, false)
                .with(SOUTH, false)
                .with(WEST, false)
                .with(UP, false)
                .with(DOWN, false)
                .with(WRENCHED_NORTH, false)
                .with(WRENCHED_EAST, false)
                .with(WRENCHED_SOUTH, false)
                .with(WRENCHED_WEST, false)
                .with(WRENCHED_UP, false)
                .with(WRENCHED_DOWN, false));

        this.SHAPES = createShapes();
    }

    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(NORTH, EAST, SOUTH, WEST, UP, DOWN,
                WRENCHED_NORTH, WRENCHED_EAST, WRENCHED_SOUTH, WRENCHED_WEST, WRENCHED_UP, WRENCHED_DOWN);
    }

    protected Map<String, VoxelShape> createShapes() {
        float heightOffset = 5f;
        float radius = 3f;
        float height = heightOffset + radius * 2;

        float radiusPlus = 8f + radius;
        float radiusMinus = 8f - radius;

        // Create all boxes
        VoxelShape middle = Block.createCuboidShape(radiusMinus, heightOffset, radiusMinus, radiusPlus, height, radiusPlus);
        VoxelShape N = Block.createCuboidShape(radiusMinus, heightOffset, 0f, radiusPlus, height, radiusPlus);
        VoxelShape E = Block.createCuboidShape(radiusPlus, heightOffset, radiusMinus, 16f, height, radiusPlus);
        VoxelShape S = Block.createCuboidShape(radiusMinus, heightOffset, radiusMinus, radiusPlus, height, 16f);
        VoxelShape W = Block.createCuboidShape(0f, heightOffset, radiusMinus, radiusMinus, height, radiusPlus);
        VoxelShape U = Block.createCuboidShape(radiusMinus, radiusPlus, radiusMinus, radiusPlus, 16f, radiusPlus);
        VoxelShape D = Block.createCuboidShape(radiusMinus, 0f, radiusMinus, radiusPlus, radiusMinus, radiusPlus);

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

    @Nullable
    public static ArrayList<Direction> getConnectionsFromBlockState(BlockState blockState) {
        ArrayList<Direction> connections = new ArrayList<>();


        if (!(blockState.getBlock() instanceof AbstractPipeBlock))
            return null;

        if (blockState.get(NORTH)) connections.add(Direction.NORTH);
        if (blockState.get(EAST)) connections.add(Direction.EAST);
        if (blockState.get(SOUTH)) connections.add(Direction.SOUTH);
        if (blockState.get(WEST)) connections.add(Direction.WEST);
        if (blockState.get(UP)) connections.add(Direction.UP);
        if (blockState.get(DOWN)) connections.add(Direction.DOWN);

        return connections.isEmpty() ? null : connections;
    }

    @Override
    public @NotNull VoxelShape getRaycastShape(BlockState state, BlockView blockGetter, BlockPos blockPos) {
        VoxelShape shape = this.SHAPES.get("middle");

        if (state.get(NORTH)) shape = VoxelShapes.union(shape, this.SHAPES.get("north"));
        if (state.get(EAST)) shape = VoxelShapes.union(shape, this.SHAPES.get("east"));
        if (state.get(SOUTH)) shape = VoxelShapes.union(shape, this.SHAPES.get("south"));
        if (state.get(WEST)) shape = VoxelShapes.union(shape, this.SHAPES.get("west"));
        if (state.get(UP)) shape = VoxelShapes.union(shape, this.SHAPES.get("up"));
        if (state.get(DOWN)) shape = VoxelShapes.union(shape, this.SHAPES.get("down"));

        return shape;
    }

    @Override
    public @NotNull VoxelShape getCollisionShape(BlockState blockState, BlockView blockGetter, BlockPos blockPos, ShapeContext collisionContext) {
        return this.getRaycastShape(blockState, blockGetter, blockPos);
    }

    @Override
    public @NotNull VoxelShape getOutlineShape(BlockState state, BlockView blockGetter, BlockPos blockPos, ShapeContext collisionContext) {
        return this.getRaycastShape(state, blockGetter, blockPos);
    }

    abstract boolean canConnect(World world, BlockPos pos, Direction direction);

    @Override
    public void appendTooltip(ItemStack itemStack, Item.TooltipContext tooltipContext, List<Text> list, TooltipType tooltipFlag) {
        list.add(Text.translatable(STATS_TOOLTIP.getTranslationKey()).formatted(Formatting.DARK_GRAY));
        list.add(Text.translatable(MAX_PRESSURE_TOOLTIP.getTranslationKey(), this.maxPressure).formatted(Formatting.DARK_GRAY));
    }

    @Override
    public void afterBreak(World level, PlayerEntity player, BlockPos blockPos, BlockState blockState, @Nullable BlockEntity blockEntity, ItemStack itemStack) {
        super.afterBreak(level, player, blockPos, blockState, blockEntity, itemStack);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        World world = ctx.getWorld();
        BlockPos pos = ctx.getBlockPos();

        return this.getDefaultState()
                .with(NORTH, canConnect(world, pos.north(), Direction.NORTH))
                .with(EAST, canConnect(world, pos.east(), Direction.EAST))
                .with(SOUTH, canConnect(world, pos.south(), Direction.SOUTH))
                .with(WEST, canConnect(world, pos.west(), Direction.WEST))
                .with(UP, canConnect(world, pos.up(), Direction.UP))
                .with(DOWN, canConnect(world, pos.down(), Direction.DOWN));
    }

    @Override
    protected @NotNull BlockState getStateForNeighborUpdate(BlockState blockState, WorldView levelReader, ScheduledTickView scheduledTickAccess, BlockPos blockPos, Direction direction, BlockPos blockPos2, BlockState blockState2, Random randomSource) {
        if (blockState.getBlock() instanceof WrenchableBlock && blockState.get(getWrenchedPropertyFromDirection(direction)))
            return blockState;

        return blockState.with(getFacingPropertyFromDirection(direction), canConnect((World) levelReader, blockPos2, direction));
    }

    @Override
    protected @NotNull ActionResult onUseWithItem(
            ItemStack itemStack, BlockState blockState, World level, BlockPos blockPos, PlayerEntity player, Hand interactionHand, BlockHitResult blockHitResult
    ) {
        if (itemStack.getItem() == ModItems.WRENCH && !level.isClient) {
            Direction clickDir = Direction.getFacing(player.getRotationVector()).getOpposite();
            BooleanProperty facingProp = getFacingPropertyFromDirection(clickDir);
            BooleanProperty wrenchedProp = getWrenchedPropertyFromDirection(clickDir);
            BlockState newBlockState = blockState
                    .with(facingProp, !blockState.get(facingProp))
                    .with(wrenchedProp, !blockState.get(wrenchedProp));

            level.setBlockState(blockPos, newBlockState, 3);

            if (!player.isCreative())
                itemStack.setDamage(itemStack.getDamage() + 1);

            return ActionResult.SUCCESS;
        }

        return super.onUseWithItem(itemStack, blockState, level, blockPos, player, interactionHand, blockHitResult);
    }

    @Override
    public boolean isShapeFullCube(BlockState blockState, BlockView blockGetter, BlockPos blockPos) {
        return false;
    }

    @Override
    protected boolean canPathfindThrough(BlockState blockState, NavigationType pathComputationType) {
        return false;
    }

    @Override
    public boolean hasDynamicBounds() {
        return true;
    }

    @Override
    public @NotNull BlockRenderType getRenderType(BlockState blockState) {
        return BlockRenderType.MODEL;
    }

    @Override
    public @NotNull VoxelShape getCameraCollisionShape(BlockState blockState, BlockView blockGetter, BlockPos blockPos, ShapeContext collisionContext) {
        return super.getCameraCollisionShape(blockState, blockGetter, blockPos, collisionContext);
    }

    @Override
    public abstract @Nullable BlockEntity createBlockEntity(BlockPos blockPos, BlockState blockState);

    @Nullable
    @Override
    public abstract <T extends BlockEntity> BlockEntityTicker<T> getTicker(World level, BlockState blockState, BlockEntityType<T> blockEntityType);

    public ArrayList<Direction> getInputAndOutputDirections() {
        return new ArrayList<>(List.of(Direction.values()));
    }
}
