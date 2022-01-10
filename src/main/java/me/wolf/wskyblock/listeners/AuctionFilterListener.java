package me.wolf.wskyblock.listeners;

import me.wolf.wskyblock.SkyblockPlugin;
import me.wolf.wskyblock.auctionhouse.AuctionItem;
import me.wolf.wskyblock.gui.guis.AuctionGUI;
import me.wolf.wskyblock.player.SkyblockPlayer;
import me.wolf.wskyblock.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AuctionFilterListener implements Listener {

    private final SkyblockPlugin plugin;

    public AuctionFilterListener(final SkyblockPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onFilterChat(AsyncPlayerChatEvent event) {
        final SkyblockPlayer player = plugin.getPlayerManager().getSkyblockPlayer(event.getPlayer().getUniqueId());
        if (!plugin.getPlayerManager().getInFilterMode().contains(player)) return;
        event.setCancelled(true); // dont send the message
        final String filter = event.getMessage().toUpperCase();
        final List<AuctionItem> allItems = new ArrayList<>(plugin.getAuctionManager().getAuctionHouse().getAuctionItems());


        final List<AuctionItem> filteredList = allItems
                .stream()
                .filter(auctionItem -> Utils.containsIgnoreCase(ChatColor.stripColor(auctionItem.getItemStack().getItemMeta().getDisplayName()),
                        filter) || Utils.containsIgnoreCase(auctionItem.getItemStack().getType().name(), filter))
                .collect(Collectors.toList());


        Bukkit.getScheduler().runTask(plugin, () -> new AuctionGUI(player, filteredList, 1, plugin)); // InventoryOpenEvent cant be triggered async

        player.sendMessage("&aStarting to filter!\n" +
                "&2" + filteredList.size() + " &aItem(s) were found based on your filter!");

        plugin.getPlayerManager().getInFilterMode().remove(player);

    }


}
