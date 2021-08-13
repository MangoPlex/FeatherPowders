package featherpowders.enums;

import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Rotatable;

public enum SquareRotation {
    
    NO_ROTATE,
    ROTATE_90_RIGHT,
    ROTATE_90_LEFT,
    ROTATE_180;
    
    public void rotateData(BlockData data) {
        if (data instanceof Rotatable rotatable) rotatable.setRotation(rotate(rotatable.getRotation()));
    }
    
    public BlockFace rotate(BlockFace face) {
        if (this == NO_ROTATE) return face;
        if (this == ROTATE_180) return face.getOppositeFace();
        if (this == ROTATE_90_RIGHT) return switch(face) {
        case NORTH -> BlockFace.EAST;
        case EAST -> BlockFace.SOUTH;
        case SOUTH -> BlockFace.WEST;
        case WEST -> BlockFace.NORTH;
        default -> face;
        };
        if (this == ROTATE_90_LEFT) return switch(face) {
        case NORTH -> BlockFace.WEST;
        case WEST -> BlockFace.SOUTH;
        case SOUTH -> BlockFace.EAST;
        case EAST -> BlockFace.NORTH;
        default -> face;
        };
        return null;
    }
    
}
