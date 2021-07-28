package featherpowders.ui.chest;

import org.bukkit.event.inventory.InventoryClickEvent;

@FunctionalInterface
public interface SlotHandler {
    
    public void onClick(InventoryClickEvent event);
    
}
