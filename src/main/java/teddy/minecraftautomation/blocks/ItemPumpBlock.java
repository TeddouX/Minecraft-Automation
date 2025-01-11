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
import teddy.minecraftautomation.blocks.entity.ItemPumpBlockEntity;
import teddy.minecraftautomation.blocks.entity.ModBlockEntities;
import teddy.minecraftautomation.utils.OrientedInventory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemPumpBlock extends BaseEntityBlock implements OrientedInventory {
    public static final EnumProperty<Direction.Axis> AXIS = BlockStateProperties.AXIS;
    public static final BooleanProperty NEGATIVE_AXIS = BooleanProperty.create("negative_axis");
    private final Map<String, VoxelShape> SHAPES;

    private final int itemsPerTransfer;
    private final int transferCooldown;
    private final int inducedPressure;

    public ItemPumpBlock(int inducedPressure, int itemsPerTransfer, int transferCooldown, Properties properties) {
        super(properties);

        this.registerDefaultState(this.defaultBlockState()
                .setValue(AXIS, Direction.Axis.Y)
                .setValue(NEGATIVE_AXIS, false));

        this.itemsPerTransfer = itemsPerTransfer;
        this.transferCooldown = transferCooldown;
        this.inducedPressure = inducedPressure;

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
        return this.defaultBlockState()
                .setValue(AXIS, blockPlaceContext.getClickedFace().getAxis())
                // If the block was placed in the negative axis
                .setValue(NEGATIVE_AXIS, blockPlaceContext.getClickedFace().getAxis().getNegative() == blockPlaceContext.getClickedFace());
    }

    @Override
    public void appendHoverText(ItemStack itemStack, Item.TooltipContext tooltipContext, List<Component> list, TooltipFlag tooltipFlag) {
        list.add(Component.translatable("block.minecraft-automation.item_pipe.tooltip.items_per_transfer", this.itemsPerTransfer)
                .withStyle(ChatFormatting.DARK_GRAY));

        float transferCooldownSeconds = (((float) this.transferCooldown)) / 20f;

        list.add(Component.translatable("block.minecraft-automation.item_pipe.tooltip.transfer_cooldown",
                // Round to .01
                ((float) Math.round(transferCooldownSeconds * 100f)) / 100f).withStyle(ChatFormatting.DARK_GRAY));

        list.add(Component.translatable("block.minecraft-automation.item_pump.tooltip.induced_pressure", this.inducedPressure)
                .withStyle(ChatFormatting.DARK_GRAY));
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
    protected @NotNull MapCodec<? extends BaseEntityBlock> codec() {
        return simpleCodec((Properties properties) -> new ItemPumpBlock(0, 0, 0, properties));
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new ItemPumpBlockEntity(blockPos, blockState, this.inducedPressure, this.itemsPerTransfer, this.transferCooldown);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        return level.isClientSide() ? null : BaseEntityBlock.createTickerHelper(blockEntityType, ModBlockEntities.ITEM_PUMP_BE, ItemPumpBlockEntity::tick);
    }

    public ArrayList<Direction> getInputDirections(BlockState blockState) {
        ArrayList<Direction> directions = new ArrayList<>();

        if (blockState.getValue(NEGATIVE_AXIS)) {
            directions.add(blockState.getValue(AXIS).getNegative());
        } else {
            directions.add(blockState.getValue(AXIS).getPositive());
        }

        return directions;
    }

    public ArrayList<Direction> getOutputDirections(BlockState blockState) {
        ArrayList<Direction> directions = new ArrayList<>();

        if (blockState.getValue(NEGATIVE_AXIS)) {
            directions.add(blockState.getValue(AXIS).getPositive());
        } else {
            directions.add(blockState.getValue(AXIS).getNegative());
        }

        return directions;
    }
}
