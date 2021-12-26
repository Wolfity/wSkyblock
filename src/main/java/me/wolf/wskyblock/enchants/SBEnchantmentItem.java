package me.wolf.wskyblock.enchants;

import me.wolf.wskyblock.utils.ItemUtils;
import me.wolf.wskyblock.utils.Utils;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class SBEnchantmentItem {


    private final ItemStack itemStack;
    private final List<SBEnchantment> sbEnchantments;
    private final String display;
    private int price;
    private List<String> lore;

    // creating the item
    public SBEnchantmentItem(final ItemStack itemStack, final List<SBEnchantment> sbEnchantments, final int price) {
        this.itemStack = itemStack;
        this.sbEnchantments = sbEnchantments;
        this.price = price;
        this.display = itemStack.getItemMeta().getDisplayName();
        setLore();
    }

    public String getDisplay() {
        return display;
    }

    public List<SBEnchantment> getSbEnchantments() {
        return sbEnchantments;
    }

    public String getName() {
        return Utils.colorize(getDisplay());
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public ItemStack getItemStack() {
        return ItemUtils.createItem(itemStack.getType(), getName(), getFormat());
    }

    public List<String> getLore() {
        final List<String> updatedLore = new ArrayList<>();
        for (int i = 0; i < getSbEnchantments().size(); i++) {
            updatedLore.add(Utils.colorize(getSbEnchantments().get(i).getDisplay() + " " + getSbEnchantments().get(i).getCurrentLevel()));
        }
        return updatedLore;
    }

    private List<String> getFormat() {
        final List<String> formatted = new ArrayList<>();
        for (int i = 0; i < getSbEnchantments().size(); i++) {
            formatted.add(Utils.colorize(getSbEnchantments().get(i).getDisplay() + " " + getSbEnchantments().get(i).getCurrentLevel()));
        }
        formatted.add(" ");
        formatted.add("&cPrice: &e" + getPrice());
        return Utils.colorize(formatted);
    }

    public void setLore() {
        final ItemMeta itemMeta = this.itemStack.getItemMeta();
        itemMeta.setLore(getFormat());
        this.itemStack.setItemMeta(itemMeta);
    }
}