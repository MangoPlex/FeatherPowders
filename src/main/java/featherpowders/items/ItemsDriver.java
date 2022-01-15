package featherpowders.items;

import java.util.ArrayList;
import java.util.Collection;

import org.bukkit.inventory.ItemStack;

import featherpowders.drivers.Driver;

/**
 * Items driver. This abstract class let you to create your own items driver, which allow other plugins to
 * access to your custom items
 * @author nahkd
 *
 */
public abstract class ItemsDriver<T extends CustomType, S extends CustomStack> extends Driver {
    
    public final Class<T> typeType;
    
    public ItemsDriver(Class<T> typeType) { this.typeType = typeType; }
    
    private static ArrayList<ItemsDriver<?, ?>> drivers = new ArrayList<>();
    private static ItemsDriver<CustomType, CustomStack> defaultDriver = null;
    
    /**
     * Get the driver from custom items type. The type must be equals to the driver type.
     * @param <T>
     * @param <S>
     * @param typeType
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T extends CustomType, S extends CustomStack> ItemsDriver<T, S> getDriver(Class<T> typeType) {
        for (ItemsDriver<?, ?> d : drivers) if (d.typeType == typeType) return (ItemsDriver<T, S>) d;
        return null;
    }
    
    @SuppressWarnings("unchecked")
    public static ItemsDriver<CustomType, CustomStack> getDefaultDriver() {
        return defaultDriver != null? defaultDriver : drivers.size() == 0? null : (ItemsDriver<CustomType, CustomStack>) drivers.get(0);
    }
    
    public static Collection<ItemsDriver<?, ?>> getDrivers() { return drivers; }

    /**
     * Get all available types from this items driver
     * @return
     */
    public abstract Collection<T> getAllTypes();
    
    /**
     * Get the item type from data ID
     * @param id
     * @return
     */
    public abstract T fromDataID(String id);
    
    /**
     * Create new custom stack with given type and amount
     * @param type
     * @param amount
     * @return
     */
    public abstract S createItem(T type, int amount);
    public S createItem(T type) { return createItem(type, 1); }
    
    /**
     * Convert {@link ItemStack} from Bukkit API to custom stack. If the given stack is not valid, it will returns null
     * @param stack
     * @return
     */
    public abstract S fromBukkit(ItemStack stack);
    
    /**
     * Convert {@link CustomStack} to {@link ItemStack} from Bukkit API. If the given stack is not valid (such as null
     * stack or corrupted stack data), it will returns null
     * @param stack
     * @return
     */
    public abstract ItemStack fromCustom(S stack);
    
    /**
     * Get driver name. The name is the class name of this driver instance
     */
    @Override
    public String getDriverName() { return this.getClass().getName(); }
    @Override
    public String getDriverCategory() { return "item"; }
    @Override
    public boolean isDefault() { return this == getDefaultDriver(); }
    
    @SuppressWarnings("unchecked")
    public S[] fromBukkit(ItemStack[] stacks) {
        CustomStack[] stacksOut = new CustomStack[stacks.length];
        for (int i = 0; i < stacks.length; i++) {
            if (stacks[i] == null) continue;
            stacksOut[i] = fromBukkit(stacks[i]);
        }
        return (S[]) stacksOut;
    }
    
    public ItemStack[] fromCustom(S[] stacks) {
        ItemStack[] stacksOut = new ItemStack[stacks.length];
        for (int i = 0; i < stacks.length; i++) {
            if (stacks[i] == null) continue;
            stacksOut[i] = fromCustom(stacks[i]);
        }
        return stacksOut;
    }
    
    /**
     * Register this driver so other plugins can use it
     */
    public void registerDriver() {
        drivers.add(this);
        addDriverToMap(this);
    }
    
}
