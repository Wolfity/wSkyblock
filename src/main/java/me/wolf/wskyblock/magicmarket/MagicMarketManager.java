package me.wolf.wskyblock.magicmarket;

import me.wolf.wskyblock.SkyblockPlugin;
import me.wolf.wskyblock.enchants.SBEnchantment;
import me.wolf.wskyblock.enchants.SBEnchantmentItem;
import me.wolf.wskyblock.files.YamlConfig;
import me.wolf.wskyblock.utils.ItemUtils;
import me.wolf.wskyblock.utils.Utils;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

public class MagicMarketManager {

    private final SkyblockPlugin plugin;
    private final MagicMarket magicMarket = new MagicMarket();

    public MagicMarketManager(final SkyblockPlugin plugin) {
        this.plugin = plugin;
    }

    public void setup(final YamlConfig yamlConfig) {
        magicMarket.setName(yamlConfig.getConfig().getString("name"));
        magicMarket.setIcon(ItemUtils.createItem(Material.valueOf(yamlConfig.getConfig().getString("icon-material"))
                , yamlConfig.getConfig().getString("icon-name")));
        magicMarket.setEnchantmentItems(loadItems(yamlConfig));
    }


    public boolean doesItemByNameExist(final String name) {
        return this.magicMarket.getEnchantmentItems().stream().anyMatch(enchantmentItem -> enchantmentItem.getName().equalsIgnoreCase(Utils.colorize(name)));
    }

    private List<SBEnchantmentItem> loadItems(final YamlConfig yamlConfig) {
        final List<SBEnchantmentItem> magicMarketItems = new ArrayList<>();

        for (final String key : yamlConfig.getConfig().getConfigurationSection("items").getKeys(false)) { //  1,2,3,4...
            final Material material = Material.valueOf(yamlConfig.getConfig().getString("items." + key + ".material"));
            final String name = Utils.colorize(yamlConfig.getConfig().getString("items." + key + ".name"));
            final int price = yamlConfig.getConfig().getInt("items." + key + ".price");
            final List<SBEnchantment> sbEnchantments = new ArrayList<>();

            for (final String enchKey : yamlConfig.getConfig().getConfigurationSection("items." + key + ".custom-enchants").getKeys(false)) {
                final String enchType = yamlConfig.getConfig().getString("items." + key + ".custom-enchants." + enchKey + ".type");
                final int level = yamlConfig.getConfig().getInt("items." + key + ".custom-enchants." + enchKey + ".level");
                final SBEnchantment sbEnchantment = plugin.getSbEnchantManager().getEnchantmentByName(enchType);

                sbEnchantment.setCurrentLevel(level);

                if (!sbEnchantment.getSupportEnchantItem().getSupportedItems().contains(material)) {
                    throw new IllegalArgumentException("This item does not support this custom enchantment!");
                } else if (sbEnchantment.getMaxLevel() < level) {
                    throw new IllegalArgumentException("This level exceeds the maximum set level of this enchantment!");
                } else {
                    sbEnchantments.add(sbEnchantment);
                }


            }
            magicMarketItems.add(new SBEnchantmentItem(ItemUtils.createItem(material, name), sbEnchantments, price));

        }
        return magicMarketItems;

    }

    public MagicMarket getMagicMarket() {
        return magicMarket;
    }
}

