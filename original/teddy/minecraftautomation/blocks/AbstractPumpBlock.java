package teddy.minecraftautomation.blocks;

import com.mojang.serialization.MapCodec;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
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
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import teddy.minecraftautomation.MinecraftAutomation;
import teddy.minecraftautomation.utils.OrientedContainer;
import teddy.minecraftautomation.utils.Tooltip;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractPumpBlock extends BaseEntityBlock implements OrientedContainer {
    public static final EnumProperty<Direction.Axis> AXIS = BlockStateProperties.AXIS;
    public static final BooleanProperty NEGATIVE_AXIS = BooleanProperty.create("negative_axis");
    private final Map<String, VoxelShape> SHAPES;

    public final int inducedPressure;

    public static final Tooltip INDUCED_PRESSURE_TOOLTIP = new Tooltip(MinecraftAutomation.MOD_ID, "pump", "induced_pressure", "Induced pressure: %spu");

    public AbstractPumpBlock(int inducedPressure, Properties properties) {
        super(properties);

        this.inducedPressure = inducedPressure;

        this.registerDefaultState(this.defaultBlockState()
                .setValue(AXIS, Direction.Axis.Y)
                .setValue(NEGATIVE_AXIS, false));

        this.SHAPES = createShapes();
    }

    protected Map<String, VoxelShape> createShapes() {
        float heightOffset = 5f;
        float radius = 3f;
        float height = heightOffset + radius * 2;

        float radiusPlus = 8f + radius;
        float radiusMinus = 8f - radius;

        VoxelShape X = Block.box(0f, heightOffset, radiusMinus, 16f, height, radiusPlus);
        VoxelShape Y = Block.box(radiusMinus, 0f, radiusMinus, radiusPlus, 16f, radiusPlus);
        VoxelShape Z = Block.box(radiusMinus, heightOffset, 0f, radiusPlus, height, 16f);

        Map<String, VoxelShape> voxelShapes = new HashMap<>();
        voxelShapes.put("x", X);
        voxelShapes.put("y", Y);
        voxelShapes.put("z", Z);

        return voxelShapes;
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AXIS, NEGATIVE_AXIS);
    }

    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        Direction.Axis axis = blockPlaceContext.getClickedFace().getAxis();
        Direction axisPositive = axis.getPositive();

        if (axis == Direction.Axis.X)
            axisPositive = axis.getNegative();

        return this.defaultBlockState()
                .setValue(AXIS, axis)
                // If the block was placed in the positive axis
                .setValue(NEGATIVE_AXIS, axisPositive == blockPlaceContext.getClickedFace());
    }

    @Override
    public void appendHoverText(ItemStack itemStack, Item.TooltipContext tooltipContext, List<Component> list, TooltipFlag tooltipFlag) {
        list.add(Component.translatable(AbstractPipeBlock.STATS_TOOLTIP.getTranslationKey()).withStyle(ChatFormatting.DARK_GRAY));
        list.add(Component.translatable(INDUCED_PRESSURE_TOOLTIP.getTranslationKey(), this.inducedPressure).withStyle(ChatFormatting.DARK_GRAY));
    }

    @Override
    public @NotNull VoxelShape getInteractionShape(BlockState state, BlockGetter blockGetter, BlockPos blockPos) {
        Direction.Axis value = state.getValue(AXIS);
        VoxelShape shape = this.SHAPES.get("z");

        if (value == Direction.Axis.X) shape = this.SHAPES.get("x");
        if (value == Direction.Axis.Y) shape = this.SHAPES.get("y");

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

    @Override
    protected abstract @NotNull MapCodec<? extends BaseEntityBlock> codec();

    @Override
    public abstract @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState);

    @Nullable
    @Override
    public abstract <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType);

    public ArrayList<Direction> getInputDirections(@NotNull BlockState blockState) {
        ArrayList<Direction> directions = new ArrayList<>();

        if (blockState.getValue(NEGATIVE_AXIS)) {
            directions.add(blockState.getValue(AXIS).getPositive());
        } else {
            directions.add(blockState.getValue(AXIS).getNegative());
        }

        return directions;
    }

    public ArrayList<Direction> getOutputDirections(@NotNull BlockState blockState) {
        ArrayList<Direction> directions = new ArrayList<>();

        if (blockState.getValue(NEGATIVE_AXIS)) {
            directions.add(blockState.getValue(AXIS).getNegative());
        } else {
            directions.add(blockState.getValue(AXIS).getPositive());
        }

        return directions;
    }
}
