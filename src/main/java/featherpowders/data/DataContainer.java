package featherpowders.data;

import org.bukkit.plugin.Plugin;

/**
 * Data container (part of Data Driver API)
 * @author nahkd
 *
 */
public interface DataContainer {
    
    /**
     * Get the storage (if it's already exists) or create new one
     * @param storageName
     * @return
     */
    public DataStorage getOrCreate(String storageName);
    
    /**
     * Get the plugin that's linked to this container
     * @return
     */
    public Plugin getPlugin();
    
}
