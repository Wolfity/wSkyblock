package me.wolf.wskyblock.gui.guis.auction;

import me.wolf.wskyblock.SkyblockPlugin;
import me.wolf.wskyblock.auctionhouse.AuctionItem;
import me.wolf.wskyblock.gui.SkyblockGUI;
import me.wolf.wskyblock.gui.guis.AuctionGUI;
import me.wolf.wskyblock.player.SkyblockPlayer;
import me.wolf.wskyblock.utils.ItemUtils;
import org.bukkit.Material;

import java.util.ArrayList;

public class ConfirmAuctionPurchaseGUI extends SkyblockGUI {

    public ConfirmAuctionPurchaseGUI(final SkyblockPlayer owner, final AuctionItem item, final SkyblockPlugin plugin) {
        super(9, "&aConfirm Purchase", owner);

        setItem(3, ItemUtils.createItem(Material.GREEN_STAINED_GLASS_PANE, "&a&lConfirm"), player -> {
            if (plugin.getAuctionManager().getAuctionHouse().getAuctionItems().contains(item)) { // checking if someone else hasnt bought the item in meantime
                plugin.getAuctionManager().processAuctionTransaction(owner, item.getSeller(), item, plugin.getSkyblockScoreboard());
                owner.sendMessage("&a&lTransaction successfull! Enjoy your purchase :)");
                owner.getBukkitPlayer().closeInventory();

                if (item.getSeller().isOnline()) { // seller is online -> send a msg
                    plugin.getPlayerManager().getSkyblockPlayer(
                            item.getSeller().getUniqueId()).sendMessage("&a&lYour item has sold successfully for " + item.getPrice() + " coins!");

                }
            } else {
                owner.sendMessage("&cErr...Something went wrong!");
                player.closeInventory();
            }
        });


        setItem(5, ItemUtils.createItem(Material.RED_STAINED_GLASS_PANE, "&c&lNo, go back!"), player -> {
            new AuctionGUI(owner, new ArrayList<>(plugin.getAuctionManager().getAuctionHouse().getAuctionItems()), 1, plugin);
        });

        openSkyblockGUI(owner);
    }
}
