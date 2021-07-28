package featherpowders.ui;

import org.bukkit.entity.Player;

import featherpowders.ui.chest.ChestUI;

public class PlayerUI {
    
    public static void openUI(Player player, ChestUI chestUI) {
        PlayerUIData data = PlayerUIData.getData(player);
        player.openInventory(chestUI.getInventory());
        data.activeChestUI = chestUI;
    }
    
}
