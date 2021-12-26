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

public class ShopPurchaseGUI extends SkyblockGUI {

    private final SkyblockPlugin plugin;


    private int selection = 1;

    public ShopPurchaseGUI(final SkyblockPlugin plugin, final SkyblockPlayer owner, final ShopItem item, final SkyblockShop skyblockShop) {
        super(18, "Purchase", owner);
        this.plugin = plugin;

        setItem(0, ItemUtils.createItem(Material.ARROW, "&c-1"), player -> setItem(4, updateSelection(-1, item.getMaterial())));
        setItem(1, ItemUtils.createItem(Material.ARROW, "&c-10"), player -> setItem(4, updateSelection(-10, item.getMaterial())));
        setItem(2, ItemUtils.createItem(Material.ARROW, "&c-64"), player -> setItem(4, updateSelection(-64, item.getMaterial())));

        setItem(6, ItemUtils.createItem(Material.ARROW, "&a+1"), player -> setItem(4, updateSelection(1, item.getMaterial())));
        setItem(7, ItemUtils.createItem(Material.ARROW, "&a+10"), player -> setItem(4, updateSelection(10, item.getMaterial())));
        setItem(8, ItemUtils.createItem(Material.ARROW, "&a+64"), player -> setItem(4, updateSelection(64, item.getMaterial())));

        setItem(13, getBackIcon(), player -> {
            new ShopMenuGUI(skyblockShop, owner, 1);
            this.selection = 1;
        });

        setItem(15, ItemUtils.createItem(Material.DIAMOND_SWORD, "&aConfirm Purchase"), player -> {
            if (canAfford(item.getPrice(), owner)) {
                processTransaction(selection, item.getPrice(), owner, item.getMaterial());
                this.selection = 1;
                player.closeInventory();
            } else owner.sendMessage("&cYou can not afford this!");
        });

        setItem(4, new ItemStack(item.getMaterial(), selection)); // displaying how much they selected of the item

        fillGUI(Material.LIME_STAINED_GLASS_PANE);
        owner.getBukkitPlayer().updateInventory();
        openSkyblockGUI(owner);
    }

    private ItemStack updateSelection(final int toUpdate, final Material material) {
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

    private boolean canAfford(final double price, final SkyblockPlayer player) {
        return player.getCoins() >= price;
    }

    private void processTransaction(final int selection, final double price, final SkyblockPlayer player, final Material item) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> plugin.getSqLiteManager().setCoins(player.getUuid(), player.getCoins() - (int) price));
        player.removeCoins(selection * (int) price);
        player.sendMessage("&aSuccessfully purchased &2" + selection + " " + item.name().toLowerCase() + " &afor &2" + ((int) price * selection) + "&a coins");
        player.getInventory().addItem(new ItemStack(item, selection));
    }
}
