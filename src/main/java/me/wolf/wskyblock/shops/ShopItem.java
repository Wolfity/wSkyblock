package me.wolf.wskyblock.shops;

import org.bukkit.Material;

public class ShopItem {

    private final Material material;
    private final double price, sellPrice;

    public ShopItem(final Material material, final double price, final double sellPrice) {
        this.material = material;
        this.price = price;
        this.sellPrice = sellPrice;
    }

    public Material getMaterial() {
        return material;
    }

    public double getPrice() {
        return price;
    }

    public double getSellPrice() {
        return sellPrice;
    }
}
