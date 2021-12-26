package me.wolf.wskyblock.gui.guis.shops;

import me.wolf.wskyblock.SkyblockPlugin;
import me.wolf.wskyblock.gui.SkyblockGUI;
import me.wolf.wskyblock.player.SkyblockPlayer;
import me.wolf.wskyblock.shops.ShopItem;
import me.wolf.wskyblock.shops.SkyblockShop;
import me.wolf.wskyblock.utils.ItemUtils;
import org.bukkit.Material;

public class ShopDecideGUI extends SkyblockGUI {
    public ShopDecideGUI(final SkyblockPlugin plugin, final SkyblockPlayer owner, final ShopItem item, final SkyblockShop skyblockShop) {
        super(9, "Sell Or Buy?", owner);
        // Here a user can make the decision between either buying or selling an item
        setItem(3, ItemUtils.createItem(Material.GREEN_STAINED_GLASS_PANE, "&aBuy"), player -> new ShopPurchaseGUI(plugin, owner, item, skyblockShop));
        setItem(5, ItemUtils.createItem(Material.RED_STAINED_GLASS_PANE, "&cSell"), player -> new ShopSellGUI(plugin, owner, item, skyblockShop));
        openSkyblockGUI(owner);

    }
}
