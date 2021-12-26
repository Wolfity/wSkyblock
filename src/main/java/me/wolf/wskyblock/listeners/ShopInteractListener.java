package me.wolf.wskyblock.listeners;

import me.wolf.wskyblock.SkyblockPlugin;
import me.wolf.wskyblock.gui.guis.shops.ShopDecideGUI;
import me.wolf.wskyblock.player.SkyblockPlayer;
import me.wolf.wskyblock.shops.ShopItem;
import me.wolf.wskyblock.shops.SkyblockShop;
import me.wolf.wskyblock.utils.Utils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class ShopInteractListener implements Listener {

    private final SkyblockPlugin plugin;

    // this is where player clicks an item in a shop, and then gets redirected to a decision menu where they can either buy/sell the item
    public ShopInteractListener(final SkyblockPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPurchase(InventoryClickEvent event) {
        if (event.getClickedInventory() == null) return;
        if (event.getCurrentItem() == null) return;
        if (plugin.getShopManager().getShopByName(Utils.colorize(event.getView().getTitle())) == null)
            return; // if it's not a shop inventory, return

        final SkyblockPlayer player = plugin.getPlayerManager().getSkyblockPlayer(event.getWhoClicked().getUniqueId());
        final SkyblockShop shop = plugin.getShopManager().getShopByName(Utils.colorize(event.getView().getTitle()));
        // if a shop item was clicked, allow them to decide whether he wants to sell or purchase the item
        for (final ShopItem item : shop.getShopItems()) {
            if (event.getCurrentItem().getType() == item.getMaterial()) {
                new ShopDecideGUI(plugin, player, item, shop);
            }
        }

    }
}
