package featherpowders.ui;

import org.bukkit.entity.Player;

import featherpowders.ui.chest.ChestUI;

/**
 * Player UI static class
 * @author nahkd
 *
 */
public class PlayerUI {
    
    /**
     * Open UI for player. This is the correct way to open custom UI created from {@link ChestUI}
     * @param player The player
     * @param chestUI The chest UI
     */
    public static void openUI(Player player, ChestUI chestUI) {
        PlayerUIData data = PlayerUIData.getData(player);
        player.openInventory(chestUI.getInventory());
        data.activeChestUI = chestUI;
    }
    
}
