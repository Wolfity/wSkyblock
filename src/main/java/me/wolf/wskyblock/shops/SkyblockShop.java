package me.wolf.wskyblock.shops;

import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public abstract class SkyblockShop implements Comparable<SkyblockShop> {

    private final String name;
    private ItemStack icon;
    private List<ShopItem> shopItems;
    private boolean isEnabled;
    private int slot;

    public SkyblockShop(final String name) {
        this.name = name;
        this.shopItems = new ArrayList<>();
        this.isEnabled = true;
    }

    public ItemStack getIcon() {
        return icon;
    }

    public void setIcon(ItemStack icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public List<ShopItem> getShopItems() {
        return shopItems;
    }

    public void setShopItems(List<ShopItem> shopItems) {
        this.shopItems = shopItems;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public int getSlot() {
        return slot;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

    @Override
    public int compareTo(SkyblockShop o) {
        return name.compareTo(o.getName());
    }
}
