package featherpowders;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import featherpowders.data.DataDriver;
import featherpowders.enums.ServerVersion;
import featherpowders.implementations.Implementations;

public class FeatherPowders extends JavaPlugin {
    
    @Override
    public void onEnable() {
        ServerVersion serverVersion = ServerVersion.getCurrentVersion();
        System.out.println("[FeatherPowders] Server version: " + Bukkit.getVersion());
        System.out.println("[FeatherPowders] Bukkit version: " + Bukkit.getBukkitVersion() + " (" + serverVersion.friendlyName + ")");
        if (serverVersion.legacyVersion) {
            getServer().getConsoleSender().sendMessage(new String[] {
                    "",
                    "§6WARNING: §fLegacy version detected!",
                    "§fYou're using " + ServerVersion.getCurrentVersion().friendlyName + ", which is too old.",
                    "§fWe haven't tested FeatherPowders on legacy version, therefore we can't guarantee",
                    "§fthat it will be bug-free",
                    "§fFeatherPowder will continue to work, but it breaks sometime. You've been warned.",
                    ""
            });
        } else if (serverVersion == ServerVersion.UNKNOWN) {
            getServer().getConsoleSender().sendMessage(new String[] {
                    "",
                    "§6WARNING: §fUnknown version (" + Bukkit.getBukkitVersion() + ") §8(either too new or too old...)",
                    "§fFeatherPowders will assume that your server support latest API.",
                    ""
            });
        }
        
        saveResource("config.yml", false);
        reloadConfig();
        
        DataDriver.setupGlobal(this);
        
        Implementations.init(this);
    }
    
    @Override
    public void onDisable() {
    }
    
}