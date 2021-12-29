package me.wolf.wskyblock.shops;

import me.wolf.wskyblock.files.FileManager;
import me.wolf.wskyblock.files.YamlConfig;
import me.wolf.wskyblock.shops.shoptypes.*;
import me.wolf.wskyblock.utils.ItemUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class ShopManager {

    private final FileManager fileManager;
    private final Set<SkyblockShop> shops = new HashSet<>();

    public ShopManager(final FileManager fileManager) {
        this.fileManager = fileManager;
    }

    public void registerShops() { // registering the shops
        addShops(new BlockShop(), new FoodShop(), new MiscShop(), new MobDropsShop(), new ToolShop(), new ValuableShop());
        // load all the items for every shop
        for (final SkyblockShop skyblockShop : shops) {
            if (skyblockShop.getName().equalsIgnoreCase("Blocks")) {
                cacheShopItems(skyblockShop, fileManager.getBlockShopConfig());
            } else if (skyblockShop.getName().equalsIgnoreCase("Food")) {
                cacheShopItems(skyblockShop, fileManager.getFoodShopConfig());
            } else if (skyblockShop.getName().equalsIgnoreCase("Misc")) {
                cacheShopItems(skyblockShop, fileManager.getMiscShopConfig());
            } else if (skyblockShop.getName().equalsIgnoreCase("Mob Drops")) {
                cacheShopItems(skyblockShop, fileManager.getMobDropConfig());
            } else if (skyblockShop.getName().equalsIgnoreCase("Tools")) {
                cacheShopItems(skyblockShop, fileManager.getToolShopConfig());
            } else if (skyblockShop.getName().equalsIgnoreCase("Valuables")) {

                cacheShopItems(skyblockShop, fileManager.getValuableConfig());
            }
        }

    }

    // loop over the config to create ShopItem objects, to then pass to the shop
    public void cacheShopItems(final SkyblockShop skyblockShop, final YamlConfig config) {
        // this is the icon that represents the shop itself (valuable shop = diamond for example)
        final ItemStack icon = ItemUtils.createItem(Material.valueOf(config.getConfig().getString("icon")), config.getConfig().getString("name"));
        final boolean enabled = config.getConfig().getBoolean("enabled");
        final int slot = config.getConfig().getInt("slot");
        final List<ShopItem> shopItems = new ArrayList<>();

        // loading in the shop items (price, sell price, material)
        for (final String key : config.getConfig().getConfigurationSection("items").getKeys(false)) {
            final Material material = Material.valueOf(config.getConfig().getString("items." + key + ".material"));
            final int price = config.getConfig().getInt("items." + key + ".price");
            final double sellPrice = config.getConfig().getDouble("items." + key + ".sell-price");
            shopItems.add(new ShopItem(material, price, sellPrice));
        }

        skyblockShop.setShopItems(shopItems);
        skyblockShop.setIcon(icon);
        skyblockShop.setEnabled(enabled);
        skyblockShop.setSlot(slot);
    }

    public void addShops(final SkyblockShop... shops) {
        this.shops.addAll(Arrays.asList(shops));
    }

    // getting a shop by passing in a name
    public SkyblockShop getShopByName(final String name) {
        for (final SkyblockShop shop : shops) {
            if (ChatColor.stripColor(shop.getName()).equalsIgnoreCase(ChatColor.stripColor(name))) {
                return shop;
            }
        }
        return null;
    }

    public Set<SkyblockShop> getShops() {
        return shops;
    }
}
