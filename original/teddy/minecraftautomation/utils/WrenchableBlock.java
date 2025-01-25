package teddy.minecraftautomation.utils;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

public interface WrenchableBlock {
    BooleanProperty WRENCHED_NORTH = BooleanProperty.create("wrenched_north");
    BooleanProperty WRENCHED_EAST = BooleanProperty.create("wrenched_east");
    BooleanProperty WRENCHED_SOUTH = BooleanProperty.create("wrenched_south");
    BooleanProperty WRENCHED_WEST = BooleanProperty.create("wrenched_west");
    BooleanProperty WRENCHED_UP = BooleanProperty.create("wrenched_up");
    BooleanProperty WRENCHED_DOWN = BooleanProperty.create("wrenched_down");

    default BooleanProperty getWrenchedPropertyFromDirection(Direction dir) {
        return switch (dir) {
            case NORTH -> WRENCHED_NORTH;
            case EAST -> WRENCHED_EAST;
            case SOUTH -> WRENCHED_SOUTH;
            case WEST -> WRENCHED_WEST;
            case UP -> WRENCHED_UP;
            case DOWN -> WRENCHED_DOWN;
        };
    }
}
