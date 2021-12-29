package me.wolf.wskyblock.mobarena;

import me.wolf.wskyblock.SkyblockPlugin;
import me.wolf.wskyblock.files.YamlConfig;
import me.wolf.wskyblock.mobs.types.MobArenaSkeleton;
import me.wolf.wskyblock.mobs.types.MobArenaSpider;
import me.wolf.wskyblock.mobs.types.MobArenaZombie;
import me.wolf.wskyblock.utils.Cuboid;
import me.wolf.wskyblock.utils.Utils;
import net.minecraft.world.level.World;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class MobArenaManager {

    private final SkyblockPlugin plugin;

    public MobArenaManager(final SkyblockPlugin plugin) {
        this.plugin = plugin;
    }


    private final Set<MobArena> mobArenas = new HashSet<>();
    private final Map<String, Integer> mobRewards = new HashMap<>(); // used in the MobArenaListener to see if a mob with these names was killed


    // creating a mob arena entry in the yml file
    public void createMobArena(final String name) {
        final MobArena mobArena = new MobArena(name);
        this.mobArenas.add(mobArena);
        final YamlConfig cfg = plugin.getFileManager().getMobArenaConfig();
        cfg.getConfig().createSection("mobarenas." + name);
        cfg.getConfig().set("mobarenas." + name + ".max-mobs", 10);
        cfg.getConfig().set("mobarenas." + name + ".spawn-routine", 30);
        cfg.saveConfig();

    }
    // removing a mob arena
    public void removeMobArena(final String name) {
        final YamlConfig cfg = plugin.getFileManager().getMobArenaConfig();
        cfg.getConfig().set("mobarenas." + name, null);
        cfg.saveConfig();
        this.mobArenas.remove(getMobArenaByName(name));
    }

    // loading all mob arenas after restart
    public void loadMobArenas() {
        loadMobData(); // loading in the mob by their name & Entity Type
        final YamlConfig cfg = plugin.getFileManager().getMobArenaConfig();
        try {
            for (final String ar : cfg.getConfig().getConfigurationSection("mobarenas").getKeys(false)) {
                final MobArena mobArena = new MobArena(ar);
                final Location loc1 = stringToLoc(Objects.requireNonNull(cfg.getConfig().getString("mobarenas." + mobArena.getName() + ".location1")));
                final Location loc2 = stringToLoc(Objects.requireNonNull(cfg.getConfig().getString("mobarenas." + mobArena.getName() + ".location2")));
                mobArena.setCuboid(new Cuboid(loc1, loc2));
                mobArena.setMaxMobs(cfg.getConfig().getInt("mobarenas." + ar + ".max-mobs"));
                mobArena.setSpawnRoutine(cfg.getConfig().getInt("mobarenas." + ar + ".spawn-routine"));
                Bukkit.getLogger().info("The mob arena " + mobArena.getName() + " was loaded successfully!");
                this.mobArenas.add(mobArena);
                startSpawning(mobArena);
            }
        } catch (final NullPointerException e) {
            Bukkit.getLogger().info("No arenas were loaded, or something went wrong loading them!");
        }
    }

    // getting a mobarena by the name
    public MobArena getMobArenaByName(final String name) {
        return mobArenas.stream().filter(mobArena -> mobArena.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    // creating the cuboid from the arena
    public void setCuboid(final MobArena mobArena, final Location loc1, final Location loc2) {
        final YamlConfig cfg = plugin.getFileManager().getMobArenaConfig();
        mobArena.setCuboid(new Cuboid(loc1, loc2));
        cfg.getConfig().set("mobarenas." + mobArena.getName() + ".location1", locToString(loc1));
        cfg.getConfig().set("mobarenas." + mobArena.getName() + ".location2", locToString(loc2));
        cfg.saveConfig();
    }

    // converting a location to a String
    private String locToString(final Location location) {
        return location.getWorld().getName() + " " + location.getX() + " " + location.getY() + " " + location.getZ();
    }

    // converting a string to a location
    private Location stringToLoc(final String locString) {
        final String[] split = locString.split(" ");
        return new Location(Bukkit.getWorld(split[0]), Double.parseDouble(split[1]), Double.parseDouble(split[2]), Double.parseDouble(split[3]));
    }

    // checking if a specific arena exists
    public boolean doesMobArenaExist(final String mob) {
        return this.mobArenas.stream().anyMatch(mobArena -> mobArena.getName().equalsIgnoreCase(mob));
    }

    public Set<MobArena> getMobArenas() {
        return mobArenas;
    }

    // spawn the mobs
    private void startSpawning(final MobArena arena) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (arena.getSpawnRoutine() > 0) { // mobs can spawn again
                    arena.decrementSpawnRoutine();
                } else {
                    if (arena.getEntityCount() < arena.getMaxMobs()) { // check if there are less mobs then the max allowed mobs in an arena
                        final int mobsToSpawn = arena.getMaxMobs() - arena.getEntityCount(); // the mobs to spawn to get back to the max allowed mobs
                        for (int i = 0; i < mobsToSpawn; i++) {
                            spawnMob(arena);
                        }
                    }
                    // resetting the spawn routine
                    arena.setSpawnRoutine(plugin.getFileManager().getMobArenaConfig().getConfig().getInt("mobarenas." + arena.getName() + ".spawn-routine"));
                }
            }
        }.runTaskTimer(plugin, 0L, 20L);
    }

    private void spawnMob(final MobArena arena) {
        final World world = ((CraftWorld) arena.getCuboid().getRandomLocation().getWorld()).getHandle();
        final Location location = arena.getCuboid().getRandomLocation();
        final int y = location.getWorld().getHighestBlockYAt(location) + 1; // let them spawn on the floor and not in the sky
        location.setY(y);
        // getting a random mob to spawn
        final int random = new Random().nextInt(3);
        switch (random) {
            case 0:
                new MobArenaZombie(world, location, plugin);
                break;
            case 1:
                new MobArenaSkeleton(world, location, plugin);
                break;
            case 2:
                new MobArenaSpider(world, location, plugin);
                break;

        }

        arena.increaseEntityCount(); // increase the amount of entities in an arena (So we don't exceed the maximum amount)
    }


    public Map<String, Integer> getMobRewards() {
        return mobRewards;
    }

    private void loadMobData() { // getting the name matching with their EntityType
        final YamlConfig cfg = plugin.getFileManager().getMobsConfig();
        for (final String s : cfg.getConfig().getConfigurationSection("mobs").getKeys(false)) {
            mobRewards.put(Utils.colorize(cfg.getConfig().getString("mobs." + s + ".name")), cfg.getConfig().getInt("mobs." + s + ".xp-reward"));
        }
    }
}
