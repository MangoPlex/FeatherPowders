package featherpowders.ui.chest;

import org.bukkit.event.inventory.InventoryClickEvent;

@FunctionalInterface
public interface SlotHandler {
    void onClick(InventoryClickEvent event);

}
