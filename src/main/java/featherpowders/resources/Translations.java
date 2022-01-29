package featherpowders.resources;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import featherpowders.FeatherPowders;

/**
 * String translations that simply read from configuration file (and set default value if the key
 * doesn't exists).<br>
 * <br>
 * Generating translations:<br>
 * To generate translations, simply create "translations.yml" inside your plugin data directory (or
 * folder if you are using Windows), run the plugin, then do some actions that involves
 * {@link #translate(String, String)} being called. Strings that haven't translated will use default
 * string and the key-value pair will be written to memory configuration. Don't forget to call
 * {@link #saveTranslations()} when you stop your plugin.
 * @author nahkd
 *
 */
public class Translations {
    
    public final FileConfiguration fileConfig;
    public final File file;
    
    public Translations(FileConfiguration fileConfig, File file) {
        this.fileConfig = fileConfig;
        this.file = file;
    }
    
    /**
     * Translate the key to selected language. If the given key is null, it will fallback to
     * "hashCode.[Hash Code]" key instead 
     * @param key
     * @param def
     * @return
     */
    public String translate(String key, String def, String... percentSReplaces) {
        if (key == null || key.length() == 0) key = "hashCode." + Integer.toHexString(def.hashCode());
        
        String finalOut;
        if (fileConfig == null) finalOut = def;
        else if (fileConfig.contains(key)) finalOut = fileConfig.getString(key);
        else {
            fileConfig.set(key, def);
            finalOut = def;
        }
        
        for (int i = 0; i < percentSReplaces.length; i++) finalOut.replace("%s", percentSReplaces[i]);
        return finalOut;
    }
    
    public void saveTranslations() {
        if (file == null) return;
        try {
            fileConfig.save(file);
        } catch (IOException e) {
            e.printStackTrace();
            JavaPlugin.getPlugin(FeatherPowders.class).getLogger().warning("Cannot save translations: IOException");
        }
    }
    
    /**
     * Get translations from plugin. It will use "translations.yml" from your plugin data
     * folder it it's exists, otherwise it will find "translations.[LANGUAGE].yml" inside
     * your plugin jar
     * @param plugin
     * @param language
     * @return
     */
    public static Translations of(Plugin plugin, String language) {
        File localTranslationsFile = new File(plugin.getDataFolder(), "translations.yml");
        if (localTranslationsFile.exists()) {
            YamlConfiguration cfg = YamlConfiguration.loadConfiguration(localTranslationsFile);
            return new Translations(cfg, localTranslationsFile);
        }
        
        InputStream stream = plugin.getResource("translations." + language + ".yml");
        if (stream == null) return new Translations(null, null);
        
        try {
            YamlConfiguration cfg = YamlConfiguration.loadConfiguration(new InputStreamReader(stream, StandardCharsets.UTF_8));
            stream.close();
            return new Translations(cfg, null);
        } catch (IOException e) {
            e.printStackTrace();
            JavaPlugin.getPlugin(FeatherPowders.class).getLogger().warning("Cannot load translations: IOException");
            return new Translations(null, null);
        }
    }
    
}
