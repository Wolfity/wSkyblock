package me.wolf.wskyblock.sql;

public final class Query {

    // TODO store warps
    public static final String CREATE_ISLAND_TABLE = "CREATE TABLE IF NOT EXISTS " +
            "islands (uuid VARCHAR(64) " +
            "NOT NULL, " +
            "name VARCHAR(16), " +
            "spawn VARCHAR(16), " +
            "visitors VARCHAR(4)," +
            "coins int)";

    public static final String CREATE_SKILLS_TABLE = "CREATE TABLE IF NOT EXISTS " +
            "skills (uuid VARCHAR(64) " +
            "NOT NULL, " +
            "name VARCHAR(16), " +
            "lumberjack VARCHAR(16), " +
            "monsterkiller VARCHAR(16), " +
            "miner VARCHAR(16))";

    public static final String CREATE_AUCTION_TABLE = "CREATE TABLE IF NOT EXISTS " +
            "auction (uuid VARCHAR(64) " +
            "NOT NULL, " +
            "name VARCHAR(16), " +
            "itemid VARCHAR(16), " +
            "item VARCHAR(16), " +
            "price int)";


    public static final String CREATE_PLAYERDATA = "INSERT INTO islands (uuid, name, spawn, visitors, coins) VALUES (?,?,?,?,?)";
    public static final String GET_PLAYERDATA = "SELECT * FROM islands WHERE uuid = ?";
    public static final String SET_PLAYER_NAME = "UPDATE islands SET name = ? WHERE uuid = ?";
    public static final String SET_SPAWN = "UPDATE islands SET spawn = ? WHERE uuid = ?";
    public static final String SET_VISITORS = "UPDATE islands SET visitors = ? WHERE uuid = ?";
    public static final String SET_COINS = "UPDATE islands SET coins = ? WHERE uuid = ?";


    public static final String GET_SKILLDATA = "SELECT * FROM skills WHERE uuid = ?";
    public static final String CREATE_SKILLDATA = "INSERT INTO skills (uuid, name, lumberjack, monsterkiller, miner) VALUES (?,?,?,?,?)";
    public static final String SET_SKILLS_PLAYER_NAME = "UPDATE skills SET name = ? WHERE uuid = ?";
    public static final String SET_LUMBERJACK = "UPDATE skills SET lumberjack = ? WHERE uuid = ?";
    public static final String SET_MONSTERKILLER = "UPDATE skills SET monsterkiller = ? WHERE uuid = ?";
    public static final String SET_MINER = "UPDATE skills SET miner = ? WHERE uuid = ?";


    public static final String SET_ITEM = "INSERT INTO auction (uuid, name, itemid, item, price) VALUES(?,?,?,?,?)";


    private Query() {
    }

}
