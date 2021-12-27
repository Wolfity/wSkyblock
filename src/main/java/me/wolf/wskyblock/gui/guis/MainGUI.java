package me.wolf.wskyblock.gui.guis;

import me.wolf.wskyblock.SkyblockPlugin;
import me.wolf.wskyblock.gui.SkyblockGUI;
import me.wolf.wskyblock.player.SkyblockPlayer;
import me.wolf.wskyblock.utils.ItemUtils;
import me.wolf.wskyblock.utils.Utils;
import org.bukkit.Material;

import java.util.Collections;
import java.util.List;

public class MainGUI extends SkyblockGUI {

    // this is the main GUI (/sb), where you can teleport to your island, open the shop/skill/pets menu, etc..
    public MainGUI(final SkyblockPlugin plugin, final SkyblockPlayer owner) {
        super(45, "&cSkyblock Profile", owner);

        // if they have an island, allow them to teleport, else make one
        final String iconName = plugin.getIslandManager().getIslandByOwner(owner) == null ? Utils.colorize("&aCreate an island!") : Utils.colorize("&aWarp to your island!");
        final List<String> lore = plugin.getIslandManager().getIslandByOwner(owner) == null ?
                Collections.singletonList(Utils.colorize("&bYou don't have an island yet!")) : Collections.singletonList(Utils.colorize("&cTeleport to your island!"));

        setItem(12, plugin.getMagicMarketManager().getMagicMarket().getIcon(), player -> new MagicMarketGUI(plugin, owner, plugin.getMagicMarketManager().getMagicMarket(), 1));
        setItem(14, ItemUtils.createItem(Material.DARK_OAK_BOAT, "&aAuction House"), player -> new AuctionGUI(owner, plugin.getAuctionManager().getAuctionHouse(), 1, plugin));
        setItem(20, ItemUtils.createItem(Material.DIAMOND_SWORD, "&cSkills"), player -> new SkillGUI(plugin, owner));
        setItem(22, ItemUtils.createItem(Material.GRASS_BLOCK, iconName, lore), player -> {
            if (iconName.equalsIgnoreCase(Utils.colorize("&aCreate an island!"))) { // the user does not have an island, create a new one
                player.closeInventory();
                owner.sendMessage("&a&lYour island has been created! Teleporting might take a while.");
                plugin.getIslandManager().createIsland(owner);

            } else if (iconName.equalsIgnoreCase(Utils.colorize("&aWarp to your island!"))) { // user has an island, telport
                player.closeInventory();
                player.teleport(plugin.getIslandManager().getIslandByOwner(owner).getSpawn());
                owner.sendMessage("&aYou were teleported to your island!");
            }
        });

        setItem(24, ItemUtils.createItem(Material.DIAMOND, "&bShop"), player -> new ShopGUI(plugin, owner));
        setItem(31, ItemUtils.createItem(Material.ENDER_PEARL, "&bWarps"), player -> new WarpGUI(plugin, owner));

        fillGUI(Material.GRAY_STAINED_GLASS_PANE);

        openSkyblockGUI(owner);
    }

}
