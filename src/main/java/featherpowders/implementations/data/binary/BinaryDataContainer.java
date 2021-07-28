package featherpowders.implementations.data.binary;

import java.io.File;

import org.bukkit.plugin.Plugin;

import featherpowders.data.DataContainer;
import featherpowders.data.DataStorage;

public class BinaryDataContainer implements DataContainer {
    
    private Plugin plugin;
    protected File containerDir;
    
    protected BinaryDataContainer(Plugin plugin) {
        this.plugin = plugin;
        this.containerDir = new File(plugin.getDataFolder(), "featherpowders-binary");
        if (!this.containerDir.exists()) this.containerDir.mkdirs();
    }

    @Override
    public DataStorage getOrCreate(String storageName) {
        return new BinaryDataStorage(this, storageName, null);
    }

    @Override
    public Plugin getPlugin() { return plugin; }

}
