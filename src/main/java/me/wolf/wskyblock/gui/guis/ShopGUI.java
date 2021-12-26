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

        setItem(12, shops.get(0).getIcon(), player -> {
            if (isEnabled(owner, shops.get(0))) {
                new ShopMenuGUI(shops.get(0), owner, 1);
            }
        });
        setItem(14, shops.get(1).getIcon(), player -> {
            if (isEnabled(owner, shops.get(1))) {
                new ShopMenuGUI(shops.get(1), owner, 1);
            }
        });
        setItem(20, shops.get(2).getIcon(), player -> {
            if (isEnabled(owner, shops.get(2))) {
                new ShopMenuGUI(shops.get(2), owner, 1);
            }
        });
        setItem(21, shops.get(3).getIcon(), player -> {
            if (isEnabled(owner, shops.get(3))) {
                new ShopMenuGUI(shops.get(3), owner, 1);
            }
        });
        setItem(22, shops.get(4).getIcon(), player -> {
            if (isEnabled(owner, shops.get(4))) {
                new ShopMenuGUI(shops.get(4), owner, 1);
            }
        });
        setItem(23, shops.get(5).getIcon(), player -> {
            if (isEnabled(owner, shops.get(5))) {
                new ShopMenuGUI(shops.get(5), owner, 1);
            }
        });
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
