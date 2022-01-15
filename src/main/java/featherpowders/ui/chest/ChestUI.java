package featherpowders.ui.chest;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public abstract class ChestUI {
    protected Player player;
    protected Inventory inventory;
    private SlotHandler[] handlers;

    private boolean cancelDragEvent = false;

    public ChestUI(Player targetedPlayer, String title, int rows) {
        player = targetedPlayer;
        inventory = Bukkit.createInventory(null, rows * 9, title);
        handlers = new SlotHandler[rows * 9];
    }

    public void setCancelDragEvent(boolean b) {
        this.cancelDragEvent = b;
    }

    public boolean isDragEventCanceled() {
        return this.cancelDragEvent;
    }

    public Inventory getInventory() { return inventory; }
    
    public void clearSlot(int x, int y) {
        inventory.setItem(y * 9 + x, null);
        handlers[y * 9 + x] = null;
    }
    
    public void set(int x, int y, ItemStack item, SlotHandler handler) {
        inventory.setItem(y * 9 + x, item);
        handlers[y * 9 + x] = handler;
    }
    
    public void set(int slot, ItemStack item, SlotHandler handler) {
        inventory.setItem(slot, item);
        handlers[slot] = handler;
    }

    public abstract void failback(InventoryClickEvent clickEvent);
    
    public void passClickEvent(InventoryClickEvent event) {
        if (event.getClickedInventory() != inventory) {
            event.setCancelled(true);
            return;
        }
        
        int slot = event.getSlot();
        if (handlers[slot] == null) failback(event);
        else handlers[slot].onClick(event);
    }

}
