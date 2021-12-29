package me.wolf.wskyblock.gui.guis;

import me.wolf.wskyblock.SkyblockPlugin;
import me.wolf.wskyblock.gui.SkyblockGUI;
import me.wolf.wskyblock.gui.guis.shops.ShopMenuGUI;
import me.wolf.wskyblock.player.SkyblockPlayer;
import me.wolf.wskyblock.shops.SkyblockShop;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ShopGUI extends SkyblockGUI {

    public ShopGUI(final SkyblockPlugin plugin, final SkyblockPlayer owner) {
        super(36, "&bShop", owner);

        final List<SkyblockShop> shops = new ArrayList<>(plugin.getShopManager().getShops());
        Collections.sort(shops);

        for (final SkyblockShop shop : shops) { // putting every shop in the menu with their item and name
            setItem(shop.getSlot(), shop.getIcon(), player -> {
                if (isEnabled(owner, shop)) { // if it's enabled, allow a player to click open
                    new ShopMenuGUI(shop, owner, 1, plugin);
                }
            });
        }

        setItem(35, getBackIcon(), player -> new MainGUI(plugin, owner));

        openSkyblockGUI(owner);
    }

    private boolean isEnabled(final SkyblockPlayer player, final SkyblockShop shop) {
        if (shop.isEnabled()) {
            return true;
        } else {
            player.sendMessage("&cThis shop is currently disabled");
            return false;
        }
    }
}
