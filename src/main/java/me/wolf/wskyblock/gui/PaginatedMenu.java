package me.wolf.wskyblock.gui;

import me.wolf.wskyblock.SkyblockPlugin;
import me.wolf.wskyblock.gui.guis.MainGUI;
import me.wolf.wskyblock.player.SkyblockPlayer;
import me.wolf.wskyblock.shops.ShopItem;
import me.wolf.wskyblock.utils.ItemUtils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;


public abstract class PaginatedMenu extends SkyblockGUI {

    private int currentPage = 1;

    public PaginatedMenu(int size, String name, SkyblockPlayer owner) {
        super(size, name, owner);
    }

    public void addMenuBorder(final SkyblockPlugin plugin, final SkyblockPlayer skyblockPlayer) { // this is the layout of the menu

        setItem(48, getPageLeft());
        setItem(49, getBackIcon(), player -> new MainGUI(plugin, skyblockPlayer));
        setItem(50, getPageRight());

        for (int i = 0; i < 10; i++) {
            if (getSkyblockGUI().getItem(i) == null) {
                setItem(i, new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
            }
        }
        // borders
        setItem(17, new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
        setItem(18, new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
        setItem(26, new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
        setItem(27, new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
        setItem(35, new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
        setItem(36, new ItemStack(Material.GRAY_STAINED_GLASS_PANE));

        for (int i = 44; i < 54; i++) {
            if (getSkyblockGUI().getItem(i) == null) {
                setItem(i, new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
            }
        }
    }

    public List<ItemStack> getPageItems(final List<ItemStack> items, final int page, final int spaces) {
        final int upper = page * spaces;
        final int lower = upper - spaces;

        final List<ItemStack> newItems = new ArrayList<>();
        for (int i = lower; i < upper; i++) {
            try {
                newItems.add(items.get(i));
            } catch (IndexOutOfBoundsException ignored) { // if it checks for an item that does not exist
            }
        }
        return newItems;
    }

    public boolean isPageValid(final List<ItemStack> items, final int page, final int spaces) {
        if (page <= 0) {
            return false;
        }
        final int upper = page * spaces;
        final int lower = upper - spaces;

        return items.size() > lower;
    }

    public boolean isShopPageValid(final List<ShopItem> items, final int page, final int spaces) {
        if (page <= 0) {
            return false;
        }
        final int upper = page * spaces;
        final int lower = upper - spaces;

        return items.size() > lower;
    }

    public List<ShopItem> getShopPageItems(final List<ShopItem> items, final int page, final int spaces) {
        final int upper = page * spaces;
        final int lower = upper - spaces;

        final List<ShopItem> newItems = new ArrayList<>();
        for (int i = lower; i < upper; i++) {
            try {
                newItems.add(items.get(i));
            } catch (IndexOutOfBoundsException ignored) { // if it checks for an item that does not exist
            }
        }
        return newItems;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void increaseCurrentPage() {
        this.currentPage++;
    }

    public void decreaseCurrentPage() {
        this.currentPage--;
    }

    public ItemStack getPageRight() {
        return ItemUtils.createItem(Material.ARROW, "&7Page Forward");
    }

    public ItemStack getPageLeft() {
        return ItemUtils.createItem(Material.ARROW, "&7Page Back");
    }

    public ItemStack getInvalidPage() {
        return ItemUtils.createItem(Material.ARROW, "&7No pages there!");
    }
}
