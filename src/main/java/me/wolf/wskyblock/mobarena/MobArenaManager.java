package me.wolf.wskyblock.mobarena;

import me.wolf.wskyblock.files.YamlConfig;
import me.wolf.wskyblock.utils.Cuboid;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class MobArenaManager {

    private final Set<MobArena> mobArenas = new HashSet<>();

    public void createMobArena(final YamlConfig cfg, final String name) {
        final MobArena mobArena = new MobArena(name);
        this.mobArenas.add(mobArena);

        cfg.getConfig().createSection("mobarenas. " + name);
        cfg.getConfig().set("mobarenas. " + name + ".max-mobs", 10);
        cfg.getConfig().set("mobarenas. " + name + ".spawn-routine", 30);
        cfg.saveConfig();

    }

    public void removeMobArena(final YamlConfig cfg, final String name) {
        cfg.getConfig().set("arenas." + name, null);
        cfg.saveConfig();
        this.mobArenas.remove(getMobArenaByName(name));
    }

    public void loadMobArenas(final YamlConfig cfg) {
        try {
            for (final String ar : cfg.getConfig().getConfigurationSection("mobarenas").getKeys(false)) {
                final MobArena mobArena = new MobArena(ar);
                final Location loc1 = stringToLoc(Objects.requireNonNull(cfg.getConfig().getString("mobarenas." + mobArena.getName() + ".location1")));
                final Location loc2 = stringToLoc(Objects.requireNonNull(cfg.getConfig().getString("mobarenas." + mobArena.getName() + ".location2")));
                mobArena.setCuboid(new Cuboid(loc1, loc2));
                mobArena.setMaxMobs(cfg.getConfig().getInt("max-mobs"));
                mobArena.setSpawnRoutine(cfg.getConfig().getInt("spawn-routine"));

                this.mobArenas.add(mobArena);
            }
        } catch (final NullPointerException e) {
            Bukkit.getLogger().info("No arenas were loaded, or something went wrong loading them!");
        }

    }

    public MobArena getMobArenaByName(final String name) {
        return mobArenas.stream().filter(mobArena -> mobArena.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public void setCuboid(final YamlConfig cfg, final MobArena mobArena, final Location loc1, final Location loc2) {
        mobArena.setCuboid(new Cuboid(loc1, loc2));
        cfg.getConfig().set("arenas." + mobArena.getName() + ".location1", locToString(loc1));
        cfg.getConfig().set("arenas." + mobArena.getName() + ".location2", locToString(loc2));
        cfg.saveConfig();
    }

    private String locToString(final Location location) {
        return location.getWorld() + " " + location.getX() + " " + location.getY() + " " + location.getZ();
    }

    private Location stringToLoc(final String locString) {
        final String[] split = locString.split(" ");
        return new Location(Bukkit.getWorld(split[0]), Double.parseDouble(split[1]), Double.parseDouble(split[2]), Double.parseDouble(split[3]));
    }

    public boolean doesMobArenaExist(final String mob) {
        return this.mobArenas.stream().anyMatch(mobArena -> mobArena.getName().equalsIgnoreCase(mob));
    }


}
