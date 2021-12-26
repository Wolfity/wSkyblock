package me.wolf.wskyblock.gui.guis.shops;

import me.wolf.wskyblock.gui.PaginatedMenu;
import me.wolf.wskyblock.player.SkyblockPlayer;
import me.wolf.wskyblock.shops.ShopItem;
import me.wolf.wskyblock.shops.SkyblockShop;
import me.wolf.wskyblock.utils.ItemUtils;

import java.util.Collections;
import java.util.List;

public class ShopMenuGUI extends PaginatedMenu {
    public ShopMenuGUI(final SkyblockShop shop, final SkyblockPlayer owner, final int page) {
        super(54, shop.getName(), owner);
        // Displays all of the items from the shop
        final List<ShopItem> shopItems = shop.getShopItems();

        if (isShopPageValid(shopItems, page - 1, 45)) { // left page
            setItem(48, getPageLeft(), player -> {
                new ShopMenuGUI(shop, owner, page - 1);
            });
        } else setItem(48, getInvalidPage());

        // checking if the page is valid, sending to the next if so
        if (isShopPageValid(shopItems, page + 1, 45)) {
            setItem(50, getPageRight(), player -> {
                new ShopMenuGUI(shop, owner, page + 1);
            });
        } else setItem(50, getInvalidPage());

        // 28 refers to the usable slots
        for (ShopItem is : getShopPageItems(shopItems, page, 28)) {
            setItem(getSkyblockGUI().firstEmpty(), ItemUtils.createItem(is.getMaterial(), Collections.singletonList(String.valueOf(is.getPrice()))));

            openSkyblockGUI(owner);
        }
    }
}
