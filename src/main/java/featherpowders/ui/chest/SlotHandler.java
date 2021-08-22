package featherpowders.ui.chest;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;

@FunctionalInterface
public interface SlotHandler {
    void onClick(InventoryClickEvent event);

}
