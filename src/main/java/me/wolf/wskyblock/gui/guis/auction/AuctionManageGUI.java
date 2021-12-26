package me.wolf.wskyblock.gui.guis.auction;

import me.wolf.wskyblock.SkyblockPlugin;
import me.wolf.wskyblock.auctionhouse.AuctionItem;
import me.wolf.wskyblock.gui.SkyblockGUI;
import me.wolf.wskyblock.gui.guis.AuctionGUI;
import me.wolf.wskyblock.player.SkyblockPlayer;

import java.util.ArrayList;
import java.util.List;

public class AuctionManageGUI extends SkyblockGUI {
    public AuctionManageGUI(final SkyblockPlayer owner, final SkyblockPlugin plugin) {
        super(54, "&cYour Auctions &7(Click to Remove)", owner);
        final List<AuctionItem> ownedItems = new ArrayList<>(plugin.getAuctionManager().getItemsFromPlayer(owner));

        if (!ownedItems.isEmpty()) {
            for (int i = 0; i < ownedItems.size(); i++) {
                int finalI = i;
                setItem(i, ownedItems.get(i).getItemStack(), player -> new ConfirmAuctionRemoveGUI(plugin, owner, ownedItems.get(finalI)));
            }
        }

        setItem(53, getBackIcon(), player -> new AuctionGUI(owner, plugin.getAuctionManager().getAuctionHouse(), 1, plugin));

        openSkyblockGUI(owner);
    }
}
