package featherpowders.schematics;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.AnaloguePowerable;
import org.bukkit.block.data.Attachable;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.FaceAttachable;
import org.bukkit.block.data.Levelled;
import org.bukkit.block.data.Lightable;
import org.bukkit.block.data.MultipleFacing;
import org.bukkit.block.data.Openable;
import org.bukkit.block.data.Orientable;
import org.bukkit.block.data.Powerable;
import org.bukkit.block.data.Rotatable;
import org.bukkit.block.data.Snowable;
import org.bukkit.block.data.Waterlogged;
import org.bukkit.block.data.type.BrewingStand;
import org.bukkit.block.data.type.BubbleColumn;
import org.bukkit.block.data.type.Cake;
import org.bukkit.block.data.type.CaveVinesPlant;
import org.bukkit.block.data.type.Farmland;
import org.bukkit.block.data.type.Jigsaw;
import org.bukkit.block.data.type.Jukebox;
import org.bukkit.block.data.type.Leaves;
import org.bukkit.block.data.type.RespawnAnchor;
import org.bukkit.block.data.type.Sapling;
import org.bukkit.block.data.type.Snow;
import org.bukkit.block.data.type.StructureBlock;
import org.bukkit.block.data.type.TNT;
import org.bukkit.block.data.type.TurtleEgg;

import featherpowders.binary.BinaryReader;
import featherpowders.binary.BinaryWriter;
import featherpowders.versions.ServerVersion;

public class Schematic {
    
    private static boolean versionInit = false;
    private static boolean r13;
    private static boolean r14;
    private static boolean r16;
    private static boolean r17;
    
    private static void initVersions() {
        versionInit = true;
        ServerVersion version = ServerVersion.getCurrentVersion();
        r13 = version.inheritFeatures(ServerVersion.R1_13);
        r14 = version.inheritFeatures(ServerVersion.R1_14);
        r16 = version.inheritFeatures(ServerVersion.R1_16);
        r17 = version.inheritFeatures(ServerVersion.R1_17);
    }
    
    private static boolean shouldBeSaved(BlockData data) {
        if (!versionInit) initVersions();
        
        boolean globalCheck =
            (data instanceof Ageable) ||
            (data instanceof AnaloguePowerable) ||
            (data instanceof Attachable) ||
            (data instanceof Bisected) ||
            (data instanceof BrewingStand) ||
            (data instanceof BubbleColumn) ||
            (data instanceof Cake) ||
            (data instanceof Directional) ||
            (data instanceof FaceAttachable) ||
            (data instanceof Farmland) ||
            (data instanceof Jukebox) ||
            (data instanceof Leaves) ||
            (data instanceof Levelled) ||
            (data instanceof Lightable) ||
            (data instanceof MultipleFacing) ||
            (data instanceof Openable) ||
            (data instanceof Orientable) ||
            (data instanceof Powerable) ||
            (data instanceof Rotatable) ||
            (data instanceof Sapling) ||
            (data instanceof Snow) ||
            (data instanceof Snowable) ||
            (data instanceof StructureBlock) ||
            (data instanceof TNT)
        ;
        boolean _r13 = r13 && (
            (data instanceof TurtleEgg) ||
            (data instanceof Waterlogged)
        );
        boolean _r14 = r14 && (
            (data instanceof Jigsaw)
        );
        boolean _r16 = r16 && (
            (data instanceof RespawnAnchor)
        );
        boolean _r17 = r17 && (
            (data instanceof CaveVinesPlant)
        );
        
        return globalCheck || _r13 || _r14 || _r16 || _r17;
    }
    
    private final HashMap<Material, Integer> mappedMaterials = new HashMap<>();
    private final ArrayList<Material> mappedIDs = new ArrayList<>();
    
    public final int width, height, depth, volume;
    public final short[] blocks;
    public final BlockData[] datas;
    
    public Schematic(int width, int height, int depth) {
        this.width = width;
        this.height = height;
        this.depth = depth;
        this.volume = width * height * depth;
        this.blocks = new short[volume];
        this.datas = new BlockData[volume];
        getID(Material.AIR);
    }
    
    public int getID(Material mat) {
        if (!mappedMaterials.containsKey(mat)) {
            mappedMaterials.put(mat, mappedIDs.size());
            mappedIDs.add(mat);
        }
        return mappedMaterials.get(mat);
    }
    
    private int pointerAt(int x, int y, int z) {
        return (y * width * depth) + z * width + x;
    }
    
    public void setBlock(int x, int y, int z, Material mat) {
        int id = getID(mat), ptr = pointerAt(x, y, z);
        blocks[ptr] = (short) id;
        
        BlockData d = mat.createBlockData();
        if (shouldBeSaved(d)) datas[ptr] = d;
        else datas[ptr] = null;
    }
    
    public void setBlock(int x, int y, int z, BlockData data) {
        Material mat = data.getMaterial();
        int id = getID(mat), ptr = pointerAt(x, y, z);
        blocks[ptr] = (short) id;
        if (shouldBeSaved(data)) datas[ptr] = data.clone();
        else datas[ptr] = null;
    }
    
    public void fromWorld(Location lowerLeftBottom) {
        World world = lowerLeftBottom.getWorld();
        int oX = lowerLeftBottom.getBlockX(), oY = lowerLeftBottom.getBlockY(), oZ = lowerLeftBottom.getBlockZ();
        
        for (int y = 0; y < height; y++) for (int z = 0; z < depth; z++) for (int x = 0; x < width; x++) {
            setBlock(x, y, z, world.getBlockAt(oX + x, oY + y, oZ + z).getBlockData());
        }
    }
    
    public void pasteToWorld(Location lowerLeftBottom) {
        World world = lowerLeftBottom.getWorld();
        int oX = lowerLeftBottom.getBlockX(), oY = lowerLeftBottom.getBlockY(), oZ = lowerLeftBottom.getBlockZ(), ptr;
        Block b;
        
        for (int y = 0; y < height; y++) for (int z = 0; z < depth; z++) for (int x = 0; x < width; x++) {
            ptr = pointerAt(x, y, z);
            b = world.getBlockAt(oX + x, oY + y, oZ + z);
            b.setType(mappedIDs.get(blocks[ptr]));
            if (datas[ptr] != null) b.setBlockData(datas[ptr]);
        }
    }
    
    public void writeToStream(OutputStream stream) throws IOException {
        BinaryWriter writer = new BinaryWriter(stream);
        writer.writeVarUint(ServerVersion.getCurrentVersion().dataID);
        
        writer.writeVarUint(width);
        writer.writeVarUint(height);
        writer.writeVarUint(depth);
        
        writer.writeVarUint(mappedIDs.size());
        for (int i = 0; i < mappedIDs.size(); i++) writer.writeString(mappedIDs.get(i).toString());
        
        for (int i = 0; i < volume; i++) writer.writeShort(blocks[i]);
        for (int i = 0; i < volume; i++) if (datas[i] != null) writer.writeString(datas[i].getAsString(true));
    }
    
    public static Schematic readFromStream(InputStream stream) throws IOException {
        BinaryReader reader = new BinaryReader(stream);
        ServerVersion version = ServerVersion.VERSIONS_BY_DATAID[reader.readVarUint()];
        if (!ServerVersion.getCurrentVersion().inheritFeatures(version)) throw new SchematicNotSupportedException(ServerVersion.getCurrentVersion(), version);
        
        int width = reader.readVarUint();
        int height = reader.readVarUint();
        int depth = reader.readVarUint();
        Schematic schematic = new Schematic(width, height, depth);
        
        int mappedIDs = reader.readVarUint();
        schematic.mappedIDs.clear();
        for (int i = 0; i < mappedIDs; i++) {
            Material mat = Material.valueOf(reader.readString());
            schematic.mappedMaterials.put(mat, i);
            schematic.mappedIDs.add(mat);
        }
        
        for (int i = 0; i < schematic.volume; i++) {
            short idS = reader.readShort();
            schematic.blocks[i] = idS;
            
            Material mat = schematic.mappedIDs.get(Short.toUnsignedInt(idS));
            BlockData d = mat.createBlockData();
            if (shouldBeSaved(d)) schematic.datas[i] = d;
            else schematic.datas[i] = null;
        }
        for (int i = 0; i < schematic.volume; i++) if (schematic.datas[i] != null) {
            schematic.datas[i] = Bukkit.createBlockData(reader.readString());
        }
        
        return schematic;
    }
    
    /**
     * Clone the schematic
     */
    @Override
    protected Schematic clone() throws CloneNotSupportedException {
        Schematic newSchem = new Schematic(width, height, depth);
        System.arraycopy(blocks, 0, newSchem.blocks, 0, volume);
        for (int i = 0; i < volume; i++) newSchem.datas[i] = datas[i] == null? null : datas[i].clone();
        return newSchem;
    }
    
}
