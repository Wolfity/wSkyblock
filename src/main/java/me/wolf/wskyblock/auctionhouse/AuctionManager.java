package me.wolf.wskyblock.auctionhouse;

import me.wolf.wskyblock.SkyblockPlugin;
import me.wolf.wskyblock.player.SkyblockPlayer;
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
    public void processAuctionTransaction(final SkyblockPlayer buyer, final OfflinePlayer seller, final AuctionItem auctionItem) {
        final SQLiteManager sqLiteManager = plugin.getSqLiteManager();
        if (plugin.getPlayerManager().getSkyblockPlayer(seller.getUniqueId()) == null) { // seller is offline
            Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                plugin.getSqLiteManager().setCoins(seller.getUniqueId(), plugin.getSqLiteManager().getCoins(seller.getUniqueId()) + auctionItem.getPrice());
            });
        }

        final SkyblockPlayer sbSeller = plugin.getPlayerManager().getSkyblockPlayer(seller.getUniqueId());

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> { // update the buyer's inventory and take the buyer's coins away
            buyer.getInventory().addItem(sqLiteManager.getRawAuctionItemByID(auctionItem.getItemID()));
            sqLiteManager.setCoins(buyer.getUuid(), buyer.getCoins() - auctionItem.getPrice());
            sqLiteManager.setCoins(seller.getUniqueId(), sqLiteManager.getCoins(seller.getUniqueId()) + auctionItem.getPrice()); // update the seller coins
            if (sbSeller != null) {
                sqLiteManager.saveData(seller.getUniqueId()); // if its not null (player is online, update their object, else no need to)
            }
            sqLiteManager.saveData(buyer.getUuid());
        });


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
