package featherpowders.data;

import org.bukkit.plugin.Plugin;

/**
 * Data storage (part of Data Driver API)
 * @author nahkd
 *
 */
public interface DataStorage {
    
    public DataContainer getContainer();
    public Plugin getPlugin();
    
    /**
     * Save the data storage. It will save the root storage
     */
    public void saveStorage();
    
    /**
     * Get parent data storage. If this value is null, there is no parent
     * @return
     */
    public DataStorage getParent();
    
    /**
     * Check if the key exists in storage
     * @param key
     * @return
     */
    public boolean exists(String key);
    
    /**
     * Set the value in storage. The actual key might be changed, depends on driver type (eg: YAML will convert dots "." to
     * dash "-")
     * @param key
     * @param value
     */
    public void set(String key, Object value);
    
    /**
     * Get the byte value
     * @param key
     * @param def
     * @return
     */
    public byte getByte(String key, byte def);
    
    /**
     * Get the short value
     * @param key
     * @param def
     * @return
     */
    public short getShort(String key, short def);
    
    /**
     * Get the 32 bit integer value
     * @param key
     * @param def
     * @return
     */
    public int getInt(String key, int def);
    
    /**
     * Get the 64 bit integer value
     * @param key
     * @param def
     * @return
     */
    public long getLong(String key, long def);
    
    /**
     * Get the single precision float value
     * @param key
     * @param def
     * @return
     */
    public float getFloat(String key, float def);
    
    /**
     * Get the double precision float value
     * @param key
     * @param def
     * @return
     */
    public double getDouble(String key, double def);
    
    /**
     * Get the boolean value
     * @param key
     * @param def
     * @return
     */
    public boolean getBoolean(String key, boolean def);
    
    /**
     * Get the string value, or null if it's doesn't exists
     * @param key
     * @return
     */
    public String getString(String key);
    
    public byte[] getBytesArray(String key);
    public short[] getShortsArray(String key);
    public int[] getIntsArray(String key);
    public long[] getLongsArray(String key);
    public float[] getFloatsArray(String key);
    public double[] getDoublesArray(String key);
    
    /**
     * Get the value from storage. Primitives arrays are primitiveType[], and complex arrays are List. Primitives default values
     * are always 0 or false
     * @param key
     * @return
     */
    public Object get(String key);
    
    /**
     * Get children storage if it's exists or create new one if it's doesn't
     * @param key
     * @return
     */
    public DataStorage children(String key);
    
}
