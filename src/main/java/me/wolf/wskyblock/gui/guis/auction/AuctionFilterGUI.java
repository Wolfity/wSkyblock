package me.wolf.wskyblock.gui.guis.auction;

import me.wolf.wskyblock.SkyblockPlugin;
import me.wolf.wskyblock.auctionhouse.AuctionItem;
import me.wolf.wskyblock.gui.SkyblockGUI;
import me.wolf.wskyblock.gui.guis.AuctionGUI;
import me.wolf.wskyblock.player.SkyblockPlayer;
import me.wolf.wskyblock.utils.ItemUtils;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AuctionFilterGUI extends SkyblockGUI {

    public AuctionFilterGUI(final SkyblockPlayer owner, final List<AuctionItem> auctionItems, final SkyblockPlugin plugin) {
        super(9, "&cFilter Auctions", owner);

        setItem(1, ItemUtils.createItem(Material.SUNFLOWER, "&eExpensive -> Cheap"), player -> {
            Collections.sort(auctionItems);
            Collections.reverse(auctionItems);
            new AuctionGUI(owner, auctionItems, 1, plugin);
        });
        setItem(3, ItemUtils.createItem(Material.DIRT, "&eCheap -> Expensive"), player -> {
            Collections.sort(auctionItems);
            new AuctionGUI(owner, auctionItems, 1, plugin);
        });
        setItem(5, ItemUtils.createItem(Material.ACACIA_SIGN, "&eSearch"), player -> {
            plugin.getPlayerManager().getInFilterMode().add(owner);
            player.closeInventory();
            owner.sendMessage("&a&lType your filter by name in the chat!");
        });

        setItem(7, ItemUtils.createItem(Material.RED_STAINED_GLASS_PANE, "&cReset Filters"), player -> new AuctionGUI(owner, new ArrayList<>(plugin.getAuctionManager().getAuctionHouse().getAuctionItems()), 1, plugin));

        setItem(8, getBackIcon(), player -> new AuctionGUI(owner, auctionItems, 1, plugin));

        fillGUI(Material.ORANGE_STAINED_GLASS_PANE);
        openSkyblockGUI(owner);

    }
}
