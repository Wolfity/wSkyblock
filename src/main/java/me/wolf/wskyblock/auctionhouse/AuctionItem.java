package me.wolf.wskyblock.auctionhouse;

import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;
import java.util.UUID;

public class AuctionItem implements Comparable<AuctionItem> {

    private final OfflinePlayer seller;
    private final int price;
    private final ItemStack itemStack;
    private final UUID itemID; // remove by UUID from the DB

    public AuctionItem(final OfflinePlayer seller, final int price, final ItemStack itemStack) { // creating a new auction
        this.seller = seller;
        this.price = price;
        this.itemStack = itemStack;
        this.itemID = UUID.randomUUID();
    }

    public AuctionItem(final OfflinePlayer seller, final int price, final ItemStack itemStack, final UUID itemID) { // loading in an auction from the DB
        this.seller = seller;
        this.price = price;
        this.itemStack = itemStack;
        this.itemID = itemID;
    }

    public OfflinePlayer getSeller() {
        return seller;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public int getPrice() {
        return price;
    }

    public UUID getItemID() {
        return itemID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuctionItem that = (AuctionItem) o;
        return itemID.equals(that.itemID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(itemID);
    }

    @Override
    public int compareTo(AuctionItem o) {
        return Integer.compare(price, o.getPrice());
    }
}

