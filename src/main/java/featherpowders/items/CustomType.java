package featherpowders.items;

/**
 * Custom item type, handled by the items driver
 * @author nahkd
 *
 */
public abstract class CustomType {
    
    /**
     * Data ID. This field will be used by plugins
     */
    public final String dataId;
    
    public CustomType(String dataId) { this.dataId = dataId; }
    
}
