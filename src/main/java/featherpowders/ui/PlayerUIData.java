package featherpowders.ui;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.entity.Player;

import featherpowders.ui.chest.ChestUI;

/**
 * Player UI data. This object contains player's UI data, which helps handles the inventory
 * interact events for custom UIs
 * @author nahkd
 *
 */
public class PlayerUIData {
    
    /**
     * The player that's linked to this data object
     */
    public final Player player;
    
    /**
     * Active chest UI. The value is null if there is no UI opened
     */
    public ChestUI activeChestUI = null;
    
    private PlayerUIData(Player player) {
        this.player = player;
    }
    
    /**
     * Finalize everything and remove player from internal map
     */
    public void finalizeData() {
        player.closeInventory();
        dataMap.remove(player.getUniqueId());
    }
    
    private static final HashMap<UUID, PlayerUIData> dataMap = new HashMap<>();
    
    /**
     * Get data for requested player
     * @param player
     * @return
     */
    public static PlayerUIData getData(Player player) {
        if (dataMap.containsKey(player.getUniqueId())) return dataMap.get(player.getUniqueId());
        PlayerUIData data = new PlayerUIData(player);
        dataMap.put(player.getUniqueId(), data);
        return data;
    }
    
}
