package teddy.minecraftautomation.blocks;

import com.mojang.serialization.MapCodec;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import teddy.minecraftautomation.MinecraftAutomation;
import teddy.minecraftautomation.blocks.entity.ItemPumpBlockEntity;
import teddy.minecraftautomation.blocks.entity.ModBlockEntities;
import teddy.minecraftautomation.utils.Tooltip;

import java.util.ArrayList;
import java.util.List;

public class ItemPumpBlock extends AbstractPumpBlock {
    private final int itemsPerTransfer;
    private final int transferCooldown;
    private final int inducedPressure;

    public static final Tooltip ITEMS_PER_TRANSFER_TOOLTIP = new Tooltip(MinecraftAutomation.MOD_ID, "item_pump", "items_per_transfer", "Items per transfer: %s");
    public static final Tooltip TRANSFER_COOLDOW_TOOLTIP = new Tooltip(MinecraftAutomation.MOD_ID, "item_pump", "transfer_cooldown", "Transfer cooldown: %ss");
    public static final Tooltip INDUCED_PRESSURE_TOOLTIP = new Tooltip(MinecraftAutomation.MOD_ID, "item_pump", "induced_pressure", "Induced pressure: %spu");

    public ItemPumpBlock(int inducedPressure, int itemsPerTransfer, int transferCooldown, Properties properties) {
        super(properties);

        this.itemsPerTransfer = itemsPerTransfer;
        this.transferCooldown = transferCooldown;
        this.inducedPressure = inducedPressure;
    }

    @Override
    public void appendHoverText(ItemStack itemStack, Item.TooltipContext tooltipContext, List<Component> list, TooltipFlag tooltipFlag) {
        list.add(Component.translatable(ITEMS_PER_TRANSFER_TOOLTIP.getTranslationKey(), this.itemsPerTransfer)
                .withStyle(ChatFormatting.DARK_GRAY));

        // From ticks to seconds
        float transferCooldownSeconds = (((float) this.transferCooldown)) / 20f;

        list.add(Component.translatable(TRANSFER_COOLDOW_TOOLTIP.getTranslationKey(),
                // Round to .01
                ((float) Math.round(transferCooldownSeconds * 100f)) / 100f).withStyle(ChatFormatting.DARK_GRAY));

        list.add(Component.translatable(INDUCED_PRESSURE_TOOLTIP.getTranslationKey(), this.inducedPressure)
                .withStyle(ChatFormatting.DARK_GRAY));
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

    @Override
    public ArrayList<Direction> getItemInventoryInputDirections(BlockState blockState) {
        return this.getInputDirections(blockState);
    }

    @Override
    public ArrayList<Direction> getItemInventoryOutputDirections(BlockState blockState) {
        return this.getOutputDirections(blockState);
    }

    @Override
    public ArrayList<Direction> getFluidContainerInputDirections(BlockState blockState) {
        return new ArrayList<>();
    }

    @Override
    public ArrayList<Direction> getFluidContainerOutputDirections(BlockState blockState) {
        return new ArrayList<>();
    }
}
