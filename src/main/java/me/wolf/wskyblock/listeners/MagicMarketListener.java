package me.wolf.wskyblock.listeners;

import me.wolf.wskyblock.SkyblockPlugin;
import me.wolf.wskyblock.gui.guis.magicmarket.MagicMarketConfirmPurchase;
import me.wolf.wskyblock.magicmarket.MagicMarket;
import me.wolf.wskyblock.player.SkyblockPlayer;
import me.wolf.wskyblock.utils.Utils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class MagicMarketListener implements Listener {

    private final SkyblockPlugin plugin;

    public MagicMarketListener(final SkyblockPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onMarketInteract(InventoryClickEvent event) {
        if (event.getClickedInventory() == null) return;
        if (event.getCurrentItem() == null) return;
        // using contains because dynamic pages are displayed

        if (!Utils.colorize(event.getView().getTitle()).contains(Utils.colorize(plugin.getMagicMarketManager().getMagicMarket().getName())))
            return;

        final MagicMarket magicMarket = plugin.getMagicMarketManager().getMagicMarket();
        final SkyblockPlayer player = plugin.getPlayerManager().getSkyblockPlayer(event.getWhoClicked().getUniqueId());

        // if you click an item in the menu, check if u have enough balance, if so, open the confirm purchase menu
        magicMarket.getEnchantmentItems().forEach(magicItem -> {
            if (event.getCurrentItem().equals(magicItem.getItemStack())) {
                if (canAfford(player, magicItem.getPrice())) {
                    new MagicMarketConfirmPurchase(player, magicItem, plugin);
                } else player.sendMessage("&cInsufficient Funds!");
            }
        });


    }


    private boolean canAfford(final SkyblockPlayer player, final int price) {
        return player.getCoins() >= price;
    }

}
