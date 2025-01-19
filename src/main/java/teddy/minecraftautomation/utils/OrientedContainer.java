package teddy.minecraftautomation.utils;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;

public interface OrientedContainer {
    ArrayList<Direction> getItemInventoryInputDirections(BlockState blockState);
    ArrayList<Direction> getItemInventoryOutputDirections(BlockState blockState);
    ArrayList<Direction> getFluidContainerInputDirections(BlockState blockState);
    ArrayList<Direction> getFluidContainerOutputDirections(BlockState blockState);
}
