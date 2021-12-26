package me.wolf.wskyblock.gui.guis.auction;

import me.wolf.wskyblock.SkyblockPlugin;
import me.wolf.wskyblock.auctionhouse.AuctionItem;
import me.wolf.wskyblock.gui.SkyblockGUI;
import me.wolf.wskyblock.player.SkyblockPlayer;
import me.wolf.wskyblock.utils.ItemUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;

public class ConfirmAuctionRemoveGUI extends SkyblockGUI {
    public ConfirmAuctionRemoveGUI(final SkyblockPlugin plugin, final SkyblockPlayer owner, final AuctionItem item) {
        super(9, "&cRemove", owner);

        setItem(3, ItemUtils.createItem(Material.GREEN_STAINED_GLASS_PANE, "&aYes, Remove this!"), player -> {
            if (plugin.getAuctionManager().getAuctionHouse().getAuctionItems().contains(item)) {
                owner.sendMessage("&a&lRemoved your item of the Auction House!");
                plugin.getAuctionManager().removeAuctionItem(item);
                player.closeInventory();

                // give the raw ItemStack without seller lore back to the owner
                Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> owner.getInventory().addItem(plugin.getSqLiteManager().getRawAuctionItemByID(item.getItemID()))); // give item back
            } else {
                owner.sendMessage("&cErr...Something went wrong!");
                player.closeInventory();
            }
        });
        setItem(4, item.getItemStack());
        setItem(5, ItemUtils.createItem(Material.RED_STAINED_GLASS_PANE, "&cNo, Go back!"), player -> {
            player.closeInventory();
            new AuctionManageGUI(owner, plugin);
        });


        openSkyblockGUI(owner);
    }
}
