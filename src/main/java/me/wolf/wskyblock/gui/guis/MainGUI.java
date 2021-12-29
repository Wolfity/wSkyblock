package me.wolf.wskyblock.gui.guis;

import me.wolf.wskyblock.SkyblockPlugin;
import me.wolf.wskyblock.gui.SkyblockGUI;
import me.wolf.wskyblock.island.Island;
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

        final Island island = plugin.getIslandManager().getIslandByOwner(owner);

        // if they have an island, allow them to teleport, else make one
        final String iconName = island == null ? Utils.colorize("&aCreate an island!") : Utils.colorize("&aWarp to your island!");
        final List<String> lore = island == null ?
                Collections.singletonList(Utils.colorize("&bYou don't have an island yet!")) : Collections.singletonList(Utils.colorize("&cTeleport to your island!"));

        for (final String gui : plugin.getConfig().getConfigurationSection("guis").getKeys(false)) {
            final Material material = Material.valueOf(plugin.getConfig().getString("guis." + gui + ".icon"));
            final int slot = plugin.getConfig().getInt("guis." + gui + ".slot");
            final String name = plugin.getConfig().getString("guis." + gui + ".name");

            if (gui.equalsIgnoreCase("island")) { // the lore/name of this item depends on whether a user has an island or not
                setItem(slot, ItemUtils.createItem(material, iconName, lore), player -> {
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
            }
            switch (gui) {
                case "magicmarket":
                    setItem(slot, ItemUtils.createItem(material, name), player -> new MagicMarketGUI(plugin, owner, plugin.getMagicMarketManager().getMagicMarket(), 1));
                    break;
                case "auctionhouse":
                    setItem(slot, ItemUtils.createItem(material, name), player -> new AuctionGUI(owner, plugin.getAuctionManager().getAuctionHouse(), 1, plugin));
                    break;
                case "skills":
                    setItem(slot, ItemUtils.createItem(material, name), player -> new SkillGUI(plugin, owner));
                    break;
                case "warps":
                    setItem(slot, ItemUtils.createItem(material, name), player -> {
                        if (island == null) {
                            owner.sendMessage("&cYou need to create an island before using this!");
                        } else new WarpGUI(plugin, owner);
                    });
                    break;
                case "shop":
                    setItem(slot, ItemUtils.createItem(material, name), player -> new ShopGUI(plugin, owner));
                    break;
            }


        }

        fillGUI(Material.GRAY_STAINED_GLASS_PANE);

        openSkyblockGUI(owner);
    }

}
