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

    /**
     * @param auctionItem the item we are adding to the auctionhouse
     *                    Method for adding an auction item to the auction house, and set it in the database
     */
    public void addAuctionItem(final AuctionItem auctionItem) {
        this.auctionHouse.addItem(auctionItem);
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> plugin.getSqLiteManager().addAuctionItem(auctionItem));

    }

    /**
     * @param item the item we are removing from the auction house
     *             Removing a method from the auction house and updating it in hte database
     */
    public void removeAuctionItem(final AuctionItem item) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> plugin.getSqLiteManager().removeAuction(item));
        this.auctionHouse.removeItem(item);

    }

    /**
     * @param buyer       the buyer of an auction item
     * @param seller      the seller of the item
     * @param auctionItem the item being sold
     *                    Method for processing an auction
     *                    Updating balances, giving the item, etc
     */
    public void processAuctionTransaction(final SkyblockPlayer buyer, final OfflinePlayer seller, final AuctionItem auctionItem, final SkyblockScoreboard sb) {
        final SQLiteManager sqLiteManager = plugin.getSqLiteManager();
        if (plugin.getPlayerManager().getSkyblockPlayer(seller.getUniqueId()) == null) { // seller is offline
            Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                plugin.getSqLiteManager().setCoins(seller.getUniqueId(), plugin.getSqLiteManager().getCoins(seller.getUniqueId()) + auctionItem.getPrice());
            });
        }

        final SkyblockPlayer sbSeller = plugin.getPlayerManager().getSkyblockPlayer(seller.getUniqueId());
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> { // update the buyer's inventory and take the buyer's coins away

            if (sqLiteManager.getRawAuctionItemByID(auctionItem.getItemID()) == null) {
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

    /**
     * @param player the player whose items we are requesting
     * @return a Set of auction items owned by the player
     */
    public Set<AuctionItem> getItemsFromPlayer(final SkyblockPlayer player) { // get all the auctions from a player
        return auctionHouse.getAuctionItems().stream().filter(auctionItem ->
                auctionItem.getSeller().getUniqueId().equals(player.getUuid())).collect(Collectors.toSet());
    }

}
