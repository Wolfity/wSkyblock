package me.wolf.wskyblock.gui.guis.magicmarket;

import me.wolf.wskyblock.SkyblockPlugin;
import me.wolf.wskyblock.enchants.SBEnchantment;
import me.wolf.wskyblock.enchants.SBEnchantmentItem;
import me.wolf.wskyblock.gui.SkyblockGUI;
import me.wolf.wskyblock.gui.guis.MagicMarketGUI;
import me.wolf.wskyblock.player.SkyblockPlayer;
import me.wolf.wskyblock.utils.ItemUtils;
import me.wolf.wskyblock.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class MagicMarketConfirmPurchase extends SkyblockGUI {
    private final SkyblockPlugin plugin;

    public MagicMarketConfirmPurchase(final SkyblockPlayer owner, final SBEnchantmentItem item, final SkyblockPlugin plugin) {
        super(9, Utils.colorize("&aConfirm"), owner);
        this.plugin = plugin;

        setItem(3, ItemUtils.createItem(Material.GREEN_STAINED_GLASS_PANE, "&aConfirm Purchase"), player -> processTransaction(owner, item));
        setItem(4, item.getItemStack());
        setItem(5, ItemUtils.createItem(Material.RED_STAINED_GLASS_PANE, "&cNo, Go Back!"), player -> new MagicMarketGUI(plugin, owner, plugin.getMagicMarketManager().getMagicMarket(), 1));
        openSkyblockGUI(owner);
    }

    private void processTransaction(final SkyblockPlayer player, final SBEnchantmentItem item) {

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> plugin.getSqLiteManager().setCoins(player.getUuid(), player.getCoins() - item.getPrice()));
        player.removeCoins(item.getPrice());
        player.sendMessage("&aSuccessfully purchased &2" + item.getDisplay() + " &afor &2" + item.getPrice() + "&a coins");


        player.getInventory().addItem(item(item));

        player.getBukkitPlayer().closeInventory();

    }

    private ItemStack item(final SBEnchantmentItem item) {
        final ItemStack is = item.getItemStack();
        final ItemMeta meta = is.getItemMeta();
        final PersistentDataContainer pdc = meta.getPersistentDataContainer();
        meta.setLore(item.getLore());

        for(final SBEnchantment enchantment : item.getSbEnchantments()) {
            final String key = enchantment.getName() + enchantment.getCurrentLevel();
            pdc.set(new NamespacedKey(plugin, enchantment.getName()), PersistentDataType.STRING, key);
            is.setItemMeta(meta);
        }
        return is;
    }


}

