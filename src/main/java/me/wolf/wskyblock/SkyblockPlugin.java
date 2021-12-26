package me.wolf.wskyblock;

import me.wolf.wskyblock.auctionhouse.AuctionManager;
import me.wolf.wskyblock.commands.impl.*;
import me.wolf.wskyblock.commands.impl.admin.GiveCoinsCommand;
import me.wolf.wskyblock.commands.impl.admin.RemoveCoinsCommand;
import me.wolf.wskyblock.commands.impl.admin.SetHubCommand;
import me.wolf.wskyblock.crafingrecipes.CustomCraftingRecipes;
import me.wolf.wskyblock.enchants.SBEnchantManager;
import me.wolf.wskyblock.files.FileManager;
import me.wolf.wskyblock.gui.GUIListener;
import me.wolf.wskyblock.island.IslandManager;
import me.wolf.wskyblock.listeners.*;
import me.wolf.wskyblock.magicmarket.MagicMarketManager;
import me.wolf.wskyblock.player.PlayerManager;
import me.wolf.wskyblock.player.SkyblockPlayer;
import me.wolf.wskyblock.scoreboards.SkyblockScoreboard;
import me.wolf.wskyblock.shops.ShopManager;
import me.wolf.wskyblock.skills.Skill;
import me.wolf.wskyblock.skills.SkillManager;
import me.wolf.wskyblock.sql.SQLiteManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.util.Arrays;

public class SkyblockPlugin extends JavaPlugin {

    private IslandManager islandManager;
    private PlayerManager playerManager;
    private SQLiteManager sqLiteManager;
    private SkillManager skillManager;
    private FileManager fileManager;
    private ShopManager shopManager;
    private AuctionManager auctionManager;
    private SkyblockScoreboard skyblockScoreboard;
    private SBEnchantManager sbEnchantManager;
    private MagicMarketManager magicMarketManager;
    private CustomCraftingRecipes recipes;

    @Override
    public void onEnable() {

        this.getConfig().options().copyDefaults();
        saveDefaultConfig();

        registerManagers();
        registerCommands();
        registerListeners();


    }

    @Override
    public void onDisable() {
        for (final SkyblockPlayer skyblockPlayer : getPlayerManager().getSkyblockPlayers().values()) {
            sqLiteManager.saveData(skyblockPlayer.getUuid());
            for (final Skill skill : skyblockPlayer.getSkills()) {
                sqLiteManager.saveSkillData(skyblockPlayer.getUuid(), skill, sqLiteManager.getSkillData(skyblockPlayer.getUuid(), skill));
            }

        }
        sqLiteManager.disconnect();
    }

    private void registerListeners() {
        Arrays.asList(
                new GUIListener(playerManager),
                new JoinQuitListener(this),
                new SkillListeners(this),
                new ShopInteractListener(this),
                new AuctionHouseListener(this),
                new MagicMarketListener(this),
                new CustomEnchantListeners(this)
        ).forEach(listener -> Bukkit.getPluginManager().registerEvents(listener, this));
    }

    private void registerCommands() {
        Arrays.asList(
                new SkyblockCommand(this),
                new IslandCommand(this),
                new SkillCommand(this),
                new HubCommand(this),
                new SetHubCommand(this),
                new ShopCommand(this),
                new AuctionHouseCommand(this),
                new MagicMarketCommand(this),
                new GiveCoinsCommand(this),
                new RemoveCoinsCommand(this)
        ).forEach(this::registerCommand);

    }

    private void registerCommand(final Command command) {
        try {
            final Field commandMapField = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            commandMapField.setAccessible(true);

            final CommandMap commandMap = (CommandMap) commandMapField.get(Bukkit.getServer());
            commandMap.register(command.getLabel(), command);

        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    private void registerManagers() {
        this.sqLiteManager = new SQLiteManager(this);
        sqLiteManager.connect();
        this.fileManager = new FileManager(this);
        this.islandManager = new IslandManager(this);
        this.playerManager = new PlayerManager(this);
        this.skillManager = new SkillManager(this);
        this.skyblockScoreboard = new SkyblockScoreboard(this);
        this.auctionManager = new AuctionManager(this);
        this.sbEnchantManager = new SBEnchantManager();
        this.shopManager = new ShopManager(fileManager);
        this.magicMarketManager = new MagicMarketManager(this);
        this.recipes = new CustomCraftingRecipes();

        recipes.loadRecipes(this);
        sbEnchantManager.loadEnchants(fileManager.getCustomEnchantsConfig());
        magicMarketManager.setup(fileManager.getMagicMarket());
        auctionManager.loadAuctionedItems();
        shopManager.registerShops();
        skillManager.loadSkills();
        skillManager.cacheRewards();

    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    public IslandManager getIslandManager() {
        return islandManager;
    }

    public SQLiteManager getSqLiteManager() {
        return sqLiteManager;
    }

    public SkillManager getSkillManager() {
        return skillManager;
    }

    public FileManager getFileManager() {
        return fileManager;
    }

    public SkyblockScoreboard getSkyblockScoreboard() {
        return skyblockScoreboard;
    }

    public AuctionManager getAuctionManager() {
        return auctionManager;
    }

    public ShopManager getShopManager() {
        return shopManager;
    }

    public SBEnchantManager getSbEnchantManager() {
        return sbEnchantManager;
    }

    public MagicMarketManager getMagicMarketManager() {
        return magicMarketManager;
    }

    public CustomCraftingRecipes getRecipes() {
        return recipes;
    }
}
