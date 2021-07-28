package featherpowders;

import org.bukkit.plugin.java.JavaPlugin;

import featherpowders.data.DataDriver;
import featherpowders.implementations.Implementations;

public class FeatherPowders extends JavaPlugin {
    
    @Override
    public void onEnable() {
        saveResource("config.yml", false);
        reloadConfig();
        
        DataDriver.setupGlobal(this);
        
        Implementations.init(this);
    }
    
}