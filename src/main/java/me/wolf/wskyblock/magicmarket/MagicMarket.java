package me.wolf.wskyblock.magicmarket;

import me.wolf.wskyblock.enchants.SBEnchantmentItem;
import me.wolf.wskyblock.utils.Utils;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class MagicMarket {

    private List<SBEnchantmentItem> enchantmentItems;
    private ItemStack icon;
    private String name;

    public ItemStack getIcon() {
        return icon;
    }

    public void setIcon(ItemStack icon) {
        this.icon = icon;
    }

    public List<SBEnchantmentItem> getEnchantmentItems() {
        return enchantmentItems;
    }

    public void setEnchantmentItems(List<SBEnchantmentItem> enchantmentItems) {
        this.enchantmentItems = enchantmentItems;
    }

    public String getName() {
        return Utils.colorize(name);
    }

    public void setName(String name) {
        this.name = Utils.colorize(name);
    }
}
