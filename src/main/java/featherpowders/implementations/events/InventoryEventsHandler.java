package featherpowders.implementations.events;

import featherpowders.FeatherPowders;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

import featherpowders.ui.PlayerUIData;
import org.bukkit.event.inventory.InventoryDragEvent;

public class InventoryEventsHandler implements Listener {
    private final FeatherPowders plugin;

    public InventoryEventsHandler(FeatherPowders plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = Bukkit.getPlayer(event.getWhoClicked().getUniqueId());
        if (player == null) return;
        
        PlayerUIData uiData = PlayerUIData.getData(player);
        if (uiData.activeChestUI != null) uiData.activeChestUI.passClickEvent(event);
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        Player player = Bukkit.getPlayer(event.getWhoClicked().getUniqueId());
        if (player == null) return;

        PlayerUIData uiData = PlayerUIData.getData(player);
        if (uiData.activeChestUI != null) {
            Bukkit.getScheduler().runTask(plugin, () -> event.setCancelled(uiData.activeChestUI.isDragEventCanceled()));
        }
    }
    
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Player player = Bukkit.getPlayer(event.getPlayer().getUniqueId());
        if (player == null) return;

        PlayerUIData uiData = PlayerUIData.getData(player);
        uiData.activeChestUI = null;
    }
    
}
