package me.wolf.wskyblock.sql;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import me.wolf.wskyblock.SkyblockPlugin;
import me.wolf.wskyblock.auctionhouse.AuctionItem;
import me.wolf.wskyblock.island.Island;
import me.wolf.wskyblock.player.SkyblockPlayer;
import me.wolf.wskyblock.skills.Skill;
import me.wolf.wskyblock.utils.ItemUtils;
import me.wolf.wskyblock.warps.Warp;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.sql.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class SQLiteManager {

    private final SkyblockPlugin plugin;
    private HikariDataSource hikari;

    // manager that handles any SQLite related stuff, saving data, loading data, creating data, etc...

    public SQLiteManager(final SkyblockPlugin plugin) {
        this.plugin = plugin;
    }

    public void connect() {
        final HikariConfig config = new HikariConfig();
        config.setConnectionTestQuery("SELECT 1");
        config.setPoolName("wSkyblock Pool");
        config.setDriverClassName("org.sqlite.JDBC");
        final File file = new File(plugin.getDataFolder(), "database.db");
        config.setJdbcUrl("jdbc:sqlite:" + file.getAbsolutePath().replace("\\", "/"));

        hikari = new HikariDataSource(config);

        createTablesIfNotExist();
    }

    public void createSkillData(final UUID uuid, final String playerName) {
        if (doesSkillPlayerExist(uuid)) {
            loadSkillData(uuid);
        } else {
            try (final Connection connection = hikari.getConnection();
                 final PreparedStatement ps = connection.prepareStatement(Query.CREATE_SKILLDATA)) {
                final Skill miner = plugin.getSkillManager().getRawSkillByName("miner");
                final Skill lumber = plugin.getSkillManager().getRawSkillByName("lumberjack");
                final Skill monsterKiller = plugin.getSkillManager().getRawSkillByName("monster killer");
                ps.setString(1, uuid.toString());
                ps.setString(2, playerName);
                ps.setString(3, "0 0 " + lumber.getExperienceNextLevel() / lumber.getExpIncreaseMultiplier() + " " + lumber.getLevelCap()); // lumber: level 0 | exp: 0 | exp to next  100 | levelcap 50
                ps.setString(4, "0 0 " + monsterKiller.getExperienceNextLevel() / monsterKiller.getExpIncreaseMultiplier() + " " + monsterKiller.getLevelCap()); // monsterkiller
                ps.setString(5, "0 0 " + miner.getExperienceNextLevel() / miner.getExpIncreaseMultiplier() + " " + miner.getLevelCap()); // miner

                ps.executeUpdate();
            } catch (final SQLException e) {
                e.printStackTrace();
            }
        }
    }


    public void createPlayerData(final UUID uuid, final String playerName) { // if the player exists, load their data, else create new data
        if (doesPlayerExist(uuid)) {
            loadData(uuid);
        } else {
            try (final Connection connection = hikari.getConnection();
                 final PreparedStatement ps = connection.prepareStatement(Query.CREATE_PLAYERDATA)) {

                ps.setString(1, uuid.toString());
                ps.setString(2, playerName);
                ps.setString(3, "null"); // spawn
                ps.setString(4, "null"); // visitors
                ps.setInt(5, 0); // coins

                ps.executeUpdate();

            } catch (final SQLException e) {
                e.printStackTrace();
            }

        }
    }

    public void disconnect() {
        if (hikari != null)
            hikari.close();
    }


    private void loadData(final UUID uuid) { // load data of an existing player

        // islands table
        this.setAcceptVisitors(uuid, this.getAcceptsVisitors(uuid));
        this.setSpawn(uuid, this.getSpawn(uuid));
        this.setCoins(uuid, this.getCoins(uuid));
        final SkyblockPlayer owner = plugin.getPlayerManager().getSkyblockPlayer(uuid);


        plugin.getIslandManager().loadIsland(owner, getSpawn(uuid), getAcceptsVisitors(uuid));
        owner.setCoins(this.getCoins(uuid));

    }

    private void loadSkillData(final UUID uuid) {
        final SkyblockPlayer player = plugin.getPlayerManager().getSkyblockPlayer(uuid);
        this.setLumberjack(uuid, this.getLumberjack(uuid));
        this.setMonsterKiller(uuid, this.getMonsterKiller(uuid));
        this.setMiner(uuid, this.getMiner(uuid));

        final String[] lumberData = this.getLumberjack(uuid).split(" "); // 0 = level | 1 = exp | 2 = xp till next | 3 = lvl cap
        final String[] monsterKillerData = this.getMonsterKiller(uuid).split(" ");
        final String[] minerData = this.getMiner(uuid).split(" ");

        for (final Skill skill : player.getSkills()) {

            if (skill.getName().equalsIgnoreCase("lumberjack")) {
                skill.setLevel(Integer.parseInt(lumberData[0]));
                skill.setCurrentExp(Integer.parseInt(lumberData[1]));
                skill.setExperienceNextLevel(Double.parseDouble(lumberData[2]));
                skill.setLevelCap(Integer.parseInt(lumberData[3]));

            } else if (skill.getName().equalsIgnoreCase("monster killer")) {
                skill.setLevel(Integer.parseInt(monsterKillerData[0]));
                skill.setCurrentExp(Integer.parseInt(monsterKillerData[1]));
                skill.setExperienceNextLevel(Double.parseDouble(monsterKillerData[2]));
                skill.setLevelCap(Integer.parseInt(monsterKillerData[3]));

            } else if (skill.getName().equalsIgnoreCase("miner")) {
                skill.setLevel(Integer.parseInt(minerData[0]));
                skill.setCurrentExp(Integer.parseInt(minerData[1]));
                skill.setExperienceNextLevel(Double.parseDouble(minerData[2]));
                skill.setLevelCap(Integer.parseInt(minerData[3]));
            }
        }
    }

    private boolean doesPlayerExist(final UUID uuid) { // checking if a specific player exists in the database
        try (final Connection connection = hikari.getConnection();
             final PreparedStatement ps = connection.prepareStatement(Query.GET_PLAYERDATA)) {
            ps.setString(1, uuid.toString());
            final ResultSet results = ps.executeQuery();

            return results.next();
        } catch (final SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean doesSkillPlayerExist(final UUID uuid) {
        try (final Connection connection = hikari.getConnection();
             final PreparedStatement ps = connection.prepareStatement(Query.GET_SKILLDATA)) {
            ps.setString(1, uuid.toString());
            final ResultSet results = ps.executeQuery();

            return results.next();
        } catch (final SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void setSkillPlayerName(final UUID uuid, final String playerName) {
        if (!doesSkillPlayerExist(uuid)) return;

        try (final Connection connection = hikari.getConnection();
             final PreparedStatement ps = connection.prepareStatement(Query.SET_SKILLS_PLAYER_NAME)) {

            ps.setString(1, playerName);
            ps.setString(2, uuid.toString());

            ps.executeUpdate();

        } catch (final SQLException e) {
            e.printStackTrace();
        }
    }


    private void setPlayerName(final UUID uuid, final String playerName) { // setting the playername in the db
        if (!doesPlayerExist(uuid)) return;

        try (final Connection connection = hikari.getConnection();
             final PreparedStatement ps = connection.prepareStatement(Query.SET_PLAYER_NAME)) {

            ps.setString(1, playerName);
            ps.setString(2, uuid.toString());

            ps.executeUpdate();

        } catch (final SQLException e) {
            e.printStackTrace();
        }


    }

    private void createTablesIfNotExist() { // if the table doesn't exist, create it
        try (final Connection connection = hikari.getConnection();
             final Statement statement = connection.createStatement()) {

            statement.executeUpdate(Query.CREATE_ISLAND_TABLE);
            statement.executeUpdate(Query.CREATE_SKILLS_TABLE);
            statement.executeUpdate(Query.CREATE_AUCTION_TABLE);
            statement.executeUpdate(Query.CREATE_WARPS_TABLE);

        } catch (final SQLException e) {
            e.printStackTrace();
        }
    }

    private void setSpawn(final UUID uuid, final String spawnLoc) {
        try (final Connection connection = hikari.getConnection();
             final PreparedStatement ps = connection.prepareStatement(Query.SET_SPAWN)) {

            ps.setString(1, spawnLoc);
            ps.setString(2, uuid.toString());

            ps.executeUpdate();

        } catch (final SQLException e) {
            e.printStackTrace();
        }
    }

    private String getSpawn(final UUID uuid) {
        try (final Connection connection = hikari.getConnection();
             final PreparedStatement ps = connection.prepareStatement(Query.GET_PLAYERDATA)) {

            ps.setString(1, uuid.toString());

            final ResultSet results = ps.executeQuery();

            return results.getString("spawn");
        } catch (final SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    private void setAcceptVisitors(final UUID uuid, final boolean accepts) {
        try (final Connection connection = hikari.getConnection();
             final PreparedStatement ps = connection.prepareStatement(Query.SET_VISITORS)) {

            ps.setBoolean(1, accepts);
            ps.setString(2, uuid.toString());

            ps.executeUpdate();

        } catch (final SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean getAcceptsVisitors(final UUID uuid) {
        try (final Connection connection = hikari.getConnection();
             final PreparedStatement ps = connection.prepareStatement(Query.GET_PLAYERDATA)) {

            ps.setString(1, uuid.toString());

            final ResultSet results = ps.executeQuery();

            return results.getBoolean("visitors");
        } catch (final SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public void setCoins(final UUID uuid, final int coins) {
        try (final Connection connection = hikari.getConnection();
             final PreparedStatement ps = connection.prepareStatement(Query.SET_COINS)) {

            ps.setInt(1, coins);
            ps.setString(2, uuid.toString());

            ps.executeUpdate();
        } catch (final SQLException e) {
            e.printStackTrace();
        }
    }

    public int getCoins(final UUID uuid) {
        try (final Connection connection = hikari.getConnection();
             final PreparedStatement ps = connection.prepareStatement(Query.GET_PLAYERDATA)) {

            ps.setString(1, uuid.toString());

            final ResultSet results = ps.executeQuery();

            return results.getInt("coins");
        } catch (final SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }


    // Skills below
    private void setLumberjack(final UUID uuid, final String lumber) { // lvl xp expTillNext
        try (final Connection connection = hikari.getConnection();
             final PreparedStatement ps = connection.prepareStatement(Query.SET_LUMBERJACK)) {

            ps.setString(1, lumber);
            ps.setString(2, uuid.toString());

            ps.executeUpdate();

        } catch (final SQLException e) {
            e.printStackTrace();
        }
    }

    private String getLumberjack(final UUID uuid) {
        try (final Connection connection = hikari.getConnection();
             final PreparedStatement ps = connection.prepareStatement(Query.GET_SKILLDATA)) {

            ps.setString(1, uuid.toString());

            final ResultSet results = ps.executeQuery();

            return results.getString("lumberjack");
        } catch (final SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    private void setMonsterKiller(final UUID uuid, final String monsterKiller) { // lvl xp expTillNext
        try (final Connection connection = hikari.getConnection();
             final PreparedStatement ps = connection.prepareStatement(Query.SET_MONSTERKILLER)) {

            ps.setString(1, monsterKiller);
            ps.setString(2, uuid.toString());

            ps.executeUpdate();

        } catch (final SQLException e) {
            e.printStackTrace();
        }
    }

    private String getMonsterKiller(final UUID uuid) {
        try (final Connection connection = hikari.getConnection();
             final PreparedStatement ps = connection.prepareStatement(Query.GET_SKILLDATA)) {

            ps.setString(1, uuid.toString());

            final ResultSet results = ps.executeQuery();

            return results.getString("monsterkiller");
        } catch (final SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    private void setMiner(final UUID uuid, final String miner) { // lvl xp expTillNext
        try (final Connection connection = hikari.getConnection();
             final PreparedStatement ps = connection.prepareStatement(Query.SET_MINER)) {

            ps.setString(1, miner);
            ps.setString(2, uuid.toString());

            ps.executeUpdate();

        } catch (final SQLException e) {
            e.printStackTrace();
        }
    }

    private String getMiner(final UUID uuid) {
        try (final Connection connection = hikari.getConnection();
             final PreparedStatement ps = connection.prepareStatement(Query.GET_SKILLDATA)) {

            ps.setString(1, uuid.toString());

            final ResultSet results = ps.executeQuery();

            return results.getString("miner");
        } catch (final SQLException e) {
            e.printStackTrace();
        }

        return null;
    }


    public void saveData(final UUID uuid) { // saving all data of the specified player
        // island

        this.setPlayerName(uuid, plugin.getPlayerManager().getSkyblockPlayer(uuid).getOfflinePlayer().getName());

        final SkyblockPlayer owner = plugin.getPlayerManager().getSkyblockPlayer(uuid);
        final Island island = plugin.getIslandManager().getIslandByOwner(owner);

        this.setSpawn(uuid, this.getSpawn(uuid));
        this.setAcceptVisitors(owner.getUuid(), island.acceptsVisitors());
        this.setCoins(uuid, owner.getCoins());
        final String[] stringLoc = this.getSpawn(uuid).split(" ");
        final Location spawn = new Location(Bukkit.getWorld(stringLoc[0]),
                Double.parseDouble(stringLoc[1]),
                Double.parseDouble(stringLoc[2]),
                Double.parseDouble(stringLoc[3]));

        island.setSpawn(spawn);
        island.setAcceptsVisitors(this.getAcceptsVisitors(owner.getUuid()));
        owner.setCoins(this.getCoins(owner.getUuid()));

    }

    public void saveSkillData(final UUID uuid, final Skill skill, final String data) {

        if (skill.getName().equalsIgnoreCase("Lumberjack")) {
            saveLumberData(uuid, data);
        } else if (skill.getName().equalsIgnoreCase("Monster Killer")) {
            saveMonsterKillerData(uuid, data);
        } else if (skill.getName().equalsIgnoreCase("Miner")) {
            saveMinerData(uuid, data);
        }

    }

    private void saveLumberData(final UUID uuid, final String lumberData) {
        this.setSkillPlayerName(uuid, plugin.getPlayerManager().getSkyblockPlayer(uuid).getOfflinePlayer().getName());
        this.setLumberjack(uuid, lumberData);
    }

    private void saveMonsterKillerData(final UUID uuid, final String monserData) {
        this.setSkillPlayerName(uuid, plugin.getPlayerManager().getSkyblockPlayer(uuid).getOfflinePlayer().getName());
        this.setMonsterKiller(uuid, monserData);
    }

    private void saveMinerData(final UUID uuid, final String minerData) {
        this.setSkillPlayerName(uuid, plugin.getPlayerManager().getSkyblockPlayer(uuid).getOfflinePlayer().getName());
        this.setMiner(uuid, minerData);
    }

    public String getSkillData(final UUID uuid, final Skill skill) {
        if (skill.getName().equalsIgnoreCase("lumberjack")) {
            return this.getLumberjack(uuid);
        } else if (skill.getName().equalsIgnoreCase("monster killer")) {
            return this.getMonsterKiller(uuid);
        } else if (skill.getName().equalsIgnoreCase("miner")) {
            return this.getMiner(uuid);
        }
        return null;
    }

    public Set<AuctionItem> loadAuctionData() { // load data of an existing player
        final Set<AuctionItem> auctionItems = new HashSet<>();
        // auction table
        try (final Connection connection = hikari.getConnection();
             final PreparedStatement ps = connection.prepareStatement("SELECT uuid, itemid, item, price FROM auction")) {

            final ResultSet results = ps.executeQuery();

            while (results.next()) {

                final OfflinePlayer seller = Bukkit.getOfflinePlayer(UUID.fromString(results.getString(1))); // seller
                // Skipping name, not really required atm
                final String itemID = results.getString(2); // itemID
                final ItemStack item = ItemUtils.serializeItemStack(results.getString(3)); // item
                final int price = results.getInt(4); // price


                auctionItems.add(new AuctionItem(seller, price, item, UUID.fromString(itemID)));
            }

        } catch (final SQLException e) {
            e.printStackTrace();
        }

        return auctionItems;
    }

    public ItemStack getRawAuctionItemByID(final UUID itemID) { // getting an AuctionItem by passing in an itemID
        try (final Connection connection = hikari.getConnection();
             final PreparedStatement ps = connection.prepareStatement("SELECT item FROM auction WHERE itemid ='" + itemID.toString() + "'")) {

            final ResultSet results = ps.executeQuery();


            return ItemUtils.serializeItemStack(results.getString(1));

        } catch (final SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void addAuctionItem(final AuctionItem item) {
        try (final Connection connection = hikari.getConnection();
             final PreparedStatement statement = connection.prepareStatement(Query.SET_ITEM)) {

            statement.setString(1, item.getSeller().getUniqueId().toString());
            statement.setString(2, item.getSeller().getName());
            statement.setString(3, item.getItemID().toString());
            statement.setString(4, ItemUtils.deserializeItemStack(item.getItemStack()));
            statement.setInt(5, item.getPrice());


            statement.executeUpdate();

        } catch (final SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeAuction(final AuctionItem auctionItem) {
        try (final Connection connection = hikari.getConnection();
             final PreparedStatement ps = connection.prepareStatement("DELETE FROM auction WHERE itemid ='" + auctionItem.getItemID().toString() + "'")) {
            ps.executeUpdate();

        } catch (final SQLException e) {
            e.printStackTrace();
        }
    }

    public void addWarp(final Island island, final Warp warp) {
        try (final Connection connection = hikari.getConnection();
             final PreparedStatement statement = connection.prepareStatement(Query.SET_WARPS)) {

            statement.setString(1, island.getOwner().getUuid().toString());
            statement.setString(2, warp.getName());
            statement.setString(3, warp.getLocation().getWorld().getName());
            statement.setInt(4, (int) warp.getLocation().getX());
            statement.setInt(5, (int) warp.getLocation().getY());
            statement.setInt(6, (int) warp.getLocation().getZ());


            statement.executeUpdate();

        } catch (final SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeWarp(final Island island, final String name) {
        try (final Connection connection = hikari.getConnection();
             final PreparedStatement ps = connection.prepareStatement("DELETE FROM warps WHERE uuid ='" + island.getOwner().getUuid().toString() + "' AND name ='" + name + "'")) {
            ps.executeUpdate();

        } catch (final SQLException e) {
            e.printStackTrace();
        }
    }

    public Set<Warp> loadWarps(final Island island) { // load data of an existing player
        final Set<Warp> warps = new HashSet<>();
        // auction table
        try (final Connection connection = hikari.getConnection();
             final PreparedStatement ps = connection.prepareStatement("SELECT name, world, x, y, z FROM warps WHERE uuid ='" + island.getOwner().getUuid() + "'")) {

            final ResultSet results = ps.executeQuery();
            while (results.next()) {
                final String name = results.getString(1);
                final Location warpLoc = new Location(Bukkit.getWorld(results.getString(2)),
                        results.getDouble(3),
                        results.getDouble(4),
                        results.getDouble(5));

                warps.add(new Warp(warpLoc, name));
            }

        } catch (final SQLException e) {
            e.printStackTrace();
        }

        return warps;
    }

}