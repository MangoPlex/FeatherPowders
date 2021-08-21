package featherpowders.utils;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemBuilder {
    private ItemStack item;
    private ItemMeta meta;
    
    public ItemBuilder(Material mat, int amount, ItemMeta meta) {
        this.item = new ItemStack(mat, amount);
        this.meta = meta;
    }
    
    public ItemBuilder(Material mat, int amount) {
        this.item = new ItemStack(mat, amount);
        this.meta = this.item.getItemMeta();
    }
    
    public ItemStack getItem() {
        item.setItemMeta(meta);
        return item;
    }
    
    public ItemBuilder name(String name) {
        meta.setDisplayName(name);
        return this;
    }
    
    public ItemBuilder lore(String... lore) {
        meta.setLore(Arrays.asList(lore));
        return this;
    }
    
}
