package teddy.minecraftautomation.utils;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.Direction;

import java.util.ArrayList;

public interface OrientedContainer {
    ArrayList<Direction> getItemInventoryInputDirections(BlockState blockState);
    ArrayList<Direction> getItemInventoryOutputDirections(BlockState blockState);
    ArrayList<Direction> getFluidContainerInputDirections(BlockState blockState);
    ArrayList<Direction> getFluidContainerOutputDirections(BlockState blockState);
}
