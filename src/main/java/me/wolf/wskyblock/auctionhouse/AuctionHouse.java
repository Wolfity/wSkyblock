package me.wolf.wskyblock.auctionhouse;

import me.wolf.wskyblock.utils.Utils;

import java.util.Set;

public class AuctionHouse {

    private final String display;
    private Set<AuctionItem> auctionItems;

    public AuctionHouse(final String display) {
        this.display = Utils.colorize(display);
    }

    public void removeItem(final AuctionItem item) {
        this.auctionItems.remove(item);
    }

    public void addItem(final AuctionItem item) {
        this.auctionItems.add(item);
    }

    public Set<AuctionItem> getAuctionItems() {
        return auctionItems;
    }

    public void setAuctionItems(Set<AuctionItem> auctionItems) {
        this.auctionItems = auctionItems;
    }

    public String getDisplay() {
        return display;
    }


}
