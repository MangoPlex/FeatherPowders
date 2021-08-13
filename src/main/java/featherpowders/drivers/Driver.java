package featherpowders.drivers;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * FeatherPowders driver class. This class let you create your own driver and assign it so other plugins can use it.
 * @author nahkd
 *
 */
public abstract class Driver {
    
    /**
     * Get the driver name. This will be used to determine which driver should be used by default (which is determined by
     * user)
     * @return
     */
    public abstract String getDriverName();
    
    /**
     * Check if this driver is default
     * @return
     */
    public boolean isDefault() { return false; }
    
    /**
     * Get the driver category
     * @return
     */
    public String getDriverCategory() { return "unknown"; }
    
    public static final HashMap<String, ArrayList<Driver>> categorizedDrivers = new HashMap<>();
    public static void addDriverToMap(Driver driver) {
        if (!categorizedDrivers.containsKey(driver.getDriverCategory())) {
            ArrayList<Driver> driverList = new ArrayList<>();
            driverList.add(driver);
            categorizedDrivers.put(driver.getDriverCategory(), driverList);
            return;
        }
        
        categorizedDrivers.get(driver.getDriverCategory()).add(driver);
    }
    
}
