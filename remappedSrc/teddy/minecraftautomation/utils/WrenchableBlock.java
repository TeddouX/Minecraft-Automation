package teddy.minecraftautomation.utils;

import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.Direction;

public interface WrenchableBlock {
    BooleanProperty WRENCHED_NORTH = BooleanProperty.of("wrenched_north");
    BooleanProperty WRENCHED_EAST = BooleanProperty.of("wrenched_east");
    BooleanProperty WRENCHED_SOUTH = BooleanProperty.of("wrenched_south");
    BooleanProperty WRENCHED_WEST = BooleanProperty.of("wrenched_west");
    BooleanProperty WRENCHED_UP = BooleanProperty.of("wrenched_up");
    BooleanProperty WRENCHED_DOWN = BooleanProperty.of("wrenched_down");

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
