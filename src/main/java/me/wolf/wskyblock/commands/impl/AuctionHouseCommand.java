package me.wolf.wskyblock.commands.impl;

import me.wolf.wskyblock.SkyblockPlugin;
import me.wolf.wskyblock.auctionhouse.AuctionItem;
import me.wolf.wskyblock.commands.BaseCommand;
import me.wolf.wskyblock.gui.guis.AuctionGUI;
import me.wolf.wskyblock.gui.guis.auction.AuctionManageGUI;
import me.wolf.wskyblock.player.SkyblockPlayer;
import me.wolf.wskyblock.utils.Utils;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;

public class AuctionHouseCommand extends BaseCommand {

    private final SkyblockPlugin plugin;

    public AuctionHouseCommand(final SkyblockPlugin plugin) {
        super("auctionhouse");
        setAliases(Collections.singletonList("ah"));
        this.plugin = plugin;
    }

    @Override
    protected void run(CommandSender sender, String[] args) {
        final Player player = (Player) sender;
        final SkyblockPlayer skyblockPlayer = plugin.getPlayerManager().getSkyblockPlayer(player.getUniqueId());
        if (args.length == 0) {
            new AuctionGUI(skyblockPlayer, new ArrayList<>(plugin.getAuctionManager().getAuctionHouse().getAuctionItems()), 1, plugin);
        }
        // ah <price>
        if (args.length == 1) {
            if (!args[0].equalsIgnoreCase("manage")) {
                int price = 0;
                try {
                    price = Integer.parseInt(args[0]);
                } catch (final NumberFormatException e) { // the player tried to enter a non numeric price
                    player.sendMessage(Utils.colorize("&cPlease enter a valid price!"));
                }
                if (player.getInventory().getItemInMainHand().getType() == Material.AIR) { // not allowing people to sell "AIR/Empty slots"
                    player.sendMessage(Utils.colorize("&cPlease hold the item you want to sell in your main hand!"));
                } else if (price == 0) { // not allowing someone to sell items for free
                    skyblockPlayer.sendMessage("&cYou can not sell items for free");
                } else {
                    if (player.getInventory().getItemInMainHand().getItemMeta().getPersistentDataContainer().isEmpty()) {
                        // add the auction to the auction house
                        plugin.getAuctionManager().addAuctionItem(new AuctionItem(skyblockPlayer.getOfflinePlayer(), price, player.getInventory().getItemInMainHand()));
                        player.getInventory().setItemInMainHand(null);
                        player.sendMessage(Utils.colorize("&aSuccessfully put your item on the auction house for &2" + price + " coins!"));
                    } else skyblockPlayer.sendMessage("&cYou can not sell Magic Items on the Auction House!");
                }
            }
        }
        if (args.length == 1 && args[0].equalsIgnoreCase("manage")) {
            new AuctionManageGUI(skyblockPlayer, plugin);
        }

    }
}
