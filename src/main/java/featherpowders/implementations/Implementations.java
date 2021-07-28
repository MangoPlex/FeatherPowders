package featherpowders.implementations;

import featherpowders.FeatherPowders;
import featherpowders.implementations.commands.AdminCommand;
import featherpowders.implementations.data.binary.BinaryDataDriver;
import featherpowders.implementations.events.InventoryEventsHandler;

public class Implementations {
    
    public static void init(FeatherPowders plugin) {
        new BinaryDataDriver().registerDriver();
        
        plugin.getServer().getPluginManager().registerEvents(new InventoryEventsHandler(), plugin);
        
        plugin.getCommand("featherpowders").setExecutor(new AdminCommand(plugin));
    }
    
}
