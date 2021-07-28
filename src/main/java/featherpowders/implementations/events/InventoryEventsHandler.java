package featherpowders.implementations.events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

import featherpowders.ui.PlayerUIData;

public class InventoryEventsHandler implements Listener {
    
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = Bukkit.getPlayer(event.getWhoClicked().getUniqueId());
        if (player == null) return;
        
        PlayerUIData uiData = PlayerUIData.getData(player);
        if (uiData.activeChestUI != null) uiData.activeChestUI.passClickEvent(event);
    }
    
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Player player = Bukkit.getPlayer(event.getPlayer().getUniqueId());
        if (player == null) return;

        PlayerUIData uiData = PlayerUIData.getData(player);
        uiData.activeChestUI = null;
    }
    
}
