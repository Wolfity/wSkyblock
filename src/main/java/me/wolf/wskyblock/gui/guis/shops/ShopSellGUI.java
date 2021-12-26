package me.wolf.wskyblock.gui.guis.shops;

import me.wolf.wskyblock.SkyblockPlugin;
import me.wolf.wskyblock.gui.SkyblockGUI;
import me.wolf.wskyblock.player.SkyblockPlayer;
import me.wolf.wskyblock.shops.ShopItem;
import me.wolf.wskyblock.shops.SkyblockShop;
import me.wolf.wskyblock.utils.ItemUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ShopSellGUI extends SkyblockGUI {
    private final SkyblockPlugin plugin;
    private int selection = 1;

    public ShopSellGUI(final SkyblockPlugin plugin, final SkyblockPlayer owner, final ShopItem item, final SkyblockShop skyblockShop) {
        super(18, "Sell", owner);
        this.plugin = plugin;

        setItem(0, ItemUtils.createItem(Material.ARROW, "&-1"), player -> setItem(4, updateSelection(-1, item.getMaterial())));
        setItem(1, ItemUtils.createItem(Material.ARROW, "&c-10"), player -> setItem(4, updateSelection(-10, item.getMaterial())));
        setItem(2, ItemUtils.createItem(Material.ARROW, "&c-64"), player -> setItem(4, updateSelection(-64, item.getMaterial())));

        setItem(6, ItemUtils.createItem(Material.ARROW, "&a+1"), player -> setItem(4, updateSelection(1, item.getMaterial())));
        setItem(7, ItemUtils.createItem(Material.ARROW, "&a+10"), player -> setItem(4, updateSelection(10, item.getMaterial())));
        setItem(8, ItemUtils.createItem(Material.ARROW, "&a+64"), player -> setItem(4, updateSelection(64, item.getMaterial())));

        setItem(13, getBackIcon(), player -> {
            new ShopMenuGUI(skyblockShop, owner, 1);
            this.selection = 1;
        });
        // By clicking this they confirm that they want to sell these items
        setItem(15, ItemUtils.createItem(Material.DIAMOND_SWORD, "&aConfirm Sell"), player -> {
            if (canSell(item.getMaterial(), selection, owner)) {
                processTransaction(selection, item.getPrice(), owner, item.getMaterial());
                this.selection = 1;
                player.closeInventory();
            } else owner.sendMessage("&cYou can not sell this!");
        });

        setItem(4, new ItemStack(item.getMaterial(), selection)); // displaying how much they selected of the item

        fillGUI(Material.LIME_STAINED_GLASS_PANE);
        owner.getBukkitPlayer().updateInventory();
        openSkyblockGUI(owner);
    }

    private ItemStack updateSelection(final int toUpdate, final Material material) { // update the itemstack, so they can see the selected amount they have
        selection += toUpdate;
        if (selection > 64) {
            selection = 64;
            return new ItemStack(material, 64);
        } else if (selection <= 0) {
            selection = 1;
            return new ItemStack(material, 1);
        } else {
            return new ItemStack(material, selection);
        }
    }

    private boolean canSell(final Material material, final int amount, final SkyblockPlayer player) { // making sure they have enough in their inventory to sell
        for (ItemStack is : player.getInventory().getContents()) {
            if (is == null) continue;
            if (is.getType() == material && is.getAmount() >= amount) {
                return true;
            }
        }
        return false;
    }

    // processing transaction
    private void processTransaction(final int selection, final double price, final SkyblockPlayer player, final Material item) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            player.setCoins(player.getCoins() + (int) price);
        });
        player.removeCoins(selection * (int) price);
        player.sendMessage("&cSuccessfully sold &e" + selection + " " + item.name().toLowerCase() + " &cfor &e" + ((int) price * selection) + " &ccoins!");
        player.getInventory().removeItem(new ItemStack(item, selection));
    }
}
