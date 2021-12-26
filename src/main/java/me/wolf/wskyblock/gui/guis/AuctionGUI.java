package me.wolf.wskyblock.gui.guis;

import me.wolf.wskyblock.SkyblockPlugin;
import me.wolf.wskyblock.auctionhouse.AuctionHouse;
import me.wolf.wskyblock.auctionhouse.AuctionItem;
import me.wolf.wskyblock.gui.PaginatedMenu;
import me.wolf.wskyblock.gui.guis.auction.AuctionManageGUI;
import me.wolf.wskyblock.player.SkyblockPlayer;
import me.wolf.wskyblock.utils.ItemUtils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AuctionGUI extends PaginatedMenu {

    public AuctionGUI(final SkyblockPlayer owner, final AuctionHouse auctionHouse, final int page, final SkyblockPlugin plugin) {
        super(54, auctionHouse.getDisplay() + " - " + page, owner);


        final List<AuctionItem> auctionItems = new ArrayList<>(auctionHouse.getAuctionItems());
        final List<ItemStack> stacks = new ArrayList<>();
        addMenuBorder(plugin, owner);
        for (AuctionItem auctionItem : auctionItems) {
            stacks.add(ItemUtils.createItem(auctionItem.getItemStack(),
                    Arrays.asList("&cSelling for: " + auctionItem.getPrice() + " Coins", "&bSeller: " + auctionItem.getSeller().getName())));
        }
        addMenuBorder(plugin, owner);
        // checking if the page is valid, sending them to the last page if so
        if (isPageValid(stacks, page - 1, 28)) { // left page
            setItem(48, getPageLeft(), player -> {
                new AuctionGUI(owner, auctionHouse, page - 1, plugin);
            });
        } else setItem(48, getInvalidPage());

        // checking if the page is valid, sending to the next if so
        if (isPageValid(stacks, page + 1, 28)) {
            setItem(50, getPageRight(), player -> {
                new AuctionGUI(owner, auctionHouse, page + 1, plugin);
            });
        } else setItem(50, getInvalidPage());

        // 28 refers to the usable slots
        for (ItemStack is : getPageItems(stacks, page, 28)) {
            setItem(getSkyblockGUI().firstEmpty(), is);

            setItem(53, ItemUtils.createItem(Material.LEGACY_BOOK_AND_QUILL, "&cManage Auctions"), player -> new AuctionManageGUI(owner, plugin));
        }

        openSkyblockGUI(owner);
    }
}
