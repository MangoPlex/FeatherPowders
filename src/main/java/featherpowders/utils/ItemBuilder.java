package featherpowders.utils;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import featherpowders.ui.chest.ChestUI;

/**
 * Item builder to help you create items with display name and custom lore easier, with the usage
 * of "chaining pattern" (Eg: <code>initSomething().addA().addB().finalize()</code>)
 * @author nahkd
 *
 */
public class ItemBuilder {
    private ItemStack item;
    private ItemMeta meta;
    
    public ItemBuilder(Material mat, int amount, ItemMeta meta) {
        this.item = new ItemStack(mat, amount);
        this.meta = meta;
    }
    
    /**
     * Create new item builder with given material and amount
     * @param mat
     * @param amount
     */
    public ItemBuilder(Material mat, int amount) {
        this.item = new ItemStack(mat, amount);
        this.meta = this.item.getItemMeta();
    }
    
    /**
     * Create new item builder with given material
     * @param mat
     */
    public ItemBuilder(Material mat) {
        this.item = new ItemStack(mat);
        this.meta = this.item.getItemMeta();
    }
    
    /**
     * Clone the {@link ItemStack}
     * @param stack
     */
    public ItemBuilder(ItemStack stack) {
        this.item = new ItemStack(stack);
        this.meta = this.item.getItemMeta();
    }
    
    /**
     * Finalize item builder and returns the item, which can be used in places like {@link ChestUI}
     * @return
     */
    public ItemStack getItem() {
        item.setItemMeta(meta);
        return item;
    }
    
    /**
     * Set the name for the item
     * @param name
     * @return
     */
    public ItemBuilder name(String name) {
        meta.setDisplayName(name);
        return this;
    }
    
    /**
     * Set the lore for the item
     * @param lore
     * @return
     */
    public ItemBuilder lore(String... lore) {
        meta.setLore(Arrays.asList(lore));
        return this;
    }
    
    /**
     * Add enchantment glow to item. This also hides all enchants
     * @param glow
     * @return
     */
    public ItemBuilder glow(Boolean glow) {
        meta.addEnchant(Enchantment.DURABILITY, 4, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        return this;
    }
}
