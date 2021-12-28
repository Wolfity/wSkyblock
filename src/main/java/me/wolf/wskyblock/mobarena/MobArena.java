package me.wolf.wskyblock.mobarena;

import me.wolf.wskyblock.utils.Cuboid;

public class MobArena {

    private final String name;
    private Cuboid cuboid;
    private int maxMobs, spawnRoutine; // spawn routine refers to how often mobs are spawned
    private int entityCount;

    public MobArena(final String name) {
        this.name = name;
        this.entityCount = 0;

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

    public void increaseEntityCount() {
        this.entityCount++;
    }

    public void decreaseEntityCount() {
        this.entityCount--;
    }

    public int getEntityCount() {
        return entityCount;
    }

    public Cuboid getCuboid() {
        return cuboid;
    }

    public void setCuboid(Cuboid cuboid) {
        this.cuboid = cuboid;
    }

    public void decrementSpawnRoutine() {
        this.spawnRoutine--;
    }

}
