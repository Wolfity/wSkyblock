package me.wolf.wskyblock.mobarena;

import me.wolf.wskyblock.utils.Cuboid;
import org.bukkit.entity.EntityType;

import java.util.HashSet;
import java.util.Set;

public class MobArena {

    private final Set<EntityType> mobs;
    private final String name;
    private Cuboid cuboid;
    private int maxMobs, spawnRoutine; // spawn routine refers to how often mobs are spawned


    public MobArena(final String name) {
        this.name = name;
        this.mobs = new HashSet<>();
    }

    public String getName() {
        return name;
    }

    public int getMaxMobs() {
        return maxMobs;
    }

    public void setMaxMobs(int maxMobs) {
        this.maxMobs = maxMobs;
    }

    public int getSpawnRoutine() {
        return spawnRoutine;
    }

    public void setSpawnRoutine(int spawnRoutine) {
        this.spawnRoutine = spawnRoutine;
    }

    public Cuboid getCuboid() {
        return cuboid;
    }

    public void setCuboid(Cuboid cuboid) {
        this.cuboid = cuboid;
    }

    public Set<EntityType> getMobs() {
        return mobs;
    }
}
