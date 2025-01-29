package teddy.minecraftautomation.blocks;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import teddy.minecraftautomation.MinecraftAutomation;
import teddy.minecraftautomation.utils.OrientedContainer;
import teddy.minecraftautomation.utils.Tooltip;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractPumpBlock extends BlockWithEntity implements OrientedContainer {
    public static final EnumProperty<Direction.Axis> AXIS = Properties.AXIS;
    public static final BooleanProperty NEGATIVE_AXIS = BooleanProperty.of("negative_axis");
    private final Map<String, VoxelShape> SHAPES;

    public final int inducedPressure;

    public static final Tooltip INDUCED_PRESSURE_TOOLTIP = new Tooltip(MinecraftAutomation.MOD_ID, "pump", "induced_pressure", "Induced pressure: %spu");

    public AbstractPumpBlock(int inducedPressure, Settings properties) {
        super(properties);

        this.inducedPressure = inducedPressure;

        this.setDefaultState(this.getDefaultState()
                .with(AXIS, Direction.Axis.Y)
                .with(NEGATIVE_AXIS, false));

        this.SHAPES = createShapes();
    }

    protected Map<String, VoxelShape> createShapes() {
        float heightOffset = 5f;
        float radius = 3f;
        float height = heightOffset + radius * 2;

        float radiusPlus = 8f + radius;
        float radiusMinus = 8f - radius;

        VoxelShape X = Block.createCuboidShape(0f, heightOffset, radiusMinus, 16f, height, radiusPlus);
        VoxelShape Y = Block.createCuboidShape(radiusMinus, 0f, radiusMinus, radiusPlus, 16f, radiusPlus);
        VoxelShape Z = Block.createCuboidShape(radiusMinus, heightOffset, 0f, radiusPlus, height, 16f);

        Map<String, VoxelShape> voxelShapes = new HashMap<>();
        voxelShapes.put("x", X);
        voxelShapes.put("y", Y);
        voxelShapes.put("z", Z);

        return voxelShapes;
    }

    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(AXIS, NEGATIVE_AXIS);
    }

    public BlockState getPlacementState(ItemPlacementContext blockPlaceContext) {
        Direction.Axis axis = blockPlaceContext.getSide().getAxis();
        Direction axisPositive = axis.getPositiveDirection();

        if (axis == Direction.Axis.X)
            axisPositive = axis.getNegativeDirection();

        return this.getDefaultState()
                .with(AXIS, axis)
                // If the block was placed in the positive axis
                .with(NEGATIVE_AXIS, axisPositive == blockPlaceContext.getSide());
    }

    @Override
    public void appendTooltip(ItemStack itemStack, Item.TooltipContext tooltipContext, List<Text> list, TooltipType tooltipFlag) {
        list.add(Text.translatable(AbstractPipeBlock.STATS_TOOLTIP.getTranslationKey()).formatted(Formatting.DARK_GRAY));
        list.add(Text.translatable(INDUCED_PRESSURE_TOOLTIP.getTranslationKey(), this.inducedPressure).formatted(Formatting.DARK_GRAY));
    }

    @Override
    public @NotNull VoxelShape getRaycastShape(BlockState state, BlockView blockGetter, BlockPos blockPos) {
        Direction.Axis value = state.get(AXIS);
        VoxelShape shape = this.SHAPES.get("z");

        if (value == Direction.Axis.X) shape = this.SHAPES.get("x");
        if (value == Direction.Axis.Y) shape = this.SHAPES.get("y");

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

    @Override
    protected abstract @NotNull MapCodec<? extends BlockWithEntity> getCodec();

    @Override
    public abstract @Nullable BlockEntity createBlockEntity(BlockPos blockPos, BlockState blockState);

    @Nullable
    @Override
    public abstract <T extends BlockEntity> BlockEntityTicker<T> getTicker(World level, BlockState blockState, BlockEntityType<T> blockEntityType);

    public ArrayList<Direction> getInputDirections(@NotNull BlockState blockState) {
        ArrayList<Direction> directions = new ArrayList<>();

        if (blockState.get(NEGATIVE_AXIS)) {
            directions.add(blockState.get(AXIS).getPositiveDirection());
        } else {
            directions.add(blockState.get(AXIS).getNegativeDirection());
        }

        return directions;
    }

    public ArrayList<Direction> getOutputDirections(@NotNull BlockState blockState) {
        ArrayList<Direction> directions = new ArrayList<>();

        if (blockState.get(NEGATIVE_AXIS)) {
            directions.add(blockState.get(AXIS).getNegativeDirection());
        } else {
            directions.add(blockState.get(AXIS).getPositiveDirection());
        }

        return directions;
    }
}
