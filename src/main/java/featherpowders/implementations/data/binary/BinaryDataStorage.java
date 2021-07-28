package featherpowders.implementations.data.binary;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.plugin.Plugin;

import featherpowders.binary.BinaryReader;
import featherpowders.binary.BinaryWriter;
import featherpowders.data.DataContainer;
import featherpowders.data.DataStorage;

public class BinaryDataStorage implements DataStorage {
    
    private static final int STORAGE_SIGNATURE = 0xFEA1DA1A;
    private static final int STORAGE_VERSION = 1;

    private BinaryDataContainer container;
    private BinaryDataStorage parent;
    private String childKey;
    private HashMap<String, Object> map;
    
    private BinaryDataStorage root;
    private File storageFile;
    
    @SuppressWarnings("unchecked")
    protected BinaryDataStorage(BinaryDataContainer container, String childKey, BinaryDataStorage parent) {
        this.container = container;
        this.parent = parent;
        this.childKey = childKey;
        
        if (this.parent != null) {
            BinaryDataStorage rootStorage = parent;
            while (rootStorage.parent != null) rootStorage = rootStorage.parent;
            root = rootStorage;
            
            if (parent.map.containsKey(childKey)) this.map = (HashMap<String, Object>) parent.map.get(childKey);
            else {
                map = new HashMap<>();
                parent.map.put(childKey, map);
            }
        } else {
            // Root
            storageFile = new File(container.containerDir, childKey + ".bin");
            map = new HashMap<>();
            if (storageFile.exists()) readStorage();
        }
    }
    
    private void readStorage() {
        try {
            FileInputStream stream = new FileInputStream(storageFile);
            BinaryReader reader = new BinaryReader(stream);
            int signature = reader.readInt();
            if (signature != STORAGE_SIGNATURE) throw new RuntimeException("Invalid data storage file (Invalid signature)");
            
            int version = reader.readVarUint();
            if (version != STORAGE_VERSION) throw new RuntimeException("Unsupported data storage version (" + version + " != " + STORAGE_VERSION + ")");
            
            _readMap(map, reader);
            
            stream.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Unable to read data storage");
            map.clear();
        }
    }
    
    private void _readMap(Map<String, Object> map, BinaryReader reader) throws IOException {
        while (reader.readVarUint() == 1) {
            String key = reader.readString();
            int type = reader.readVarUint();
            if (type == 0) map.put(key, (byte) reader.stream().read());
            if (type == 1) map.put(key, reader.readShort());
            if (type == 2) map.put(key, reader.readInt());
            if (type == 3) map.put(key, reader.readLong());
            if (type == 4) map.put(key, reader.readFloat());
            if (type == 5) map.put(key, reader.readDouble());
            if (type == 6) map.put(key, reader.readString());
            if (type == 7) {
                Map<String, Object> mapObj = new HashMap<>();
                _readMap(mapObj, reader);
                map.put(key, mapObj);
            }
        }
    }
    
    @Override
    public DataContainer getContainer() { return container; }

    @Override
    public Plugin getPlugin() { return container.getPlugin(); }

    @Override
    public void saveStorage() {
        if (this.root != null) this.root.saveStorage();
        else try {
            FileOutputStream out = new FileOutputStream(storageFile);
            BinaryWriter writer = new BinaryWriter(out);
            
            writer.writeInt(STORAGE_SIGNATURE);
            writer.writeVarUint(STORAGE_VERSION);
            
            _writeMap(map, writer);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Unable to save data storage");
        }
    }
    
    @SuppressWarnings("unchecked")
    private void _writeMap(Map<String, Object> map, BinaryWriter writer) throws IOException {
        for (Entry<String, Object> entry : map.entrySet()) {
            Object obj = entry.getValue();
            if (obj == null) continue;
            if (obj instanceof Map<?, ?> mapObj && mapObj.size() == 0) continue;
            
            writer.writeVarUint(1); // Has next
            writer.writeString(entry.getKey());
            if (obj instanceof Byte b) { writer.writeVarUint(0); writer.stream().write(Byte.toUnsignedInt(b)); }
            if (obj instanceof Short s) { writer.writeVarUint(1); writer.writeShort(s); }
            if (obj instanceof Integer i) { writer.writeVarUint(2); writer.writeInt(i); }
            if (obj instanceof Long l) { writer.writeVarUint(3); writer.writeLong(l); }
            if (obj instanceof Float f) { writer.writeVarUint(4); writer.writeFloat(f); }
            if (obj instanceof Double d) { writer.writeVarUint(5); writer.writeDouble(d); }
            if (obj instanceof String s) { writer.writeVarUint(6); writer.writeString(s); }
            if (obj instanceof Map<?, ?> mapObj) { writer.writeVarUint(7); _writeMap((Map<String, Object>) mapObj, writer); }
        }
        writer.writeVarUint(0); // End of map
    }

    @Override
    public DataStorage getParent() { return null; }

    @Override
    public boolean exists(String key) { return map.containsKey(key); }

    @Override
    public void set(String key, Object value) {
        // TODO convert the input value to correct type
        if (value == null && map.containsKey(key)) map.remove(key);
        else map.put(key, value);
    }

    @Override
    public byte getByte(String key, byte def) {
        if (!exists(key)) return def;
        Object obj = map.get(key);
        if (obj instanceof Byte) return (byte) obj;
        return def;
    }

    @Override
    public short getShort(String key, short def) {
        if (!exists(key)) return def;
        Object obj = map.get(key);
        if (obj instanceof Short) return (short) obj;
        return def;
    }

    @Override
    public int getInt(String key, int def) {
        if (!exists(key)) return def;
        Object obj = map.get(key);
        if (obj instanceof Integer) return (int) obj;
        return def;
    }

    @Override
    public long getLong(String key, long def) {
        if (!exists(key)) return def;
        Object obj = map.get(key);
        if (obj instanceof Long) return (long) obj;
        return def;
    }

    @Override
    public float getFloat(String key, float def) {
        if (!exists(key)) return def;
        Object obj = map.get(key);
        if (obj instanceof Float) return (float) obj;
        return def;
    }

    @Override
    public double getDouble(String key, double def) {
        if (!exists(key)) return def;
        Object obj = map.get(key);
        if (obj instanceof Double) return (double) obj;
        return def;
    }

    @Override
    public boolean getBoolean(String key, boolean def) {
        if (!exists(key)) return def;
        Object obj = map.get(key);
        if (obj instanceof Boolean) return (boolean) obj;
        return def;
    }

    @Override
    public String getString(String key) {
        if (!exists(key)) return null;
        Object obj = map.get(key);
        if (obj instanceof String) return (String) obj;
        return null;
    }

    @Override
    public byte[] getBytesArray(String key) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public short[] getShortsArray(String key) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int[] getIntsArray(String key) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long[] getLongsArray(String key) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public float[] getFloatsArray(String key) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public double[] getDoublesArray(String key) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Object get(String key) { return map.get(key); }

    @Override
    public DataStorage children(String key) { return new BinaryDataStorage(container, key, this); }

}
