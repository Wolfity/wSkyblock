package me.wolf.wskyblock.gui.guis;

import me.wolf.wskyblock.SkyblockPlugin;
import me.wolf.wskyblock.enchants.SBEnchantmentItem;
import me.wolf.wskyblock.gui.PaginatedMenu;
import me.wolf.wskyblock.player.SkyblockPlayer;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class MagicMarketGUI extends PaginatedMenu {

    public MagicMarketGUI(final SkyblockPlugin plugin, final SkyblockPlayer owner, final me.wolf.wskyblock.magicmarket.MagicMarket blackMarket, final int page) {
        super(54, blackMarket.getName() + " - " + page, owner);

        addMenuBorder(plugin, owner);

        final List<SBEnchantmentItem> sbEnchantmentItems = new ArrayList<>(blackMarket.getEnchantmentItems());
        final List<ItemStack> stacks = new ArrayList<>();
        addMenuBorder(plugin, owner);
        for (SBEnchantmentItem sbEnchantmentItem : sbEnchantmentItems) {
            stacks.add(sbEnchantmentItem.getItemStack());
        }
        addMenuBorder(plugin, owner);
        // checking if the page is valid, sending them to the last page if so
        if (isPageValid(stacks, page - 1, 28)) { // left page
            setItem(48, getPageLeft(), player -> {
                new MagicMarketGUI(plugin, owner, plugin.getMagicMarketManager().getMagicMarket(), page - 1);
            });
        } else setItem(48, getInvalidPage());

        // checking if the page is valid, sending to the next if so
        if (isPageValid(stacks, page + 1, 28)) {
            setItem(50, getPageRight(), player -> {
                new MagicMarketGUI(plugin, owner, plugin.getMagicMarketManager().getMagicMarket(), page + 1);
            });
        } else setItem(50, getInvalidPage());

        // 28 refers to the usable slots
        for (ItemStack is : getPageItems(stacks, page, 28)) {
            setItem(getSkyblockGUI().firstEmpty(), is);

        }


        openSkyblockGUI(owner);

    }
}
