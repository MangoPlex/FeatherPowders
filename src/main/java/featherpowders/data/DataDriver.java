package featherpowders.data;

import java.util.ArrayList;
import java.util.Collection;

import org.bukkit.plugin.Plugin;

import featherpowders.FeatherPowders;
import featherpowders.drivers.Driver;

/**
 * Data driver. This abstract class let you create your own data driver, which allow other plugins to store data
 * in format of your choice. The user can choose which driver they want to use.
 * @author nahkd
 *
 */
public abstract class DataDriver extends Driver {
    
    /**
     * Get the driver name. This will be used to determine which driver should be used by default (which is determined by
     * user)
     * @return
     */
    public abstract String getDriverName();
    
    /**
     * Get data container for plugin. If the container does not exists, it will be created
     * @param plugin
     * @return
     */
    public abstract DataContainer getContainerFor(Plugin plugin);
    
    /**
     * Register driver and make it publicly available for other plugins
     */
    public void registerDriver() {
        if (getDriver(getDriverName()) != null) throw new RuntimeException("The driver with name '" + getDriverName() + "' already registered! Perhaps plugins conflict occured?");
        drivers.add(this);
        if (getDriverName().equalsIgnoreCase(plugin.getConfig().getString("Drivers.Data.Default Driver"))) defaultDriver = this;
    }
    
    
    
    private static FeatherPowders plugin;
    public static void setupGlobal(FeatherPowders p) { plugin = p; }
    
    private static DataDriver defaultDriver = null;
    private static ArrayList<DataDriver> drivers = new ArrayList<>();
    
    public static DataDriver getDefaultDriver() {
        return defaultDriver != null? defaultDriver : drivers.size() > 0? drivers.get(0) : null;
    }
    
    public static DataDriver getDriver(String name) {
        for (DataDriver driver : drivers) if (driver.getDriverName().equalsIgnoreCase(name.toLowerCase())) return driver;
        return null;
    }
    
    public static Collection<DataDriver> getAllDrivers() { return drivers; }
    
}
