package me.wolf.wskyblock.files;


import me.wolf.wskyblock.SkyblockPlugin;
import me.wolf.wskyblock.utils.Utils;
import org.bukkit.Bukkit;

public class FileManager {

    private YamlConfig minerConfig, monsterKillerConfig, lumberjackConfig,
            blockShopConfig, foodShopConfig, miscShopConfig, mobDropConfig, toolShopConfig, valuableConfig, customEnchantsConfig, magicMarket;


    public FileManager(final SkyblockPlugin plugin) {
        try {
            blockShopConfig = new YamlConfig("blockshop.yml", plugin);
            foodShopConfig = new YamlConfig("foodshop.yml", plugin);
            miscShopConfig = new YamlConfig("miscshop.yml", plugin);
            mobDropConfig = new YamlConfig("mobdropshop.yml", plugin);
            toolShopConfig = new YamlConfig("toolshop.yml", plugin);
            valuableConfig = new YamlConfig("valuableshop.yml", plugin);
            minerConfig = new YamlConfig("miner.yml", plugin);
            monsterKillerConfig = new YamlConfig("monsterkiller.yml", plugin);
            customEnchantsConfig = new YamlConfig("customenchants.yml", plugin);
            lumberjackConfig = new YamlConfig("lumberjack.yml", plugin);
            magicMarket = new YamlConfig("magicmarket.yml", plugin);
        } catch (final Exception e) {
            Bukkit.getLogger().info(Utils.colorize("&4Something went wrong while loading the yml files"));
            e.printStackTrace();
        }

    }

    public void reloadConfigs() {
        minerConfig.reloadConfig();
        monsterKillerConfig.reloadConfig();
        lumberjackConfig.reloadConfig();
        blockShopConfig.reloadConfig();
        foodShopConfig.reloadConfig();
        miscShopConfig.reloadConfig();
        mobDropConfig.reloadConfig();
        toolShopConfig.reloadConfig();
        valuableConfig.reloadConfig();
        customEnchantsConfig.reloadConfig();
        magicMarket.reloadConfig();
    }

    public YamlConfig getLumberjackConfig() {
        return lumberjackConfig;
    }

    public YamlConfig getMinerConfig() {
        return minerConfig;
    }

    public YamlConfig getMonsterKillerConfig() {
        return monsterKillerConfig;
    }

    public YamlConfig getBlockShopConfig() {
        return blockShopConfig;
    }

    public YamlConfig getFoodShopConfig() {
        return foodShopConfig;
    }

    public YamlConfig getMiscShopConfig() {
        return miscShopConfig;
    }

    public YamlConfig getMobDropConfig() {
        return mobDropConfig;
    }

    public YamlConfig getToolShopConfig() {
        return toolShopConfig;
    }

    public YamlConfig getValuableConfig() {
        return valuableConfig;
    }

    public YamlConfig getCustomEnchantsConfig() {
        return customEnchantsConfig;
    }

    public YamlConfig getMagicMarket() {
        return magicMarket;
    }
}
