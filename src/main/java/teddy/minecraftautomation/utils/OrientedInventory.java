package teddy.minecraftautomation.utils;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;

public interface OrientedInventory {
    ArrayList<Direction> getInputDirections(BlockState blockState);
    ArrayList<Direction> getOutputDirections(BlockState blockState);
}
