package featherpowders.ui;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.entity.Player;

import featherpowders.ui.chest.ChestUI;

public class PlayerUIData {
    
    public final Player player;
    public ChestUI activeChestUI = null;
    
    private PlayerUIData(Player player) {
        this.player = player;
    }
    
    public void finalizeData() {
        player.closeInventory();
        dataMap.remove(player.getUniqueId());
    }
    
    private static final HashMap<UUID, PlayerUIData> dataMap = new HashMap<>();
    public static PlayerUIData getData(Player player) {
        if (dataMap.containsKey(player.getUniqueId())) return dataMap.get(player.getUniqueId());
        PlayerUIData data = new PlayerUIData(player);
        dataMap.put(player.getUniqueId(), data);
        return data;
    }
    
}
