package me.wolf.wskyblock.listeners;

import me.wolf.wskyblock.SkyblockPlugin;
import me.wolf.wskyblock.auctionhouse.AuctionHouse;
import me.wolf.wskyblock.gui.guis.auction.AuctionManageGUI;
import me.wolf.wskyblock.gui.guis.auction.ConfirmAuctionPurchaseGUI;
import me.wolf.wskyblock.player.SkyblockPlayer;
import me.wolf.wskyblock.utils.Utils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class AuctionHouseListener implements Listener {

    private final SkyblockPlugin plugin;

    public AuctionHouseListener(final SkyblockPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onAuctionInteract(InventoryClickEvent event) {
        if (event.getClickedInventory() == null) return;
        if (event.getCurrentItem() == null) return;
        // using contains because dynamic pages are displayed

        if (!Utils.colorize(event.getView().getTitle()).contains(Utils.colorize(plugin.getAuctionManager().getAuctionHouse().getDisplay())))
            return;

        final AuctionHouse auctionHouse = plugin.getAuctionManager().getAuctionHouse();
        final SkyblockPlayer player = plugin.getPlayerManager().getSkyblockPlayer(event.getWhoClicked().getUniqueId());
        // Buying an item

        auctionHouse.getAuctionItems().forEach(auctionItem -> {

            if (event.getCurrentItem().equals(auctionItem.getItemStack())) { // clicked item is an itemstack available in the AH
                if (!auctionItem.getSeller().getUniqueId().equals(player.getUuid())) { // if the clicker isn't the seller, allow him to buy
                    if (!canPurchase(player, auctionItem.getPrice())) {
                        player.sendMessage("&cInsufficient funds!");
                    } else new ConfirmAuctionPurchaseGUI(player, auctionItem, plugin);

                } else { // clicker = seller -> allow him to remove his auctioned item
                    new AuctionManageGUI(player, plugin);
                }

            }
        });
    }


    private boolean canPurchase(final SkyblockPlayer player, final int price) {
        return player.getCoins() >= price;
    }

}
