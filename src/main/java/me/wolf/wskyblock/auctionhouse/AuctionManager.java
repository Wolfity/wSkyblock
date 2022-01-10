package me.wolf.wskyblock.auctionhouse;

import me.wolf.wskyblock.SkyblockPlugin;
import me.wolf.wskyblock.player.SkyblockPlayer;
import me.wolf.wskyblock.scoreboards.SkyblockScoreboard;
import me.wolf.wskyblock.sql.SQLiteManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.Set;
import java.util.stream.Collectors;

public class AuctionManager {

    private final SkyblockPlugin plugin;
    private AuctionHouse auctionHouse;


    public AuctionManager(final SkyblockPlugin plugin) {
        this.plugin = plugin;
    }

    public void loadAuctionedItems() {
        this.auctionHouse = new AuctionHouse("&aAuction House");

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            Set<AuctionItem> storedItems = plugin.getSqLiteManager().loadAuctionData();
            auctionHouse.setAuctionItems(storedItems);
        });

    }

    public void addAuctionItem(final AuctionItem auctionItem) {
        this.auctionHouse.addItem(auctionItem);
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> plugin.getSqLiteManager().addAuctionItem(auctionItem));

    }

    public void removeAuctionItem(final AuctionItem item) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> plugin.getSqLiteManager().removeAuction(item));
        this.auctionHouse.removeItem(item);

    }

    // process a transaction of an item
    public void processAuctionTransaction(final SkyblockPlayer buyer, final OfflinePlayer seller, final AuctionItem auctionItem, final SkyblockScoreboard sb) {
        final SQLiteManager sqLiteManager = plugin.getSqLiteManager();
        if (plugin.getPlayerManager().getSkyblockPlayer(seller.getUniqueId()) == null) { // seller is offline
            Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                plugin.getSqLiteManager().setCoins(seller.getUniqueId(), plugin.getSqLiteManager().getCoins(seller.getUniqueId()) + auctionItem.getPrice());
            });
        }

        final SkyblockPlayer sbSeller = plugin.getPlayerManager().getSkyblockPlayer(seller.getUniqueId());
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> { // update the buyer's inventory and take the buyer's coins away

            if (auctionItem == null || sqLiteManager.getRawAuctionItemByID(auctionItem.getItemID()) == null) {
                buyer.sendMessage("&cSomething went wrong!");
                removeAuctionItem(auctionItem);
            } else {
                buyer.getInventory().addItem(sqLiteManager.getRawAuctionItemByID(auctionItem.getItemID()));
                if (sbSeller != null) {
                    sbSeller.addCoins(auctionItem.getPrice());
                }
                buyer.removeCoins(auctionItem.getPrice());
                // update the seller coins
            }


        });

        sb.skyblockScoreboard(buyer);
        if (sbSeller != null) {
            sb.skyblockScoreboard(sbSeller);
        }
        removeAuctionItem(auctionItem);

    }

    public AuctionHouse getAuctionHouse() {
        return auctionHouse;
    }

    public Set<AuctionItem> getItemsFromPlayer(final SkyblockPlayer player) { // get all the auctions from a player
        return auctionHouse.getAuctionItems().stream().filter(auctionItem ->
                auctionItem.getSeller().getUniqueId().equals(player.getUuid())).collect(Collectors.toSet());
    }

}
