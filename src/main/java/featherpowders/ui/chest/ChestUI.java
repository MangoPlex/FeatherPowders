package featherpowders.ui.chest;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import featherpowders.ui.PlayerUI;

/**
 * Chest UI abstract class. You can create your own chest UI by extends this class and use methods
 * to set item inside UI. To open UI, please use {@link PlayerUI#openUI(Player, ChestUI)}
 * @author nahkd
 * @see #set(int, int, ItemStack, SlotHandler)
 * @see #clearSlot(int, int)
 * @see PlayerUI#openUI(Player, ChestUI)
 *
 */
public abstract class ChestUI {
    protected Player player;
    protected Inventory inventory;
    private SlotHandler[] handlers;

    private boolean cancelDragEvent = false;

    /**
     * Initialize chest UI with given parameters
     * @param targetedPlayer The player that belongs to this UI
     * @param title The UI title. Cannot be changed
     * @param rows Number of rows to create. In legacy version, you can have up to 9 rows.
     */
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

    /**
     * Get the underlying inventory. To open UI correctly, please use {@link PlayerUI#openUI(Player, ChestUI)}
     * @return
     * @see PlayerUI#openUI(Player, ChestUI)
     */
    public Inventory getInventory() { return inventory; }
    
    /**
     * Clear item and events handler
     * @param x X position (starts from 0)
     * @param y Y position (starts from 0)
     */
    public void clearSlot(int x, int y) {
        inventory.setItem(y * 9 + x, null);
        handlers[y * 9 + x] = null;
    }
    
    /**
     * Set the item + events handler at targeted XY
     * @param x X position (starts from 0)
     * @param y Y position (starts from 0)
     * @param item The item, which is used for displaying
     * @param handler The event handler, which handles click events
     * @see #set(int, ItemStack, SlotHandler)
     * @see #clearSlot(int, int)
     */
    public void set(int x, int y, ItemStack item, SlotHandler handler) {
        inventory.setItem(y * 9 + x, item);
        handlers[y * 9 + x] = handler;
    }
    
    /**
     * Set the item + events handler at targeted slot index
     * @param slot The slot index (starts at 0)
     * @param item The item
     * @param handler The events handler
     * @see #set(int, int, ItemStack, SlotHandler)
     */
    public void set(int slot, ItemStack item, SlotHandler handler) {
        inventory.setItem(slot, item);
        handlers[slot] = handler;
    }

    /**
     * Failback for slots without events handler. This must be implemented to ensure no unexpected
     * behavior is occurred
     * @param clickEvent
     */
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
